package com.petya136900.rcebot.tools;

import java.lang.reflect.Type;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonParser {
	public static String  toJson(Object obj) {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		return gson.toJson(obj);
	}
	private static Gson getGson() {
		GsonBuilder builder = new GsonBuilder();
		return builder.create();
	}
	public static <T> T   fromJson(String jsonRequest, Class<T> class1) {
		//System.out.println(class1.getName());
		return getGson().fromJson(jsonRequest, class1);
	}
	public static <T> T fromJson(String jsonStr, Type type) {
        return getGson().fromJson(jsonStr, type);
	}
	public static boolean isJson(String Json) {
        try {
            new JSONObject(Json);
        } catch (JSONException ex) {
            try {
                new JSONArray(Json);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }	
}
