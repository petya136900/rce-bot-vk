package com.petya136900.rcebot.vk.structures;

public class LongPollObjectCB {
	private Integer failed;
	private String ts;
	private VKJsonCB[] updates;
	public VKJsonCB[] getUpdates() {
		return updates;
	}
	public void setUpdates(VKJsonCB[] updates) {
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
	public LongPollObject toLongPollObject() {
		VKJson[] updates = new VKJson[this.updates.length];
		for(int i=0;i<this.updates.length;i++) {
			if(this.updates[i]==null) {
				updates[i]=null;
			} else {
				updates[i] = this.updates[i].toVKJson();
			}
		}
		return new LongPollObject(failed,ts,updates);
	}
}
