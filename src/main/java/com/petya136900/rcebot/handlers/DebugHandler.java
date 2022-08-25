package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.vk.VK;

public class DebugHandler implements HandlerInterface {

	@Override
	public void handle(VK vkContent) {
		vkContent.reply(JsonParser.toJson(vkContent
						.getVK()
						.getObject())
						.replaceAll(",",",\n")
				);
	}

}
