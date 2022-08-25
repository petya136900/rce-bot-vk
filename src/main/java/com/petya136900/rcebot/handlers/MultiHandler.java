package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.vk.VK;

public class MultiHandler implements HandlerInterface {
	private static Integer counter = 0;
	private Integer peerID;
	private String message;
	private Integer curCounter;
	@Override
	public void handle(VK vkContent) {
		this.curCounter=MultiHandler.counter+1;
		this.peerID=vkContent.getVK().getPeer_id();
		this.message=vkContent.getVK().getText();
		MultiHandler.counter++;
		if(curCounter>10) {
			curCounter=1;
			MultiHandler.counter=1;
		}
		try {
			Thread.sleep(10000/curCounter);
			VK.sendMessage(peerID, "#"+curCounter+"/"+counter+" | "+message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
