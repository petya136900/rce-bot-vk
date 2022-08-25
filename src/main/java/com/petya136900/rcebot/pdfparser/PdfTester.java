package com.petya136900.rcebot.pdfparser;

import java.io.IOException;
import java.net.URL;

public class PdfTester {
    public static void main(String[] args) throws IOException, Exception {
    //	PdfParser pdfParser = new PdfParser("C:\\Apache24\\htdocs\\rasp\\220120205.pdf",true);
    	String[] rasps = new String[] {
    			"01062020.pdf",
    			"010620201.pdf",
    			"02062020.pdf",
    			"03062020.pdf",
    			"04062020.pdf",
    			"040620201.pdf",
    			"05062020.pdf",
    			"06052020.pdf",
    			"06062020.pdf",
    			"070420201.pdf",
    			"07052020.pdf",
    			"08052020.pdf",
    			"080520201.pdf",
    			"08062020.pdf",
    			"09062020.pdf",
    			"090620201.pdf",
    			"10042020.pdf",
    			"100620201.pdf",
    			"11042020.pdf",
    			"15042020.pdf",
    			"15052020.pdf",
    			"16012020.pdf",
    			"220120205.pdf",
    			"27012020.pdf",
    			"280120202.pdf",
    			"30062020.pdf",
    			"300620201.pdf",
    			"300620202.pdf",  			
    	};
    	System.out.println("Начинаю парсинг "+rasps.length+" пдф");
    	long start = System.currentTimeMillis();
    	for(String rasp : rasps) {
        	PdfParser pdfParser = new PdfParser(new URL("http://localhost/rasp/"+rasp),true);
        	pdfParser.parse();
    	}
    	System.out.println("Парсинг завершен за "+(System.currentTimeMillis()-start)+"ms");
	}
}