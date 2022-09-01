package com.petya136900.rcebot.lifecycle;

import java.util.Arrays;

import com.petya136900.rcebot.tools.RegexpTools;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.VKAttachment;
import com.petya136900.rcebot.vk.structures.VKJson;

public class Mentions {
	private static  String names;
	public static void    registerNames(String[] names) {
		Mentions.names=Arrays.toString(names).replace(" ","").replace("[","").replace("]","").replace(",","|");
	}
	public static void clearNames() {
		names=null;
	}
	public static String getNames() {
		return names;
	}
	public static boolean isMention(VKJson vkJson) {
		if(vkJson.getMessage()==null) {
			return vkJson.getObject().getUser_id().equals(vkJson.getObject().getPeer_id());
		}
		String oldMessage = vkJson.getText();
		if(oldMessage==null) {
			oldMessage="";
		}
		if(oldMessage.trim().length()<1) {
			if(vkJson.getAttachments()!=null) {
				if(vkJson.getAttachments().length>0) {
					VKAttachment audioM = vkJson.getAttachments()[0];
					if(audioM.getType().equalsIgnoreCase("audio_message")) {
						oldMessage = RegexpTools.replaceRegexp(VK.getAudioMessageTranscript(vkJson.getPeer_id(),vkJson.getConversation_message_id(),10),"[^а-яА-Яa-zA-Z 0-9]", "", true);
						vkJson.setText(oldMessage);
						Logger.printInfo(oldMessage);
					}
				}
			}
		}
		if(oldMessage.length()!=0) {
			if(oldMessage.length()>100) {
				oldMessage=oldMessage.substring(0,99);
			}
			String mentionRegexp = "^("+names+")+([., ])*([^a-zа-я0-9]|$)";
			String bracketMentionRegexp="^\\[("+names+")\\|+.*]+([ ,])*($| |)";
			if(checkLocalRegexp(bracketMentionRegexp,oldMessage,vkJson)) {
				return true;
			} else if(checkLocalRegexp(mentionRegexp, oldMessage, vkJson)) {
				return true;
			}
		}
		return vkJson.getMessage().getFrom_id().equals(vkJson.getMessage().getPeer_id());
	}
	private static boolean checkLocalRegexp(String regexp,String oldMessage,VKJson vkJson) {
		if(RegexpTools.checkRegexp(regexp,oldMessage)) {
			vkJson.setText(RegexpTools.removeFirstString(vkJson.getText(), regexp)); // Remove mention
			return true;
		} else {
			return false;
		}
	}

}
