package com.petya136900.rcebot.tools;

import com.google.re2j.Matcher;
import com.google.re2j.Pattern;

public class RegexpTools {
	public static StoredString storeString(String text) {
		return new StoredString(text);
	}
	public static Boolean checkRegexp(Pattern keyP, String text) {
		Matcher matcher=keyP.matcher(text.trim().toLowerCase());
		return matcher.find();
	}
	public static Boolean checkRegexp(String stringPattern, String text) {
		Pattern keyP = Pattern.compile(stringPattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher=keyP.matcher(text.trim().toLowerCase());
		return matcher.find();
	}
	public static String removeFirstString(String message, String remove) {
		return replaceRegexp(message, remove, "",false);
	}
	public static String removeAllString(String message, String remove) {
		return replaceRegexp(message, remove, "",true);
	}
	public static String replaceRegexp(String message, String what, String replacement, Boolean all) {
		if (message==null) {
			return "";
		}
		if(all) {
			return Pattern.compile("(?i)"+what,Pattern.CASE_INSENSITIVE).matcher(message).replaceAll(replacement);
		} else {
			return Pattern.compile("(?i)"+what,Pattern.CASE_INSENSITIVE).matcher(message).replaceFirst(replacement);
		}
	}
	public static int rIndexOf(String whatRegexp, String text) {
		Pattern keyP = Pattern.compile(whatRegexp, Pattern.CASE_INSENSITIVE);
		Matcher matcher=keyP.matcher(text.trim().toLowerCase());
		if(matcher.find()) {
			return matcher.start();
		} else {
			return -1;
		}
	}		
}
