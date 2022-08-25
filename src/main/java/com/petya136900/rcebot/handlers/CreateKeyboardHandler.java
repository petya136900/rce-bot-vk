package com.petya136900.rcebot.handlers;

import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.db.MySqlConnector;
import com.petya136900.rcebot.rce.timetable.GroupsHistoryObject;
import com.petya136900.rcebot.rce.timetable.PeerSettings;
import com.petya136900.rcebot.rce.timetable.TimetableException;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.tools.RegexpTools;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.Button;
import com.petya136900.rcebot.vk.structures.Keyboard;
import com.petya136900.rcebot.vk.structures.KeyboardLine;
import com.petya136900.rcebot.vk.structures.Payload;
import com.petya136900.rcebot.vk.structures.Button.Type;

public class CreateKeyboardHandler implements HandlerInterface {
	private Boolean code=false;
	private Boolean inline=false;
	private Boolean one_time=false;
	
	private static final String ST_SLASH="!!!!--%SLASH%--!!!!";
	private static final String ST_COMMA="!!!!--%COMMA%--!!!!";
	public static final Keyboard DEFAULT_PM_KEYBOARD = new Keyboard(
						new KeyboardLine(
							new Button(Type.TEXT, "Пары", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "pairs")),
							new Button(Type.TEXT, "Кабинеты", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "cabs"))
						),
						new KeyboardLine(
							new Button(Type.TEXT, "Звонки", Button.COLOR_PRIMARY).setPayload(new Payload("pairs", "calls"))
						)
						//,
						//new KeyboardLine(
						//	new Button(Type.OPEN_LINK,"Сайт Колледжа").setLink("https://xn--j1al4b.xn--p1ai/index.php/extras-2/ekzameny")
						//)
					).setOne_time(false);
	public static final Keyboard CALLS_HANDLER = new Keyboard(
						new KeyboardLine(
							new Button(Type.TEXT, "Главное меню", Button.COLOR_NEGATIVE).setPayload(new Payload("pairs", "main"))
						),
						new KeyboardLine(
							new Button(Type.TEXT, "Звонки", Button.COLOR_POSITIVE),
							new Button(Type.TEXT, "Звонки на завтра", Button.COLOR_POSITIVE)
						),
						new KeyboardLine(
							new Button(Type.TEXT, "Пары звонки", Button.COLOR_PRIMARY).setPayload(new Payload().setData("from_keyboard")),
							new Button(Type.TEXT, "Пары звонки на завтра", Button.COLOR_PRIMARY).setPayload(new Payload().setData("from_keyboard"))
						),
						new KeyboardLine(
							new Button(Type.TEXT,"Звонки на неделю",Button.COLOR_PRIMARY).setPayload(new Payload("pairs", "calls_on_week"))
						)
					).setOne_time(false);
	public static final Keyboard PAIRS_ON_WEEK = new Keyboard(
						new KeyboardLine(
							new Button(Type.TEXT, "Назад", Button.COLOR_NEGATIVE).setPayload(new Payload("pairs", "pairs"))
						),
						new KeyboardLine(
							new Button(Type.TEXT,"Понедельник",Button.COLOR_POSITIVE).setPayload(new Payload("week", "Понедельник").setData("from_keyboard")),
							new Button(Type.TEXT,"Вторник",Button.COLOR_POSITIVE).setPayload(new Payload("week", "Вторник").setData("from_keyboard")),
							new Button(Type.TEXT,"Среда",Button.COLOR_POSITIVE).setPayload(new Payload("week", "Среда").setData("from_keyboard"))
						),
						new KeyboardLine(
							new Button(Type.TEXT,"Четверг",Button.COLOR_POSITIVE).setPayload(new Payload("week", "Четверг").setData("from_keyboard")),
							new Button(Type.TEXT,"Пятница",Button.COLOR_POSITIVE).setPayload(new Payload("week", "Пятница").setData("from_keyboard")),
							new Button(Type.TEXT,"Суббота",Button.COLOR_POSITIVE).setPayload(new Payload("week", "Суббота").setData("from_keyboard"))
						)
					).setOne_time(false);
	public static final Keyboard CALLS_ON_WEEK = new Keyboard(
			new KeyboardLine(
				new Button(Type.TEXT, "Назад", Button.COLOR_NEGATIVE).setPayload(new Payload("pairs", "calls"))
			),
			new KeyboardLine(
				new Button(Type.TEXT,"Понедельник",Button.COLOR_POSITIVE).setPayload(new Payload("call", "Понедельник")),
				new Button(Type.TEXT,"Вторник",Button.COLOR_POSITIVE).setPayload(new Payload("call", "Вторник")),
				new Button(Type.TEXT,"Среда",Button.COLOR_POSITIVE).setPayload(new Payload("call", "Среда"))
			),
			new KeyboardLine(
				new Button(Type.TEXT,"Четверг",Button.COLOR_POSITIVE).setPayload(new Payload("call", "Четверг")),
				new Button(Type.TEXT,"Пятница",Button.COLOR_POSITIVE).setPayload(new Payload("call", "Пятница")),
				new Button(Type.TEXT,"Суббота",Button.COLOR_POSITIVE).setPayload(new Payload("call", "Суббота"))
			)
		).setOne_time(false);	
	public static final Keyboard BACK_TO_GROUPS = new Keyboard(
						new KeyboardLine(
							new Button(Type.TEXT, "Назад", Button.COLOR_NEGATIVE).setPayload(new Payload("pairs", "groups").setData("0"))
						)
					).setOne_time(false);
	public static final Keyboard CABS_MAIN_MENU = new Keyboard(
						new KeyboardLine(
							new Button(Type.TEXT, "Главное меню", Button.COLOR_NEGATIVE).setPayload(new Payload("pairs", "main"))
						),
						new KeyboardLine(
							new Button(Type.TEXT,"На сегодня",Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "cabs_on_today")),
							new Button(Type.TEXT,"На завтра",Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "cabs_on_tomorrow"))
						),
						new KeyboardLine(
							new Button(Type.TEXT,"На неделю",Button.COLOR_PRIMARY).setPayload(new Payload("pairs", "cabs_on_week"))
						)
					).setOne_time(false);
	public static final Keyboard CABS_ON_WEEK = new Keyboard(
						new KeyboardLine(
							new Button(Type.TEXT, "Назад", Button.COLOR_NEGATIVE).setPayload(new Payload("pairs", "cabs"))
						),
						new KeyboardLine(
							new Button(Type.TEXT,"Понедельник",Button.COLOR_POSITIVE).setPayload(new Payload("cabs_week", "Понедельник")),
							new Button(Type.TEXT,"Вторник",Button.COLOR_POSITIVE).setPayload(new Payload("cabs_week", "Вторник")),
							new Button(Type.TEXT,"Среда",Button.COLOR_POSITIVE).setPayload(new Payload("cabs_week", "Среда"))
						),
						new KeyboardLine(
							new Button(Type.TEXT,"Четверг",Button.COLOR_POSITIVE).setPayload(new Payload("cabs_week", "Четверг")),
							new Button(Type.TEXT,"Пятница",Button.COLOR_POSITIVE).setPayload(new Payload("cabs_week", "Пятница")),
							new Button(Type.TEXT,"Суббота",Button.COLOR_POSITIVE).setPayload(new Payload("cabs_week", "Суббота"))
						)
					).setOne_time(false);
	public static final Keyboard COLLAPSED_KEYBOARD = new Keyboard(
						new KeyboardLine(
							new Button(Type.TEXT, "Развернуть", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "expand_keyboard"))
						)
					).setOne_time(false);
	public static final Keyboard NOTIF_HOURS_0 = new Keyboard(
						new KeyboardLine(
							new Button(Type.TEXT, "Назад", Button.COLOR_NEGATIVE).setPayload(new Payload("pairs", "notifications"))
						),
						new KeyboardLine(
							new Button(Type.TEXT, "0", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "notifications_set_time_selected").setData("0")),
							new Button(Type.TEXT, "1", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "notifications_set_time_selected").setData("1")),
							new Button(Type.TEXT, "2", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "notifications_set_time_selected").setData("2")),
							new Button(Type.TEXT, "3", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "notifications_set_time_selected").setData("3"))
						),
						new KeyboardLine(
							new Button(Type.TEXT, "4", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "notifications_set_time_selected").setData("4")),
							new Button(Type.TEXT, "5", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "notifications_set_time_selected").setData("5")),
							new Button(Type.TEXT, "6", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "notifications_set_time_selected").setData("6")),
							new Button(Type.TEXT, "7", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "notifications_set_time_selected").setData("7"))
						),
						new KeyboardLine(
							new Button(Type.TEXT, "Следующие", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "notifications_set_time").setData("1"))
						)						
					).setOne_time(false);
	public static final Keyboard NOTIF_HOURS_1 = new Keyboard(
			new KeyboardLine(
				new Button(Type.TEXT, "Назад", Button.COLOR_NEGATIVE).setPayload(new Payload("pairs", "notifications"))
			),
			new KeyboardLine(
				new Button(Type.TEXT, "8", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "notifications_set_time_selected").setData("8")),
				new Button(Type.TEXT, "9", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "notifications_set_time_selected").setData("9")),
				new Button(Type.TEXT, "10", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "notifications_set_time_selected").setData("10")),
				new Button(Type.TEXT, "11", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "notifications_set_time_selected").setData("11"))
			),
			new KeyboardLine(
				new Button(Type.TEXT, "12", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "notifications_set_time_selected").setData("12")),
				new Button(Type.TEXT, "13", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "notifications_set_time_selected").setData("13")),
				new Button(Type.TEXT, "14", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "notifications_set_time_selected").setData("14")),
				new Button(Type.TEXT, "15", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "notifications_set_time_selected").setData("15"))
			),
			new KeyboardLine(
				new Button(Type.TEXT, "Предыдущие", Button.COLOR_NEGATIVE).setPayload(new Payload("pairs", "notifications_set_time").setData("0")),
				new Button(Type.TEXT, "Следующие", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "notifications_set_time").setData("2"))
			)						
		).setOne_time(false);
	public static final Keyboard NOTIF_HOURS_2 = new Keyboard(
			new KeyboardLine(
				new Button(Type.TEXT, "Назад", Button.COLOR_NEGATIVE).setPayload(new Payload("pairs", "notifications"))
			),
			new KeyboardLine(
				new Button(Type.TEXT, "16", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "notifications_set_time_selected").setData("16")),
				new Button(Type.TEXT, "17", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "notifications_set_time_selected").setData("17")),
				new Button(Type.TEXT, "18", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "notifications_set_time_selected").setData("18"))
			),
			new KeyboardLine(
				new Button(Type.TEXT, "Предыдущие", Button.COLOR_NEGATIVE).setPayload(new Payload("pairs", "notifications_set_time").setData("1"))
			)						
		).setOne_time(false);	
	@Override
	public void handle(VK vkContent) {
		String text = (RegexpTools.replaceRegexp(vkContent.getVK().getText().trim(), "^(genkey|клав)+(.)*?(\\b)", "", false));
		//System.out.println(text);
		if((text.trim().equalsIgnoreCase("delete"))|
			(text.trim().equalsIgnoreCase("удалить"))|
			  (text.trim().equalsIgnoreCase("отключить"))|
			  (text.trim().equalsIgnoreCase("выключить"))) {
			vkContent.reply("Keyboard deleted c:",null,new Keyboard().setOne_time(false));
			try {
				MySqlConnector.putKeyboardReplaced(vkContent.getVK().getPeer_id(),1);
			} catch (TimetableException e) {
				vkContent.reply(e.getMessage());
			}
			return;
		}else if(text.trim().split("\n")[0].trim().contains("json")) {
			if(text.trim().length()>200) {
				vkContent.reply("Ошибка, введите текст не превышающий 200 символов");
			} else {
				text = text.trim().replaceFirst("json", "").trim();
				Boolean isSay = RegexpTools.checkRegexp("^(say|скажи)", text);
				text = text.replaceFirst("^(say|скажи)", "").trim();
				vkContent.reply(JsonParser.toJson(
						new Payload(isSay?"say":"text",
									isSay?getMD5x2(text,vkContent.getVK().getPeer_id()):null
									).setData(text))
						.replaceAll("\\\\", "\\\\\\\\")
						.replaceAll(",", "\\\\,")); // fix
			}
			return;
		} else if(text.trim().equalsIgnoreCase("help")) {
			vkContent.reply(
					"клавиатура [help] [delete] [json] [default | обычная] [code] \n ## delete - delete(удалить) keyboard\n ## code - show keyboard JSON code \n ## default(обычная) - Load default keyboard\n ## json - Genereate JSON payload code for button "+"\n"
					+ "[inline] [one_time] "+"\n ## 2st line\n ## keyboard arguments\n ## False by default"+"\n\n"
					+ "<> - required, [] - optional\n\n"
					+ "<type> \\ [label] \\ [color] \\ [payload] \\ [link] \\ [hash] \\ [app_id] \\ [owner_id] \n ## 1st keyboard line\n ## buttons are separated by a comma"+"\n"
					+ "<type> \\ [label] \\ [color] \\ [payload] \\ [link] \\ [hash] \\ [app_id] \\ [owner_id] \n ## 2nd keyboard line etc"+"\n"
					+ "\n"
					+ "Available types: text, open_link, vkpay, open_app, callback, location"+"\n\n"
					+ "text \\ <label> \\ [color] \\ [payload] "+"\n"
					+ "open_link \\ <label> \\ null \\ [payload] \\ <link> "+"\n"
					+ "vkpay \\ null \\ null \\ [payload] \\ null \\ <hash> "+"\n"
					+ "open_app \\ <label> \\ null \\ [payload] \\ null \\ [hash] \\ <app_id> \\ [owner_id] "+"\n"
					+ "callback \\ <label> \\ [color] \\ [payload] "+"\n"
					+ "location \\ null \\ null \\ [payload] "+"\n"
					+ "\n"
					+ "Available colors: primary, secondary, positive, negative"+"\n\n"
					+ "If you need to use \"\\\" or \",\" use \"\\\\\" or \"\\,\" respectively"+"\n\n"
					+ ""
					+ "Example:\n\n"
					+ "genkey code\n"
					+ "inline\n"
					+ "text \\ button1 \\ primary, text \\ button2 \\ secondary"+"\n"
					+ "text \\ button3 \\ positive, text \\ button4 \\ negative\n"
					+ "\n"
					+ "If some parameter is missing, write null\r\n"
					+ "For example:\r\n"
					+ "genkey code\r\n"
					+ "inline\r\n"
					+ "open_link \\ Press Me! \\ null \\ null \\ https://google.com\n\n"
					+ "If you want to use one button name and hang the action on another text, use:\n "
					+ "text \\ ButtonName \\ Color \\ {\"handler\":\"text\"\\,\"data\":\"Real action text\"}\n"
					+ "For example: \n"
					+ "genkey\n"
					+ "inline\n"
					+ "text \\ Tomorrow \\ Positive \\ {\"handler\":\"text\"\\,\"data\":\"Пары на завтра\"}");
			return;
		} else if((text.trim().equalsIgnoreCase("default"))|
				  (text.trim().equalsIgnoreCase("обычная"))|
				  (text.trim().equalsIgnoreCase("сброс"))|
				  (text.trim().equalsIgnoreCase("сбросить"))|
				  (text.trim().equalsIgnoreCase("включить"))) {
			if(vkContent.getVK().getFrom_id().equals(vkContent.getVK().getPeer_id())) {
				vkContent.reply("Loaded default keyboard for private messages! :3",null,DEFAULT_PM_KEYBOARD);
			} else {
				vkContent.reply("Loaded default keyboard for chats! :3",null,createDefaultChatKeyboard(vkContent.getVK().getPeer_id()));
			}
			try {
				MySqlConnector.putKeyboardReplaced(vkContent.getVK().getPeer_id(),0);
			} catch (TimetableException e) {
				vkContent.reply(e.getMessage());
			}
			return;
		} else if(text.trim().equalsIgnoreCase("test")) {
			vkContent.reply(InfoAboutClientHandler.getInfo(vkContent));
			return;
		}
		String[] lines = text.split("\n");
		int bS = 2;
		Keyboard keyboard = new Keyboard();
		Boolean buttonsNotAdded=true;
		try {
			for(int i=0;i<lines.length;i++) {
				if(lines[i].trim().length()<1) {
					continue;
				}
				if(i==0) {
					if(lines[i].toLowerCase().contains("code")) {
						code=true;
					}
				}
				if(i==1) {
					if(lines[i].toLowerCase().contains("inline")) {
						inline=true;
					}
					if(lines[i].toLowerCase().contains("one_time")) {
						one_time=true;
					}
					if(!one_time&!inline) {
						bS--;
					}
				}
				if(one_time&inline) {
					throw new IllegalArgumentException("One_time field is not available for inline keyboard");
				}
				if(i>=bS) {
					buttonsNotAdded=false;
					keyboard.addKeyboardLine(parseKeyLine(lines[i]));
				}
			}
		} catch (IllegalArgumentException iae) {
			//iae.printStackTrace();
			vkContent.reply("ERROR | Can't create keyboard:\n"+iae.getMessage());
			return;
		}
		if(buttonsNotAdded) {
			vkContent.reply("ERROR | You haven't added any buttons\n\n"
					+ "help - FAQ\n"
					+ "delete(удалить) - delete keyboard\n"
					+ "default(обычная) - Load default keyboard");
			return;
		}
		keyboard.setInline(inline);
		keyboard.setOne_time(one_time);
		/*
		Keyboard keyboard2 = new Keyboard(
				new KeyboardLine(
					new Button(Type.TEXT,"0_0",Button.COLOR_PRIMARY).setPayload("0_0"),
					new Button(Type.TEXT,"0_1",Button.COLOR_POSITIVE).setPayload("0_1")
				),
				new KeyboardLine(
					new Button(Type.TEXT,"0_0",Button.COLOR_NEGATIVE).setPayload("1_0"),
					new Button(Type.TEXT,"0_1",Button.COLOR_SECONDARY).setPayload("1_1")
				)
		).setOne_time(false);
		*/
		if(vkContent.reply("Keyboard has been successfully generated!",null,keyboard)==null) {
			vkContent.reply("Failed to generate keyboard :<\nVK returned an error");
			return;
		} else {
			if((!inline)) {
				try {
					MySqlConnector.putKeyboardReplaced(vkContent.getVK().getPeer_id(),1);
				} catch (TimetableException e) {
					//
				}
			}
		}
		if(code) {
			sendParts(vkContent,"CODE:\n\n"+JsonParser.toJson(keyboard));
		}
	}
	public static String getMD5x2(String text, Integer peer_id) {
		String firstHex = DigestUtils.md5Hex(text+peer_id);
		return DigestUtils.md5Hex(firstHex);
	}
	private void sendParts(VK vkContent, String string) {
		if(string.length()>3500) {
			String part1 = string.substring(0, 3000);
			String part2 = string.substring(3000,string.length());
			sendParts(vkContent, part1);
			sendParts(vkContent, part2);
		} else {
			vkContent.reply(string);
		}
	}
	private KeyboardLine parseKeyLine(String string) {
		KeyboardLine line = new KeyboardLine();
		string = string.replaceAll("\\\\\\\\", ST_SLASH);
		string = string.replaceAll("\\\\,", ST_COMMA);
		String[] stringButtons = string.split(",");
		for(String stringButton : stringButtons) {
			line.addButton(parseButton(stringButton));
		}
		return line;
	}
	private Button parseButton(String stringButton) {
		Button button = null;
		String[] buttonArguments = stringButton.split("\\\\");
		Type type;
		String label;
		for(int i=0;i<buttonArguments.length;i++) {
			String argument = buttonArguments[i].replaceAll(ST_COMMA, ",");
			argument = argument.replaceAll(ST_SLASH, "\\\\").trim();
			if(i>7) {
				break;
			}
			if(argument.equalsIgnoreCase("null")) {
				continue;
			}
			switch(i) {
				case(0): // type
					type =  Button.Type.getType(argument);
					button = new Button(type);
					break;
				case(1): // label
					label = argument.trim();
					button.setLabel(label);
					break;
				case(2): // color
					button.setColor(argument);
					break;
				case(3): // payload
					button.setPayload(argument,false);
					break;
				case(4): // link
					button.setLink(argument);
					break;
				case(5): // hash
					button.setHash(argument);
					break;
				case(6): // app_id
					try {
						button.setApp_id(Integer.parseInt(argument));
					} catch(Exception e) {
						throw new IllegalArgumentException("app_id must be Integer");
					}
					break;
				case(7): // owner_id
					try {
						button.setOwner_id(Integer.parseInt(argument));
					} catch(Exception e) {
						throw new IllegalArgumentException("owner_id must be Integer");
					}
					break;
			}
		}
		if(!(button.getType().equals(Button.Type.LOCATION))&!(button.getType().equals(Button.Type.VKPAY))) {
			if(button.getLabel()==null) {
				throw new IllegalArgumentException("Label is required for type "+button.getType().toString());
			}
		}
		if(button.getType().equals(Button.Type.OPEN_LINK)) {
			if(button.getLink()==null) {
				throw new IllegalArgumentException("Link is required for type \"open_link\"");
			}
		}
		if(button.getType().equals(Button.Type.VKPAY)) {
			if(button.getHash()==null) {
				throw new IllegalArgumentException("Hash is required for type \"vkpay\"");
			}
		}
		if(button.getType().equals(Button.Type.VKAPPS)) {
			if(button.getApp_id()==null) {
				throw new IllegalArgumentException("app_id is required for type \"open_app\"");
			}
		}				
		return button;
	}
	public static Keyboard generateNotificationsMenu(Integer peerID, Boolean isPM) {
		PeerSettings ps=null;
		try {
			ps = MySqlConnector.getPeerSettings(peerID);
		} catch (Exception ignored) {
			
		}
		return new Keyboard(
			new KeyboardLine(
				new Button(Type.TEXT, isPM?"Назад":"Главное меню", Button.COLOR_NEGATIVE).setPayload(new Payload("pairs", isPM?"pairs":"main"))
			),
			new KeyboardLine(
				(ps!=null&&ps.getNotifications().equals(1))?
						new Button(Type.TEXT,"Уведомления: Вкл",Button.COLOR_POSITIVE).setPayload(new Payload("pairs","notifications_toggle")):
						new Button(Type.TEXT,"Уведомления: Выкл",Button.COLOR_NEGATIVE).setPayload(new Payload("pairs","notifications_toggle")),
				(ps!=null&&ps.getWorkMode().equals(1))?
						new Button(Type.TEXT,"Режим: Все",Button.COLOR_POSITIVE).setPayload(new Payload("pairs","notifications_change_wm")):
						new Button(Type.TEXT,"Режим: Только замены",Button.COLOR_PRIMARY).setPayload(new Payload("pairs","notifications_change_wm"))
			),
			new KeyboardLine(
				new Button(Type.TEXT,String.format("Уведомлять с %s",getCase(ps)),Button.COLOR_PRIMARY).setPayload(new Payload("pairs","notifications_set_time").setData("0"))
			)
		).setOne_time(false);
	}
	private static String getCase(PeerSettings ps) {
		if(ps==null) 
			return "SQL-Error";
		switch(ps.getNotifHour()) {
			case(1):
				return "1 часа";
			default:
				return ps.getNotifHour()+" часов";
		}
	}
	public static Keyboard generatePairsMainMenu(VK vkContent) {
		String dbGroupName=null;
		try {
			dbGroupName = MySqlConnector.getGroupNameByPeerID(vkContent.getVK().getPeer_id());
		}catch (Exception e) {
			//
		}
		//
		PeerSettings ps=null;
		try {
			ps = MySqlConnector.getPeerSettings(vkContent.getVK().getPeer_id());
		} catch (Exception ignored) {
			
		}
		return new Keyboard(
				new KeyboardLine(
					new Button(Type.TEXT, "Главное меню", Button.COLOR_NEGATIVE).setPayload(new Payload("pairs", "main"))
				),
				new KeyboardLine(
					new Button(Type.TEXT, "Активная группа: "+(dbGroupName==null?"Не указана":dbGroupName), (dbGroupName==null?Button.COLOR_NEGATIVE:Button.COLOR_POSITIVE)).setPayload(new Payload("pairs", "groups").setData("0"))
				),
				new KeyboardLine(
						(ps!=null&&ps.getNotifications().equals(1))?
							new Button(Type.TEXT,"Уведомления: Вкл",Button.COLOR_POSITIVE).setPayload(new Payload("pairs","notifications")):
							new Button(Type.TEXT,"Уведомления: Выкл",Button.COLOR_NEGATIVE).setPayload(new Payload("pairs","notifications"))
				),
				new KeyboardLine(
					new Button(Type.TEXT,"Пары на сегодня",Button.COLOR_POSITIVE).setPayload(new Payload().setData("from_keyboard")),
					new Button(Type.TEXT,"Пары на завтра",Button.COLOR_POSITIVE).setPayload(new Payload().setData("from_keyboard"))
				),
				new KeyboardLine(
					new Button(Type.TEXT,"Пары на неделю",Button.COLOR_PRIMARY).setPayload(new Payload("pairs", "pairs_on_week"))
				)
		).setOne_time(false);
	}
	public static Keyboard generateGroupsMenu(VK vkContent, Boolean ignorePayload) {
		String dbGroupName=null;
		try {
			dbGroupName = MySqlConnector.getGroupNameByPeerID(vkContent.getVK().getPeer_id());
		}catch (Exception e) {
			//
		}
		Integer list_num=null;
		if(ignorePayload) {
			list_num=0;
		} else {
			try {
				Payload payload = JsonParser.fromJson(vkContent.getVK().getPayload(), Payload.class);
				list_num = Integer.parseInt(payload.getData());
			} catch (Exception e) {
				vkContent.reply("Bad payload arguments =_=");
				throw new IllegalArgumentException("Bad payload arguments =_=");
			}
		}
		Keyboard keyboard = new Keyboard(
				new KeyboardLine(
					new Button(Type.TEXT, "Назад", Button.COLOR_NEGATIVE).setPayload(new Payload("pairs", "pairs"))
				),
				new KeyboardLine(
					new Button(Type.TEXT, "Активная группа: "+(dbGroupName==null?"Не указана":dbGroupName), (dbGroupName==null?Button.COLOR_NEGATIVE:Button.COLOR_POSITIVE)).setPayload(new Payload("pairs", "groups").setData(list_num+""))
				),
				new KeyboardLine(
					new Button(Type.TEXT,"Ввести вручную",Button.COLOR_PRIMARY).setPayload(new Payload("pairs", "manual_group_input"))
				)
		).addKeyboardLines(get9historyGroups(vkContent.getVK().getPeer_id(),vkContent.getVK().getFrom_id(),list_num)).setOne_time(false);
		keyboard.prepire();
		//System.out.println(JsonParser.toJson(keyboard));
		return keyboard;
	}
	private static KeyboardLine[] get9historyGroups(Integer peer_id, Integer from_id, Integer list_num) {
		//System.out.println("Peer_id: "+peer_id+" | User_id: "+from_id+" | Offset: "+list_num);
		GroupsHistoryObject gho;
		try {
			gho = MySqlConnector.getGroupHistory(peer_id,from_id,list_num,"group");
		} catch (TimetableException e) {
			e.printStackTrace();
			return new KeyboardLine[] {
				new KeyboardLine(
					new Button(Type.TEXT,"MySql Error",Button.COLOR_NEGATIVE)
				)
			};
		}
		ArrayList<KeyboardLine> klAL = new ArrayList<KeyboardLine>();
		int curButtonNum=0;
		KeyboardLine curKeyLine = null;
		Boolean added=false;
		for(int i = 0 ; i < gho.getGroupNames().length ; i++) {
			if(gho.getGroupNames()[i]!=null) {
				added=true;
				if(curKeyLine==null) {
					curKeyLine = new KeyboardLine();
				}
				curKeyLine.addButton(
					new Button(Type.TEXT,gho.getGroupNames()[i]).setPayload(new Payload("pairs","group_from_history"))
				);
				curButtonNum++;
				if(curButtonNum==3) {
					curButtonNum=0;
					klAL.add(curKeyLine);
					curKeyLine=null;
				}
			}
		}
		if(curButtonNum!=0) {
			if(curKeyLine!=null) {
				klAL.add(curKeyLine);
			}
		}
		if(!added) {
			klAL.add(
					new KeyboardLine(
						new Button(Type.TEXT, "Нет истории", Button.COLOR_SECONDARY).setPayload(new Payload("pairs", "groups").setData(list_num+""))
					)
			);
		}
		if(gho.getPrevAvailable()|gho.getNextAvailable()) {
				klAL.add(
					new KeyboardLine(
						(gho.getPrevAvailable()?new Button(Type.TEXT, "Предыдущие", Button.COLOR_NEGATIVE).setPayload(new Payload("pairs", "groups").setData((gho.getNewOffset()-1)+"")):null),
						(gho.getNextAvailable()?new Button(Type.TEXT, "Следующие", Button.COLOR_POSITIVE).setPayload(new Payload("pairs", "groups").setData((gho.getNewOffset()+1)+"")):null)
					)
				);
				klAL.add(
					new KeyboardLine(
							new Button(Type.TEXT, "Очистить историю", Button.COLOR_NEGATIVE).setPayload(new Payload("pairs", "remove_group_history"))
					)
				);
		}
		return klAL.toArray(new KeyboardLine[klAL.size()]);
	}
	public static Keyboard createDefaultChatKeyboard(Integer peer_id) {
		String dbGroupName=null;
		try {
			dbGroupName = MySqlConnector.getGroupNameByPeerID(peer_id);
		}catch (Exception e) {
			//
		}
		PeerSettings ps=null;
		try {
			ps = MySqlConnector.getPeerSettings(peer_id);
		} catch (Exception ignored) {
			
		}
		Keyboard keyboard = new Keyboard(
			new KeyboardLine(
				new Button(Type.TEXT, "Активная группа: "+(dbGroupName==null?"Не указана":dbGroupName), 
						(dbGroupName==null?Button.COLOR_NEGATIVE:Button.COLOR_POSITIVE))
							.setPayload(new Payload("pairs", "main_chat"))
			),
			new KeyboardLine(
				new Button(Type.TEXT,"Пары на сегодня",Button.COLOR_POSITIVE).setPayload(new Payload().setData("from_keyboard")),
				new Button(Type.TEXT,"Пары на завтра",Button.COLOR_POSITIVE).setPayload(new Payload().setData("from_keyboard"))
			),
			new KeyboardLine(new Button(Type.TEXT, "Пары на понедельник", Button.COLOR_PRIMARY).setPayload(new Payload().setData("from_keyboard"))),
			new KeyboardLine(new Button(Type.TEXT, "Звонки", Button.COLOR_PRIMARY)),
			new KeyboardLine(
				//new Button(Type.OPEN_LINK,"Сайт Колледжа").setLink("https://xn--j1al4b.xn--p1ai/index.php/extras-2/ekzameny"),
				((ps!=null&&ps.getNotifications().equals(1))?
					new Button(Type.TEXT,"Уведомления: Вкл",Button.COLOR_POSITIVE).setPayload(new Payload("pairs","notifications")):
					new Button(Type.TEXT,"Уведомления: Выкл",Button.COLOR_NEGATIVE).setPayload(new Payload("pairs","notifications"))),
				new Button(Type.TEXT,"Свернуть",Button.COLOR_NEGATIVE).setPayload(new Payload("pairs","collapse_keyboard"))
			)
		).setOne_time(false);
		return keyboard;
	}
	public static Keyboard createCabsOnDay(String day, Integer peer_id, Integer offset) {
		//System.out.println("День выбран..");
		Keyboard keyboard = new Keyboard(
				new KeyboardLine(
					new Button(Type.TEXT, "Назад", Button.COLOR_NEGATIVE).setPayload(new Payload("pairs", "cabs"))
				),
				new KeyboardLine(
					new Button(Type.TEXT,"Ввести вручную",Button.COLOR_PRIMARY).setPayload(new Payload("pairs", "manual_cab_input").setData(day))
				)
		).addKeyboardLines(get9historyCabs(peer_id,offset,day)).setOne_time(false);
		keyboard.prepire();
		//System.out.println(JsonParser.toJson(keyboard));
		return keyboard;
	}
	private static KeyboardLine[] get9historyCabs(Integer peer_id, Integer offset, String day) {
		GroupsHistoryObject gho;
		try {
			//gho = MySqlConnector.getCabsHistory(peer_id,peer_id,offset);
			gho = MySqlConnector.getGroupHistory(peer_id,peer_id,offset,"cab");
		} catch (TimetableException e) {
			e.printStackTrace();
			return new KeyboardLine[] {
				new KeyboardLine(
					new Button(Type.TEXT,"MySql Error",Button.COLOR_NEGATIVE)
				)
			};
		}
		ArrayList<KeyboardLine> klAL = new ArrayList<KeyboardLine>();
		int curButtonNum=0;
		KeyboardLine curKeyLine = null;
		Boolean added=false;
		for(int i = 0 ; i < gho.getGroupNames().length ; i++) {
			if(gho.getGroupNames()[i]!=null) {
				added=true;
				if(curKeyLine==null) {
					curKeyLine = new KeyboardLine();
				}
				curKeyLine.addButton(
					new Button(Type.TEXT,gho.getGroupNames()[i],Button.COLOR_POSITIVE).setPayload(new Payload("cab_on_day",gho.getGroupNames()[i]).setData(day+","+offset))
				);
				curButtonNum++;
				if(curButtonNum==3) {
					curButtonNum=0;
					klAL.add(curKeyLine);
					curKeyLine=null;
				}
			}
		}
		if(curButtonNum!=0) {
			if(curKeyLine!=null) {
				klAL.add(curKeyLine);
			}
		}
		if(!added) {
			klAL.add(
				new KeyboardLine(
					new Button(Type.TEXT, "Нет истории", Button.COLOR_SECONDARY).setPayload(new Payload("pairs", "cabs_with_offset_and_day").setData(day+",0"))
				)
			);
		}
		if(gho.getPrevAvailable()|gho.getNextAvailable()) {
				klAL.add(
					new KeyboardLine(
						(gho.getPrevAvailable()?new Button(Type.TEXT, "Предыдущие", Button.COLOR_NEGATIVE)
								.setPayload(new Payload("pairs", "cabs_with_offset_and_day")
										.setData(day+","+(gho.getNewOffset()-1)+"")):null),
						(gho.getNextAvailable()?new Button(Type.TEXT, "Следующие", Button.COLOR_POSITIVE)
								.setPayload(new Payload("pairs", "cabs_with_offset_and_day")
										.setData(day+","+(gho.getNewOffset()+1)+"")):null)
					)
				);
				klAL.add(
					new KeyboardLine(
							new Button(Type.TEXT, "Очистить историю", Button.COLOR_NEGATIVE).setPayload(new Payload("pairs", "remove_cab_history").setData(day))
					)
				);
		}
		return klAL.toArray(new KeyboardLine[klAL.size()]);
	}
	public static Keyboard getBackToCab(String day) {
		Keyboard keyboard = new Keyboard(
				new KeyboardLine(
						new Button(Type.TEXT, "Назад", Button.COLOR_NEGATIVE).setPayload(new Payload("pairs", "cabs_with_offset_and_day").setData(day+",0"))
					)
				).setOne_time(false);
		return keyboard;
	}
}
