package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.vk.VK;

public class NotifyHandlerHelp implements HandlerInterface {

	@Override
	public void handle(VK vkContent) {
		VK vk = vkContent;
		vk.getVK().setText(vkContent.getVK().getText()+" вкл ");
		new NotifyHandler().handle(vk);
	}

}
