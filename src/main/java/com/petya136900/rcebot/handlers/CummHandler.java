package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.lifecycle.Protocol;
import com.petya136900.rcebot.raccoonremote.ConditionType;
import com.petya136900.rcebot.raccoonremote.LoadedCondition;
import com.petya136900.rcebot.raccoonremote.RaccoonRemote;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.tools.RegexpTools;
import com.petya136900.rcebot.vk.VK;

public class CummHandler implements HandlerInterface {
	
	public static Long cummAgentId = null;
	
	private String host;
	private String user;
	private String password;
	private String camHost;
	private Integer camPort;
	
	private static final Integer DEF_PORT = 544;
	
	@Override
	public void handle(VK vkContent) {
		String stage = vkContent.getVK().getStage();
		String text = vkContent.getVK().getText().trim();
		RaccoonRemote rInst = RaccoonRemote.getInstance();
		switch(stage) {
			case("cumm-cancel"):
				vkContent.reply("Okk");
				vkContent.getVK().removeStages();
				break;
			case("1"):
				text = text.replaceFirst("^cumm", "").trim();
				text.replaceAll("( )+"," ").trim();
				if(RegexpTools.checkRegexp("^(id \\d)", text)) {
				try {
					cummAgentId = Long.parseLong(text.replaceAll("[^\\d]",""));
				vkContent.reply("ID "+cummAgentId+" установлен");
				} catch (Exception e) {
					vkContent.reply("Bad ID");
					}
				}
				if(cummAgentId==null) {
					vkContent.reply("Не указан ID Агента, используйте cumm id *id*");
					return;
				}
				if(!rInst.isConnected()) {
					vkContent.reply("Агент не подключен к серверу, используйте команду remote");
					return;
				}
				vkContent.reply("Введите адрес камеры");
				vkContent.getVK().setStage("cumm-enter-host");
			break;
			case("cumm-enter-host"):
				String host = text.replaceAll("[^a-zA-Z0-9\\.:-_а-яА-Я]","");
				if(host.length()<1) {
					vkContent.reply("Неправильный хост");
					return;
				}
				this.host=host;
				vkContent.reply("Введите пользователя камеры");
				vkContent.getVK().setStage("cumm-enter-login");
				break;
			case("cumm-enter-login"):
				if(text.length()<1) {
					vkContent.reply("Пользователь не может быть пустым");
					return;
				}
				this.user=text;
				vkContent.reply("Введите пароль пользователя камеры");
				vkContent.getVK().setStage("cumm-enter-pass");
				break;
			case("cumm-enter-pass"):
				if(text.length()<1) {
					vkContent.reply("Пароль не может быть пустым");
					return;
				}
				this.password=text;
				if(this.host.contains(":")) {
					String[] parts = this.host.split(":", 2);
					if(parts[0].length()<1) {
						vkContent.reply("Неправильный хост");
						return;
					}
					camHost = parts[0];
					try {
						camPort = Integer.parseInt(parts[1]);
					} catch (Exception e) {
						vkContent.reply("Неправильный порт");
						vkContent.getVK().removeStages();
						return;
					}
				} else {
					camHost = this.host;
					camPort = DEF_PORT;
				}
				Integer freePort = null;
				try {
					freePort = rInst.getFreePort();
				} catch (Exception e) {
					vkContent.reply("Не удалось открыть порт, нет свободных");
					return;
				}
				LoadedCondition cond = getCond(camHost,camPort,freePort);
				try {
					rInst.addCond(cond);
					vkContent.reply("Правило добавлено: "+JsonParser.toJson(cond));
				} catch (Exception e) {
					vkContent.reply("Не удалось создать правило, ошибка: "+e.getMessage());
					return;
				}
				break;			
		}
		
	}

	private LoadedCondition getCond(String camHost, Integer camPort, Integer freePort) {
		LoadedCondition cond = new LoadedCondition();
		cond.setAutorun(true);
		cond.setFirewall(true);
		cond.setCondType(ConditionType.DEFAULT.toString());
		cond.setCondTypeEnum(ConditionType.DEFAULT);
		cond.setCondData("");
		cond.setDevId(cummAgentId);
		cond.setName("CREATED BY RCE_BOT");
		cond.setProtocol(Protocol.TCP.toString());
		cond.setExtPort(freePort);
		cond.setTargetHost(camHost);
		cond.setTargetPort(camPort);
		return cond;
	}

}
