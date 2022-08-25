package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.db.MySqlConnector;
import com.petya136900.rcebot.rce.teachers.Teacher;
import com.petya136900.rcebot.rce.teachers.TeacherPair;
import com.petya136900.rcebot.rce.timetable.MainShelude;
import com.petya136900.rcebot.rce.timetable.TimetableClient;
import com.petya136900.rcebot.rce.timetable.TimetableException;
import com.petya136900.rcebot.rce.timetable.TimetableServer;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.tools.RegexpTools;
import com.petya136900.rcebot.vk.VK;

public class TeacherHandler implements HandlerInterface {
	private String dateValue;
	private int weekInt;
	private Boolean weekType;
	private Teacher[] teachers;
	private Boolean debug=false;
	@Override
	public void handle(VK vkContent) {
		String message = vkContent.getVK().getText().toLowerCase().trim().replaceFirst("(^препод)+(.)*?\\b","").trim();
		String currentStage = vkContent.getVK().getStage();
		StringBuilder sbResponse;	
		switch(currentStage) {
			case("1"):				
				String teacher="";
				String date = "";
				if(RegexpTools.checkRegexp("\\binfo\\b", message)) {
					debug=true;
				}
				message=message.replaceAll("\\binfo\\b","").trim();				
				if(RegexpTools.checkRegexp("\\bна\\b", message)) {
					String stringDate = message.substring(RegexpTools.rIndexOf("\\bна\\b",message));
					try {
						date = TimetableClient.checkDate(stringDate);
					} catch (Exception e) {
						vkContent.reply("ERROR | Неправильно указан день, разрешено: \n"
								+ "День недели, число(dd.MM.yyyy), наречия(Завтра, сегодня)");
						return;
					}
					teacher = message.substring(0, RegexpTools.rIndexOf("\\bна\\b",message)).trim();
				} else {
					date = TimetableClient.getCurDay();
					teacher = message.trim();
				}
				if(teacher.length()<2) {
					vkContent.reply("Преподаватель | Ошибка\n"
							+ "Вы не указали имя преподавателя, например:\n"
							+ "Преподаватель Щербаченко на завтра");
					return;
				}
				teacher=teacher.replaceAll("\\s\\s+/g", " ");
				if(teacher.length()>300) {
					teacher = teacher.substring(0,300);
				}
				dateValue=date;
				try {
					weekInt = TimetableClient.getDayOfWeek(dateValue);
				} catch (TimetableException e) {
					vkContent.reply(e.getMessage());
					return;
				}
				try {
					teachers = MySqlConnector.getTeachers(teacher);
				} catch (TimetableException e1) {
					vkContent.reply(e1.getMessage());
					return;
				}
				try {
					weekType = TimetableClient.getWeekType(date);
					sbResponse = new StringBuilder("Преподаватель | "+
																TimetableClient.beautifyDate(dateValue)+" | "+
																TimetableClient.getDayOfWeekRus(weekInt)+" | "+
																(weekType?"Числитель":"Знаменатель")+"\n\n");
				} catch (TimetableException e) {
					vkContent.reply(e.getMessage());
					return;
				}	
				//sbResponse.append(Arrays.toString(teacher.split(" "))+"\n");
				if(teachers.length<1) {
					sbResponse.append("Преподавателей по данному запросу не найдено\n");
					vkContent.reply(sbResponse.toString());
					return;
				} else if(teachers.length==1) {
					Teacher teacherN = teachers[0];
					sbResponse.append(teacherN.getFullName()+"\n");
					String teacherPairsOnDay = null;
					try {
						teacherPairsOnDay = getTeacherPairs(teacherN,dateValue);
					} catch (TimetableException e) {
						vkContent.reply(e.getMessage());
						return;
					}
					if(teacherPairsOnDay!=null) {
						sbResponse.append(teacherPairsOnDay);
					} else {
						sbResponse.append("В указанный день не найдено пар для этого преподавателя");
					}
					vkContent.reply(sbResponse.toString());
				} else {
					vkContent.getVK().setStage("selectTeacher");
					sbResponse.append("Найдено несколько подходящих преподавателей, Выберите: \n\n0) Выход\n");
					for(int i=0;i<teachers.length;i++) {
						if(sbResponse.length()>VK.MAX_MESSAGE_LENGTH) {
							vkContent.reply(sbResponse +"\n");
							sbResponse.setLength(0);
						}
						sbResponse.append((i+1)+ ") "+teachers[i].getFullName()+"\n");
					}
					vkContent.reply(sbResponse.toString());
				}
				//vkContent.getVK().setStage("2");
			break;
			case("selectTeacher"):
				Integer num;
				try {
					sbResponse = new StringBuilder("Преподаватель | "+
																TimetableClient.beautifyDate(dateValue)+" | "+
																TimetableClient.getDayOfWeekRus(weekInt)+" | "+
																(weekType?"Числитель":"Знаменатель")+"\n\n");
				} catch (Exception e) {
					vkContent.reply("Unkwn error");
					return;
				}	
				if(RegexpTools.checkRegexp("(выход|exit|quit|leave|(\\b0\\b))", message.toLowerCase())) {
					vkContent.reply("Вышел!");
					vkContent.getVK().removeStages();
					return;
				}
				try {
					num = Integer.parseInt(message);
				} catch(Exception e) {
					vkContent.reply("Преподаватель | Ошибка\nНе верно указан номер преподавателя\n"
							+ "Выберите доступное число или 0 для выхода");
					return;
				}
				if(num>teachers.length) {
					vkContent.reply("Преподаватель | Ошибка\nНе верно указан номер преподавателя\n"
							+ "Выберите доступное число(1-"+teachers.length+") или 0 для выхода");
					return;
				}
				Teacher selectedTeached = teachers[num-1];
				vkContent.getVK().removeStages();
				//vkContent.reply("Вы выбрали: "+selectedTeached.getFullName()+"["+selectedTeached.getId()+"]"+"\n");
				sbResponse.append(selectedTeached.getFullName()+"\n");
				String teacherPairsOnDay = null;
				try {
					teacherPairsOnDay = getTeacherPairs(selectedTeached,dateValue);
				} catch (TimetableException e) {
					vkContent.reply(e.getMessage());
					return;
				}
				if(teacherPairsOnDay!=null) {
					sbResponse.append(teacherPairsOnDay);
				} else {
					sbResponse.append("В указанный день не найдено пар для этого преподавателя");
				}
				vkContent.reply(sbResponse.toString());				
			break;
			default:
				vkContent.reply("class TeacherHandler: ERROR\nUnknown action");
				vkContent.getVK().removeStages();
			break;
		}
	}
	private String getTeacherPairs(Teacher teacher, String date) throws TimetableException {
		TeacherPair[] teacherPairs = MySqlConnector.getTeachersPairs(teacher.getId());
		if(teacherPairs.length<1) {
			return("Пары этого преподавателя не заполнены(0)");
		}
		new TimetableServer(date,"").get(true);
		MainShelude[] replaces = CabinetHandler.sortAscending(MySqlConnector.getAllTimetablesByPairNames(date,teacherPairs));
		StringBuilder sb = new StringBuilder("");
		Boolean wasReplases=false;
		Boolean wasMain=false;
		if(replaces!=null) {
			for(MainShelude r : replaces) {
				if(!wasReplases) {
					wasReplases=true;
					sb.append("По замене: \n");
				}
				//System.out.println(r.getPairs()[0].getPairNum()+" Пара - "+r.getGroupName()+" - "+r.getPairs()[0].getPairName()+" | "+r.getPairs()[0].getPairCab()+"\n");
				sb.append(r.getPairs()[0].getPairNum()+" Пара - "+r.getGroupName()+" - "+r.getPairs()[0].getPairName()+" | "+r.getPairs()[0].getPairCab()+"\n");
			}
		}
		String[] groupsWithReplace = MySqlConnector.getGroupsWithReplace(date);
		MainShelude[] mains = CabinetHandler.sortAscending(MySqlConnector.getMainsTimetablesByPairNames(TimetableClient.getDayOfWeekEng(this.weekInt),weekType,teacherPairs,groupsWithReplace));
		for(MainShelude r : mains) {
			if(!wasMain) {
				wasMain=true;
				sb.append("По основному: \n");
			}
			sb.append(r.getPairs()[0].getPairNum()+" Пара - "+r.getGroupName()+" - "+r.getPairs()[0].getPairName()+" | "+r.getPairs()[0].getPairCab()+"\n");
		}
		if(!(wasReplases|wasMain)) {
			return null;
		}
		if(debug) {
			sb.append("\n"+JsonParser.toJson(teacherPairs));
		}
		return sb +"\n";
	}
	public static String removeTrash(String string) {
		String fixedString=string.replace("ё","е");
		fixedString=fixedString.replace("Ё","Е");
		fixedString = fixedString.replaceAll("([^а-яА-Яa-zA-Z0-9 \\.\\-])", "").trim();
		fixedString=fixedString.replaceAll("\\s\\s+", " ");
		if(fixedString.length()>300) {
			fixedString=fixedString.substring(0,300).trim();
		}
		return fixedString;
	}
}
