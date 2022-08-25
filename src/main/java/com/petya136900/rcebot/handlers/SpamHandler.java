package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.lifecycle.MainHandler;
import com.petya136900.rcebot.vk.VK;

public class SpamHandler implements HandlerInterface {

	@Override
	public void handle(VK vkContent) {
		if(MainHandler.checkAdmin(vkContent.getVK().getFrom_id())) {
			String spamMessage = vkContent.getVK().getText().replace("spam", "").trim();
			if(spamMessage.length()>0) {
				for(int i=0;i<20;i++) {
					vkContent.reply(spamMessage);
					try {
						Thread.sleep(150);
					} catch (InterruptedException e) {
						//e.printStackTrace();
					}
				}
			} else {
				vkContent.reply("Вы не указали сообщение");
			}
		} else {
			vkContent.reply("Access denied");
		}
	}

}
