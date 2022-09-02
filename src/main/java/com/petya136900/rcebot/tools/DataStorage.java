package com.petya136900.rcebot.tools;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class DataStorage {
	private static final ConcurrentHashMap<Object, HashMap<String, Object>> data = new ConcurrentHashMap<Object, HashMap<String, Object>>();
	public static HashMap<String, Object> getHM(Object o) {
		return data.get(o);
	}
	public static HashMap<String, Object> putHM(Object o) {
		HashMap<String, Object> nHM = new HashMap<String, Object>();
		data.put(o, nHM);
		return nHM;
	}
	public static void clearHM(Object o) {
		data.remove(o);
	}
}
