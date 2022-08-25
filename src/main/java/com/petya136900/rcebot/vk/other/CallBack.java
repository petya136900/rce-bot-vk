package com.petya136900.rcebot.vk.other;

import com.petya136900.rcebot.web.SpringServer;

public class CallBack {
	private static SpringServer springServer;
	private static Boolean callBackEnabled=false;
	private static String confirmationCode="";
	public static Boolean getEnabled() {
		return callBackEnabled;
	}
	synchronized public static void start(Integer port, String[] args) {
		if(!LongPoll.getEnabled()) {
			if(!callBackEnabled) {
				callBackEnabled=true;
				springServer = new SpringServer(args);
				springServer.setPort(port);
				new Thread(springServer).start(); //
			} else {
				System.out.println("Callback already launched");
			}
		} else {
			System.out.println("Уже запущен LongPoll-сервер");
		}
	}
	
	public static void stop() {
		if(springServer!=null) {
			springServer.stopServer();
		} 
	}
	public static void setConfirmationCode(String confirmationCode) {
		CallBack.confirmationCode=confirmationCode;
	}	
	public static String getConfirmationCode() {
		return confirmationCode;
	}		
}
