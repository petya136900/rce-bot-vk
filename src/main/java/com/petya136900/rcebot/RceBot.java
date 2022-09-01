package com.petya136900.rcebot;

import com.petya136900.rcebot.tools.*;
import org.fusesource.jansi.AnsiConsole;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.petya136900.rcebot.lifecycle.Logger;
import com.petya136900.rcebot.lifecycle.NotifyLoop;
import com.petya136900.rcebot.handlers.DefaultHandler;
import com.petya136900.rcebot.vk.VK;
@SpringBootApplication
public class RceBot {
	
	static {
		AnsiConsole.systemInstall();
	}
	static String[] myBotNames = new String[] { // case-insensitive
			"бот",
			"рке",
			"ркэ",
			"rce_bot",
			"rce",
			"club189353129",
	};
	
	public static void main(String[] args) {
		Settings settings = ConsoleParser.parseArgs(args);
		settings.addBotNames(myBotNames); // The names to which the bot reacts in chats
		// GROUP_TOKEN contained within bot.ini file(INI format, any section)
		System.out.println(settings);
		VK.setAdminID(550940196);
		VK.setup(settings);
		VK.addHandler("Hello!$",vkContent ->
				VK.sendMessage(vkContent.getVK().getPeer_id(), "World!")
		);
		VK.registerBasicHandlers(); // Register Handlers are located in HandlerMapping
		VK.setDefaultHandler(new DefaultHandler("Я не понял тебя, возможно, ты пропустил слово Пары?\n"
				+ "Например: Бот, Пары на завтра\n"+
				"Напиши \"Справка\" для получения команд")); // Register Default Handler
		VK.setPerformOnlyHandler(true); // Handle only the first matching handler
		VK.setTestMode(settings.getTestMode()); // Reply only to admin
		Logger.setCheckSql(!settings.getTestMode());
		Logger.setSendToVk(!settings.getTestMode());

		if(!settings.getTestMode()) {
			NotifyLoop nl = new NotifyLoop();
			nl.setDaemon(false);
			nl.start();
		}
	}
}