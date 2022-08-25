package com.petya136900.rcebot.rce.timetable;

public class GroupsHistoryObject {
	private Integer newOffect=0;
	private Boolean prevAvailable=false;
	private Boolean nextAvailable=false;
	private String[] groupNames;
	public String[] getGroupNames() {
		return groupNames;
	}
	public void setGroupNames(String[] groupNames) {
		this.groupNames = groupNames;
	}
	public Boolean getNextAvailable() {
		return nextAvailable;
	}
	public void setNextAvailable(Boolean nextAvailable) {
		this.nextAvailable = nextAvailable;
	}
	public Boolean getPrevAvailable() {
		return prevAvailable;
	}
	public void setPrevAvailable(Boolean prevAvailable) {
		this.prevAvailable = prevAvailable;
	}
	public Integer getNewOffset() {
		return newOffect;
	}
	public void setNewOffect(Integer newOffect) {
		this.newOffect = newOffect;
	}
}
