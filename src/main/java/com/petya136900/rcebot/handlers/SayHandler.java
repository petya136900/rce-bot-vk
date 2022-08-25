package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.Payload;

public class SayHandler implements HandlerInterface {

	@Override
	public void handle(VK vkContent) {	
		if(vkContent.getVK().getPayload()==null) {
			vkContent.reply("ERROR | Данная команда доступна только для кнопок");
			return;
		}
		try {
			Payload payload = JsonParser.fromJson(vkContent.getVK().getPayload(), Payload.class);
			if(payload.getStage().equals(CreateKeyboardHandler.getMD5x2(payload.getData(), vkContent.getVK().getPeer_id()))) {
				vkContent.reply(payload.getData());
			} else {
				vkContent.reply("ERROR | Попытка подделать содержимое кнопки пользователем vk.com/id"+vkContent.getVK().getFrom_id());	
			}
		} catch(Exception e) {
			vkContent.reply("ERROR | Кнопка была создана с ошибками");
		}
	}

}
