package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.lifecycle.MessagesInfoStorage;
import com.petya136900.rcebot.db.MySqlConnector;
import com.petya136900.rcebot.rce.timetable.PeerSettings;
import com.petya136900.rcebot.rce.timetable.TimetableClient;
import com.petya136900.rcebot.rce.timetable.TimetableException;
import com.petya136900.rcebot.rce.timetable.TimetableException.ExceptionCode;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.tools.RegexpTools;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.Keyboard;
import com.petya136900.rcebot.vk.structures.Payload;

public class TimetableHandler implements HandlerInterface {
	private String message;
	private String date;
	private String groupName;
	//private VK vkContent;
	private final String AT=" на ";
	private final String FOR=" для ";
	private Boolean callRequest=false;
	public static final String IDK_ERROR="Произошла какая-то ошибка..";
	public static final String UNKWN_STAGE="Ошибка, неизвестный этап";
	private String dayForCab="";
	private Boolean alex = false;
	
	@Override
	public void handle(VK vkContent) {
		//this.vkContent=vkContent;
		this.message=vkContent.getVK().getText().toLowerCase();
		Integer peerID = vkContent.getVK().getPeer_id();
		switch(vkContent.getVK().getStage()) {
			case("waiting_for_input_group"):
				String text = vkContent.getVK().getMessage().getText().trim();
				if(text.equalsIgnoreCase("назад")|text.equalsIgnoreCase("0")) {
					//vkContent.reply("Группы",null,CreateKeyboardHandler.generateGroupsMenu(vkContent,true));
					MessagesInfoStorage.sendUndUpdate(peerID,"Группы",null,CreateKeyboardHandler.generateGroupsMenu(vkContent,true));
					vkContent.getVK().removeStages();
					return;
				}
				//vkContent.reply("Вы ввели: "+text);
				try {
					vkContent.getVK().removeStages();
					String addedGroup = setGroup(vkContent.getVK().getPeer_id(), vkContent.getVK().getText(), true);
					vkContent.reply("Группа \""+addedGroup+"\" добавлена!",null,CreateKeyboardHandler.generateGroupsMenu(vkContent,true));
				} catch(IllegalArgumentException te) {
					vkContent.reply("Ошибка: \n"+te.getMessage());
				}
				return;
			case("waiting_for_input_cab"):
				text = vkContent.getVK().getMessage().getText().trim();
				if(text.equalsIgnoreCase("назад")|text.equalsIgnoreCase("0")) {
					//vkContent.reply("Группы",null,CreateKeyboardHandler.generateGroupsMenu(vkContent,true));
					MessagesInfoStorage.sendUndUpdate(peerID,"Группы",null,CreateKeyboardHandler.generateGroupsMenu(vkContent,true));
					vkContent.getVK().removeStages();
					return;
				}
				//vkContent.reply("Вы ввели: "+text);
				try {
					String addedCab = text.replaceAll("(\\$|'|;|\\\\|&|\"|\\*)", "").trim();
					if(addedCab.length()>16) {
						addedCab = addedCab.substring(0,16);
					}
					if(addedCab.trim().length()<1) {
						vkContent.reply("ERROR | Указан некорректный кабинет, попробуйте снова");
						return;
					}
					try {
						MySqlConnector.addToCabHistory(vkContent.getVK().getPeer_id(), addedCab);
					} catch (TimetableException e) {
						throw new IllegalArgumentException(e.getMessage());
					}
					vkContent.getVK().removeStages();
					vkContent.reply("Кабинет "+addedCab+" добавлен!",null,CreateKeyboardHandler.createCabsOnDay(dayForCab,vkContent.getVK().getPeer_id(),0));
				} catch(IllegalArgumentException te) {
					vkContent.reply("Ошибка: \n"+te.getMessage());
				}
				return;
		}
		Boolean from_keyboard=false;
		Boolean keyboardReplaced = true;
		try {
			keyboardReplaced = MySqlConnector.getKeyboardReplaced(vkContent.getVK().getPeer_id());
		} catch(TimetableException te) {
			keyboardReplaced=true;
		}
		if(vkContent.getVK().getPayload()!=null) {
			Payload payload;
			try {
				payload = JsonParser.fromJson(vkContent.getVK().getPayload(), Payload.class);
				try {
					if(payload.getData().equalsIgnoreCase("from_keyboard")) {
						from_keyboard=true;
					}
				} catch(Exception e) {
					//
				}
				if(payload.getStage()!=null) {
					if(payload.getStage().equalsIgnoreCase("expand_keyboard")) {
						try {
							MySqlConnector.putKeyboardReplaced(vkContent.getVK().getPeer_id(),0);
						} catch (TimetableException e) { vkContent.reply(e.getMessage()); }
						loadDefaultKeyboardWithMessage(vkContent,"Развернуто",false);
						return;
					}
				}
				if(keyboardReplaced) {
					vkContent.reply("У вас установлена пользовательская клавиатура.. \nСбросьте её командой \"Клавиатура сброс\"");
					return;
				}
				Boolean isPM = vkContent.getVK().getPeer_id().equals(vkContent.getVK().getFrom_id());
				PeerSettings ps;
				if(!from_keyboard) {
					switch(payload.getStage()) {
						case("main"):
							loadDefaultKeyboardWithMessage(vkContent,"Меню",false);
							break;
						case("main_chat"):
							loadDefaultKeyboardWithMessage(vkContent,"Чтобы установить другую активную группу напишите:\n\nПары для *название-группы*",true);
							break;
						case("collapse_keyboard"):
							try {
								MySqlConnector.putKeyboardReplaced(peerID,1);
							} catch (TimetableException e) { vkContent.reply(e.getMessage()); }							
							//vkContent.reply("Свернуто",null,CreateKeyboardHandler.COLLAPSED_KEYBOARD);
							MessagesInfoStorage.sendUndUpdate(peerID,"Свернуто",null,CreateKeyboardHandler.COLLAPSED_KEYBOARD);
							break;
						case("pairs"):
							//vkContent.reply("Пары",null,CreateKeyboardHandler.generatePairsMainMenu(vkContent));
							MessagesInfoStorage.sendUndUpdate(peerID,"Пары",null,CreateKeyboardHandler.generatePairsMainMenu(vkContent));
							break;
						case("groups"):
							//vkContent.reply("Группы",null,CreateKeyboardHandler.generateGroupsMenu(vkContent,false));
							MessagesInfoStorage.sendUndUpdate(peerID,"Группы",null,CreateKeyboardHandler.generateGroupsMenu(vkContent,false));
							break;
						case("remove_group_history"):
							try {
								MySqlConnector.removeGroupHistory(peerID,vkContent.getVK().getFrom_id());
							} catch(TimetableException te) {
								vkContent.reply(te.getMessage());
							}
							//vkContent.reply("Группы",null,CreateKeyboardHandler.generateGroupsMenu(vkContent,true));
							MessagesInfoStorage.sendUndUpdate(peerID,"Группы",null,CreateKeyboardHandler.generateGroupsMenu(vkContent,true));
							break;
						case("group_from_history"):
							String addedGroup="";
							try {
								addedGroup = setGroup(peerID, vkContent.getVK().getText(), true);
								vkContent.reply("Группа "+addedGroup+" установлена!",
										null,CreateKeyboardHandler.generateGroupsMenu(vkContent,true));
							} catch(IllegalArgumentException te) {
								vkContent.reply("ERROR | Игры с Payload?:\n"+te.getMessage());
								vkContent.reply("Группы",null,CreateKeyboardHandler.generateGroupsMenu(vkContent,true));
							}	
							break;
						case("manual_group_input"):
							vkContent.getVK().setStage("waiting_for_input_group");
							vkContent.reply("Введите новую группу: \n\n"
									+ "(Для выхода напишите 0 или назад)",
									null,CreateKeyboardHandler.BACK_TO_GROUPS);
							break;
						case("calls_on_week"):
							//vkContent.reply("Звонки на неделю",null,CreateKeyboardHandler.CALLS_ON_WEEK);
							MessagesInfoStorage.sendUndUpdate(peerID,"Звонки на неделю",null,CreateKeyboardHandler.CALLS_ON_WEEK);
							break;
						case("pairs_on_week"):
							//vkContent.reply("Пары на неделю",null,CreateKeyboardHandler.PAIRS_ON_WEEK);
							MessagesInfoStorage.sendUndUpdate(peerID,"Пары на неделю",null,CreateKeyboardHandler.PAIRS_ON_WEEK);
							break;
						case("cabs"):
							//vkContent.reply("Кабинеты",null,CreateKeyboardHandler.CABS_MAIN_MENU);
							MessagesInfoStorage.sendUndUpdate(peerID,"Кабинеты",null,CreateKeyboardHandler.CABS_MAIN_MENU);
							break;
						case("cabs_with_offset_and_day"):
							String dayAndOffset = payload.getData();
							String day = dayAndOffset.split(",")[0];
							String offset = dayAndOffset.split(",")[1];
							/*
							vkContent.reply(day,null,
									CreateKeyboardHandler.createCabsOnDay(
											day,
											peerID,
											Integer.parseInt(offset)));
							*/
							MessagesInfoStorage.sendUndUpdate(peerID,day,null,CreateKeyboardHandler.createCabsOnDay(
									day,
									peerID,
									Integer.parseInt(offset)));
							break;
						case("cabs_on_today"):
							//vkContent.reply("Кабинеты на сегодня",null,CreateKeyboardHandler.createCabsOnDay("сегодня",peerID,0));
							MessagesInfoStorage.sendUndUpdate(peerID,"Кабинеты на сегодня",null,CreateKeyboardHandler.createCabsOnDay("сегодня",peerID,0));
							break;
						case("cabs_on_tomorrow"):
							//vkContent.reply("Кабинеты на завтра",null,CreateKeyboardHandler.createCabsOnDay("завтра",peerID,0));
							MessagesInfoStorage.sendUndUpdate(peerID,"Кабинеты на завтра",null,CreateKeyboardHandler.createCabsOnDay("завтра",peerID,0));
							break;
						case("cabs_on_week"):
							//vkContent.reply("Кабинеты на неделю",null,CreateKeyboardHandler.CABS_ON_WEEK);
							MessagesInfoStorage.sendUndUpdate(peerID,"Кабинеты на неделю",null,CreateKeyboardHandler.CABS_ON_WEEK);
							break;
						case("manual_cab_input"):
							dayForCab=payload.getData();
							vkContent.getVK().setStage("waiting_for_input_cab");
							vkContent.reply("Введите новый кабинет: \n\n"
									+ "(Для выхода напишите 0 или назад)",
									null,CreateKeyboardHandler.getBackToCab(payload.getData()));
							break;
						case("remove_cab_history"):
							try {
								MySqlConnector.removeCabHistory(peerID,vkContent.getVK().getFrom_id());
							} catch(TimetableException te) {
								vkContent.reply(te.getMessage());
							}
							//vkContent.reply("История удалена",null,CreateKeyboardHandler.createCabsOnDay(payload.getData(),peerID,0));
							MessagesInfoStorage.sendUndUpdate(peerID,"История удалена",null,CreateKeyboardHandler.createCabsOnDay(payload.getData(),peerID,0));
							break;
						case("calls"):
							//vkContent.reply("Звонки",null,CreateKeyboardHandler.CALLS_HANDLER);
							MessagesInfoStorage.sendUndUpdate(peerID,"Звонки",null,CreateKeyboardHandler.CALLS_HANDLER);
							break;
						case("notifications"):
							//vkContent.reply("Уведомления",null,CreateKeyboardHandler.generateNotificationsMenu(peerID,isPM));
							MessagesInfoStorage.sendUndUpdate(peerID,"Уведомления",null,CreateKeyboardHandler.generateNotificationsMenu(peerID,isPM));
							break;
						case("notifications_toggle"):
							ps=null;
							try {
								ps = MySqlConnector.getPeerSettings(peerID);
								ps.setNotifications(ps.getNotifications()==0?1:0);
								MySqlConnector.updatePeerSettings(ps);
							} catch (Exception ignored) {
								
							}
							//vkContent.reply("Обновлено",null,CreateKeyboardHandler.generateNotificationsMenu(peerID,isPM));
							MessagesInfoStorage.sendUndUpdate(peerID,"Обновлено",null,CreateKeyboardHandler.generateNotificationsMenu(peerID,isPM));
							break;
						case("notifications_change_wm"):
							ps=null;
							try {
								ps = MySqlConnector.getPeerSettings(peerID);
								ps.setWorkMode(ps.getWorkMode()==0?1:0);
								MySqlConnector.updatePeerSettings(ps);
							} catch (Exception ignored) {
								
							}
							//vkContent.reply("Обновлено",null,CreateKeyboardHandler.generateNotificationsMenu(peerID,isPM));		
							MessagesInfoStorage.sendUndUpdate(peerID,"Обновлено",null,CreateKeyboardHandler.generateNotificationsMenu(peerID,isPM));
							break;
						case("notifications_set_time"):
							switch(payload.getData()) {
								case("0"):
									//vkContent.reply("Выберите час",null,CreateKeyboardHandler.NOTIF_HOURS_0);
									MessagesInfoStorage.sendUndUpdate(peerID,"Выберите час",null,CreateKeyboardHandler.NOTIF_HOURS_0);
									break;
								case("1"):
									//vkContent.reply("Выберите час",null,CreateKeyboardHandler.NOTIF_HOURS_1);
									MessagesInfoStorage.sendUndUpdate(peerID,"Выберите час",null,CreateKeyboardHandler.NOTIF_HOURS_1);
									break;
								case("2"):
									//vkContent.reply("Выберите час",null,CreateKeyboardHandler.NOTIF_HOURS_2);
									MessagesInfoStorage.sendUndUpdate(peerID,"Выберите час",null,CreateKeyboardHandler.NOTIF_HOURS_2);								
									break;
								default:
									loadDefaultKeyboardWithMessage(vkContent,"Попытка подделать кнопку",true);
									break;
							}
							break;
						case("notifications_set_time_selected"):
							try {
								Integer hour = Integer.parseInt(payload.getData()); 
								if(hour>=0&hour<19) {
									ps=null;
									try {
										ps = MySqlConnector.getPeerSettings(peerID);
										ps.setNotifHour(hour);
										MySqlConnector.updatePeerSettings(ps);
									} catch (Exception ignored) {
										
									}
									//vkContent.reply("Обновлено",null,CreateKeyboardHandler.generateNotificationsMenu(peerID,isPM));
									MessagesInfoStorage.sendUndUpdate(peerID,"Обновлено",null,CreateKeyboardHandler.generateNotificationsMenu(peerID,isPM));
								} else {
									loadDefaultKeyboardWithMessage(vkContent,"Произошла ошибка",true);
								}
							} catch (Exception e) {
								loadDefaultKeyboardWithMessage(vkContent,"Произошла ошибка",true);
							}
							break;
						default:
							loadDefaultKeyboardWithMessage(vkContent,UNKWN_STAGE,true);
							break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				loadDefaultKeyboardWithMessage(vkContent,IDK_ERROR,true);
			}
			if(!from_keyboard) {
				return;
			}
		}
		if(RegexpTools.checkRegexp("звонк|звонок", message)) {
			callRequest=true;
			this.message=RegexpTools.removeFirstString(message, "звонок|звонки|звонок|звонк");
		}
		if(RegexpTools.checkRegexp("( для( ))+(.)*(( )на(( )|$))", message)) {
			setGroupAndDate(message);	
		} else if(RegexpTools.checkRegexp("( на( ))+(.)*(( )для(( )|$))", message)) {
			setDateAndGroup(message);			
		} else {
			if(RegexpTools.checkRegexp("(( )на( ))", message)) {
				setDate(message);
			} else if(RegexpTools.checkRegexp("(( )для( ))", message)) {
				setGroup(message);
			}
		}
		if(RegexpTools.checkRegexp("\\bмне\\b", message)) {
			this.alex=true;
		}
		//sayRawData(vkContent);	
		try {
			new TimetableClient().getTimetable(date,groupName,vkContent,callRequest,false,alex);
		} catch(TimetableException te) {
			Boolean isChat = vkContent.getVK().getPeer_id()>=2000000000;
			vkContent.reply(te.getMessage(),null,(((!keyboardReplaced)&(!from_keyboard))?(isChat?CreateKeyboardHandler.createDefaultChatKeyboard(vkContent.getVK().getPeer_id()):CreateKeyboardHandler.DEFAULT_PM_KEYBOARD):null));
			if(!(te.getCode().equals(ExceptionCode.BAD_DAY))
			&!(te.getCode().equals(ExceptionCode.BAD_GROUP))
			&!(te.getCode().equals(ExceptionCode.SET_GROUP))
			&!(te.getCode().equals(ExceptionCode.SUNDAY))) {
				VK.sendMessage(VK.getAdminID(), te.messageForAdmin(vkContent));
			}
		}
	}
	public String setGroup(Integer peer_id, String groupName, Boolean changeSql) {
		if(groupName==null) {
			throw new IllegalArgumentException("Группа не может быть null");
		}
		groupName = groupName.trim();
		groupName = TimetableClient.removeTrash(TimetableClient.toRus(groupName));
		if(groupName==null) {
			throw new IllegalArgumentException("Группа не может быть null");
		}
		if(groupName.length()>16) {
			groupName=groupName.substring(0,16);
		}
		if(groupName.length()<1) {
			throw new IllegalArgumentException("Неправильное название группы");
		}
		if(changeSql) {
			try {
				MySqlConnector.setGroupNameByPeerID(peer_id,groupName.trim());
			} catch (TimetableException e) {
				VK.sendMessage(peer_id,e.getMessage());
			}
		}
		return groupName.trim();
	}
	public static Keyboard getDefaultKeyboardMainMenu(VK vkContent) {
		if(vkContent.getVK().getPeer_id().equals(vkContent.getVK().getFrom_id())) {
			return CreateKeyboardHandler.DEFAULT_PM_KEYBOARD;
		} else {
			return CreateKeyboardHandler.createDefaultChatKeyboard(vkContent.getVK().getPeer_id());
		}
	}
	public static void loadDefaultKeyboardWithMessage(VK vkContent, String message, boolean showMessage) {
		Boolean keyboardReplaced = null;
		try {
			keyboardReplaced = MySqlConnector.getKeyboardReplaced(vkContent.getVK().getPeer_id());
		} catch(TimetableException te) {
			keyboardReplaced=true;
			return;
		}
		if(keyboardReplaced) {
			vkContent.reply(message);
		} else {
			Keyboard defKeyboard = getDefaultKeyboardMainMenu(vkContent);
			if(showMessage) {
				vkContent.reply(message,null,defKeyboard);
			} else {
				MessagesInfoStorage.sendUndUpdate(vkContent.getVK().getPeer_id(), message, null, defKeyboard);
			}
		}
	}
	@SuppressWarnings("unused")
	private void sayRawData(VK vkContent) {
		vkContent.reply("GroupName: "+groupName+"\n"+
				"Date: "+date);	
	}
	private void setGroup(String message) {
		this.groupName=message.substring(message.indexOf(FOR)).replace(FOR, "");
	}
	private void setDate(String message) {
		this.date=message.substring(message.indexOf(AT)).replace(AT, "");
	}
	private void setDateAndGroup(String message) {
		Integer atIndex=message.indexOf(AT);
		Integer forIndex=message.indexOf(FOR);
		this.date=message.substring(atIndex,forIndex)
				.replace(AT, "").replace(FOR, "");
		this.groupName=message.substring(message.indexOf(FOR)).replace(FOR, "");
	}
	private void setGroupAndDate(String message) {
		Integer atIndex=message.indexOf(AT);
		Integer forIndex=message.indexOf(FOR);		
		this.groupName=message.substring(forIndex,atIndex)
				.replace(FOR, "").replace(AT, "");
		this.date=message.substring(message.indexOf(AT)).replace(AT, "");
	}
}
