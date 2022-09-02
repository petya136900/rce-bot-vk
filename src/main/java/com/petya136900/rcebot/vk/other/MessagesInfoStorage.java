package com.petya136900.rcebot.vk.other;

import java.util.concurrent.ConcurrentHashMap;

//import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.Keyboard;
import com.petya136900.rcebot.vk.structures.MessageSendResponse.MessageInfo;

public class MessagesInfoStorage {
	private static ConcurrentHashMap<Integer, MessageInfo> infoMap;
	static {
		infoMap = new ConcurrentHashMap<Integer, MessageInfo>();
	}
	public static MessageInfo getMessageInfo(Integer peerID) {
		MessageInfo mi = infoMap.get(peerID);
		return mi;
	}
	public static void setMessageInfo(Integer peerID,MessageInfo mi) {
		infoMap.put(peerID, mi);
	}
	public static MessageInfo sendUndUpdate(Integer peer_id, String message, String[] attachs, Keyboard keyboard) {
		MessageInfo mi = infoMap.get(peer_id);
		MessageInfo newMi;
		if(mi==null) {
			//System.out.println("mi == null, send new");
			newMi = VK.sendMessage(peer_id, message, attachs, keyboard);
		} else {
			//System.out.println("mi != null, try to edit old");
			newMi = mi.editMessageOrDeleteAndReply(message, attachs, keyboard);
			//System.out.println("newMi is null: "+(newMi==null));
			if(newMi!=null) {
				//System.out.println(JsonParser.toJson(newMi));
			}
		}
		if(mi==null||!(newMi.equals(mi))) {
			infoMap.put(peer_id, newMi);
		}
		return newMi;
	}
}
