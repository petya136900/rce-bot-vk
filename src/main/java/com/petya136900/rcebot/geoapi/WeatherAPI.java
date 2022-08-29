package com.petya136900.rcebot.geoapi;

import com.petya136900.rcebot.geoapi.WeatherException.ExceptionCode;
import com.petya136900.rcebot.tools.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WeatherAPI {
	private static final String WEATHER_API_HOST = "api.openweathermap.org/data/2.5/weather";
	private static final String WEATHER_API_HOST_SCHEME = "https://";
	
	private final String appID;
	public WeatherAPI(String appID) {
		this.appID = appID;
	}
	public WeatherData getWeatherByCityName(String cityName) throws WeatherException {
		if(cityName==null) {
			throw new WeatherException(ExceptionCode.SET_CITY);
		} else if(!((cityName=cityName.trim()).length()>0)) {
			throw new WeatherException(ExceptionCode.SET_CITY);
		}
		String urlHost = WEATHER_API_HOST_SCHEME+WEATHER_API_HOST;
		String urlParams;
		try {
			urlParams = "q="+ URLEncoder.encode(cityName, "UTF-8")
			+"&APPID="+appID
			+"&lang=ru";
		} catch (UnsupportedEncodingException e) {
			throw new WeatherException(ExceptionCode.UNKWN_ERROR,e.getLocalizedMessage());
		}
		WeatherData weatherData = JsonParser.fromJson(sendApiRequest(urlHost,urlParams)
				.replaceAll("\"1h\"", "\"_1h\"")
				.replaceAll("\"3h\"", "\"_3h\""), WeatherData.class);
		if(!weatherData.getCod().equals(200)) {
			if(weatherData.getMessage().contains("not found")) {
				throw new WeatherException(ExceptionCode.BAD_CITY);
			}			
			switch(weatherData.getCod()) {
				case(404):
						throw new WeatherException(ExceptionCode.UNKWN_ERROR);
				case(401):
					throw new WeatherException(ExceptionCode.BAD_APPID);
				case(429):
					throw new WeatherException(ExceptionCode.ACC_DISABLED);
				default:
					System.out.println(JsonParser.toJson(weatherData));
					throw new WeatherException(ExceptionCode.UNKWN_ERROR);			
			}
		}
		return fixRussian(weatherData);
	}
	private static WeatherData fixRussian(WeatherData weatherData) {
		weatherData.getMain().setTemp(weatherData.getMain().getTemp()-273.15f);
		weatherData.getMain().setFeels_like(weatherData.getMain().getFeels_like()-273.15f);
		return weatherData;
	}
	private static String sendApiRequest(String urlHost, String urlParams) throws WeatherException {
		String response;
		try {
			 HttpURLConnection.setFollowRedirects(false);
		     HttpURLConnection conn = (HttpURLConnection) new URL(urlHost+"?"+urlParams).openConnection();
		     conn.setRequestMethod("GET");
			 conn.setRequestProperty("Content-Type","application/json; charset=utf-8");
		     conn.setDoOutput(true);
			 InputStream resultContentIS;
			 if(conn.getResponseCode()==200) {
				 resultContentIS = conn.getInputStream();
			 } else {
				 resultContentIS = conn.getErrorStream();
			 }
	         BufferedReader reader = new BufferedReader(new InputStreamReader(resultContentIS, StandardCharsets.UTF_8));
	         response = reader.readLine();
			 //System.out.println("API request: "+urlHost+"?"+urlParams+" | "+response);
			 return response;
		} catch (Exception e) {
			//System.err.println("Can't perform API request: "+urlHost+"?"+urlParams+" | "+e.printStackTrace());
			throw new WeatherException(ExceptionCode.UNKWN_ERROR,e.getMessage());
		}
	}	
}
