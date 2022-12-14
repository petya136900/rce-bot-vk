package com.petya136900.rcebot.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import com.petya136900.rcebot.geoapi.*;
import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.geoapi.GeoAPI.GeoData;
import com.petya136900.rcebot.other.Tokens;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.MessageSendResponse.MessageInfo;
import com.petya136900.rcebot.vk.structures.VKAttachment.Photo;
import com.petya136900.rcebot.vk.structures.VKMessage.Geo;

public class GeoHandler implements HandlerInterface {
	private static final String GEO_APIKEY = Tokens.AGROMONITORING_API_KEY;
	private Geo geo;
	private GeoJson geoJson;
	private final File tempDir = new File("Temp");
	private final File geoDir = new File(tempDir+"/geo");
	private static final float DEFAULT_SCALE = 1;
	@Override
	synchronized public void handle(VK vkContent) {
		GeoAPI geoApi = new GeoAPI(GEO_APIKEY);
		if(vkContent.getVK().getGeo()==null) {
			vkContent.reply("Ты не прикрепил место на карте");
			return;
		}
		MessageInfo mess = vkContent.reply("Ожидание...");
		geo=vkContent.getVK().getGeo();
		geoJson=new GeoJson();
		float scale;
		try {
			scale = Float.parseFloat(vkContent.getVK().getText().toLowerCase()
					.replace("спутник", "")
					.replace("geo","").trim());
		} catch (Exception e) {
			scale = DEFAULT_SCALE;
		}
		geoJson.getGeo_json().getGeometry().setCoordinates(geo.get5points(0.015f*scale));
		try(GeoData geoData = geoApi.createPolygon(geoJson)) {
			mess.editMessage("Полигон создан, поиск снимков..");
			if(!geoDir.exists()) {
				geoDir.mkdirs();
			}
			GeoSatelliteLite[] satellites = geoApi.getSatellite(geoData.getId());
			ArrayList<Photo> alp=new ArrayList<Photo>();
			ArrayList<Thread> art=new ArrayList<Thread>();
			AtomicInteger uploadedImagesCount = new AtomicInteger(0);
			if(!(satellites.length==0)) {
				mess.editMessage("Загружено снимков: 0/"+satellites.length);
				for(GeoSatelliteLite satellite : satellites) {
					Thread t = new Thread(new GeoPhotoUploadThread(vkContent,
							alp,
							satellite.getImage().getTruecolor(),
							mess,uploadedImagesCount,satellites.length));
					t.start();
					art.add(t);
				}			
			} else {
				mess.editMessage("Для этого участка нет снимков");
				return;
			}
			for(Thread thread : art) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			mess.deleteMessage();
			vkContent.sendCreatedImages(alp.toArray(new Photo[] {}));
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof GeoException) {
				GeoException geoEx = (GeoException) e;
				mess.editMessage(
						String.format("Во время выполнения API запроса произошла ошибка: %s",
								geoEx.getMessage()));
			} else
				mess.editMessage("Ошибка выполнения: "+e.getLocalizedMessage());
		}
	} 
}
