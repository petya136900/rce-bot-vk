package com.petya136900.rcebot.handlers;

import java.util.Arrays;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.raccoonremote.DeviceGson;
import com.petya136900.rcebot.raccoonremote.RaccoonRemote;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.Button;
import com.petya136900.rcebot.vk.structures.Button.Type;
import com.petya136900.rcebot.vk.structures.Keyboard;
import com.petya136900.rcebot.vk.structures.KeyboardLine;
import com.petya136900.rcebot.vk.structures.Payload;
import com.petya136900.rcebot.vk.structures.VKAttachment;

public class RaccoonRemoteHandler implements HandlerInterface {

	private static final Keyboard RESET_KEYBOARD = new Keyboard(new KeyboardLine(new Button(Type.TEXT, "Отмена", Button.COLOR_NEGATIVE)
			.setPayload(new Payload("remote", "cancel")))).setInline(true);

	private String host;
	private boolean useHttps=true;
	private Integer port;
	private String login;
	private String pass;
	private static final Integer DEF_PORT = 8443;
	
	@Override
	public void handle(VK vkContent) {
		String text = vkContent.getVK().getText().trim();
		Payload payload = null;
		String sPayload = vkContent.getVK().getPayload();
		if(sPayload!=null) {
			payload = JsonParser.fromJson(sPayload, Payload.class);
			if(payload.getStage()!=null&&payload.getStage().equals("cancel")) {
				vkContent.getVK().removeStages();
				showMain(vkContent);
				return;
			}
		}
		switch(vkContent.getVK().getStage()) {
			case("remote-enter-server"):
				host = text.trim();
				if(host.length()<1) {
					VKAttachment[] attachs = vkContent.getVK().getAttachments();
					if(attachs.length<1||(!attachs[0].getType().equals("link"))) {
						vkContent.reply("Неправильный хост");
						return;
					}
					host = attachs[0].getLink().getUrl();
				}
				if(host.contains("http://")) {
					useHttps=false;
				}
				host=host.replaceAll("http.?://", "");
				if(host.contains(":")) {
					String[] parts = host.split(":", 2);
					if(parts[0].length()<1) {
						vkContent.reply("Неправильный хост");
						return;
					}
					host = parts[0];
					try {
						port = Integer.parseInt(parts[1]);
					} catch (Exception e) {
						vkContent.reply("Неправильный порт");
						vkContent.getVK().removeStages();
						return;
					}
				} else {
					port = DEF_PORT;
				}
				vkContent.getVK().setStage("remote-enter-login");
				vkContent.reply("Введите логин: ",null,RESET_KEYBOARD);
				return;
			case("remote-enter-login"):
				if(text.length()<3) {
					vkContent.reply("Логин не может быть меньше 3 символов\n\n"
							+ "Введите логин: ",null,RESET_KEYBOARD);
					return;
				}
				login = text;
				vkContent.getVK().setStage("remote-enter-pass");
				vkContent.reply("Введите пароль: ",null,RESET_KEYBOARD);
				return;
			case("remote-enter-pass"):
				if(text.length()<6) {
					vkContent.reply("Пароль не может быть меньше 6 символов\n\n"
							+ "Введите пароль: ",null,RESET_KEYBOARD);
					return;
				}
				pass = text;
				vkContent.getVK().removeStages();
				vkContent.reply("Пробую войти с данными: "+JsonParser.toJson(this));
				try {
					RaccoonRemote.getInstance().connect(host,port,login,pass,useHttps,vkContent);
				} catch (Exception e) {
					vkContent.reply("Произошла ошибка: \n"+e.getLocalizedMessage());
				}
				showMain(vkContent);
				return;
		}
		text = text.replaceFirst("remote","").trim();
		String idS = text.replaceAll("[^\\d]","");
		if(text.length()>0) {
			if(payload==null) {
				payload = new Payload("remote", text.split(" ")[0]);
			}
		}
		if(VK.getAdminID().equals(vkContent.getVK().getFrom_id())) {
			if(payload!=null) {
				RaccoonRemote rInst = RaccoonRemote.getInstance();
				switch (payload.getStage()) {
					case "main":
						showMain(vkContent);
						return;
					case "nothing":
						//
						break;
					case "login":
						rInst = RaccoonRemote.getInstance();
						if(rInst.isConnected()) {
							vkContent.reply("Уже подключен");
							showMain(vkContent);
							return;
						}
						vkContent.getVK().setStage("remote-enter-server");
						vkContent.reply("[Если ВК прикрепляет ссылку к сообщению - удалите её]\n\nВведите сервер: ",null,RESET_KEYBOARD);
						break;
					case "disc":
						RaccoonRemote.getInstance().disconnect();
						vkContent.reply("Отключен");
						showMain(vkContent);
						break;
					case "agent":
						long id=0l;
						if(idS.length()>0) {
							try {
								id = Long.parseLong(idS);
							} catch (Exception ignored) {}
						} else {
							try {
								id = Long.parseLong(payload.getData());
							} catch (Exception ignored) {}
						}
						showAgent(vkContent,null,id);
						break;
					case "agents":
						long list=0l;
						if(idS.length()>0) {
							try {
								list = Long.parseLong(idS);
							} catch (Exception ignored) {}
						} else {
							try {
								list = Long.parseLong(payload.getData());
							} catch (Exception ignored) {}
						}
						if(!rInst.isConnected()) {
							vkContent.reply("Не подключен к серверу");
							showMain(vkContent);
							return;
						}
						showAgents(vkContent,null,list);
						break;
					default:
						vkContent.reply("Неизвестное действие");
						break;
				}
			} else {			
				showMain(vkContent);
			}
		} else {
			vkContent.reply("Access denied");
		}
	}
	
	private void showAgent(VK vkContent, String string, long id) {
		RaccoonRemote instance = RaccoonRemote.getInstance();
		if(instance.isConnected()) {
			try {
				DeviceGson device = instance.getDevice(id);
				vkContent.reply((string==null?"RaccoonRemote | Agent":string), null, getAgentKeyboard(device));
				vkContent.reply(("Агент: "+device.getName()+"\n"
								+("ID: "+device.getId())+"\n"
								+(device.isConnected()?"Подключен":"Отключен")+"\n"
								+(device.isLocal()?"Локальное\n":"")
								+((device.isConnected()?("Local IP: "+device.getIp()):""))),
						null, getAgentKeyboard(device));
			} catch (Exception e) {
				vkContent.reply("Ошибка: "+e.getMessage());
			}
		} else {
			vkContent.reply("Не подключен к серверу");
		}
	}
	
	private Keyboard getAgentKeyboard(DeviceGson device) {
		//Keyboard keyboard = new Keyboard();
		return null;
	}

	private void showAgents(VK vkContent, String string, long list) {
		RaccoonRemote instance = RaccoonRemote.getInstance();
		if(instance.isConnected()) {
			try {
				DeviceGson[] devices = instance.getDevices();
				vkContent.reply((string==null?"RaccoonRemote | Agents":string), null, getAgentsKeyboard(devices,list));
			} catch (Exception e) {
				e.printStackTrace();
				vkContent.reply("Ошибка: "+e.getMessage());
			}
		} else {
			vkContent.reply("Не подключен к серверу");
		}
	}

	public static long roundUp(long num, long divisor) {
	    return (num + divisor - 1) / divisor;
	}
	
	private Keyboard getAgentsKeyboard(DeviceGson[] devices, long list) {
		System.out.println("Гружу лист: "+list);
		if(list<1)
			list=1;
		Keyboard keyboard = new Keyboard();
		long maxList = 0;
		//int maxDevices = 4;
		boolean hasNext=false;
		boolean hasPrevious=false;
		if(devices.length>4) {
			maxList = roundUp(devices.length, 4);
			if(list>maxList)
				list = maxList;
			if(list<maxList)
				hasNext=true;
			if(list>1)
				hasPrevious=true;
		} else {
			maxList = 1;
			//maxDevices = 5;
		}
		System.out.println("list: "+list+ " | maxList: "+maxList);
		// header
		keyboard.addKeyboardLine(new KeyboardLine(new Button(Type.TEXT, "Назад", Button.COLOR_PRIMARY)
				.setPayload(new Payload("remote", "main"))));
		// body
		if(devices.length>0) {
			System.out.println("devLength: "+devices.length);
			if(devices.length<=5) {
				devices = Arrays.copyOfRange(devices, 0, devices.length);	
			} else {
				int s = new Long((list-1)*4).intValue();
				int l = s+4;
				System.out.println("s: "+s);
				System.out.println("l: "+l);
				if(l>devices.length)
					l=devices.length;
				System.out.println("l: "+l);
				devices = Arrays.copyOfRange(devices, s, l);
			}
			for(DeviceGson device : devices) {
				keyboard.addKeyboardLine(new KeyboardLine(new Button(Type.TEXT, device.getName(), 
								device.isConnected()?Button.COLOR_POSITIVE:Button.COLOR_NEGATIVE)
								.setPayload(new Payload("remote","agent").setData(device.getId()+""))));
			}	
		}
		// footer
		keyboard.addKeyboardLine(maxList>1?(
			new KeyboardLine(
					(hasPrevious?new Button(Type.TEXT,"Prev",Button.COLOR_NEGATIVE).setPayload(
							new Payload("remote", "agents").setData((list-1)+"")):null),
					(hasNext?new Button(Type.TEXT,"Next",Button.COLOR_POSITIVE).setPayload(
							new Payload("remote", "agents").setData((list+1)+"")):null))
			):null);
		return keyboard.setInline(true);
	}

	private void showMain(VK vkContent) {
		showMain(vkContent, null);
	}
	
	private void showMain(VK vkContent, String string) {
		vkContent.reply((string==null?"RaccoonRemote | Main":string), null, getMainKeyboard(RaccoonRemote.getInstance()));
	}

	private Keyboard getMainKeyboard(RaccoonRemote raccoonRemote) {
		Keyboard keyboard = new Keyboard();
		if(raccoonRemote.isConnected()) {
			keyboard.addKeyboardLine(new KeyboardLine(new Button(Type.TEXT, "Подключен", Button.COLOR_POSITIVE)
					.setPayload(new Payload("remote", "nothing"))));
			///////
			keyboard.addKeyboardLine(new KeyboardLine(new Button(Type.TEXT, "Сервер: ", Button.COLOR_PRIMARY)
					.setPayload(new Payload("remote", "nothing")), 
					new Button(Type.TEXT, raccoonRemote.getWebAddress(), Button.COLOR_POSITIVE)
					.setPayload(new Payload("remote", "nothing"))));
			////////
			keyboard.addKeyboardLine(new KeyboardLine(new Button(Type.TEXT, "Пользователь: ", Button.COLOR_PRIMARY)
					.setPayload(new Payload("remote", "nothing")), 
					new Button(Type.TEXT, raccoonRemote.getLogin(), Button.COLOR_POSITIVE)
					.setPayload(new Payload("remote", "nothing"))));
			/////////
			keyboard.addKeyboardLine(new KeyboardLine(new Button(Type.TEXT, "Список агентов", Button.COLOR_PRIMARY)
					.setPayload(new Payload("remote", "agents"))));
			/////////
			keyboard.addKeyboardLine(new KeyboardLine(new Button(Type.TEXT, "Отключиться", Button.COLOR_NEGATIVE)
					.setPayload(new Payload("remote", "disc"))));
		} else {
			keyboard.addKeyboardLine(new KeyboardLine(new Button(Type.TEXT, "Не подключен", Button.COLOR_NEGATIVE)
					.setPayload(new Payload("remote", "nothing"))));
			////////
			keyboard.addKeyboardLine(new KeyboardLine(new Button(Type.TEXT, "Подключиться", Button.COLOR_POSITIVE)
					.setPayload(new Payload("remote", "login"))));
		}
		return keyboard.setInline(true);
	}

}
