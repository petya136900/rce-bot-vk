package com.petya136900.rcebot.rce.timetable;

import java.io.File;

import com.petya136900.rcebot.rce.timetable.TimetableException.ExceptionCode;
import com.petya136900.rcebot.trash.Foo;

public class TimetableDirectories {

	private static final String USERNAME = System.getProperty("user.name");
	private static final String WORK_DIR = "/home/"+USERNAME+"/timetables";
	
	public static void checkDirectories(String day) throws TimetableException {
		File workDir = new File(WORK_DIR);
		File dayDir = new File(workDir+"/"+day);
		try {
			if(!dayDir.exists()) {
				dayDir.mkdirs();
			}
		} catch(Exception e) {
			System.err.println("Can't craete dir: "+dayDir.getAbsolutePath());
			class MN{}; throw new TimetableException(ExceptionCode.UNKWN_ERROR, Foo.getMethodName(MN.class), e.getLocalizedMessage(), e, dayDir.getAbsolutePath());
		}
	}

}
