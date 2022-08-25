package com.petya136900.rcebot.geoapi;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.MessageSendResponse.MessageInfo;
import com.petya136900.rcebot.vk.structures.VKAttachment.Photo;

public class GeoPhotoUploadThread extends Thread {
	private final ArrayList<Photo> array;
	private final String url;
	private final VK vk;
	private final AtomicInteger uploadedImagesCount;
	private final int totalImages;
	private MessageInfo messageInfo;
	public GeoPhotoUploadThread(VK vk,ArrayList<Photo> array,String url, MessageInfo messageInfo, AtomicInteger uploadedImagesCount, int totalImages) {
		this.vk=vk;
		this.array=array;
		this.url=url;
		this.uploadedImagesCount=uploadedImagesCount;
		this.messageInfo=messageInfo;
		this.totalImages=totalImages;
	}
	@Override 
	public void run() {
		try {
			this.array.add(vk.getUploadedPhoto(new URL(this.url)));
			messageInfo.editMessage("Загружено снимков: "+uploadedImagesCount.incrementAndGet()+"/"+totalImages);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
