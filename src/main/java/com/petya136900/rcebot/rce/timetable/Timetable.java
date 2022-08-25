package com.petya136900.rcebot.rce.timetable;

public class Timetable {
	private Boolean practic=false;
	private String teachers;
	private BlockPair[] pairs;
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
}