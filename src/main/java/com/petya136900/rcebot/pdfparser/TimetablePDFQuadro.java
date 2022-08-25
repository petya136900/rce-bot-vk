package com.petya136900.rcebot.pdfparser;

import java.util.ArrayList;

import com.petya136900.rcebot.rce.timetable.TimetableBlock;

public class TimetablePDFQuadro {
	private Boolean isPractic=false;
	private ArrayList<TimetableBlock> blocks = new ArrayList<TimetableBlock>();
	/**
	 * @return the blocks
	 */
	public ArrayList<TimetableBlock> getBlocks() {
		return blocks;
	}
	/**
	 * @param blocks the blocks to set
	 */
	public void setBlocks(ArrayList<TimetableBlock> blocks) {
		this.blocks = blocks;
	}
	/**
	 * @return the isPractic
	 */
	public Boolean getIsPractic() {
		return isPractic;
	}
	/**
	 * @param isPractic the isPractic to set
	 */
	public void setIsPractic(Boolean isPractic) {
		this.isPractic = isPractic;
	}
	public TimetablePDFQuadro addBlock(TimetableBlock timetableBlock) {
		this.blocks.add(timetableBlock);
		return this;
	}
	public void addBlock(String groupName) {
		this.blocks.add(new TimetableBlock(groupName));
	}
}
