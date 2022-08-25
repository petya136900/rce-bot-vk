package com.petya136900.rcebot.pdfparser;

public class RaccoonFloat implements Comparable<RaccoonFloat> {
	private Float value;
	private Float minFloat; // FIX
	public RaccoonFloat(Float value) {
		this.value=value;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass()) {
			try {
				Float f = (Float) obj;
				if(f.equals(value)) {
					return true;
				}
			} catch (Exception e) {
				return false;
			}
		}
		RaccoonFloat other = (RaccoonFloat) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	public Float getMinFloat() {
		return minFloat;
	}
	public void setMinFloat(Float minFloat) {
		this.minFloat = minFloat;
	}
	public Float getValue() {
		return value;
	}
	public void setValue(Float value) {
		this.value = value;
	}
	@Override
	public int compareTo(RaccoonFloat o) {
		return value.compareTo(o.getValue());
	}
}