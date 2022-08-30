package com.petya136900.rcebot.geoapi;

import com.petya136900.rcebot.other.Tokens;
import com.petya136900.rcebot.tools.JsonParser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.petya136900.rcebot.geoapi.GeoAPI.GeoData;
@RestController 
public class SatelliteController {
	private static final String GEO_APIKEY = Tokens.AGROMONITORING_API_KEY;
	float lat,lon;
	private GeoSatelliteLite[] satellites;
	private static String lastID=null;
	@RequestMapping(value = "/satellite", method = RequestMethod.GET)
	synchronized public String satelliteController(@RequestParam(value = "lat", required = true, defaultValue = "54.628304") String latS, 
										@RequestParam(value = "lon", required = true, defaultValue = "39.72951") String lonS) {
		GeoAPI geoApi = new GeoAPI(GEO_APIKEY);
		lat = Float.parseFloat(latS);
		lon = Float.parseFloat(lonS);
		try {
			if (lastID != null) {
				geoApi.deletePolygon(lastID);
			}
			GeoJson geoJ = new GeoJson(lat, lon, 0.02f);
			try (GeoData geoData = geoApi.createPolygon(geoJ)) {
				lastID=geoData.getId();
				GeoSatelliteLite[] satellites = geoApi.getSatellite(geoData.getId());
			}
		}catch (Exception geoEx) {
			return JsonParser.toJson(geoEx);
		}
		String pictures="";
		
		for(GeoSatelliteLite satellite : satellites) {
			satellite.updateUrls();
			pictures+="</br>"+"Время: "+satellite.getDt()+"</br>"+
					  "Спутник: "+satellite.getType()+"</br>"+
					  "Видимость: "+satellite.getDc()+"%"+"</br>"+
					  "Облачность: "+satellite.getCl()+"%"+"</br>"+
					  "Изображение: "+satellite.getImage().getTruecolor()+"</br>"+
					  "Tile: "+satellite.getTile().getTruecolor()+"</br>";
		}
		return "Hello!"+"</br>"
				+ "lat: "+lat+"</br>"
				+ "lon: "+lon+"</br>"
				+ "Polygon-ID: "+"5f2ccc16714b524bc0e0d69d"+"</br>"
				+ "Satellite-Json: </br>"
				+pictures;	
	}
}
