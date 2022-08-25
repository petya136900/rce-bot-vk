package com.petya136900.rcebot.pdfparser;
import java.util.Comparator;


class RaccoonComparator implements Comparator<RaccoonFloat>{
	@Override
	// REVERSE
	public int compare(RaccoonFloat o1, RaccoonFloat o2) {
		return o2.getValue().compareTo(o1.getValue());
	}
}