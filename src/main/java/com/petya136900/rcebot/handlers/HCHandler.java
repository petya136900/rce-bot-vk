package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.vk.VK;

public class HCHandler implements HandlerInterface {

	@Override
	public void handle(VK vkContent) {
		String stageName = vkContent.getVK().getStage();
		vkContent.reply("Stage: "+stageName);
		//vkContent.getVK().getPayload();
		switch(stageName) {
			case("0"):
				vkContent.reply("Initial");
				break;
			default:
				vkContent.reply("Error: Unknown Stage");
				break;
		}
	}

}
