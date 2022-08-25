package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.vk.VK;

public class EnterHandler implements HandlerInterface {
	private static String login;
	private static String password;
	@Override
	public void handle(VK vkContent) {
		String message = vkContent.getVK().getText().trim();
		switch(vkContent.getVK().getStage()) {
		case("1"):
			vkContent.reply("Выполняется вход!\n"
					+ "Введите Ваш логин: ");
			vkContent.getVK().setStage("2");
			break;
		case("2"):
			login = message;
			vkContent.reply("Теперь введите пароль: ");
			vkContent.getVK().setStage("3");			
			break;
		case("3"):
			password = message;
			vkContent.reply("Вы зарегистрированы!\n"
					+ "Ваш логин: "+login+"\n"
							+ "Ваш пароль: "+password);
			vkContent.getVK().removeStages();
			break;		
		}
	}

}
