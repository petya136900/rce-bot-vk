package com.petya136900.rcebot.lifecycle;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.petya136900.rcebot.tools.RegexpTools;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.VKAttachment;
import com.petya136900.rcebot.vk.structures.VKJson;

public class Mentions {
	private static  String names;
	public static void    registerNames(String[] names) {
		Mentions.names=Arrays.toString(names).replace(" ","").replace("[","").replace("]","").replace(",","|");
	}
	public static String getNames() {
		return names;
	}
	public  Boolean isMention(VKJson vkJson) {
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
			if(oldMessage.length()>40) {
				oldMessage=oldMessage.substring(0,40);
			}
			String regexp = "\\b("+names+")+((.)+?(\\b|$)|$)"; 
			// String regexp = "(?i)^("+names+")+(.)?($|(\\b))+";
			String regexp2="(?i)^(\\[("+names+")\\|.+?\\]+(,)?)";
			if(checkLocalRegexp(regexp2,oldMessage,vkJson)) {
				return true;
			} else if(checkLocalRegexp(regexp,oldMessage,vkJson)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	private boolean checkLocalRegexp(String regexp,String oldMessage,VKJson vkJson) {
		Pattern pattern = Pattern.compile(regexp, Pattern.UNICODE_CASE);
		Matcher matcher=pattern.matcher(oldMessage.toLowerCase());
		if(matcher.find()) {
			vkJson.setText(RegexpTools.removeFirstString(vkJson.getText(), regexp)); // Remove
														                        // Mention
			return true;
		} else {
			return false;
		}
	}

}
