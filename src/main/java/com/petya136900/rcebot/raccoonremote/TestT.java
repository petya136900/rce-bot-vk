package com.petya136900.rcebot.raccoonremote;
import static com.petya136900.rcebot.handlers.RaccoonRemoteHandler.roundUp;

public class TestT {

	public static void main(String[] args) {
		ru(1,2);
		ru(1,3);
		ru(9,4);
		ru(16,4);
		ru(3,4);
		ru(8,4);
		ru(9,4);
	}

	private static void ru(long i, long j) {
		System.out.println(i+"/"+j+": "+roundUp(i, j));
	}

}
