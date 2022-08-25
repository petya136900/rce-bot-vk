package com.petya136900.rcebot.trash;
import java.lang.reflect.Field;
public class Bar {
	public static void   main(String[] args) {
		Foo foo = new Foo();
		foo.setBarValue("barValue");
		Class<?> c = foo.getClass();
		for (Field field : c.getDeclaredFields()) {
			try {
				System.out.println(field.getName()+hash(field.getName()) + " = " +  field.get(foo)+hash(field.get(foo)));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		System.out.println(foo.findValue(foo,foo.fooVariable1));
	}
	public static String hash(Object object) {
		return "("+object.hashCode()+")";
	}
}
