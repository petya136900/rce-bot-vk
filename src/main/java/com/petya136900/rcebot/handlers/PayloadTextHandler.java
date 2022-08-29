package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.lifecycle.MainHandler;
import com.petya136900.rcebot.lifecycle.Mentions;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.Payload;

public class PayloadTextHandler implements HandlerInterface {

	@Override
	public void handle(VK vkContent) {
		try {
			Payload payload = JsonParser.fromJson(vkContent.getVK().getPayload(),Payload.class);
			vkContent.getVK().setText((payload.getData()==null?"":payload.getData()));
			vkContent.getVK().setPayload(null);
			Mentions.isMention(vkContent.getVK());
			if((!MainHandler.getTestMode())|MainHandler.checkTestMode(vkContent.getVK().getFrom_id(),true,vkContent.getVK().getPeer_id())) {
				MainHandler.findHandler(vkContent,false);
			}
		} catch(Exception e) {
			vkContent.reply("ERROR | Кнопка была создана с ошибками");
		}
	}

}
