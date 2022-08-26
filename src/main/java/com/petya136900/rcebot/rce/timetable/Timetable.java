package com.petya136900.rcebot.rce.timetable;

import java.util.Arrays;
import java.util.Objects;

public class Timetable {
	private Boolean exceptionThrowed = false;
	private Boolean fromDB = false;
	private TimetableException timetableException;
	public Boolean isExceptionThrowed() {
		return exceptionThrowed;
	}
	public TimetableException getTimetableException() {
		return timetableException;
	}
	public Boolean getRceDown() {
		return rceDown;
	}
	private Boolean rceDown=false;
	private Boolean practic=false;
	private String teachers;
	private BlockPair[] pairs;
	private Timetable() {}
	public Timetable(BlockPair[] pairs, String teachers, Boolean practic) {
		BlockPair[] fixedPairs = new BlockPair[pairs.length];
		for(int i=0;i<pairs.length;i++) {
			fixedPairs[i]= new BlockPair(pairs[i].getPairNum(),
					MainsheludeData.removeTrashPair(pairs[i].getPairName()),
					MainsheludeData.removeTrashPair(pairs[i].getPairCab()));
		}
		this.pairs=fixedPairs;
		this.teachers=teachers;
		this.practic=practic;
	}
	public Timetable(BlockPair[] pairs) {
		this.pairs=pairs;
	}
	public BlockPair[] getPairs() {
		return pairs;
	}
	public void setPairs(BlockPair[] pairs) {
		this.pairs = pairs;
	}
	public Boolean getPractic() {
		return practic;
	}
	public void setPractic(Boolean practic) {
		this.practic = practic;
	}
	/**
	 * @return the teachers
	 */
	public String getTeachers() {
		return teachers;
	}
	/**
	 * @param teachers the teachers to set
	 */
	public void setTeachers(String teachers) {
		this.teachers = teachers;
	}
	public static Timetable rceDown(TimetableException e) {
		Timetable t = new Timetable();
		t.exceptionThrowed=true;
		t.rceDown=true;
		t.timetableException=e;
		return t;
	}
	public static Timetable exception(TimetableException te) {
		Timetable t = new Timetable();
		t.exceptionThrowed=true;
		t.timetableException=te;
		return t;
	}
	public Boolean isFromDB() {
		return fromDB;
	}
	public void setFromDB(boolean b) {
		fromDB=b;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Timetable timetable = (Timetable) o;
		return Objects.equals(practic, timetable.practic) && Objects.equals(teachers, timetable.teachers) && Arrays.equals(pairs, timetable.pairs);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(practic, teachers);
		result = 31 * result + Arrays.hashCode(pairs);
		return result;
	}
}