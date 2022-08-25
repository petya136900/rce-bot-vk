package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.db.MySqlConnector;
import com.petya136900.rcebot.rce.timetable.TimetableException;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.Payload;

public class CabsOnWeekHandler implements HandlerInterface {

	@Override
	public void handle(VK vkContent) {
		// vkContent.reply("Кабинеты на завтра",null,CreateKeyboardHandler.createCabsOnDay("завтра"));
		if(vkContent.getVK().getPayload()!=null) {
			Payload payload;
			Boolean keyboardReplaced = true;
			try {
				keyboardReplaced = MySqlConnector.getKeyboardReplaced(vkContent.getVK().getPeer_id());
			} catch(TimetableException te) {
				keyboardReplaced=true;
			}
			if(keyboardReplaced) {
				vkContent.reply("У вас установлена пользовательская клавиатура.. \nСбросьте её командой \"Клавиатура сброс\"");
				return;
			}
			try {
				payload = JsonParser.fromJson(vkContent.getVK().getPayload(), Payload.class);
				String day = payload.getStage();
				vkContent.reply(day,null,CreateKeyboardHandler.createCabsOnDay(day,vkContent.getVK().getPeer_id(),0));
			} catch (Exception e) {
				TimetableHandler.loadDefaultKeyboardWithMessage(vkContent,TimetableHandler.IDK_ERROR,true);
			}
			return;
		}

	}

}
