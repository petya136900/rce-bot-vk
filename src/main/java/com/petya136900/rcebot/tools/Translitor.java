package com.petya136900.rcebot.tools;

import java.util.HashMap;
import java.util.Map;

import com.ibm.icu.text.Transliterator;

public class Translitor {
	 public static final String CYRILLIC_TO_LATIN = "Cyrillic-Latin";
	 public static final String LATIN_TO_CYRILLIC = "Latin-Cyrillic";
	 private static final Map<String, String> letters = new HashMap<String, String>();
	 static {
	    letters.put("F", "А");
		letters.put("<", "Б");
		letters.put("D", "В");
		letters.put("U", "Г");
		letters.put("L", "Д");
		letters.put("T", "Е");
		letters.put("~", "Ё");
		letters.put(":", "Ж");
		letters.put("P", "З");
		letters.put("B", "И");
		letters.put("Q", "Й");
		letters.put("R", "К");
		letters.put("K", "Л");
		letters.put("V", "М");
		letters.put("Y", "Н");
		letters.put("J", "О");
		letters.put("G", "П");
		letters.put("H", "Р");
		letters.put("C", "С");
		letters.put("N", "Т");
		letters.put("E", "У");
		letters.put("A", "Ф");
		letters.put("{", "Х");
		letters.put("W", "Ц");
		letters.put("X", "Ч");
		letters.put("I", "Ш");
		letters.put("O", "Щ");
		letters.put("}", "Ъ");
		letters.put("S", "Ы");
		letters.put("M", "Ь");
		letters.put("\"", "Э");
		letters.put(">", "Ю");
		letters.put("Z", "Я");
		letters.put("f", "а");
		letters.put(",", "б");
		letters.put("d", "в");
		letters.put("u", "г");
		letters.put("l", "д");
		letters.put("t", "е");
		letters.put("`", "ё");
		letters.put(";", "ж");
		letters.put("p", "з");
		letters.put("b", "и");
		letters.put("q", "й");
		letters.put("r", "к");
		letters.put("k", "л");
		letters.put("v", "м");
		letters.put("y", "н");
		letters.put("j", "о");
		letters.put("g", "п");
		letters.put("h", "р");
		letters.put("c", "с");
		letters.put("n", "т");
		letters.put("e", "у");
		letters.put("a", "ф");
		letters.put("[", "х");
		letters.put("w", "ц");
		letters.put("x", "ч");
		letters.put("i", "ш");
		letters.put("o", "о");
		letters.put("]", "ъ");
		letters.put("s", "ы");
		letters.put("m", "ь");
		letters.put("'", "э");
		letters.put(".", "ю");
		letters.put("z", "я");
		letters.put("/", ".");
	}	 
	public String toCyrillic(String latinString) {
		Transliterator toCyrillicTrans = Transliterator.getInstance(LATIN_TO_CYRILLIC);
		return toCyrillicTrans.transliterate(latinString);
	}
	public String toLatin(String cyrillicString) {
		Transliterator toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);
		return toLatinTrans.transliterate(cyrillicString);
	}
	public String fixTranslit(String brokenString) {
		StringBuilder sb = new StringBuilder(brokenString.length());
		for (int i = 0; i<brokenString.length(); i++) {
			String l = brokenString.substring(i, i+1);
			if (letters.containsKey(l)) {
				sb.append(letters.get(l));
			} else {
				sb.append(l);
			}
		}
		return sb.toString();		
	}
}
