package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.lifecycle.MainHandler;
import com.petya136900.rcebot.db.BotSettings;
import com.petya136900.rcebot.db.MySqlConnector;
import com.petya136900.rcebot.rce.timetable.TimetableException;
import com.petya136900.rcebot.tools.RegexpTools;
import com.petya136900.rcebot.vk.VK;

public class BotSettingsHandler implements HandlerInterface {

	@Override
	public void handle(VK vkContent) {
		if(MainHandler.checkAdmin(vkContent.getVK().getFrom_id())) {
			String command = vkContent.getVK().getText().replaceFirst("settings","").trim();
			try {
				BotSettings bs = MySqlConnector.getBotSettings();
				if(RegexpTools.checkRegexp("^get -all", command)) {
					vkContent.reply(bs.getAll());
				} else if(RegexpTools.checkRegexp("^get", command)){
					String varName;
					vkContent.reply((varName=(command.replace("get", "").trim())).length()>0?bs.get(varName):"[ERROR] Вы не указали переменную");
				} else if(RegexpTools.checkRegexp("^set -default", command)) {
					bs.setDefault();
					vkContent.reply("Настройки по-умолчанию загружены");
				} else if(RegexpTools.checkRegexp("^set", command)) {
					String varName=(command.replaceFirst("set", "").trim());
					if(!(varName.length()>0)) {
						vkContent.reply("[ERROR] | Вы не указали переменную");
					} else if(varName.indexOf(" ")==-1) {
						vkContent.reply("[ERROR] | Вы не указали значение для переменной");
					} else {
						varName=varName.substring(0,varName.indexOf(" ")).trim();
						String value = command.replaceFirst("set", "").trim().replace(varName, "").trim();
						vkContent.reply(bs.set(varName,value));
					}
				} else if(RegexpTools.checkRegexp("^drop db", command)) {
					MySqlConnector.dropDB();
					vkContent.reply("БД удалена");
				} else {
					vkContent.reply("[ERROR] | Неизвестная команда");
				}
			} catch(TimetableException te) {
				vkContent.reply(te.getMessage());
				VK.sendMessage(VK.getAdminID(), te.messageForAdmin(vkContent));		
			}
		} else {
			vkContent.reply("[ERROR] | Access denied");
		}
	}

}
