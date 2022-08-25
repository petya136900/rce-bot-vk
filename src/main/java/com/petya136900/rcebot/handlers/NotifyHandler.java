package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.db.MySqlConnector;
import com.petya136900.rcebot.rce.timetable.PeerSettings;
import com.petya136900.rcebot.rce.timetable.TimetableException;
import com.petya136900.rcebot.rce.timetable.TimetableException.ExceptionCode;
import com.petya136900.rcebot.tools.RegexpTools;
import com.petya136900.rcebot.vk.VK;

public class NotifyHandler implements HandlerInterface {

	@Override
	public void handle(VK vkContent) {
		Boolean badInput=true;
		try {
			String message = vkContent.getVK().getText();
			PeerSettings ps = MySqlConnector.getPeerSettings(vkContent.getVK().getPeer_id());
			String notifHour = RegexpTools.replaceRegexp(message, "([^\\d])", "",true).trim();
			if(ps==null) {
				vkContent.reply("Для настройки уведомлений сначала \nзапросите расписание для Вашей группы");
				return;				
			} else if(ps.getGroupName().trim().length()<1) {
				vkContent.reply("Для настройки уведомлений сначала \nзапросите расписание для Вашей группы");
				return;	
			}
			StringBuilder sb = new StringBuilder("");
			if(RegexpTools.checkRegexp("( выкл| откл)", message)) {
				badInput=false;
				if(ps.getNotifications().equals(0)) {
					vkContent.reply("Уведомления уже отключены");
				} else { 
					// Отключаем уведомления
					ps.setNotifications(0);
					MySqlConnector.updatePeerSettings(ps);
					vkContent.reply("Уведомления отключены");
				}
				return;
			} else {
				if(notifHour.length()!=0) {
					badInput=false;
					if(notifHour.length()>2) {
						sb.append("Не верно указан час, допустимо от 0 до 18");
					} else {
						Integer hour = Integer.parseInt(notifHour);
						if((hour>18)|(hour<0)) {
							sb.append("Не верно указан час, допустимо от 0 до 18");
						} else {
							if(!(ps.getNotifHour().equals(hour))) {
								sb.append("Час изменен на "+hour+"\n");
								ps.setNotifHour(hour);
							}
						}
					}
				}
				if(RegexpTools.checkRegexp("( вкл)", message)) {
					badInput=false;
					if(ps.getNotifications().equals(1)) {
						sb.append("Уведомления уже включены ("+(ps.getWorkMode().equals(2)?"Только замены":"Все")+")\n");
						sb.append("С "+ps.getNotifHour()+" часов");
						// Включаем уведомления
					} else {
						ps.setNotifications(1);
						if(RegexpTools.checkRegexp("( все)", message)) {
							ps.setWorkMode(1);
						} else if(RegexpTools.checkRegexp("( замен)", message)) {
							ps.setWorkMode(2);
						}
						sb.append("Уведомления включены ("+(ps.getWorkMode().equals(2)?"Только замены":"Все")+")\n");
						sb.append("С "+ps.getNotifHour()+" часов");
					}
				} else if(RegexpTools.checkRegexp("( все)", message)) {
					badInput=false;
					sb.append("Уведомления включены ("+"Все"+")\n");
					sb.append("С "+ps.getNotifHour()+" часов");					
					ps.setWorkMode(1);
				} else if(RegexpTools.checkRegexp("( замен)", message)) {
					badInput=false;
					sb.append("Уведомления включены ("+"Только замены"+")\n");
					sb.append("С "+ps.getNotifHour()+" часов");					
					ps.setWorkMode(2);
				}
				if(badInput) {
					vkContent.reply("Вы пропустили аргумент, синтаксис команды: \n"
							+ "Уведомления [вкл|откл] [час(0-18)] [все|замены]");
				} else {
					MySqlConnector.updatePeerSettings(ps);
					vkContent.reply(sb.toString());
				}
			}
		} catch(TimetableException te) {
			vkContent.reply(te.getMessage());
			if(!(te.getCode().equals(ExceptionCode.BAD_DAY))
			&!(te.getCode().equals(ExceptionCode.BAD_GROUP))
			&!(te.getCode().equals(ExceptionCode.SET_GROUP))
			&!(te.getCode().equals(ExceptionCode.SUNDAY))) {
				VK.sendMessage(VK.getAdminID(), te.messageForAdmin(vkContent));
			}
		}
	}
}
