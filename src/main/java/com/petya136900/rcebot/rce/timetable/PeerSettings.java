package com.petya136900.rcebot.rce.timetable;

public class PeerSettings {
	private Integer peer_id;
	private String groupName;
	private Integer notifications; // 0 - Disabled, 1 - Enabled 
	private String lastPairs; // md5(pairs+cabs+(message!=null)+(calls!=null)+practic)
	private Integer notifHour; // Hour to check replaces for groupName | 0 - 18
	private Integer workMode; // 1 - All, 2 - Only Replaces	
	/**
	 * @return the peer_id
	 */
	public Integer getPeer_id() {
		return peer_id;
	}
	/**
	 * @param peer_id the peer_id to set
	 */
	public void setPeer_id(Integer peer_id) {
		this.peer_id = peer_id;
	}
	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}
	public PeerSettings(Integer peer_id, String groupName, Integer notifications, String lastPairs, Integer notifHour,
			Integer workMode) {
		this.peer_id = peer_id;
		this.groupName = groupName;
		this.notifications = notifications;
		this.lastPairs = lastPairs;
		this.notifHour = notifHour;
		this.workMode = workMode;
	}
	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/**
	 * @return the notifications
	 */
	public Integer getNotifications() {
		return notifications;
	}
	/**
	 * @param notifications the notifications to set
	 */
	public void setNotifications(Integer notifications) {
		this.notifications = notifications;
	}
	/**
	 * @return the lastPairs
	 */
	public String getLastPairs() {
		return lastPairs;
	}
	/**
	 * @param lastPairs the lastPairs to set
	 */
	public void setLastPairs(String lastPairs) {
		this.lastPairs = lastPairs;
	}
	/**
	 * @return the notifHour
	 */
	public Integer getNotifHour() {
		return notifHour;
	}
	/**
	 * @param notifHour the notifHour to set
	 */
	public void setNotifHour(Integer notifHour) {
		this.notifHour = notifHour;
	}
	/**
	 * @return the workMode
	 */
	public Integer getWorkMode() {
		return workMode;
	}
	/**
	 * @param workMode the workMode to set
	 */
	public void setWorkMode(Integer workMode) {
		this.workMode = workMode;
	}
}
