package com.petya136900.rcebot.pdfparser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.petya136900.rcebot.pdfparser.RaccoonTextExtractionStrategy.StringData;
import com.petya136900.rcebot.rce.timetable.BlockPair;
import com.petya136900.rcebot.rce.timetable.TimetableBlock;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.tools.Pair;
import com.petya136900.rcebot.tools.RegexpTools;

public class PdfParser {
	private Boolean practicMode=false;
	private Boolean debug=true;
	private File parseLog=null;	
	private FileWriter fw=null;
	private Boolean logCreated=false;
	private ArrayList<Pair<Float, String>> stringDetails=null;
	private PdfReader reader=null;
	private String filename="";
    public PdfParser(String pdf, Boolean debug) throws IOException {
    	File timeTablePdfFile = new File(pdf); // new File(pdf);
    	this.reader=new PdfReader(timeTablePdfFile.getAbsolutePath());
    	this.filename=timeTablePdfFile.getName();
    	this.debug=debug;
    }
	public PdfParser(URL url, boolean debug) throws IOException {
		
		HttpURLConnection.setFollowRedirects(false);
		//System.out.println("2Открываю соединение..");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:221.0) Gecko/20100101 Firefox/31.0");
		//System.out.println("GET");
		conn.setRequestMethod("GET");
		//System.out.println(conn.getResponseCode());
		//System.out.println(conn.getResponseMessage());		
		
		this.reader=new PdfReader(conn.getInputStream());
		this.filename=url.getPath().substring(url.getPath().lastIndexOf("/")+1);
    	this.debug=debug;
	}
	public TimetablePDF parse() throws Exception {
		logCreated=false;
        if(debug) {
        	try {
	        	//parseLog= new File("C:\\Users\\petya136900\\Desktop\\log"+filename+".pdf.java");
	        	File logsDir = new File("/home/"+System.getProperty("user.name")+"/timetables/logs/");
	        	logsDir.mkdirs();
	        	parseLog= new File(logsDir+"/log"+filename+".pdf.java");
	        	if(parseLog.exists()) {
	        		logCreated=true;
	        	}
        	} catch (Exception e) {
        		System.err.println("Can't create logFile: "+parseLog!=null?parseLog.getAbsolutePath():null);
			}
        }
        TimetablePDF tpdf;
        if(filename.contains("%A0%D0%B0%D1%81%D0%BF%D0")) {
        	filename = filename.replace("%D0%A0%D0%B0%D1%81%D0%BF%D0%B8%D1%81%D0%B0%D0%BD%D0%B8%D0%B5%20%D0%B7%D0%B2%D0%BE%D0%BD%D0%BA%D0%BE%D0%B2%20%D0%BD%D0%B0%20", "")
        			.replace("%D1%80%D0%B0%D1%81%D0%BF%D0%B8%D1%81%D0%B0%D0%BD%D0%B8%D1%8F%20%D0%B7%D0%B2%D0%BE%D0%BD%D0%BA%D0%BE%D0%B2%20%D0%BD%D0%B0%20", "").trim();
        	tpdf = new TimetablePDF(filename.substring(0,8), 
        			"call"+(filename.substring(8,filename.lastIndexOf("."))));
        } else {
	        tpdf = new TimetablePDF(filename.substring(0,8),
	        									filename.substring(8,filename.lastIndexOf(".")));
        }
        if(debug&logCreated) {
        	fw = new FileWriter(parseLog);
        }
        try {     
	        for (int i = 1; i <= reader.getNumberOfPages(); ++i) {
	            RaccoonTextExtractionStrategy strategy = new RaccoonTextExtractionStrategy();
	            long startTime = System.currentTimeMillis();
	            if(debug) {
	            	System.out.printf("Начинаю парсинг %d страницы%n", i);
	            }
	            PdfTextExtractor.getTextFromPage(reader,i, strategy);
	            if(debug) {
		            System.out.printf("Парсинг завершен: %dms%n", (System.currentTimeMillis()-startTime));
		            System.out.println("Page : " + i);
	            }
	            ArrayList<Pair<Float, String>> strings=strategy.getStringsWithCoordinates();
	            // Создаем новую страницу	
	            TimetablePDFPage page = new TimetablePDFPage();	            
	            for(Pair<Float, String> entrys : strings) {
	            	if(debug) {
	            		System.out.println("E: "+entrys.getValue());
	            	}
	            	if(page.isCalls()==false) { 
	            		if(RegexpTools.checkRegexp("([а-яА-Я]{2}-\\d{2})|(сании занятий)", entrys.getValue().toLowerCase())) { 			
	            			if(debug) {
	            				System.out.println("Это расписание");
	            			}	            			
	            			page.setIsTimetable(true);
	            			tpdf.setType("1");
	            		}
	            		if(entrys.getValue().toLowerCase().contains("звонков")) {
	            			if(debug) {
	            				System.out.println("Это замена звонков");
	            			}
	            			page.setIsTimetable(false);
	            			page.setCalls(true);
	            		}
	            	} else {
	            		if(page.getOtherStringData()==null) {
	            			page.setOtherStringData("");
	            		}
	            		page.setOtherStringData(page.getOtherStringData()+entrys.getValue().trim()+"\n");
	            	}
	            }
	            if(page.isCalls()) {
	            	StringBuilder sb = new StringBuilder("");
	            	for(Pair<Float, String> string : strings) {
	            		String st = string.getValue().trim();
	            		if(st.length()>0) {
	            			if(!(st.equals("РАСПИСАНИЕ"))) {
	            				if(!(st.equals("ЗВОНКОВ"))) {
	            					sb.append(st+"\n");
	            				}
	            			}
	            		}
	            	}
	            	page.setOtherStringData(sb.toString());
	            	tpdf.addPage(page);
	            	continue;
	            }
	            if((!page.getIsTimetable())&!page.isCalls()) {
            		if(page.getOtherStringData()==null) {
            			page.setOtherStringData("");
            		}	            	
	            	for(Pair<Float, String> entrys : strings) {
	            		if(!(entrys.getValue().replaceAll("\\d", "").trim().length()<1)) {
	            			page.setOtherStringData(page.getOtherStringData()+
	            					entrys.getValue().trim()+"\n");
	            		}
	            	}
	            	tpdf.addPage(page);
	            	continue;
	            	//throw new Exception("WTFException");
	            }
	            stringDetails=strategy.getStringsWithCoordinatesDetail();
	            TreeMap<RaccoonFloat, TreeMap<Float, StringData>> stringObjects=strategy.getLines();
	            for(int line=0;line<strings.size();line++) {
	            	if(strings.get(line).getValue().toLowerCase().contains("учебных практик")) {
	            		if(debug) {
	            			System.out.println("Практика!");
	            		}
	            		practicMode=true;
	            		//break;
	            	}
	            	if(RegexpTools.checkRegexp("([а-яА-Я]{2}-\\d{2})", strings.get(line).getValue())) {
	            		if(debug&logCreated) {
		            		fw.append("Группы!"+"\n");
		            		//System.out.println("Группы!");
	            		}
	            		// Ищем группы
	            		// Блок из 4-ех груп
	            		TimetablePDFQuadro tquadro = new TimetablePDFQuadro();
	            		Float key = strings.get(line).getKey();
						parseGroups(tquadro,
	            				stringObjects.get(new RaccoonFloat(key)));
	            		if(practicMode) {
	            			tquadro.setIsPractic(true);
	            			line=line+1;
	            			key=strings.get(line).getKey();
	            			parseTeachers(tquadro, 
	            					stringObjects.get(new RaccoonFloat(key)),strings,line);
	            		}		            		
	            		if(tquadro.getBlocks()!=null) {
	            			if(tquadro.getBlocks().size()>0) {
	            				// Парсим пары
	            				line=line+parsePairs(tquadro,
	            							stringObjects,
	            							strings,
	            							line
	            						);
	            			}
	            		}
	            		page.addPart(tquadro);		            		
	            	}
	            	if(debug&logCreated) {
		            	fw.append(strings.get(line).getValue()+"\n");
		            	fw.append("[y "+stringDetails.get(line).getKey().toString()+"]:" + " " + stringDetails.get(line).getValue().replaceAll("\\[x:", "\n\t\\[x:")+"\n");
		            	//System.out.println(strings.get(line).getValue());
		            	//System.out.println("[y "+stringDetails.get(line).getKey().toString()+"]:" + " " + stringDetails.get(line).getValue());		            	
	            	}
	            }    
	            tpdf.addPage(page);
	    	}
	        if(debug&logCreated) {
	        	fw.close();
	        }
	        if(debug) {
	        	System.out.println(JsonParser.toJson(tpdf));
        	}
	    } catch(IOException ioe){
	        System.out.println(ioe.getMessage());
	    }                 
        reader.close();		    
		return tpdf;
	}
	private int parsePairs(TimetablePDFQuadro tquadro, TreeMap<RaccoonFloat, TreeMap<Float, StringData>> stringObjects,
			ArrayList<Pair<Float, String>> strings, int line) throws IOException {
		int fix=0;
		int i=0;
		int pc=8;
		TreeMap<Float, StringData> supportStrings = null;
		if(practicMode) {
			pc=7;
		}
		for(;i<pc;) {
			int lineNum = line+i+fix+1;
			if(strings.size()<=lineNum) {
				break;
			}
			Float key = strings.get(lineNum).getKey();
			Float nextKey=null;
			if(strings.size()>lineNum+1) {
				if(!practicMode) {
					if(strings.get(lineNum+1)!=null) {
						nextKey = strings.get(lineNum+1).getKey();	
					} 	
				}
			}
			if((strings.get(lineNum).getValue().trim().length()>0)) {
				if(nextKey!=null) {
					if(strings.get(lineNum+1).getValue().trim().length()>0) {
						if((checkLineBlock(stringObjects.get(new RaccoonFloat(nextKey))))) {
							//System.out.println("Найден блок!");
							// Fix for white blocks
							if(supportStrings==null) {
								//System.out.println("Блок в первый раз");
								supportStrings=stringObjects.get(new RaccoonFloat(key));
							} else {
								//System.out.println("Блок в последующий раз");
								supportStrings.putAll(stringObjects.get(new RaccoonFloat(key)));
							}
							fix++;
							continue;
						}
					}
				}
				System.out.println("Пара#"+i+": "+strings.get(lineNum).getValue().trim());
				if(debug&logCreated) {
					fw.append(strings.get(lineNum).getValue()+"\n");
	            	fw.append("[y "+stringDetails.get(lineNum).getKey().toString()+"]:" + " " + stringDetails.get(lineNum).getValue().replaceAll("\\[x:", "\n\t\\[x:")+"\n");
				}
				TreeMap<Float, StringData> stringObject = stringObjects.get(new RaccoonFloat(key));
				if(supportStrings!=null) {
					stringObject.putAll(supportStrings);
					supportStrings=null;
				}
				parsePairLine(i,tquadro,stringObject);
				i++;
			} else {
				fix++;
			}
		}
		return (i+fix);
	}
	private boolean checkLineBlock(TreeMap<Float, StringData> treeMap) {
		for(Float xs : treeMap.keySet()) {
			if(xs<30f) {
				return false;
			}
			break;
		}
		return true;
	}
	private void parsePairLine(int pairNum, TimetablePDFQuadro quadro,
			TreeMap<Float, StringData> treeMap) {
		//System.out.println("Пара#"+i+": "+strings.get(line+i+fix+1).getValue().trim());
		StringBuilder[] pairs = new StringBuilder[4];
		StringBuilder[] cabs = new StringBuilder[4];
		for(int j=0;j<4;j++) {
			pairs[j] = new StringBuilder("");
			cabs[j] = new StringBuilder("");
		}	
		int[] delimeters;
		if(!practicMode) {
			delimeters = new int[] {47,207,240,397,433,591,621,780,821};
		} else {
			delimeters = new int[] {43,310,563,813};
		}
		/*
		 * GROUPS
		 * 
		 * 47 - 240
		 * 240 - 433
		 * 433 - 621  
		 * 621 - 821 
		 * 
		 * // {47,240,433,621,821};
		 *
		 * PAIR | CAB
		 *
		 * 47  - 207 | 207 - 240
		 * 240 - 397 | 397 - 433
		 * 433 - 591 | 591 - 621  
		 * 621 - 780 | 780 - 821 
		 * 
		 * // {47,207,240,397,433,591,621,780,821};
		 * 
		 * 
		 * PRACTIC 
		 * 
		 * 43  - 310 
		 * 310 - 563
		 * 563 - 815
		 * 
		 * // {43,310,563,813};
		 */
		for(Entry<Float, StringData> stringPart : treeMap.entrySet()) {
				if(!practicMode) {
					if((stringPart.getKey()>=delimeters[0])&stringPart.getKey()<=delimeters[1]) {
						appendString(0,pairs,stringPart,false);
					} else if((stringPart.getKey()>delimeters[1])&stringPart.getKey()<=delimeters[2]) {
						appendString(0,cabs,stringPart,false);
					} else if((stringPart.getKey()>delimeters[2])&stringPart.getKey()<=delimeters[3]) {
						appendString(1,pairs,stringPart,false);
					} else if((stringPart.getKey()>delimeters[3])&stringPart.getKey()<=delimeters[4]) {
						appendString(1,cabs,stringPart,false);
					} else if((stringPart.getKey()>delimeters[4])&stringPart.getKey()<=delimeters[5]) {
						appendString(2,pairs,stringPart,false);
					} else if((stringPart.getKey()>delimeters[5])&stringPart.getKey()<=delimeters[6]) {
						appendString(2,cabs,stringPart,false);
					} else if((stringPart.getKey()>delimeters[6])&stringPart.getKey()<=delimeters[7]) {
						appendString(3,pairs,stringPart,false);
					} else if((stringPart.getKey()>delimeters[7])&stringPart.getKey()<=delimeters[8]) {
						appendString(3,cabs,stringPart,false);
					}			
				} else {
					if((stringPart.getKey()>=delimeters[0])&stringPart.getKey()<=delimeters[1]) {
						appendString(0,pairs,stringPart,false);
					} else if((stringPart.getKey()>delimeters[1])&stringPart.getKey()<=delimeters[2]) {
						appendString(1,pairs,stringPart,false);
					} else if((stringPart.getKey()>delimeters[2])&stringPart.getKey()<=delimeters[3]) {
						appendString(2,pairs,stringPart,false);
					} 				
				}
		}
		for(int c = 0; c < 4; c++) {
			if(pairs[c].toString().trim().length()>0) {
				//System.out.print("Пара#"+pairNum+" Группа#"+c+" - "+pairs[c].toString().trim());
				//System.out.print(" | "+cabs[c].toString().trim());
				//System.out.println();
			}
			TimetableBlock group;
			if(quadro.getBlocks().size()>c) {
				group = quadro.getBlocks().get(c);
			} else {
				group=null;
			}
			if(group!=null) {
				if(pairs[c].toString().trim().length()>0) {
					group.setPair(pairNum, new BlockPair(pairNum, pairs[c].toString().trim(), cabs[c].toString().trim()));
				}
			} else {
				//System.err.println("Group not created");
			}
		}		
	}
	private void parseTeachers(TimetablePDFQuadro tquadro, 
		    TreeMap<Float, StringData> treeMap, ArrayList<Pair<Float, String>> strings, int line) throws IOException {
    	if(debug&logCreated) {
        	fw.append(strings.get(line).getValue()+"\n");
        	fw.append("[y "+stringDetails.get(line).getKey().toString()+"]:" + " " + stringDetails.get(line).getValue().replaceAll("\\[x:", "\n\t\\[x:")+"\n");
        	//System.out.println(strings.get(line).getValue());
        	//System.out.println("[y "+stringDetails.get(line).getKey().toString()+"]:" + " " + stringDetails.get(line).getValue());		            	
    	}		
		StringBuilder[] teachers = new StringBuilder[3];
		for(int j=0;j<3;j++) {
			teachers[j]=new StringBuilder("");
		}	
		int[] delimeters = new int[] {43,310,563,813};
		for(Entry<Float, StringData> entry : treeMap.entrySet()) { 
			if((entry.getKey()>=delimeters[0])&entry.getKey()<=delimeters[1]) {
				appendString(0,teachers,entry,false);
			} else if((entry.getKey()>delimeters[1])&entry.getKey()<=delimeters[2]) {
				appendString(1,teachers,entry,false);
			} else if((entry.getKey()>delimeters[2])&entry.getKey()<=delimeters[3]) {
				appendString(2,teachers,entry,false);
			} 
		}
		for(int i=0;i<tquadro.getBlocks().size();i++) {
			//System.out.println("Преподы: "+teachers[i].toString());
			tquadro.getBlocks().get(i).setTeachers(teachers[i].toString().trim());
		}		
	}
	private void parseGroups(TimetablePDFQuadro tquadro, 
		    TreeMap<Float, StringData> treeMap) {
		StringBuilder[] groups = new StringBuilder[4];
		for(int i=0;i<4;i++) {
			groups[i] = new StringBuilder("");
		}
		int[] delimeters = new int[] {47,240,433,621}; 
		if(practicMode) {
			delimeters = new int[] {43,310,563,816};
		}
		for(Entry<Float, StringData> entry : treeMap.entrySet()) {
			//System.out.println(entry.getKey()+" - x");
			//System.out.println(entry.getValue().getString()+" - value");
			// 47 - 240
			// 240 - 433
			// 433 - 621
			// 621 - ~
			if(practicMode) {
				if((entry.getKey()>=delimeters[0])&entry.getKey()<=delimeters[1]) {
					appendString(0,groups,entry,true);
				} else if((entry.getKey()>delimeters[1])&entry.getKey()<=delimeters[2]) {
					appendString(1,groups,entry,true);
				} else if((entry.getKey()>delimeters[2])&entry.getKey()<=delimeters[3]) {
					appendString(2,groups,entry,true);
				}				
			} else {
				if((entry.getKey()>=delimeters[0])&entry.getKey()<=delimeters[1]) {
					appendString(0,groups,entry,true);
				} else if((entry.getKey()>delimeters[1])&entry.getKey()<=delimeters[2]) {
					appendString(1,groups,entry,true);
				} else if((entry.getKey()>delimeters[2])&entry.getKey()<=delimeters[3]) {
					appendString(2,groups,entry,true);
				} else if((entry.getKey()>delimeters[3])) {
					appendString(3,groups,entry,true);
				}
			}
		}
		for(StringBuilder group : groups) {
			if(group.toString().trim().length()>0) {
				tquadro.addBlock(group.toString().trim());
			}
		}
		for(int i=0;i<4;i++) {
			//System.out.println("Group "+(i+1)+" - "+groups[i].toString());
		}
		//System.out.println(JsonParser.toJson(tquadro));
	}
	private void appendString(int i, StringBuilder[] array, Entry<Float, StringData> entry, Boolean trim) {
		String line = entry.getValue().getString();
		if(trim) {
			line = line.trim();
		}
		array[i].append(line);
	}
}
