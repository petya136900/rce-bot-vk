package com.petya136900.rcebot.rce.timetable;

import com.petya136900.rcebot.db.BotSettings;
import com.petya136900.rcebot.tools.RegexpTools;

public class MainsheludeData {
	private String password;
	private MainShelude[] groups;
	private BotSettings botSettings;
	private String ip;
	/**
	 * @return the groups
	 */
	public MainShelude[] getGroups() {
		return groups;
	}
	/**
	 * @param groups the groups to set
	 */
	public void setGroups(MainShelude[] groups) {
		this.groups = groups;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	public void fixData() {
		for(MainShelude group : groups) {
			group.setGroupName(TimetableClient.removeTrash(TimetableClient.toRus(group.getGroupName())));
			for(BlockPair pair : group.getPairs()) {
				pair.setPairName(removeTrashPair(pair.getPairName()));
				pair.setPairCab(removeTrashPair(pair.getPairCab()));
			}
		}
	}
	public static String removeTrashPair(String string) {
		String newString=string.replaceAll("\\\\","/");
		newString = newString.replace("ё","е");
		newString = newString.replace("Ё","Е");
		newString = RegexpTools.replaceRegexp(newString, "([^а-яА-Я a-zA-Z!:,\\.\\/\\d\\|\\-])", "", true);
		newString = newString.replaceAll("\\s\\s+", " ");
		return newString;
	}

	/**
	 * @return the botSettings
	 */
	public BotSettings getBotSettings() {
		return botSettings;
	}
	/**
	 * @param botSettings the botSettings to set
	 */
	public void setBotSettings(BotSettings botSettings) {
		this.botSettings = botSettings;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
}
