package com.petya136900.rcebot.geoapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.petya136900.rcebot.geoapi.WeatherException.ExceptionCode;
import com.petya136900.rcebot.tools.JsonParser;

public class WeatherAPI {
	private static final String WEATHER_API_HOST = "api.openweathermap.org/data/2.5/weather";
	private static final String WEATHER_API_HOST_SCHEME = "https://";
	
	private String appID; 
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
		String urlParams = "q="+cityName
		+"&APPID="+appID
		+"&lang=ru";
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
		weatherData=fixRussian(weatherData);
		return weatherData;		
	}
	private static WeatherData fixRussian(WeatherData weatherData) {
		WeatherData tempWeather = weatherData;
		tempWeather.getMain().setTemp(tempWeather.getMain().getTemp()-273.15f);
		tempWeather.getMain().setFeels_like(tempWeather.getMain().getFeels_like()-273.15f);
		return tempWeather;
	}
	private static String sendApiRequest(String urlHost, String urlParams) {
		String response;
		try {
			 HttpURLConnection.setFollowRedirects(false);
		     HttpURLConnection conn = (HttpURLConnection) new URL(urlHost+"?"+urlParams).openConnection();
		     conn.setRequestMethod("GET");
		     conn.setDoOutput(true);
			 InputStream resultContentIS;
			 if(conn.getResponseCode()==200) {
				 resultContentIS = conn.getInputStream();
			 } else {
				 resultContentIS = conn.getErrorStream();
			 }
	         BufferedReader reader = new BufferedReader(new InputStreamReader(resultContentIS));
	         response = reader.readLine();
		     //System.out.println(response);
		} catch (IOException e) {
			response=null;
			//System.err.println("Can't perform API request: "+urlHost+"?"+urlParams);
			//e.printStackTrace();
		}
		return response;
	}	
}
