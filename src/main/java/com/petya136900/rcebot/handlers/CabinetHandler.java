package com.petya136900.rcebot.handlers;

import java.util.ArrayList;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.db.BotSettings;
import com.petya136900.rcebot.db.MySqlConnector;
import com.petya136900.rcebot.rce.timetable.MainShelude;
import com.petya136900.rcebot.rce.timetable.TimetableClient;
import com.petya136900.rcebot.rce.timetable.TimetableException;
import com.petya136900.rcebot.rce.timetable.TimetableServer;
import com.petya136900.rcebot.tools.RegexpTools;
import com.petya136900.rcebot.vk.VK;

public class CabinetHandler implements HandlerInterface {
	private boolean from_keyboard=false;
	private String day;
	private Integer offset;
	
	private String dateForStepTwo;
	
	public CabinetHandler() {
		// TODO Auto-generated constructor stub
	}
	public CabinetHandler(boolean from_keyboard,String day, Integer offset) {
		this.from_keyboard=from_keyboard;
		this.day=day;
		this.offset=offset;
	}
	@Override
	public void handle(VK vkContent) {
		if(vkContent.getVK().getStage()!=null) {
			switch(vkContent.getVK().getStage()) {
			case("cabname"):
				if(vkContent.getVK().getText().trim().equals("0")) {
					vkContent.getVK().removeStages();
					vkContent.reply("Окей!");
					return;
				}
				vkContent.getVK().setText("кабинет "+vkContent.getVK().getText().trim()+" на "+dateForStepTwo);
				vkContent.reply("Для выхода из режима напишите 0");
				vkContent.getVK().setStage("cabnameentered");
				this.handle(vkContent);
				return;
			case("cabnameentered"):
				vkContent.getVK().setStage("cabname");
				break;
			default:
				vkContent.getVK().removeStages();
				break;
			}
		}
		String message = vkContent.getVK().getText().toLowerCase().trim().replaceFirst("(^каб)+(.)*?($| )","");
		message = message.trim().replaceFirst("(^палат)+(.)*?($| )","");
		String date = "";
		String cabinet = "";
		Integer dateInt=null; 
		String stringDate=null;
		Boolean weekType=true;
		String dayRus="";
		
		Boolean needStep2 = false;
		// Кабинет *
		//System.out.println("Cab: "+message);
		Boolean keyboardReplaced = true;
		try {
			keyboardReplaced = MySqlConnector.getKeyboardReplaced(vkContent.getVK().getPeer_id());
		} catch(TimetableException te) {
			keyboardReplaced=true;
		}
		if(message.length()>0) {
			// Кабинет * на day
			try {
				if(message.contains("@")) {
					needStep2=true;
				}
				if(RegexpTools.checkRegexp("(( )на( ))", message)) {
					stringDate = message.substring(RegexpTools.rIndexOf("(( )на( ))",message));
					date = TimetableClient.checkDate(stringDate);
					cabinet = message.substring(0, RegexpTools.rIndexOf("(( )на( ))",message)).trim();
				} else {
					date = TimetableClient.getCurDay();
					cabinet = message.trim();
				}
				if(!needStep2) {
					cabinet=cabinet.replaceAll("(\\$|'|;|\\\\|&|\"|\\*)", "");
					if(cabinet.length()>16) {
						cabinet = cabinet.substring(0,16);
					}
					if(cabinet.trim().length()<1) {
						vkContent.reply("ERROR | Вы не указали кабинет\n Пример: Кабинет 303 на завтра",null,
								(from_keyboard?CreateKeyboardHandler.createCabsOnDay(day,vkContent.getVK().getPeer_id(),offset):(!keyboardReplaced?CreateKeyboardHandler.DEFAULT_PM_KEYBOARD:null)));
						return;
					}
				}
				try {
					dateInt=TimetableClient.getDayOfWeek(date);
				} catch(Exception e) {
					vkContent.reply("ERROR | Неправильно указан день, разрешено: \n"
							+ "День недели, число(dd.MM.yyyy), наречия(Завтра, сегодня)",null,
							(from_keyboard?CreateKeyboardHandler.createCabsOnDay(day,vkContent.getVK().getPeer_id(),offset):(!keyboardReplaced?CreateKeyboardHandler.DEFAULT_PM_KEYBOARD:null)));
					return;
				}			
				if(needStep2) {
					dateForStepTwo = date;
					vkContent.getVK().setStage("cabname");
					vkContent.reply("Введите кабинет или 0 для выхода");
					//
					return;
				}
				try {
					weekType=TimetableClient.getWeekType(date);
					dayRus=TimetableClient.getDayOfWeekRus(dateInt);
				} catch (TimetableException e) {
					vkContent.reply(e.getMessage(),null,
							(from_keyboard?CreateKeyboardHandler.createCabsOnDay(day,vkContent.getVK().getPeer_id(),offset):(!keyboardReplaced?CreateKeyboardHandler.DEFAULT_PM_KEYBOARD:null)));
					return;
				}
				Boolean rceDown=false;
				try {
					new TimetableServer(date,"").get(true);
				} catch (TimetableException te) {
					Exception causedExc = te.getCausedException();
					if(causedExc instanceof TimetableException) {
						if(((TimetableException) causedExc).getCode().equals(TimetableException.ExceptionCode.RCE_UNAVAILABLE)) {
							System.err.println("RCE DOWN");
							rceDown=true;
						} else {
							throw te;	
						}
					} else {
						throw te;
					}
				}
				if(!from_keyboard) {
					try {
						MySqlConnector.addToCabHistory(vkContent.getVK().getPeer_id(), cabinet);
					} catch (TimetableException e) {
						vkContent.reply(e.getMessage());
					}
				}
				MainShelude[] replaces = sortAscending(MySqlConnector.getAllTimetablesByCab(date,cabinet));
				StringBuilder sb = new StringBuilder("");
				Boolean wasReplases=false;
				Boolean wasMain=false;
				if(replaces!=null) {
					for(MainShelude r : replaces) {
						if(!wasReplases) {
							wasReplases=true;
							sb.append("По замене: \n");
						}
						sb.append(r.getPairs()[0].getPairNum()+" Пара - "+r.getGroupName()+" - "+r.getPairs()[0].getPairName()+(r.getPairs()[0].getPairCab().toLowerCase().contains("экзамен")?r.getPairs()[0].getPairCab():"")+"\n");
						//System.out.println(r.getGroupName()+" - R");
					}
				}
				//System.out.println("Список групп с заменами..");
				String[] groupsWithReplace = MySqlConnector.getGroupsWithReplace(date);
				//System.out.println("Группы по основому расписанию с учетом замен");
				MainShelude[] mains = new MainShelude[0];
				Boolean bsReplaced=false;
				try {
					BotSettings bs = MySqlConnector.getBotSettings();
					bsReplaced=bs.getReplace_mainshelude();
				} catch (Exception e) {
					//
				}
				if(!bsReplaced) {
					mains = sortAscending(MySqlConnector.getMainsTimetables(TimetableClient.getDayOfWeekEng(dateInt),weekType,cabinet,groupsWithReplace));
				}
				for(MainShelude r : mains) {
					if(!wasMain) {
						wasMain=true;
						sb.append("По основному: \n");
					}
					sb.append(r.getPairs()[0].getPairNum()+" Пара - "+r.getGroupName()+" - "+r.getPairs()[0].getPairName()+"\n");
					//System.out.println(r.getGroupName()+" - M");
				}
				vkContent.reply((rceDown?"[⚠Сайт Колледжа недоступен, результаты могут быть устаревшими!]\n\n":"")+"Кабинет ["+cabinet+"] | "+TimetableClient.beautifyDate(date)+" | "+dayRus+" | "+(weekType?"Числитель":"Знаменатель")+"\n"
						+(!(wasReplases|wasMain)?"\nВ указанный день нет пар в этом кабинете.":sb.toString())
						+((bsReplaced)?"\n\n[Часть кабинетов скрыта, т.к. основное расписание в процессе заполнения]":""),null,
						(from_keyboard?CreateKeyboardHandler.createCabsOnDay(day,vkContent.getVK().getPeer_id(),offset):(!keyboardReplaced?CreateKeyboardHandler.DEFAULT_PM_KEYBOARD:null)));
			} catch (TimetableException te) {
				vkContent.reply(te.getMessage(),null,
						(from_keyboard?CreateKeyboardHandler.createCabsOnDay(day,vkContent.getVK().getPeer_id(),offset):(!keyboardReplaced?CreateKeyboardHandler.DEFAULT_PM_KEYBOARD:null)));
				return;
			}	
		} else { 
			vkContent.reply("ERROR | Вы не указали кабинет\n Пример: Кабинет 303 на завтра",
					null,
					(from_keyboard?CreateKeyboardHandler.createCabsOnDay(day,vkContent.getVK().getPeer_id(),offset):(!keyboardReplaced?CreateKeyboardHandler.DEFAULT_PM_KEYBOARD:null)));
		}
	}
	public static MainShelude[] sortAscending(MainShelude[] mains) {
		if(mains==null) {
			return null;
		}
		ArrayList<MainShelude> newArray=new ArrayList<MainShelude>();
		Integer[] inserted = new Integer[mains.length];
		Integer[] min = new Integer[2]; // index, value
		while(newArray.size()!=mains.length) {
			min = new Integer[] {null,null};
			for(int i=0;i<mains.length;i++) {
				int cN;
				if(!(intContains(inserted, i))) {
					cN = mains[i].getPairs()[0].getPairNum();
					if(cN==0) {
						min[0]=i;
						min[1]=cN;
						break;
					}
					if(min[1]==null) {
						min[0]=i;
						min[1]=cN;
					} else if(min[1]>cN) {
						min[0]=i;
						min[1]=cN;
					}
				} else {

				}
			}
			if(min[0]!=null) {
				inserted[newArray.size()]=min[0];
				newArray.add(mains[min[0]]);
			} else {

			}
		}
		return newArray.toArray(new MainShelude[newArray.size()]);
	}
	private static boolean intContains(Integer[] inserted, Integer i) {
		for(Integer ins : inserted) {
			if(ins!=null) {
				if(ins.equals(i)) {
					return true;
				}
			}
		}
		return false;
	}
}
