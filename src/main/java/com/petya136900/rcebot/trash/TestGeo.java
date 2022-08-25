package com.petya136900.rcebot.trash;

import com.petya136900.rcebot.geoapi.GeoAPI;
import com.petya136900.rcebot.geoapi.GeoJson;
import com.petya136900.rcebot.geoapi.GeoAPI.GeoData;

public class TestGeo {
	public static void main(String[] args) {
		GeoJson geoJ = new GeoJson(); 
		geoJ.getGeo_json().getGeometry().setCoordinates(
			new float[][][] {{
				{39.70951f,54.648304f},
				{39.70951f,54.608303f},
				{39.74951f,54.608303f},
				{39.74951f,54.648304f},
				{39.70951f,54.648304f}
			}}
		);
		GeoAPI geoApi = new GeoAPI("apikey");
		GeoData geoData = geoApi.createPolygon(geoJ);
		geoApi.deletePolygon(geoData.getId());
	}
}
