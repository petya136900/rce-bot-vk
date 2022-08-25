package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.Button;

public class InfoAboutClientHandler implements HandlerInterface {
	
	@Override
	public boolean keyboardSupportStages() {
		return true;
	}
	
	@Override
	public void handle(VK vkContent) {
		vkContent.reply(getInfo(vkContent));
	}
	public static String getInfo(VK vkContent) {
		return "TEST PAYLOAD"+"\n"+
				"from_id: "+vkContent.getVK().getFrom_id()+"\n"+
				"peer_id: "+vkContent.getVK().getPeer_id()+"\n"+
				"conv_message_id: "+vkContent.getVK().getConversation_message_id()+"\n"+
				"message_id: "+vkContent.getVK().getId()+"\n"+
				"message: "+(vkContent.getVK().getText().length()>16?vkContent.getVK().getText().substring(0, 16)+"...":vkContent.getVK().getText())+"\n\n"+
				"keyboard: "+vkContent.getClient_info().getKeyboard()+"\n"+
				"inline: "+vkContent.getClient_info().getInline_keyboard()+"\n"+
				"lang_id: "+vkContent.getClient_info().getLang_id()+"\n\n"+
				"text: "+vkContent.getClient_info().support(Button.Type.TEXT)+"\n"+
				"location: "+vkContent.getClient_info().support(Button.Type.LOCATION)+"\n"+
				"open_link: "+vkContent.getClient_info().support(Button.Type.OPEN_LINK)+"\n"+
				"open_app: "+vkContent.getClient_info().support(Button.Type.VKAPPS)+"\n"+
				"vkpay: "+vkContent.getClient_info().support(Button.Type.VKPAY)+"\n"+
				"callback: "+vkContent.getClient_info().support(Button.Type.CALLBACK);
	}
}
