package com.petya136900.rcebot.rce.timetable;

import java.util.ArrayList;

public class TimetableBlock {
	private String groupName="";
	private String teachers;
	private ArrayList<BlockPair> pairs = new ArrayList<BlockPair>();
	public TimetableBlock(String groupName) {
		this.groupName=groupName;
	}
	public TimetableBlock setup() {
		this.pairs=new ArrayList<BlockPair>();
		for(int i=0;i<8;i++) {
			pairs.add(new BlockPair(i, "", ""));
		}
		return this;
	}
	/**
	 * @return the pairs
	 */
	public ArrayList<BlockPair> getPairs() {
		return pairs;
	}
	/**
	 * @param pairs the pairs to set
	 */
	public void setPairs(ArrayList<BlockPair> pairs) {
		this.pairs = pairs;
	}
	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}
	/**
	 * @param groupName the groupName to set
	 * @return BlockPair or null if pair with pairNum not found
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/**
	 * @param pairNum the pairNum of BlockPair to find
	 * @return BlockPair or null if BlockPair with pairNum not found
	 */	
	public BlockPair getPair(Integer pairNum) {
		for(BlockPair pair : this.getPairs()) {
			if(pair.getPairNum().equals(pairNum)) {
				return pair;
			}
		}
		return null;
	}
	public void setPair(Integer pairNum, BlockPair blockPair) {
		BlockPair neededPair = null;
		for(BlockPair pair : this.getPairs()) {
			if(pair.getPairNum().equals(pairNum)) {
				neededPair=pair;
				break;
			}
		}
		if(neededPair!=null) {
			this.getPairs().set(pairNum,blockPair);
		} else {
			this.pairs.add(blockPair);
		}
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
