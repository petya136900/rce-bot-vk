package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.lifecycle.MainHandler;
import com.petya136900.rcebot.lifecycle.NotifyLoop;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.other.CallBack;
import com.petya136900.rcebot.vk.other.LongPoll;

public class StopHandler implements HandlerInterface {

	@Override
	public void handle(VK vkContent) {
		if(MainHandler.checkAdmin(vkContent.getVK().getFrom_id())) {
			String hostName = vkContent.getVK().getText().replaceFirst("stop","")
					.replaceFirst("стоп","").trim();
			if(hostName.trim().length()>0)
				if(!(HostNameHandler.getUserHostname().toLowerCase().contains(hostName.trim().toLowerCase())))
					return;
			vkContent.reply("Остановка..");
			NotifyLoop.stopNotify();
			if(CallBack.getEnabled()) {
				CallBack.stop();
			} 
			if(LongPoll.getEnabled()) {
				LongPoll.stopLongPoll();
			}
			vkContent.reply("Bye! ^_^");
			System.exit(0);
			} else {
			vkContent.reply("ERROR | Access denied");
		}
	}

}
