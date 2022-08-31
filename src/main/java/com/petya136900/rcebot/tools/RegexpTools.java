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
		Matcher matcher = getMatcher(text.toLowerCase(),stringPattern);
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
		Matcher matcher = getMatcher(text.toLowerCase(),whatRegexp);
		if(matcher.find()) {
			return matcher.start();
		} else {
			return -1;
		}
	}
	public static String rString(String s, int times) {
		if(times<1)
			return "";
		return new String(new char[times]).replace("\0", s);
	}
    public static Matcher getMatcher(String string, String regexp) {
		Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
		return pattern.matcher(string);
    }
}
