package com.petya136900.rcebot.rce.timetable;

import java.util.Objects;

public class BlockPair {
	private Integer pairNum;
	private String pairName;
	private String pairCab;
	public BlockPair(Integer pairNum,String pairName,String pairCab) {
		this.pairNum=pairNum;
		this.pairName=pairName;
		this.pairCab=pairCab;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BlockPair blockPair = (BlockPair) o;
		return Objects.equals(pairNum, blockPair.pairNum) && Objects.equals(pairName, blockPair.pairName) && Objects.equals(pairCab, blockPair.pairCab);
	}
	@Override
	public int hashCode() {
		return Objects.hash(pairNum, pairName, pairCab);
	}

	/**
	 * @return the pairCab
	 */
	public String getPairCab() {
		return pairCab;
	}
	/**
	 * @param pairCab the pairCab to set
	 */
	public void setPairCab(String pairCab) {
		this.pairCab = pairCab;
	}
	/**
	 * @return the pairName
	 */
	public String getPairName() {
		return pairName;
	}
	/**
	 * @param pairName the pairName to set
	 */
	public void setPairName(String pairName) {
		this.pairName = pairName;
	}
	/**
	 * @return the pairNum
	 */
	public Integer getPairNum() {
		return pairNum;
	}
	/**
	 * @param pairNum the pairNum to set
	 */
	public void setPairNum(Integer pairNum) {
		this.pairNum = pairNum;
	}
}
