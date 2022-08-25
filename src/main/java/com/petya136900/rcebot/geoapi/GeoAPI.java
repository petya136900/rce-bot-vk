package com.petya136900.rcebot.geoapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.petya136900.rcebot.geoapi.GeoJson.Geo_Json;
import com.petya136900.rcebot.tools.JsonParser;

public class GeoAPI {
	private static final String POLYGONS_API_HOST = "api.agromonitoring.com/agro/1.0/polygons";
	private static final String IMAGESEARCH_API_HOST = "api.agromonitoring.com/agro/1.0/image/search";
	private static final String API_SCHEME = "https://";
	
	private final String appID;
	public GeoAPI(String appID) {
		this.appID=appID;
	}
	public class GeoData {
		private String id;
		private Geo_Json geo_json;
		private String name;
		private double[] center;
		private double area;
		private String user_id;
		public String getUser_id() {
			return user_id;
		}
		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
		public double getArea() {
			return area;
		}
		public void setArea(double area) {
			this.area = area;
		}
		public double[] getCenter() {
			return center;
		}
		public void setCenter(double[] center) {
			this.center = center;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Geo_Json getGeo_json() {
			return geo_json;
		}
		public void setGeo_json(Geo_Json geo_json) {
			this.geo_json = geo_json;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}		
	}
	public GeoData createPolygon(GeoJson geoJson) {
		String urlHost = API_SCHEME+POLYGONS_API_HOST+"?appid="+appID;
		String urlParams = JsonParser.toJson(geoJson);
		//System.out.println(urlHost+"?"+urlParams);
		String response = sendApiRequest(urlHost,urlParams);
		return JsonParser.fromJson(response, GeoData.class);		
	}
	private static String sendApiRequest(String urlHost, String urlParams,String... method) {
		String response = null;
		HttpURLConnection conn=null;
		if(urlParams==null) {
			urlParams="";
		}
		try {
			 HttpURLConnection.setFollowRedirects(false);
		     conn = (HttpURLConnection) new URL(urlHost).openConnection();
		     conn.setDoOutput(true);
		     if(!(method.length>0)) {
		    	 conn.setRequestMethod("POST");
			     conn.setRequestProperty("Content-Type", "application/json");
			     conn.setRequestProperty("Accept", "application/json");
			     try(OutputStream os = conn.getOutputStream()) {
			    	    byte[] input = urlParams.getBytes(StandardCharsets.UTF_8);
			    	    os.write(input, 0, input.length);			
			     }			     
		     } else {
		    	 conn.setRequestMethod(method[0]);
		     }
			 InputStream resultContentIS = conn.getInputStream();
	         BufferedReader reader = new BufferedReader(new InputStreamReader(resultContentIS));
	         response = reader.readLine();
		} catch (IOException e) {
	         //System.err.println("Can't perform API request: "+urlHost+"?"+urlParams);
	         //e.printStackTrace();			
	         BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	         try {
				response = reader.readLine();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return response;
	}
	public String deletePolygon(String id) {
		String urlHost = API_SCHEME+POLYGONS_API_HOST+"/"+id+"?appid="+appID;
		//System.out.println(urlHost+"?"+urlParams);
		String response = sendApiRequest(urlHost,null,"DELETE");
		return response;
	}
	public GeoSatelliteLite[] getSatellite(String id) {
		long curTimeUnix=System.currentTimeMillis()/1000;
		String urlHost = API_SCHEME+IMAGESEARCH_API_HOST+
			"?start="+(curTimeUnix-(30*24*60*60))
				+ "&end="+(curTimeUnix-(2*60*60))
					+"&polyid="+id
						+ "&appid="+appID;
		String urlParams = null;
		String response = sendApiRequest(urlHost,urlParams,"GET");
		return JsonParser.fromJson(response, GeoSatelliteLite[].class);
	}	
}
