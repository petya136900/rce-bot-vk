package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.EventData;
import com.petya136900.rcebot.vk.structures.EventData.Type;

public class CallBack1Handler implements HandlerInterface {
	
	@Override
	public void handle(VK vkContent) {
		EventData event_data = new EventData(Type.SHOW_SNACKBAR,"Отлично! У вас есть поддержка Callback-кнопок\n"
				+ ("peer_id: "+vkContent.getVK().getObject().getPeer_id()+"\n")+
				("user_id: "+vkContent.getVK().getObject().getUser_id()+"\n")+
				("event_id: "+vkContent.getVK().getObject().getEvent_id()+"\n"));
		vkContent.replyMessageEventAnswer(event_data);
	}
	
	@Override
	public void callbackUnsupportedHandle(VK vkContent) {
		vkContent.reply("Oh, your VK-client don't support CallBack-buttons");
	}

}
