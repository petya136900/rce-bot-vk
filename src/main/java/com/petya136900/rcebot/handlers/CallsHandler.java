package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.db.MySqlConnector;
import com.petya136900.rcebot.rce.timetable.TimetableClient;
import com.petya136900.rcebot.rce.timetable.TimetableException;
import com.petya136900.rcebot.tools.RegexpTools;
import com.petya136900.rcebot.vk.VK;

public class CallsHandler implements HandlerInterface {
	private String date;
	private final String AT=" на ";
	@Override
	public void handle(VK vkContent) {
		try {
			if(RegexpTools.checkRegexp("(( )на( ))", vkContent.getVK().getText())) {
				setDate(vkContent.getVK().getText());
			} 
			Integer dayOfWeek=null;
			if(date==null) {
				dayOfWeek = TimetableClient.getDayOfWeek(TimetableClient.getCurDay());
			} else if(date.length()<2) {
				dayOfWeek = TimetableClient.getDayOfWeek(TimetableClient.getCurDay());
			} else {
				try {
				dayOfWeek = TimetableClient.getDayOfWeek(TimetableClient.checkDate(date));
				} catch(Exception e) {
					vkContent.reply("ERROR | Неправильно указан день\n"
							+ "Формат: dd.MM.yyyy, например, 05.06.2001 или день недели, понедельник\\вторник и т.п.");
					return;
				}
			}
			if(dayOfWeek==1) {
				vkContent.reply("Воскресенье - выходной");
				return;
			}
			StringBuilder sb = new StringBuilder("");
			String[] arrayCalls;
			if(date==null) {
				date=TimetableClient.getCurDay();
			}
			String callReplaceMessage = MySqlConnector.getCallReplace(TimetableClient.checkDate(date));
			sb.append(TimetableClient.getDayOfWeekRus(dayOfWeek)+"\n");
			if(callReplaceMessage!=null) {
				sb.append("Звонки | "+TimetableClient.beautifyDate(TimetableClient.checkDate(date))+"\n"+callReplaceMessage);
			} else {
				if(dayOfWeek==7) {
					arrayCalls=TimetableClient.saturdayCalls;
				} else {
					arrayCalls=TimetableClient.normalCalls;
				}
				for(int i=0;i<arrayCalls.length;i++) {
					sb.append(i+" Пара: "+arrayCalls[i]+"\n");
				}
			}
			vkContent.reply(sb.toString());
		} catch(TimetableException te) {
			vkContent.reply("Something went wrong..");
		}
	}
	private void setDate(String message) {
		this.date=message.substring(message.indexOf(AT)).replace(AT, "");
	}
}
