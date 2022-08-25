package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.tools.Translitor;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.VKAttachment;
import com.petya136900.rcebot.vk.structures.VKMessage;

public class TransHandler implements HandlerInterface {
	private Boolean empty=true;
	@Override
	public void handle(VK vkContent) {
		String transcript;
		if(vkContent.getVK().getReply_message()!=null) {
			if(vkContent.getVK().getReply_message().getText().length()>0) {
				empty=false;
				vkContent.reply( 
						new Translitor().fixTranslit(vkContent.getVK().getReply_message().getText()));
			}
			VKAttachment[] attach = vkContent.getVK().getReply_message().getAttachments();
			if(attach!=null) {
				if(attach.length>0) {
					if(attach[0].getType().equals("audio_message")) {
						empty=false;
						transcript=attach[0].getAudio_message().getTranscript()!=null?attach[0].getAudio_message().getTranscript():
							"Голосовая обрабатывается или длиннее 30~ секунд";
						vkContent.reply(transcript.trim().length()>0?transcript:"Тишина");
					}
				}
			}		
		}	
		for(VKMessage fwd : vkContent.getVK().getFwd_messages()) {
			if(fwd.getText().length()>0) {
				empty=false;
				vkContent.reply(
						new Translitor().fixTranslit(fwd.getText()));
			}
			for(VKAttachment attach : fwd.getAttachments()) {
				if(attach.getType().equals("audio_message")) {
					empty=false;
					transcript=attach.getAudio_message().getTranscript()!=null?attach.getAudio_message().getTranscript():
						"Голосовая обрабатывается или длиннее 30~ секунд";
					vkContent.reply(transcript.trim().length()>0?transcript:"Тишина");
				}				
			}
		}	
		if(empty) {
			vkContent.reply("Ты не переслал мне голосовое или текстовое сообщение");
		}
	}

}
