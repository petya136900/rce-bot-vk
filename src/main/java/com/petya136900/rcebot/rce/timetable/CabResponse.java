package com.petya136900.rcebot.rce.timetable;

import java.util.ArrayList;

public class CabResponse {
	private Boolean error=false;
	private CabsData[] replaces;
	private CabsData[] mains;
	public CabResponse(MainShelude[] replaces, MainShelude[] mains) {
		ArrayList<CabsData> replacesAL = new ArrayList<CabsData>();
		ArrayList<CabsData> mainsAL = new ArrayList<CabsData>();
		for(MainShelude replace : replaces) {
			replacesAL.add(new CabsData(replace.getPairs()[0].getPairNum(),
					replace.getGroupName(),
					replace.getPairs()[0].getPairName()));
		}
		for(MainShelude main : mains) {
			mainsAL.add(new CabsData(main.getPairs()[0].getPairNum(),
					main.getGroupName(),
					main.getPairs()[0].getPairName()));
		}
		if(replacesAL.size()>0) {
			this.replaces=replacesAL.toArray(new CabsData[replacesAL.size()]);
		}
		if(mainsAL.size()>0) {
			this.mains=mainsAL.toArray(new CabsData[mainsAL.size()]);
		}
	}
	public CabsData[] getMains() {
		return mains;
	}
	public void setMains(CabsData[] mains) {
		this.mains = mains;
	}
	public CabsData[] getReplaces() {
		return replaces;
	}
	public void setReplaces(CabsData[] replaces) {
		this.replaces = replaces;
	}
	public Boolean getError() {
		return error;
	}
	public void setError(Boolean error) {
		this.error = error;
	}
}
