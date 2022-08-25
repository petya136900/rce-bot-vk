package com.petya136900.rcebot.rce;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.petya136900.rcebot.db.BotSettings;
import com.petya136900.rcebot.db.MySqlConnector;
//import com.petya136900.rcebot.rce.timetable.DateO;
import com.petya136900.rcebot.rce.timetable.TimetableException;
import com.petya136900.rcebot.rce.timetable.TimetableException.ExceptionCode;
import com.petya136900.rcebot.trash.Foo;

public class RCE {
	private static String RCE_HOST; // ="http://127.0.0.1";
	//private static Long lastAddCheck=0L;
	//private static Long addCheckInterval = 1000L*60L*5L;// ms
	// "https://xn--j1al4b.xn--p1ai";
	private static String TTDIR; // = "/rasp/";
	private static final Integer notFoundLimit = 10;
	public static String getRceHost() {
		return RCE_HOST;
	}
	public static String getTTDir() {
		return TTDIR;
	}
	public static String[] checkForDate(String date) throws TimetableException {
		ArrayList<String> vers = new ArrayList<String>();		
		Integer notFoundCur=0;
		Integer counter=-1;
		String urlForCheck;
		BotSettings bs = MySqlConnector.getBotSettings();
		RCE_HOST=bs.getDomain();
		TTDIR=bs.getPath();
		while(notFoundCur<=notFoundLimit) {
			//System.out.println("Обращение к ркэ..");
			if(counter==-1) {
				urlForCheck=RCE_HOST+TTDIR+"/"+date+".pdf";
			} else {
				urlForCheck=RCE_HOST+TTDIR+"/"+date+counter+".pdf";
			}
			if(checkUrl(urlForCheck)) {
				notFoundCur=0;
				if(counter==-1) {
					vers.add("");
				} else {
					vers.add(counter+"");
				}
			} else {
				notFoundCur++;
			}
			if(counter==-1) {
				urlForCheck=RCE_HOST+TTDIR+"/%D0%A0%D0%B0%D1%81%D0%BF%D0%B8%D1%81%D0%B0%D0%BD%D0%B8%D0%B5%20%D0%B7%D0%B2%D0%BE%D0%BD%D0%BA%D0%BE%D0%B2%20%D0%BD%D0%B0%20"+date+".pdf";
			} else {
				urlForCheck=RCE_HOST+TTDIR+"/%D0%A0%D0%B0%D1%81%D0%BF%D0%B8%D1%81%D0%B0%D0%BD%D0%B8%D0%B5%20%D0%B7%D0%B2%D0%BE%D0%BD%D0%BA%D0%BE%D0%B2%20%D0%BD%D0%B0%20"+date+counter+".pdf";
			}
			if(checkUrl(urlForCheck)) {
				if(counter==-1) {
					vers.add("call");
				} else {
					vers.add("call"+counter);
				}
			}
			counter++;
		}
		//String[] calls = additionalCheckForCalls(date);
		return vers.toArray(new String[vers.size()]);
	}
	private static boolean checkUrl(String urlForCheck) throws TimetableException {
		//System.out.println("Чекаю: "+urlForCheck);
		try {
			//System.out.println(urlForCheck);
			HttpURLConnection.setFollowRedirects(false);
			//System.out.println("Открываю соединение..");
			HttpURLConnection conn = (HttpURLConnection) new URL(urlForCheck).openConnection();
			conn.setConnectTimeout(3000);
			conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:221.0) Gecko/20100101 Firefox/31.0");
			//System.out.println("HEAD");
			conn.setRequestMethod("HEAD");
			//System.out.println(conn.getResponseCode());
			//System.out.println(conn.getResponseMessage());
			if(conn.getResponseCode()==HttpURLConnection.HTTP_OK) {
				//System.out.println("Найдено!");
				return true;
			} else {
				//System.out.println("Не Найдено");
				return false;
			}
		} catch(Exception e) {
			e.printStackTrace();
			class MN {}; throw new TimetableException(ExceptionCode.RCE_UNAVAILABLE,Foo.getMethodName(MN.class),e.getLocalizedMessage(),e);
		}
	}

}
