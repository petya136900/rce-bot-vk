package com.petya136900.rcebot.pdfparser;

import java.util.ArrayList;

public class TimetablePDFPage {
	private Boolean isTimetable=false;
	private Boolean calls=false;
	private ArrayList<TimetablePDFQuadro> parts = new ArrayList<TimetablePDFQuadro>();
	private String otherStringData;
	/**
	 * @return the parts
	 */
	public ArrayList<TimetablePDFQuadro> getParts() {
		return parts;
	}

	/**
	 * @param parts the parts to set
	 */
	public void setParts(ArrayList<TimetablePDFQuadro> parts) {
		this.parts = parts;
	}

	public TimetablePDFPage addPart(TimetablePDFQuadro timetablePDFQuadro) {
		this.parts.add(timetablePDFQuadro);
		return this;
	}

	/**
	 * @return the otherStringData
	 */
	public String getOtherStringData() {
		return otherStringData;
	}

	/**
	 * @param otherStringData the otherStringData to set
	 */
	public void setOtherStringData(String otherStringData) {
		this.otherStringData = otherStringData;
	}

	/**
	 * @return the isTimetable
	 */
	public Boolean getIsTimetable() {
		return isTimetable;
	}

	/**
	 * @param isTimetable the isTimetable to set
	 */
	public void setIsTimetable(Boolean isTimetable) {
		this.isTimetable = isTimetable;
	}

	public void setCalls(boolean b) {
		this.calls=b;
	}
	public Boolean isCalls() {
		return calls;
	}
}
