package com.petya136900.rcebot.pdfparser;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.parser.Vector;
import com.petya136900.rcebot.tools.Pair;

public class RaccoonTextExtractionStrategy implements TextExtractionStrategy {
	public class StringData {
		private String string;
		private Float length;
		private Float endX;	
		private Float minY; // FIX For no-line windows !!!!
		public StringData(String string, Float length, Float endX) {
			this.string=string;
			this.length=length;
			this.endX=endX;
		}
		/**
		 * @return the length
		 */
		public Float getLength() {
			return length;
		}
		/**
		 * @param length the length to set
		 */
		public void setLength(Float length) {
			this.length = length;
		}
		/**
		 * @return the string
		 */
		public String getString() {
			return string;
		}
		/**
		 * @param string the string to set
		 */
		public void setString(String string) {
			this.string = string;
		}
		/**
		 * @return the endX
		 */
		public Float getEndX() {
			return endX;
		}
		/**
		 * @param endX the endX to set
		 */
		public void setEndX(Float endX) {
			this.endX = endX;
		}
		/**
		 * @return the minY
		 */
		public Float getMinY() {
			return minY;
		}
		/**
		 * @param minY the minY to set
		 */
		public void setMinY(Float minY) {
			this.minY = minY;
		}
	}
    private TreeMap<RaccoonFloat, TreeMap<Float, StringData>> textMap;
    public TreeMap<RaccoonFloat, TreeMap<Float, StringData>> getLines() {
    	return textMap;
    }
    public RaccoonTextExtractionStrategy() {
        // reverseOrder используется потому что координата y на странице идет снизу вверх
        textMap = new TreeMap<RaccoonFloat, TreeMap<Float, StringData>>(new RaccoonComparator());
    }
    @Override
    public String getResultantText() {
        StringBuilder stringBuilder = new StringBuilder();
        // итерируемся по строкам
        for (Map.Entry<RaccoonFloat, TreeMap<Float, StringData>> stringMap: textMap.entrySet()) {
            // итерируемся по частям внутри строки
            for (Map.Entry<Float, StringData> entry: stringMap.getValue().entrySet()) {
                stringBuilder.append(entry.getValue());
            }
            stringBuilder.append('\n');
        }

        return stringBuilder.toString();
    }
    @Override
    public void beginTextBlock() {}
    @Override
    public void renderText(TextRenderInfo renderInfo) {
	        // вытаскиваем координаты
	        Float x = renderInfo.getBaseline().getStartPoint().get(Vector.I1);
	        Float y = renderInfo.getBaseline().getStartPoint().get(Vector.I2);
	        Float length = renderInfo.getBaseline().getLength();
        	// Номер страницы не нужен	        
	        if(!((x.equals(779.28f)|x.equals(779.26f))&(length.equals(6.0f)))) {
		        Float endX = renderInfo.getBaseline().getEndPoint().get(Vector.I1);
		        // Если строки имеют небольшой сдвиг вверх\вниз
		        /*
		        for(Float value : textMap.keySet()) {
		        	if(Math.abs(value-y)<3.65f) { // 1.5f // 2.1f // 3.6 - BC teachers error // 3.65
		        		y=value;
		        	}
		        }
		        // если до этого мы не добавляли элементы из этой строчки файла.
		        if (!textMap.containsKey(y)) {
		            textMap.put(y, new TreeMap<Float, StringData>());
		        }
		        textMap.get(y).put(x, new StringData(renderInfo.getText(), length, endX));		        
		        */
		        for(RaccoonFloat rf : textMap.keySet()) {
		        	if(Math.abs(rf.getValue()-y)<3.65f) { // 1.5f // 2.1f // 3.6 - BC teachers error // 3.65
		        		y=rf.getValue();
		        	}
		        }
		        // если до этого мы не добавляли элементы из этой строчки файла.
		        RaccoonFloat rf = new RaccoonFloat(y);
		        if (!textMap.containsKey(rf)) {
		            textMap.put(rf, new TreeMap<Float, StringData>());
		        }
		        textMap.get(rf).put(x, new StringData(renderInfo.getText(), length, endX));
    		} else {
    			//System.out.println("Это номер страницы! - "+renderInfo.getText());
    		}
    }
    @Override
    public void endTextBlock() {}
    @Override
    public void renderImage(ImageRenderInfo imageRenderInfo) {}
    // метод для извлечения строчек с их y-координатой
    ArrayList<Pair<Float, String>> getStringsWithCoordinatesDetail() {
        ArrayList<Pair<Float, String>> result = new ArrayList<Pair<Float, String>>();
        for (Map.Entry<RaccoonFloat, TreeMap<Float, StringData>> stringMap: textMap.entrySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<Float, StringData> entry: stringMap.getValue().entrySet()) {
            	//stringBuilder.append(entry.getValue());
                stringBuilder.append("[x: "+entry.getKey()+
                					 ",l: "+entry.getValue().getLength()+
                					 ",e: "+entry.getValue().getEndX()+
                					 "]: ("+entry.getValue().getString()+
                					 ")");
            }
            result.add(new Pair<Float, String>(stringMap.getKey().getValue(), stringBuilder.toString()));
        }
        return result;
    }
    ArrayList<Pair<Float, String>> getStringsWithCoordinates() {
        ArrayList<Pair<Float, String>> result = new ArrayList<Pair<Float, String>>();
        for (Map.Entry<RaccoonFloat, TreeMap<Float, StringData>> stringMap: textMap.entrySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<Float, StringData> entry: stringMap.getValue().entrySet()) {
            	//stringBuilder.append(entry.getValue());
                stringBuilder.append(entry.getValue().getString());
            }
            result.add(new Pair<Float, String>(stringMap.getKey().getValue(), stringBuilder.toString()));
        }
        return result;
    }   
}
