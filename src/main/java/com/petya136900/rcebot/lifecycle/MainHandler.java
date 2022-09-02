package com.petya136900.rcebot.lifecycle;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import com.google.re2j.Pattern;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.tools.RegexpTools;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.Payload;

public class MainHandler implements Runnable {
	private final VK parsedMessage;
	private final static CopyOnWriteArrayList<String> eventList = new CopyOnWriteArrayList<>();
	/**
	 * default true
	 */
	private static Boolean performOnlyOneTask=true;
	/**
	 * default true
	 */
	private static Boolean ignoreUnknowsCallbacks=true;
	private static HandlerInterface defaultHandler=null;
	/**
	 * default false
	 */
	private static Boolean testMode=false;
	private static ConcurrentHashMap<String, Stage> stages = new ConcurrentHashMap<>();
	/**
	 * default true
	 */
	private static boolean quiteTestMode=true;
	/**
	 * Mark messages as readed
	 * default true
	 */
	private static boolean readByDefault=true;
	private final static String MESSAGE_DEV = "@INFO | Development mode enabled+\n"+
								  "@ИНФО | Включен режим разработки\n"+
								  "⌛Попробуйте сделать запрос позже";
	public MainHandler(VK parsedMessage) {
		boolean isMention = Mentions.isMention(parsedMessage.getVK());
		parsedMessage.setInternalMention(isMention);
		this.parsedMessage=parsedMessage;
	}
	public static void setPerformOnlyHandler(Boolean bol) {
		MainHandler.performOnlyOneTask=bol;
	}
	@Override
	public void run() {
		if(checkEventID(parsedMessage.getVK().getEvent_id())) { // Avoid duplicating messages
			Integer peer_id=null;
			Integer from_id=null;
			boolean messageEvent=false;
			if(parsedMessage.getVK().getType().equalsIgnoreCase("message_new")) {
				peer_id=parsedMessage.getVK().getPeer_id();
				from_id=parsedMessage.getVK().getFrom_id();
				Logger.printNewMessage(parsedMessage);
			} else if(parsedMessage.getVK().getType().equalsIgnoreCase("message_event")) {
				peer_id = parsedMessage.getVK().getObject().getPeer_id();
				from_id = parsedMessage.getVK().getObject().getUser_id();
				messageEvent=true;
				Logger.printNewEvent(parsedMessage);
			}
			if(peer_id==null)
				return;
			if(messageEvent|peer_id.equals(from_id) // If pm
			|(!messageEvent && (parsedMessage.isInternalMention()))) { // If mention
				if(!messageEvent&&readByDefault) {
					VK.markAsRead(parsedMessage.getVK().getPeer_id());
				}
				if((!testMode)|checkTestMode(from_id,!messageEvent,peer_id)) {
					findHandler(parsedMessage,messageEvent);
				} else {
					if(!quiteTestMode) {
						parsedMessage.reply(MESSAGE_DEV);	
					}
				}
			}

		}
	}
	public static Boolean getTestMode() {
		return testMode;
	}
	public static Boolean checkAdmin(Integer id) {
		return id.equals(VK.getAdminID()) |
				id.equals(VK.getHelperID());
	}
	public static boolean checkTestMode(Integer from_id, Boolean send, Integer peer_id) {
		if (testMode&(checkAdmin(from_id))) {
				if(send) {
					VK.sendMessage(peer_id,"@DEV MODE\n");
				}
				return true;
		}
		return false;
	}
	public static void findHandler(VK vkData, Boolean callback) {
		String text="";
		if(!callback) {
			text = vkData.getVK().getText();
			if(text==null) {
				text="";
			}
		}
		//vkData.reply("Message come: \n"+text);
		boolean handlerNotFound=true;
		boolean callbackNotSupportedByClient=false;
		boolean fromKeyboard=false;
		String key = (callback?(vkData.getVK().getObject().getPeer_id().toString()):(vkData.getVK().getPeer_id()))
					 +"_"
					 +(callback?(vkData.getVK().getObject().getUser_id().toString()):(vkData.getVK().getFrom_id()));
		Stage prevStage = stages.get(key);
		//System.out.println("key: "+key);
		//
		Payload payload = null;
		if(callback) {
			payload = vkData.getVK().getObject().getPayload();
			fromKeyboard=true;
		} else {
			if(vkData.getVK().getPayload()!=null) {
				try {
					payload = JsonParser.fromJson(vkData.getVK().getPayload(), Payload.class);
					if(payload.isUnsupportedCallback()) {
						callbackNotSupportedByClient=true;
						payload = payload.getUnsupportedCallbackPayload();
					}
					fromKeyboard=true;
				} catch(Exception e) {
					//
				}
			}
		}		
		//
		if(prevStage!=null) {
			//System.out.println("Найден prevStage");
			if(callback|fromKeyboard) {
				if(!(prevStage.getHandler().keyboardSupportStages())) {
					//System.out.println("Но он не поддерживает stages с клавиатурой");
					prevStage=null;
				}
			}
		}
		if(prevStage!=null) {
			// stages
			vkData.getVK().setHandler(prevStage.getHandler());
			vkData.getVK().setStage(prevStage.getName());
			prevStage.getHandler().handle(vkData);
		} else {
			if(payload!=null) {
				//System.out.println("payload!=null");
				String handlerName;
				if((handlerName = payload.getHandler())!=null) {
					//System.out.println("handlerName!=null: "+handlerName);
					Set<Entry<Pattern, HandlerInterface>> entrySet;
					if(callback|callbackNotSupportedByClient) {
						entrySet = HandlerMapping.getCallbackPayloadHandlers().entrySet();
					} else {
						entrySet = HandlerMapping.getPayloadHandlers().entrySet();
					}
					for(Entry<Pattern,HandlerInterface> entry : entrySet) {
						if(RegexpTools.checkRegexp(entry.getKey(),handlerName)) {
							handlerNotFound=false;	
							//HandlerInterface handler = entry.getValue().getClass().newInstance(); // We Need New Class Every Time ( i miss you nio )
							HandlerInterface handler = getInstance(entry.getValue()); // We Need New Class Every Time ( i miss you nio )
							if(callback) {
								vkData.getVK().setIsCallback(true);
							}
							vkData.getVK().setHandler(handler);
							if(callbackNotSupportedByClient) {
								vkData.getVK().setPayload(JsonParser.toJson(payload));
								handler.callbackUnsupportedHandle(vkData);
							} else {
								handler.handle(vkData);	
							}
							//vkData.reply("Regexp["+entry.getKey().toString()+"] - true");
							if(performOnlyOneTask) {
								return;
							}
						}
					}
				}
			}
			if(callback&ignoreUnknowsCallbacks) {
				System.out.println("Unknown callback.. - "+ (payload != null ? payload.getHandler() : null));
				return;
			}
			for(Entry<Pattern,HandlerInterface> entry : HandlerMapping.getHandlers().entrySet()) {
				if(RegexpTools.checkRegexp(entry.getKey(),text)) {
					handlerNotFound=false;	
					//HandlerInterface handler = entry.getValue().getClass().newInstance(); // We Need New Class Every Time ( i miss you nio )
					HandlerInterface handler = getInstance(entry.getValue()); // We Need New Class Every Time ( i miss you nio )
					vkData.getVK().setHandler(handler);
					handler.handle(vkData); 
					//vkData.reply("Regexp["+entry.getKey().toString()+"] - true");
					if(performOnlyOneTask) {
						break;
					}
				}
			}
			// Default Handler if no one 
			if(handlerNotFound) {
				if(getDefaultHandler()!=null) {
					getDefaultHandler().handle(vkData);
				}
			}
		}
	}
	private static HandlerInterface getInstance(HandlerInterface value) {
		try {
			//noinspection deprecation
			return value.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			return value;
		}
	}
	synchronized private boolean checkEventID(String event_id) {
        for (String tempEventID : eventList) {
            if (tempEventID.contains(event_id)) {
            	return false;
            }
        } 
		eventList.add(0,event_id);
		if(!(eventList.size()<25)) {
			eventList.remove(24);
		}
		return true;
	}
	public static void setTestMode(boolean testMode) {
		MainHandler.testMode=testMode;
	}
	public static HandlerInterface getDefaultHandler() {
		return defaultHandler;
	}
	public static void setDefaultHandler(HandlerInterface defaultHandler) {
		MainHandler.defaultHandler = defaultHandler;
	}
	/**
	 * @return the stages
	 */
	public static ConcurrentHashMap<String, Stage> getStages() {
		return stages;
	}
	/**
	 * @param stages the stages to set
	 */
	public static void setStages(ConcurrentHashMap<String, Stage> stages) {
		MainHandler.stages = stages;
	}
	public static void setQuiteTestMode(boolean b) {
		MainHandler.quiteTestMode=b;
	}
	public static Boolean getIgnoreUnknowsCallbacks() {
		return ignoreUnknowsCallbacks;
	}
	public static void setIgnoreUnknowsCallbacks(Boolean ignoreUnknowsCallbacks) {
		MainHandler.ignoreUnknowsCallbacks = ignoreUnknowsCallbacks;
	}
	public static boolean isReadByDefault() {
		return readByDefault;
	}
	public static void setReadByDefault(boolean readByDefault) {
		MainHandler.readByDefault = readByDefault;
	}
}
