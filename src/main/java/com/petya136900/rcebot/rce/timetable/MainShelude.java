package com.petya136900.rcebot.rce.timetable;

import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;

public class MainShelude {
	private String groupName;
	private String weekDay;
	private BlockPair[] pairs;
	private String hash;
	/**
	 * @return the pairs
	 */
	public MainShelude(String groupName, String weekDay, BlockPair[] pairs) {
		this.groupName=groupName;
		this.weekDay=weekDay;
		this.pairs=pairs;
	}
	public BlockPair[] getPairs() {
		return pairs;
	}
	/**
	 * @param pairs the pairs to set
	 */
	public void setPairs(BlockPair[] pairs) {
		this.pairs = pairs;
	}
	/**
	 * @return the weekDay
	 */
	public String getWeekDay() {
		return weekDay;
	}
	/**
	 * @param weekDay the weekDay to set
	 */
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}
	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public void fixWeekType(Boolean bool) {
		ArrayList<BlockPair> newPairs = new ArrayList<BlockPair>();
		for(BlockPair pair : this.pairs) {
			if(pair.getPairName().contains("/")) {
				String newPairName="";
				String oldPairName=pair.getPairName();
				String newPairCab="";
				String oldPairCab=pair.getPairCab();
				if(bool) {
					newPairName=oldPairName.substring(0,oldPairName.indexOf("/"));
				} else {
					newPairName=oldPairName.substring(oldPairName.indexOf("/")).replaceAll("/","");
				}
				if(oldPairCab.contains("/")) {
					if(bool) {
						newPairCab=oldPairCab.substring(0,oldPairCab.indexOf("/"));
					} else {
						newPairCab=oldPairCab.substring(oldPairCab.indexOf("/")).replaceAll("/","");
					}
				} else {
					newPairCab=oldPairCab;
				}
				if(newPairName.trim().length()>0) {
					newPairs.add(new BlockPair(pair.getPairNum(),newPairName,newPairCab));
				}
			} else {
				pair.setPairCab(pair.getPairCab().replace("/",""));
				newPairs.add(pair);
			}
		}
		this.pairs=newPairs.toArray(new BlockPair[newPairs.size()]);
	}
	public void updateHash() {
		StringBuilder sb=new StringBuilder(this.getGroupName()+this.getWeekDay());
		for(BlockPair pair : this.pairs) {
			sb.append(pair.getPairNum()+pair.getPairName()+pair.getPairCab());
		}
		this.hash=DigestUtils.md5Hex(sb.toString());
	}
	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}
	/**
	 * @param hash the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}
}
