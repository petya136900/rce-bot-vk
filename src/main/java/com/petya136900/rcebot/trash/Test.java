package com.petya136900.rcebot.trash;

import java.util.ArrayList;

import com.petya136900.rcebot.rce.teachers.TeacherException;
import com.petya136900.rcebot.rce.timetable.MainShelude;
import com.petya136900.rcebot.rce.timetable.TimetableException;

public class Test {
	public static void main(String[] args) throws TimetableException, InterruptedException, TeacherException {
		/*
		SQLData[] timetables = MySqlConnector.
								//getTimetablesByDay("29072020");
								  getAllTimetables();
		for(SQLData timetable : timetables) {
			System.out.println("Группа: "+
						timetable.getGroupName());
				for(BlockPair pair : timetable.getJsonData().getPairs()) {
					System.out.println("\tПара#"+pair.getPairNum());
					System.out.println("\t\tНазвание: "+pair.getPairName());
					System.out.println("\t\tКабинет: "+pair.getPairCab());
				}
		}
		*/
		//System.out.println(RegexpTools.checkRegexp("\\b(бот)\\b", "бот"));
		//System.out.println(RegexpTools.checkRegexp("(?i)^("+"бот"+")+(.)?(\\b)+", "Бот,пары "));
		//System.out.println(RegexpTools.checkRegexp("(?i)^("+"бот"+")+(\\b)+", "Ботт,"));
		//System.out.println(RegexpTools.checkRegexp("(?i)^("+"бот"+")+(\\b)+", "Бот пары"));
		//System.out.println(RegexpTools.checkRegexp("(?i)^("+"бот"+")+(\\b)+", "Бот,z"));
		
		/*
		System.out.println(Character.isUpperCase('F'));
		System.out.println(Character.isUpperCase('А'));
		System.out.println(Character.isUpperCase('!'));
		System.out.println(Character.isUpperCase('\"'));
		System.out.println(Character.isUpperCase('}'));
		
		String cabName = "/";
		int sIndex = cabName.indexOf("/");
		String cabNameC = cabName.substring(0,sIndex);
		String cabNameZ = cabName.substring(sIndex+1,cabName.length());
		System.out.println(cabNameC);
		System.out.println(cabNameZ);
		
		//MySqlConnector.testPut();
		MySqlConnector.addNewUnknownPairs(new TeacherPair[] {
				new TeacherPair("ИсТория"),
				new TeacherPair("ИнфОрматика"),
				new TeacherPair("математика экзамен")
		});
		
		TeacherPair[] teachersPairs = MySqlConnector.getTeachersPairs();
		for(TeacherPair teachersPair : teachersPairs) {
			System.out.println(teachersPair.getId()+" | "+
							   teachersPair.getType()+" | "+
							   teachersPair.getName()+" | "+
							   teachersPair.getTeacherID());
		}
		MySqlConnector.testGetMainSSA();
		String name =" Щёрбаёа аываыва dfsdfsdf 124124 !!$@$!%~ -0---";
		name = name.replace("ё", "е");
		name = name.replaceAll("([^а-яА-Яa-zA-Z0-9 \\-])", "");
		System.out.println(name);
		*/
		/*
		MainShelude[] mains = new MainShelude[] {
			new MainShelude("ССА-301", "monday", new BlockPair[] {
				new BlockPair(3, "КС", "303")	
			}),
			new MainShelude("ССА-301", "monday", new BlockPair[] {
				new BlockPair(2, "КС", "303")	
			}),
			new MainShelude("ССА-301", "monday", new BlockPair[] {
				new BlockPair(0, "КС", "303")	
			}),
			new MainShelude("ССА-301", "monday", new BlockPair[] {
				new BlockPair(1, "КС", "303")	
			}),
			new MainShelude("ССА-301", "monday", new BlockPair[] {
				new BlockPair(2, "КС", "303")	
			}),
		};
		mains=sortAscending(mains);
		for(MainShelude r : mains) {
			System.out.println((r.getPairs()[0].getPairNum()+" Пара - "+r.getGroupName()+" - "+r.getPairs()[0].getPairName()+"\n"));
		}
		*/
		try {
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unused")
	private static MainShelude[] sortAscending(MainShelude[] mains) {
		ArrayList<MainShelude> newArray=new ArrayList<MainShelude>();
		Integer[] inserted = new Integer[mains.length];
		Integer[] min = new Integer[2]; // index, value
		while(newArray.size()!=mains.length) {
			min = new Integer[] {null,null};
			//System.out.println("Новый проход по элементам");
			for(int i=0;i<mains.length;i++) {
				int cN;
				if(!(intContains(inserted, i))) {
					//System.out.println("Элемент с индексом "+i+" не добавлен, анализ");
					cN = mains[i].getPairs()[0].getPairNum();
					if(cN==0) {
						//System.out.println("Значение 0 - добавляем без сравнений");
						min[0]=i;
						min[1]=cN;
						break;
					}
					if(min[1]==null) {
						//System.out.println("Ненулевое значение, первый проход");
						min[0]=i;
						min[1]=cN;
					} else if(min[1]>cN) {
						//System.out.println("Ненулевое значение, меньше предыдущего");
						min[0]=i;
						min[1]=cN;
					}
				} else {
					//System.out.println("Элемент с индексом "+i+" уже добавлен");
				}
			}
			if(min[0]!=null) {
				inserted[newArray.size()]=min[0];
				newArray.add(mains[min[0]]);
				//System.out.println("Добавляем новый элемент с индексом "+min[0]+"(Значение "+min[1]+")");
				//System.out.println("Всего в новом массиве элементов: "+newArray.size());
			} else {
				//System.out.println("Не нашли, кого добавлять");
			}
		}
		return newArray.toArray(new MainShelude[newArray.size()]);
	}
	private static boolean intContains(Integer[] inserted, Integer i) {
		for(Integer ins : inserted) {
			if(ins!=null) {
				if(ins.equals(i)) {
					return true;
				}
			}
		}
		return false;
	}
}
