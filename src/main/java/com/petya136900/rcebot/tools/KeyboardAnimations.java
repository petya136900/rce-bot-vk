package com.petya136900.rcebot.tools;

import java.util.stream.Stream;

import com.petya136900.rcebot.vk.structures.Button;
import com.petya136900.rcebot.vk.structures.Keyboard;
import com.petya136900.rcebot.vk.structures.KeyboardLine;
import com.petya136900.rcebot.vk.structures.Button.Type;

public class KeyboardAnimations {
		// 6 x 5
		private static final int RED=3;
		private static final int GREEN=2;
		private static final int BLUE=1;
		private static final int WHITE=0;
		public static Keyboard genKeyFrame(int[][] colors) {
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
		public static Stream<Keyboard> getFurryStream() {
			return Stream.of(furry_kf0,furry_kf1,furry_kf2,furry_kf3,furry_kf4,furry_kf5,furry_kf6,furry_kf7,furry_kf8,furry_kf9,furry_kf10,furry_kf11,furry_kf12,furry_kf13,furry_kf14,furry_kf15,
					furry_kf16,furry_kf17,furry_kf18,furry_kf19,furry_kf20,furry_kf21,furry_kf22,furry_kf23);
		}
		private static Keyboard furry_kf0 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{WHITE,WHITE,WHITE,WHITE,WHITE}}
		);
		private static Keyboard furry_kf1 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{WHITE,WHITE,WHITE,WHITE,GREEN},
		{WHITE,WHITE,WHITE,WHITE,GREEN},
		{WHITE,WHITE,WHITE,WHITE,GREEN},
		{WHITE,WHITE,WHITE,WHITE,GREEN},
		{WHITE,WHITE,WHITE,WHITE,GREEN}}
		);
		private static Keyboard furry_kf2 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{WHITE,WHITE,WHITE,GREEN,GREEN},
		{WHITE,WHITE,WHITE,GREEN,WHITE},
		{WHITE,WHITE,WHITE,GREEN,GREEN},
		{WHITE,WHITE,WHITE,GREEN,WHITE},
		{WHITE,WHITE,WHITE,GREEN,WHITE}}
		);
		private static Keyboard furry_kf3 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{WHITE,WHITE,GREEN,GREEN,WHITE},
		{WHITE,WHITE,GREEN,WHITE,WHITE},
		{WHITE,WHITE,GREEN,GREEN,WHITE},
		{WHITE,WHITE,GREEN,WHITE,WHITE},
		{WHITE,WHITE,GREEN,WHITE,WHITE}}
		);
		private static Keyboard furry_kf4 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{WHITE,GREEN,GREEN,WHITE,GREEN},
		{WHITE,GREEN,WHITE,WHITE,GREEN},
		{WHITE,GREEN,GREEN,WHITE,GREEN},
		{WHITE,GREEN,WHITE,WHITE,GREEN},
		{WHITE,GREEN,WHITE,WHITE,GREEN}}
		);
		private static Keyboard furry_kf5 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{GREEN,GREEN,WHITE,GREEN,WHITE},
		{GREEN,WHITE,WHITE,GREEN,WHITE},
		{GREEN,GREEN,WHITE,GREEN,WHITE},
		{GREEN,WHITE,WHITE,GREEN,WHITE},
		{GREEN,WHITE,WHITE,GREEN,GREEN}}
		);
		private static Keyboard furry_kf6 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{GREEN,WHITE,GREEN,WHITE,GREEN},
		{WHITE,WHITE,GREEN,WHITE,GREEN},
		{GREEN,WHITE,GREEN,WHITE,GREEN},
		{WHITE,WHITE,GREEN,WHITE,GREEN},
		{WHITE,WHITE,GREEN,GREEN,GREEN}}
		);
		private static Keyboard furry_kf7 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{WHITE,GREEN,WHITE,GREEN,WHITE},
		{WHITE,GREEN,WHITE,GREEN,WHITE},
		{WHITE,GREEN,WHITE,GREEN,WHITE},
		{WHITE,GREEN,WHITE,GREEN,WHITE},
		{WHITE,GREEN,GREEN,GREEN,WHITE}}
		);
		private static Keyboard furry_kf8 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{GREEN,WHITE,GREEN,WHITE,GREEN},
		{GREEN,WHITE,GREEN,WHITE,GREEN},
		{GREEN,WHITE,GREEN,WHITE,GREEN},
		{GREEN,WHITE,GREEN,WHITE,GREEN},
		{GREEN,GREEN,GREEN,WHITE,GREEN}}
		);
		private static Keyboard furry_kf9 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{WHITE,GREEN,WHITE,GREEN,WHITE},
		{WHITE,GREEN,WHITE,GREEN,WHITE},
		{WHITE,GREEN,WHITE,GREEN,WHITE},
		{WHITE,GREEN,WHITE,GREEN,WHITE},
		{GREEN,GREEN,WHITE,GREEN,WHITE}}
		);
		private static Keyboard furry_kf10 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{GREEN,WHITE,GREEN,GREEN,WHITE},
		{GREEN,WHITE,GREEN,WHITE,GREEN},
		{GREEN,WHITE,GREEN,GREEN,WHITE},
		{GREEN,WHITE,GREEN,WHITE,GREEN},
		{GREEN,WHITE,GREEN,WHITE,GREEN}}
		);
		private static Keyboard furry_kf11 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{WHITE,GREEN,GREEN,WHITE,WHITE},
		{WHITE,GREEN,WHITE,GREEN,WHITE},
		{WHITE,GREEN,GREEN,WHITE,WHITE},
		{WHITE,GREEN,WHITE,GREEN,WHITE},
		{WHITE,GREEN,WHITE,GREEN,WHITE}}
		);
		private static Keyboard furry_kf12 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{GREEN,GREEN,WHITE,WHITE,GREEN},
		{GREEN,WHITE,GREEN,WHITE,GREEN},
		{GREEN,GREEN,WHITE,WHITE,GREEN},
		{GREEN,WHITE,GREEN,WHITE,GREEN},
		{GREEN,WHITE,GREEN,WHITE,GREEN}}
		);
		private static Keyboard furry_kf13 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{GREEN,WHITE,WHITE,GREEN,GREEN},
		{WHITE,GREEN,WHITE,GREEN,WHITE},
		{GREEN,WHITE,WHITE,GREEN,GREEN},
		{WHITE,GREEN,WHITE,GREEN,WHITE},
		{WHITE,GREEN,WHITE,GREEN,WHITE}}
		);
		private static Keyboard furry_kf14 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{WHITE,WHITE,GREEN,GREEN,WHITE},
		{GREEN,WHITE,GREEN,WHITE,GREEN},
		{WHITE,WHITE,GREEN,GREEN,WHITE},
		{GREEN,WHITE,GREEN,WHITE,GREEN},
		{GREEN,WHITE,GREEN,WHITE,GREEN}}
		);
		private static Keyboard furry_kf15 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{WHITE,GREEN,GREEN,WHITE,WHITE},
		{WHITE,GREEN,WHITE,GREEN,WHITE},
		{WHITE,GREEN,GREEN,WHITE,WHITE},
		{WHITE,GREEN,WHITE,GREEN,WHITE},
		{WHITE,GREEN,WHITE,GREEN,WHITE}}
		);
		private static Keyboard furry_kf16 = genKeyFrame(new
		int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{GREEN,GREEN,WHITE,WHITE,GREEN},
		{GREEN,WHITE,GREEN,WHITE,GREEN},
		{GREEN,GREEN,WHITE,WHITE,WHITE},
		{GREEN,WHITE,GREEN,WHITE,WHITE},
		{GREEN,WHITE,GREEN,WHITE,WHITE}}
		);
		private static Keyboard furry_kf17 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{GREEN,WHITE,WHITE,GREEN,WHITE},
		{WHITE,GREEN,WHITE,GREEN,WHITE},
		{GREEN,WHITE,WHITE,WHITE,GREEN},
		{WHITE,GREEN,WHITE,WHITE,GREEN},
		{WHITE,GREEN,WHITE,WHITE,GREEN}}
		);
		private static Keyboard furry_kf18 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{WHITE,WHITE,GREEN,WHITE,GREEN},
		{GREEN,WHITE,GREEN,WHITE,GREEN},
		{WHITE,WHITE,WHITE,GREEN,WHITE},
		{GREEN,WHITE,WHITE,GREEN,WHITE},
		{GREEN,WHITE,WHITE,GREEN,WHITE}}
		);
		private static Keyboard furry_kf19 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{WHITE,GREEN,WHITE,GREEN,WHITE},
		{WHITE,GREEN,WHITE,GREEN,WHITE},
		{WHITE,WHITE,GREEN,WHITE,WHITE},
		{WHITE,WHITE,GREEN,WHITE,WHITE},
		{WHITE,WHITE,GREEN,WHITE,WHITE}}
		);
		private static Keyboard furry_kf20 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{GREEN,WHITE,GREEN,WHITE,RED},
		{GREEN,WHITE,GREEN,WHITE,RED},
		{WHITE,GREEN,WHITE,WHITE,WHITE},
		{WHITE,GREEN,WHITE,WHITE,WHITE},
		{WHITE,GREEN,WHITE,WHITE,WHITE}}
		);
		private static Keyboard furry_kf21 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{WHITE,GREEN,WHITE,RED,RED},
		{WHITE,GREEN,WHITE,RED,WHITE},
		{GREEN,WHITE,WHITE,WHITE,WHITE},
		{GREEN,WHITE,WHITE,WHITE,RED},
		{GREEN,WHITE,WHITE,WHITE,RED}}
		);
		private static Keyboard furry_kf22 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{GREEN,WHITE,RED,RED,RED},
		{GREEN,WHITE,RED,WHITE,RED},
		{WHITE,WHITE,WHITE,WHITE,RED},
		{WHITE,WHITE,WHITE,RED,WHITE},
		{WHITE,WHITE,WHITE,RED,WHITE}}
		);
		private static Keyboard furry_kf23 = genKeyFrame(new int[][] {
		{WHITE,WHITE,WHITE,WHITE,WHITE},
		{WHITE,RED,RED,RED,WHITE},
		{WHITE,RED,WHITE,RED,WHITE},
		{WHITE,WHITE,WHITE,RED,WHITE},
		{WHITE,WHITE,RED,WHITE,WHITE},
		{WHITE,WHITE,RED,WHITE,WHITE}}
		);
}
