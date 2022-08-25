package com.petya136900.rcebot.vk.structures;

public class LongPollObject {
	private Integer failed;
	private String ts;
	private VKJson[] updates;
	public LongPollObject() {
		//
	}
	public LongPollObject(Integer failed, String ts, VKJson[] updates) {
		this.failed=failed;
		this.ts=ts;
		this.updates=updates;
	}
	public VKJson[] getUpdates() {
		return updates;
	}
	public void setUpdates(VKJson[] updates) {
		this.updates = updates;
	}
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	public Integer getFailed() {
		return failed;
	}
	public void setFailed(Integer failed) {
		this.failed = failed;
	}
}
