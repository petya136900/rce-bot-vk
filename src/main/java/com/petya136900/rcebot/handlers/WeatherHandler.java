package com.petya136900.rcebot.handlers;

import java.text.SimpleDateFormat;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.geoapi.WeatherAPI;
import com.petya136900.rcebot.geoapi.WeatherData;
import com.petya136900.rcebot.geoapi.WeatherException;
import com.petya136900.rcebot.other.Tokens;
import com.petya136900.rcebot.vk.VK;

public class WeatherHandler implements HandlerInterface {
	private static final String WEATHER_APIKEY = Tokens.OPENWEATHER_API_KEY;
	@Override
	public void handle(VK vkContent) {
		try {
			WeatherData weather = new WeatherAPI(WEATHER_APIKEY)
												.getWeatherByCityName(vkContent.getVK().
					getText()
					.toLowerCase()
					.replace("погода", "")
					.replace("weather", "")
					.trim());
			StringBuilder sb = new StringBuilder("");
			
			sb.append("\n"+weather.getName()+" - Погода - "+weather.getWeather()[0].getDescription());
			if(weather.getRain()!=null) {
				if(weather.getRain().get_1h()!=null) {
					sb.append("\n"+"Уровень осадков за час: "+weather.getRain().get_1h()+"mm");
				}
				if(weather.getRain().get_3h()!=null) {
					sb.append("\n"+"Уровень осадков за 3 часа: "+weather.getRain().get_3h()+"mm");	
				}				
			}
			if(weather.getSnow()!=null) {
				if(weather.getSnow().get_1h()!=null) {
					sb.append("\n"+"Уровень снега за час: "+weather.getSnow().get_1h()+"mm");
				}
				if(weather.getSnow().get_3h()!=null) {
					sb.append("\n"+"Уровень снега за 3 часа: "+weather.getSnow().get_3h()+"mm");
				}					
			}				
			sb.append("\n"+"Температура: "+String.format("%8.2f", weather.getMain().getTemp())+"°C");
			sb.append("\n"+"Ощущается как "+String.format("%8.2f", weather.getMain().getFeels_like())+"°C");
			sb.append("\n"+"Влажность: "+weather.getMain().getHumidity()+"%");
			sb.append("\n"+"Видимость: "+weather.getVisibility()+"м.");
			sb.append("\n"+"Давление: "+weather.getMain().getPressure());
			sb.append("\n"+"Скорость ветра: "+weather.getWind().getSpeed()+"м/s");
			sb.append("\n"+"Облачность: "+weather.getClouds().getAll()+"%");
			sb.append("\n"+"Закат: "+timeStampToTime(weather.getSys().getSunset()));
			sb.append("\n"+"Восход: "+timeStampToTime(weather.getSys().getSunrise()));
			vkContent.reply(sb.toString());
		} catch (WeatherException we) {
			vkContent.reply(we.getMessage());
			//we.printStackTrace();
		}
	}
	private static String timeStampToTime(Long timeStamp) {
		return new SimpleDateFormat("HH:mm").format(new java.util.Date((long)timeStamp*1000));
	}
}
