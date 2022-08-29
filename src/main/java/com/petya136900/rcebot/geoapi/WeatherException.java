package com.petya136900.rcebot.geoapi;
import java.util.HashMap;

public class WeatherException extends Exception {
	private static final long serialVersionUID = -1464027277079081698L;
	public enum ExceptionCode {
		BAD_CITY,
		SET_CITY,
		BAD_APPID,
		ACC_DISABLED,
		UNKWN_ERROR
	}
	private static final HashMap<ExceptionCode, String> descriptions = new HashMap<ExceptionCode, String>(){
		private static final long serialVersionUID = -4570336837213840322L;
		{
			put(ExceptionCode.BAD_CITY, 
				"Город не найден");
			put(ExceptionCode.SET_CITY, 
				"Вы не указали название города");
			put(ExceptionCode.BAD_APPID,
				"Неверный WeatherAPI key");
			put(ExceptionCode.ACC_DISABLED,
				"Аккаунт отключен");
			put(ExceptionCode.UNKWN_ERROR, 
				"Неизвестная ошибка: %s");
	}};	
	private String message;
	public WeatherException(ExceptionCode code, String ...  additionValues) {
		Object[] addInfo = new Object[additionValues.length];
		for(int i=0;i<additionValues.length;i++) {
			addInfo[i]=additionValues[i];
		}
		this.message = String.format(descriptions.get(code),addInfo); 
	}
	public WeatherException(ExceptionCode code) {
		this.message = descriptions.get(code);
	}	
	@Override
	public StackTraceElement[] getStackTrace() {
		return super.getStackTrace();
	}
	public String getStringStackTrace() {
		return toString(getStackTrace());
	}
	private String toString(StackTraceElement[] stackTrace) {
		StringBuilder sb = new StringBuilder("");
		for(StackTraceElement stack : stackTrace) {
			sb.append(stack.toString()+"\n");
		}
		return sb.toString();
	}
	@Override
	public String getMessage() {
		return message;
	}	
}
