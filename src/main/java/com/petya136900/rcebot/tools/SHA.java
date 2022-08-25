package com.petya136900.rcebot.tools;

import org.apache.commons.codec.digest.DigestUtils;

public class SHA {
	public static String hashSHA256(String string) {
		return DigestUtils.sha256Hex(string.getBytes());
	}
}
