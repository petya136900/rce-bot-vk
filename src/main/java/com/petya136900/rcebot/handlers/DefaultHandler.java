package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.vk.VK;

public class DefaultHandler implements HandlerInterface {
	private String message="Default message";
	public DefaultHandler(String message) {
		this.message=message;
	}
	@Override
	public void handle(VK vkContent) {
		vkContent.reply(message);
	}
}
