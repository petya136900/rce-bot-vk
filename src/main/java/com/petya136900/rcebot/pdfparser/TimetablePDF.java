package com.petya136900.rcebot.pdfparser;

import java.util.ArrayList;

public class TimetablePDF {
	private String day;
	private String ver;
	private String type="2";
	private ArrayList<TimetablePDFPage> pages = new ArrayList<TimetablePDFPage>();
	public TimetablePDF(String day, String ver) {
		this.setDay(day);
		this.setVer(ver);
	}
	/**
	 * @return the pages
	 */
	public ArrayList<TimetablePDFPage> getPages() {
		return pages;
	}

	/**
	 * @param pages the pages to set
	 */
	public void setPages(ArrayList<TimetablePDFPage> pages) {
		this.pages = pages;
	}

	public TimetablePDF addPage(TimetablePDFPage timetablePDFPage) {
		this.pages.add(timetablePDFPage);
		return this;
	}
	/**
	 * @return the ver
	 */
	public String getVer() {
		return ver;
	}
	/**
	 * @param ver the ver to set
	 */
	public void setVer(String ver) {
		this.ver = ver;
	}
	/**
	 * @return the day
	 */
	public String getDay() {
		return day;
	}
	/**
	 * @param day the day to set
	 */
	public void setDay(String day) {
		this.day = day;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
