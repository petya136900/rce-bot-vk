package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.Payload;

public class CabOnDayHandler implements HandlerInterface {

	@Override
	public void handle(VK vkContent) {
		Payload payload = JsonParser.fromJson(vkContent.getVK().getPayload(), Payload.class);
		String day = payload.getData().split(",")[0];
		String offset = payload.getData().split(",")[1];
		//System.out.println("Запрос кабинета: "+payload.getStage()+" на "+day+" offset: "+offset);
		vkContent.getVK().setText("Кабинет "+payload.getStage()+" на "+day);
		vkContent.getVK().setPayload(null);
		new CabinetHandler(true,day,Integer.parseInt(offset)).handle(vkContent);
	}

}
