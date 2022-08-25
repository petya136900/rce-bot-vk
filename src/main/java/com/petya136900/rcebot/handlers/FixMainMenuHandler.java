package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.vk.VK;

public class FixMainMenuHandler implements HandlerInterface {

	@Override
	public void handle(VK vkContent) {
		TimetableHandler.loadDefaultKeyboardWithMessage(vkContent,"Главное меню",true);
	}

}
