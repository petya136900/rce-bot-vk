package com.petya136900.rcebot.handlers;

import java.util.stream.Stream;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.Button;
import com.petya136900.rcebot.vk.structures.Keyboard;
import com.petya136900.rcebot.vk.structures.KeyboardLine;
import com.petya136900.rcebot.vk.structures.Button.Type;
import com.petya136900.rcebot.vk.structures.MessageSendResponse.MessageInfo;

public class TestEditKeyHandler implements HandlerInterface {
	// 6 x 5
	private static final int RED=3;
	private static final int GREEN=2;
	private static final int BLUE=1;
	private static final int WHITE=0;
	
	@Override
	public void handle(VK vkContent) {
		Keyboard kf0 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{WHITE,WHITE,WHITE,WHITE,GREEN},
			{WHITE,WHITE,WHITE,WHITE,GREEN},
			{WHITE,WHITE,WHITE,WHITE,GREEN},
			{WHITE,WHITE,WHITE,WHITE,GREEN},
			{WHITE,WHITE,WHITE,WHITE,GREEN}}
		);
		Keyboard kf1 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{WHITE,WHITE,WHITE,GREEN,GREEN},
			{WHITE,WHITE,WHITE,GREEN,GREEN},
			{WHITE,WHITE,WHITE,GREEN,GREEN},
			{WHITE,WHITE,WHITE,GREEN,WHITE},
			{WHITE,WHITE,WHITE,GREEN,WHITE}}
		);
		Keyboard kf2 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{WHITE,WHITE,GREEN,GREEN,WHITE},
			{WHITE,WHITE,GREEN,GREEN,WHITE},
			{WHITE,WHITE,GREEN,GREEN,WHITE},
			{WHITE,WHITE,GREEN,WHITE,WHITE},
			{WHITE,WHITE,GREEN,WHITE,WHITE}}
		);
		Keyboard kf3 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{WHITE,GREEN,GREEN,WHITE,GREEN},
			{WHITE,GREEN,GREEN,WHITE,GREEN},
			{WHITE,GREEN,GREEN,WHITE,GREEN},
			{WHITE,GREEN,WHITE,WHITE,GREEN},
			{WHITE,GREEN,WHITE,WHITE,GREEN}}
		);
		Keyboard kf4 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{GREEN,GREEN,WHITE,GREEN,GREEN},
			{GREEN,GREEN,WHITE,GREEN,WHITE},
			{GREEN,GREEN,WHITE,GREEN,WHITE},
			{GREEN,WHITE,WHITE,GREEN,WHITE},
			{GREEN,WHITE,WHITE,GREEN,GREEN}}
		);
		Keyboard kf5 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{GREEN,WHITE,GREEN,GREEN,GREEN},
			{GREEN,WHITE,GREEN,WHITE,GREEN},
			{GREEN,WHITE,GREEN,WHITE,GREEN},
			{WHITE,WHITE,GREEN,WHITE,GREEN},
			{WHITE,WHITE,GREEN,GREEN,GREEN}}
		);
		Keyboard kf6 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{WHITE,GREEN,GREEN,GREEN,WHITE},
			{WHITE,GREEN,WHITE,GREEN,WHITE},
			{WHITE,GREEN,WHITE,GREEN,WHITE},
			{WHITE,GREEN,WHITE,GREEN,WHITE},
			{WHITE,GREEN,GREEN,GREEN,WHITE}}
		);
		Keyboard kf7 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{GREEN,GREEN,GREEN,WHITE,GREEN},
			{GREEN,WHITE,GREEN,WHITE,GREEN},
			{GREEN,WHITE,GREEN,WHITE,GREEN},
			{GREEN,WHITE,GREEN,WHITE,GREEN},
			{GREEN,GREEN,GREEN,WHITE,GREEN}}
		);
		Keyboard kf8 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{GREEN,GREEN,WHITE,GREEN,WHITE},
			{WHITE,GREEN,WHITE,GREEN,GREEN},
			{WHITE,GREEN,WHITE,GREEN,WHITE},
			{WHITE,GREEN,WHITE,GREEN,WHITE},
			{WHITE,GREEN,WHITE,GREEN,WHITE}}
		);
		Keyboard kf9 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{GREEN,WHITE,GREEN,WHITE,GREEN},
			{GREEN,WHITE,GREEN,GREEN,GREEN},
			{GREEN,WHITE,GREEN,WHITE,GREEN},
			{GREEN,WHITE,GREEN,WHITE,GREEN},
			{GREEN,WHITE,GREEN,WHITE,GREEN}}
		);
		Keyboard kf10 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{WHITE,GREEN,WHITE,GREEN,WHITE},
			{WHITE,GREEN,GREEN,GREEN,WHITE},
			{WHITE,GREEN,WHITE,GREEN,WHITE},
			{WHITE,GREEN,WHITE,GREEN,WHITE},
			{WHITE,GREEN,WHITE,GREEN,WHITE}}
		);
		Keyboard kf11 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{GREEN,WHITE,GREEN,WHITE,WHITE},
			{GREEN,GREEN,GREEN,WHITE,GREEN},
			{GREEN,WHITE,GREEN,WHITE,GREEN},
			{GREEN,WHITE,GREEN,WHITE,GREEN},
			{GREEN,WHITE,GREEN,WHITE,GREEN}}
		);
		Keyboard kf12 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{WHITE,GREEN,WHITE,WHITE,GREEN},
			{GREEN,GREEN,WHITE,GREEN,WHITE},
			{WHITE,GREEN,WHITE,GREEN,GREEN},
			{WHITE,GREEN,WHITE,GREEN,WHITE},
			{WHITE,GREEN,WHITE,GREEN,WHITE}}
		);
		Keyboard kf13 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{GREEN,WHITE,WHITE,GREEN,GREEN},
			{GREEN,WHITE,GREEN,WHITE,GREEN},
			{GREEN,WHITE,GREEN,GREEN,GREEN},
			{GREEN,WHITE,GREEN,WHITE,GREEN},
			{GREEN,WHITE,GREEN,WHITE,GREEN}}
		);
		Keyboard kf14 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{WHITE,WHITE,GREEN,GREEN,WHITE},
			{WHITE,GREEN,WHITE,GREEN,WHITE},
			{WHITE,GREEN,GREEN,GREEN,WHITE},
			{WHITE,GREEN,WHITE,GREEN,WHITE},
			{WHITE,GREEN,WHITE,GREEN,WHITE}}
		);
		Keyboard kf15 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{WHITE,GREEN,GREEN,WHITE,RED},
			{GREEN,WHITE,GREEN,WHITE,RED},
			{GREEN,GREEN,GREEN,WHITE,RED},
			{GREEN,WHITE,GREEN,WHITE,RED},
			{GREEN,WHITE,GREEN,WHITE,RED}}
		);
		Keyboard kf16 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{GREEN,GREEN,WHITE,RED,RED},
			{WHITE,GREEN,WHITE,RED,WHITE},
			{GREEN,GREEN,WHITE,RED,WHITE},
			{WHITE,GREEN,WHITE,RED,WHITE},
			{WHITE,GREEN,WHITE,RED,WHITE}}
		);
		Keyboard kf17 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{GREEN,WHITE,RED,RED,WHITE},
			{GREEN,WHITE,RED,WHITE,WHITE},
			{GREEN,WHITE,RED,WHITE,WHITE},
			{GREEN,WHITE,RED,WHITE,WHITE},
			{GREEN,WHITE,RED,WHITE,WHITE}}
		);
		Keyboard kf18 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{WHITE,RED,RED,WHITE,GREEN},
			{WHITE,RED,WHITE,WHITE,GREEN},
			{WHITE,RED,WHITE,WHITE,GREEN},
			{WHITE,RED,WHITE,WHITE,GREEN},
			{WHITE,RED,WHITE,WHITE,GREEN}}
		);
		Keyboard kf19 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{RED,RED,WHITE,GREEN,GREEN},
			{RED,WHITE,WHITE,GREEN,WHITE},
			{RED,WHITE,WHITE,GREEN,GREEN},
			{RED,WHITE,WHITE,GREEN,WHITE},
			{RED,WHITE,WHITE,GREEN,GREEN}}
		);
		Keyboard kf20 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{RED,WHITE,GREEN,GREEN,WHITE},
			{WHITE,WHITE,GREEN,WHITE,WHITE},
			{WHITE,WHITE,GREEN,GREEN,WHITE},
			{WHITE,WHITE,GREEN,WHITE,WHITE},
			{WHITE,WHITE,GREEN,GREEN,WHITE}}
		);
		Keyboard kf21 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{WHITE,GREEN,GREEN,WHITE,WHITE},
			{WHITE,GREEN,WHITE,WHITE,GREEN},
			{WHITE,GREEN,GREEN,WHITE,GREEN},
			{WHITE,GREEN,WHITE,WHITE,GREEN},
			{WHITE,GREEN,GREEN,WHITE,WHITE}}
		);
		Keyboard kf22 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,WHITE,GREEN},
			{GREEN,GREEN,WHITE,WHITE,WHITE},
			{GREEN,WHITE,WHITE,GREEN,WHITE},
			{GREEN,GREEN,WHITE,GREEN,WHITE},
			{GREEN,WHITE,WHITE,GREEN,WHITE},
			{GREEN,GREEN,WHITE,WHITE,GREEN}}
		);
		Keyboard kf23 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,WHITE,GREEN,GREEN},
			{GREEN,WHITE,WHITE,WHITE,WHITE},
			{WHITE,WHITE,GREEN,WHITE,GREEN},
			{GREEN,WHITE,GREEN,WHITE,GREEN},
			{WHITE,WHITE,GREEN,WHITE,GREEN},
			{GREEN,WHITE,WHITE,GREEN,GREEN}}
		);
		Keyboard kf24 = this.genKeyFrame(new int[][] {
			{WHITE,WHITE,GREEN,GREEN,WHITE},
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{WHITE,GREEN,WHITE,GREEN,WHITE},
			{WHITE,GREEN,WHITE,GREEN,WHITE},
			{WHITE,GREEN,WHITE,GREEN,WHITE},
			{WHITE,WHITE,GREEN,GREEN,WHITE}}
		);
		Keyboard kf25 = this.genKeyFrame(new int[][] {
			{WHITE,GREEN,GREEN,WHITE,WHITE},
			{WHITE,WHITE,WHITE,WHITE,WHITE},
			{GREEN,WHITE,GREEN,WHITE,WHITE},
			{GREEN,WHITE,GREEN,WHITE,WHITE},
			{GREEN,WHITE,GREEN,WHITE,WHITE},
			{WHITE,GREEN,GREEN,WHITE,WHITE}}
		);
		MessageInfo messageInfo = vkContent.reply("gotit");
		Stream<Keyboard> kfStream = Stream.of(kf0,kf1,kf2,kf3,kf4,kf5,kf6,kf7,kf8,kf9,kf10,kf11,kf12,kf13,kf14,kf15,
				kf16,kf17,kf18,kf19,kf20,kf21,kf22,kf23,kf24,kf25);
		kfStream.forEach(x->{
			try{Thread.sleep(300);}catch(Exception ignored){}
			messageInfo.editMessage("gotit", null, x);
		});
	}
	public Keyboard genKeyFrame(int[][] colors) {
		Keyboard keyboard = new Keyboard();
		for(int[] line : colors) {
			KeyboardLine keyLine = new KeyboardLine();
			for(int element : line) {
				Button button = new Button(Type.TEXT);
				switch(element) {
					case(RED):
						button.setLabel("X");
						button.setColor(Button.COLOR_NEGATIVE);
						break;
					case(GREEN):
						button.setLabel("0");
						button.setColor(Button.COLOR_POSITIVE);						
						break;
					case(BLUE):
						button.setLabel("0");
						button.setColor(Button.COLOR_PRIMARY);
						break;
					case(WHITE):
						button.setLabel("_");
						button.setColor(Button.COLOR_SECONDARY);
						break;
				}
				keyLine.addButton(button);
			}
			keyboard.addKeyboardLine(keyLine);
		}
		return keyboard;
	}
}
