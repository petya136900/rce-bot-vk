package com.petya136900.rcebot.handlers;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.vk.VK;

public class HostNameHandler implements HandlerInterface {

	@Override
	public void handle(VK vkContent) {
		vkContent.reply(getUserHostname()+((VK.getTestMode())?" | Test Mode":""));
	}
	public static String getUserHostname() {
		String hostName="UnknownHostName";
		try {
			hostName=InetAddress.getLocalHost().getHostName();
		} catch(UnknownHostException ignored) {}
		return System.getProperty("user.name")+"@"+hostName;
	}

}
