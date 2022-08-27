package com.petya136900.rcebot.lifecycle;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.re2j.Pattern;
import com.petya136900.rcebot.handlers.BotSettingsHandler;
import com.petya136900.rcebot.handlers.CabOnDayHandler;
import com.petya136900.rcebot.handlers.CabinetHandler;
import com.petya136900.rcebot.handlers.CabsOnWeekHandler;
import com.petya136900.rcebot.handlers.CallBack1Handler;
import com.petya136900.rcebot.handlers.CallOnWeekHandler;
import com.petya136900.rcebot.handlers.CallsHandler;
import com.petya136900.rcebot.handlers.CreateKeyboardHandler;
import com.petya136900.rcebot.handlers.DebugHandler;
import com.petya136900.rcebot.handlers.EnterHandler;
import com.petya136900.rcebot.handlers.FaqHandler;
import com.petya136900.rcebot.handlers.FixMainMenuHandler;
import com.petya136900.rcebot.handlers.FurryHandler;
import com.petya136900.rcebot.handlers.GeoHandler;
import com.petya136900.rcebot.handlers.HCHandler;
import com.petya136900.rcebot.handlers.HostNameHandler;
import com.petya136900.rcebot.handlers.InfoAboutClientHandler;
import com.petya136900.rcebot.handlers.MangaHandler;
import com.petya136900.rcebot.handlers.MultiHandler;
import com.petya136900.rcebot.handlers.NothingHandler;
import com.petya136900.rcebot.handlers.NotifyHandler;
import com.petya136900.rcebot.handlers.NotifyHandlerHelp;
import com.petya136900.rcebot.handlers.NotifyHandlerHelp2;
import com.petya136900.rcebot.handlers.PairOnWeekHandler;
import com.petya136900.rcebot.handlers.PayloadTextHandler;
import com.petya136900.rcebot.handlers.RegTestHandler;
import com.petya136900.rcebot.handlers.RememberHandler;
import com.petya136900.rcebot.handlers.SayHandler;
import com.petya136900.rcebot.handlers.SpamHandler;
import com.petya136900.rcebot.handlers.StopHandler;
import com.petya136900.rcebot.handlers.TeacherHandler;
import com.petya136900.rcebot.handlers.TestEditKeyHandler;
import com.petya136900.rcebot.handlers.TestHandler;
import com.petya136900.rcebot.handlers.TimetableHandler;
import com.petya136900.rcebot.handlers.TransHandler;
import com.petya136900.rcebot.handlers.WeatherHandler;
import com.petya136900.rcebot.tools.ArrayTools;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.MessageSendResponse.MessageInfo;
import com.petya136900.rcebot.vk.structures.VKMessage;

public class HandlerMapping {
	private static final LinkedHashMap<Pattern, HandlerInterface> handlers = new LinkedHashMap<>();
	private static final LinkedHashMap<Pattern, HandlerInterface> payloadHandlers = new LinkedHashMap<>();
	private static final LinkedHashMap<Pattern, HandlerInterface> callbackPayloadHandlers = new LinkedHashMap<>();
	public static void   registerBasicHandlers() {
		addHandler("^(#|!)",new NothingHandler());
		addHandler("^(genkey|клав)+(.)*?(\\b)", new CreateKeyboardHandler());
		addHandler("(multi)",new MultiHandler());
		addHandler("((вкл)+(.)+?ув.д.)",new NotifyHandlerHelp());
		addHandler("((выкл|откл)+(.)+?ув.д.)",new NotifyHandlerHelp2());	
		addHandler("^(главное меню)",new FixMainMenuHandler());
		addHandler("(^|^( ))+(пар|расписани|замен)+([а-я]{1})+($|( ))+",new TimetableHandler()); // \s
		addHandler("^(test|тест)$",new TestHandler());
		addHandler("((транс|trans)+(([^a-zа-я])*)?)$",new TransHandler());
		addHandler("((debug)+(([^a-zа-я])*)?)$",new DebugHandler());
		addHandler("^(furry|фурри|anime|аниме)",new FurryHandler());
		addHandler("(^settings)",new BotSettingsHandler());
		addHandler("(^stop|стоп)$",new StopHandler());
		addHandler("(^(погода|weather))",new WeatherHandler());
		addHandler("(^(hostname))",new HostNameHandler());
		addHandler("^(time)",x-> x.reply(   new Date(System.currentTimeMillis()).toString()   ));
		addHandler("(^(spam))",new SpamHandler());
		addHandler("(^спутник|geo)",new GeoHandler());		
		addHandler("(ув.домлен.)",new NotifyHandler());
		addHandler("^(help|помощь|команды|справка)",new FaqHandler());
		addHandler("(^(звонк))", new CallsHandler());
		addHandler("^(вход)$", new EnterHandler());
		addHandler("^(хачик|хачан|манга)", new MangaHandler());
		addHandler("^(каб|кабинет|палат)", new CabinetHandler());
		addHandler("^(препод|tdh47)", new TeacherHandler());
		addHandler("^(say|скажи)",new SayHandler());
		addHandler("^names$", x->x.reply("Текущие имена: \n"+Stream.of(Mentions.getNames().split("\\|"))
			.filter(y->y!=null&&y.length()>0)
			.distinct()
			.collect(Collectors.joining(", "))
		));
		//
		addHandler("^(regtest)",new RegTestHandler());
		addHandler("^(напомни)", new RememberHandler());
		addHandler("^(удали|delete)",x->{
			VKMessage replyMessage = x.getVK().getReply_message();
			VKMessage[] fwdMessages = x.getVK().getFwd_messages();
			VKMessage[] pinnedMessages = ArrayTools.concatenate(new VKMessage[] {replyMessage}, fwdMessages);
			if(pinnedMessages.length<1) {
				x.reply("Ты не прикрепил сообщения бота, которые нужно удалить");
				return;
			}
			for(VKMessage pinnedMessage : pinnedMessages) {
				if(pinnedMessage==null)
					continue;
				VKMessage[] messages = VK.getByConversationMessageId(pinnedMessage.getPeer_id(), pinnedMessage.getConversation_message_id());
				if(messages!=null) {
					for(VKMessage message : messages) {
						VK.deleteMessageByPeerAndConv(message.getPeer_id(),message.getConversation_message_id());
						try {
							Thread.sleep(25);
						} catch (InterruptedException ignored) {
						}
					}
				}
				try {
					Thread.sleep(120);
				} catch (InterruptedException ignored) {
				}
			}
		});
		//
		addHandler("^(hc)",new HCHandler());
		//
		addHandler("servertime",x->x.reply(VK.getServerTime()+""));
		//
		addHandler("testdelete",x->{
			MessageInfo messageInfo = x.reply("Get it!");
			x.reply("MessageInfo: "+JsonParser.toJson(messageInfo));
			try {Thread.sleep(2200);}catch (Exception ignored) {}
			if(!(messageInfo.getMessage_id().equals(0))) {
				messageInfo.deleteMessage();
			} else {
				x.reply("Ошибка: Нельзя удалять сообщения в этой беседе");
			}
		});
		//
		addHandler("romagay",new TestEditKeyHandler());
		//
		addHandler("testedit", x->{
			MessageInfo messageInfo = x.reply("Hi!");
			try {Thread.sleep(1000);} catch (Exception e) {e.printStackTrace();}
			VK.editMessage(messageInfo.getPeer_id(),messageInfo.getConversation_message_id(),("MessageInfo: "+JsonParser.toJson(messageInfo)));
			try {Thread.sleep(1000);} catch (Exception e) {e.printStackTrace();}
			StringBuffer sb = new StringBuffer();
			"Hello World!".chars()
	            .mapToObj(Character::toChars)
	            .forEach(z->{
					sb.append(z);
					try {Thread.sleep(150);} catch (Exception e) {e.printStackTrace();}
					VK.editMessage(messageInfo.getPeer_id(),messageInfo.getConversation_message_id(),sb.toString());
				});
			try {Thread.sleep(250);} catch (Exception e) {e.printStackTrace();}
			sb.append(" ");
			"Done! ^^".chars()
            .mapToObj(Character::toChars)
            .forEach(z->{
				sb.append(z);
				try {Thread.sleep(150);} catch (Exception e) {e.printStackTrace();}
				VK.editMessage(messageInfo.getPeer_id(),messageInfo.getConversation_message_id(),sb.toString());
			});		
		});
		//
		//////////////////////	EXAMPLES
		//
		addHandler("^(hello)", x->x.reply("World!"));
		//
		// anonymous class
		addHandler("testAnonym", new HandlerInterface() {			
			String oldMessage; // !			
			@Override public void handle(VK vkContent) {
				try {switch(vkContent.getVK().getStage()) {
					case("1"): 
						vkContent.reply("{A}Stage 1 \n Enter message: "); 
						vkContent.getVK().setStage("2"); 
						break; 
					case("2"): 
						oldMessage = vkContent.getVK().getText();
						vkContent.reply("{A}Stage 2\n Ok! I remember it, write more"); 
						vkContent.getVK().setStage("3");
						break;
					case("3"): 
						vkContent.reply("{A}Stage 3"); 
						vkContent.getVK().removeStages();
						vkContent.reply("{A}You asked me to remember: "+oldMessage);
						break;
					default: vkContent.reply("{A}Unknown stage"); break;
				}} catch (Exception e) { vkContent.reply("{A}Stage is null"); }
			}
		});
		// lambda
		addHandler("testLambda", (vkContent)->{
			try {switch(vkContent.getVK().getStage()) {
				case("1"): 
					vkContent.reply("{L}Stage 1 \n Enter message: "); 
					vkContent.getVK().setStage("2"); 
					break; 
				case("2"): 
					vkContent.getVK().dataSet("oldMessage", vkContent.getVK().getText()); // !
					vkContent.reply("{L}Stage 2\n Ok! I remember it, write more"); 
					vkContent.getVK().setStage("3");
					break;
				case("3"): 
					vkContent.reply("{L}Stage 3"); 
					vkContent.getVK().removeStages();
					vkContent.reply("{L}You asked me to remember: "+vkContent.getVK().dataGet("oldMessage")); // !
					vkContent.getVK().dataClear(); // !
					break;
				default: vkContent.reply("{L}Unknown stage"); break;
			}} catch (Exception e) { vkContent.reply("{L}Stage is null"); }
		});
		//////////////////////		
		addPayloadHandler("^text",new PayloadTextHandler());
		addPayloadHandler("^test_payload_handler",new InfoAboutClientHandler());
		addPayloadHandler("^pairs",new TimetableHandler());
		addPayloadHandler("^week",new PairOnWeekHandler());
		addPayloadHandler("^call",new CallOnWeekHandler());
		addPayloadHandler("^cabs_week",new CabsOnWeekHandler());
		addPayloadHandler("^cab_on_day",new CabOnDayHandler());
		addPayloadHandler("^say",new SayHandler());
		//
		addCallbackPayloadHandler("^callback1", new CallBack1Handler());
	}	
	public static void   addHandler(String regex, HandlerInterface handler) {
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		HandlerMapping.handlers.put(pattern, handler);
	}
	public static void   addPayloadHandler(String regex, HandlerInterface handler) {
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		HandlerMapping.payloadHandlers.put(pattern, handler);
	}
	public static void   addCallbackPayloadHandler(String regex, HandlerInterface handler) {
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		HandlerMapping.callbackPayloadHandlers.put(pattern, handler);
	}
	public static LinkedHashMap<Pattern, HandlerInterface> getHandlers() {
		return handlers;
	}
	public static LinkedHashMap<Pattern, HandlerInterface> getPayloadHandlers() {
		return payloadHandlers;
	}
	public static LinkedHashMap<Pattern, HandlerInterface> getCallbackPayloadHandlers() {
		return callbackPayloadHandlers;
	}
	public static void setDefaultHandler(HandlerInterface dfltHandler) {
		MainHandler.setDefaultHandler(dfltHandler);
	}
	public static void setPerformOnlyHandler(boolean b) {
		MainHandler.setPerformOnlyHandler(b);
	}
	public static void setTestMode(boolean b) {
		MainHandler.setTestMode(b);
	}	
	public static void setQuiteTestMode(boolean b) {
		MainHandler.setQuiteTestMode(b);
	}
	public static boolean getTestMode() {
		return MainHandler.getTestMode();
	}
}
