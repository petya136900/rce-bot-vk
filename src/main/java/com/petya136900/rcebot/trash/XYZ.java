package com.petya136900.rcebot.trash;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import com.petya136900.rcebot.tools.RegexpTools;

public class XYZ {
	public static void main(String[] args) throws IOException, InterruptedException {
		String message="Бот пары!";
		System.out.println(message);
		System.out.println(RegexpTools.checkRegexp("бот", message));
		System.out.println(message.replaceAll("(?i)"+Pattern.quote("бот"), ""));
		System.out.println(Pattern.compile("(?i)бот",Pattern.UNICODE_CASE).matcher(message).replaceAll(""));
		System.out.println(RegexpTools.removeAllString(message,"бот"));
		System.out.println("Thread-999".substring(0,10));
		try {
			System.out.println(InetAddress.getLocalHost().getHostName());
		} catch(UnknownHostException ignored) {
			
		}
		ConcurrentHashMap<Integer, String> consoles = new ConcurrentHashMap<>();
		consoles.put(1, "One");
		consoles.put(2, "Two");
		consoles.put(3, "Three");
		consoles.put(4, "Four");
		consoles.put(5, "Five");
		consoles.put(6, "Six");
		consoles.put(7, "Seven");
		consoles.put(8, "Eight");
		new Thread(() -> {
			try {
				Thread.sleep(1500);
				consoles.remove(7);
				System.out.println("Removed");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		for(Integer key : consoles.keySet()) {
			System.out.println(key); 
			String value;
			if((value=consoles.get(key))!=null) {
				System.out.println(value);
			}
			Thread.sleep(250);
		}
		new Thread("Bot#1") {
			@Override
			public void run() {
				try {
					Thread.sleep(500);
					System.out.println(Thread.currentThread().getName());
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();		
		new Thread("Bot#1") {
			@Override
			public void run() {
				try {
					Thread.sleep(500);
					System.out.println(Thread.currentThread().getName());
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();			
	}
}
