package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.vk.VK;

public class TestHandler implements HandlerInterface {

	@Override
	public void handle(VK vkContent) {
		vkContent.reply("Test Handle");
	}
}
