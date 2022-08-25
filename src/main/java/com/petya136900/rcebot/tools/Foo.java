package com.petya136900.rcebot.tools;

import java.lang.reflect.Field;

public class Foo {
	public String fooVariable1="bar1";
	public String fooVariable2="bar2";
	public String fooVariable3="bar3";
	public String Best="rce(no)";
	public void   setBarValue(final String x) {
		this.fooVariable1 = x;
	}
	public String findValue(Object foo, String fooVariable) { // foo - Class Container, fooVar - Class Variable
		int varHash = fooVariable.hashCode();
		Class<?> c = foo.getClass();
		String varName="UNKNOWN";
		for (Field field : c.getDeclaredFields()) {
			try {
				if(varHash==field.get(foo).hashCode()) {
					varName=field.getName();
					break;
				}
			} catch(IllegalAccessException e) {
				varName="IAE_ERROR";
			}
		}
		return varName;
	}
	public static String getMethodName(Class<?> class1) {
		return class1.getEnclosingMethod().getName();
	}
}
