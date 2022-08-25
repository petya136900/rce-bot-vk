package com.petya136900.rcebot.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.geoapi.GeoAPI;
import com.petya136900.rcebot.geoapi.GeoJson;
import com.petya136900.rcebot.geoapi.GeoSatelliteLite;
import com.petya136900.rcebot.geoapi.GeoPhotoUploadThread;
import com.petya136900.rcebot.geoapi.GeoAPI.GeoData;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.MessageSendResponse.MessageInfo;
import com.petya136900.rcebot.vk.structures.VKAttachment.Photo;
import com.petya136900.rcebot.vk.structures.VKMessage.Geo;

public class GeoHandler implements HandlerInterface {
	private static final String GEP_APIKEY = "AGROMONITORING_API_KEY_HERE";
	private Geo geo;
	private GeoJson geoJson;
	private final File tempDir = new File("Temp");
	private final File geoDir = new File(tempDir+"/geo");
	private static final float DEFAULT_SCALE = 1;
	@Override
	synchronized public void handle(VK vkContent) {
		GeoAPI geoApi = new GeoAPI(GEP_APIKEY);
		if(vkContent.getVK().getGeo()==null) {
			vkContent.reply("Ты не прикрепил место на карте");
			return;
		}
		MessageInfo mess = vkContent.reply("Ожидание...");
		geo=vkContent.getVK().getGeo();
		geoJson=new GeoJson();
		float scale = DEFAULT_SCALE;
		try {
			Float.parseFloat(vkContent.getVK().getText().toLowerCase()
					.replace("спутник", "")
					.replace("geo","").trim());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		geoJson.getGeo_json().getGeometry().setCoordinates(geo.get5points(0.015f*scale));
		GeoData geoData = geoApi.createPolygon(geoJson);
		mess.editMessage("Полигон создан, поиск снимков..");
		try {
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
			mess.editMessage("Не удалось получить снимки: "+e.getLocalizedMessage());
		} finally {
			geoApi.deletePolygon(geoData.getId());
		}
	} 
}
