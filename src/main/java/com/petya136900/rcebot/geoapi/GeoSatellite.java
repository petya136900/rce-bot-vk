package com.petya136900.rcebot.geoapi;

public class GeoSatellite {
	private long dt; // UnixTime
	private String type; // Satellite name
	private float dc; // Approximate %
	private float cl; // clouds %
	private Sun sun;
	public class Sun {
		private float azimuth;
		private float elevation;
		public float getAzimuth() {
			return azimuth;
		}
		public void setAzimuth(float azimuth) {
			this.azimuth = azimuth;
		}
		public float getElevation() {
			return elevation;
		}
		public void setElevation(float elevation) {
			this.elevation = elevation;
		}
	}
	private Image image;
	public class Image {
		private String truecolor;
		private String falsecolor;
		private String nvdi;
		private String evi;
		public String getEvi() {
			return evi;
		}
		public void setEvi(String evi) {
			this.evi = evi;
		}
		public String getNvdi() {
			return nvdi;
		}
		public void setNvdi(String nvdi) {
			this.nvdi = nvdi;
		}
		public String getFalsecolor() {
			return falsecolor;
		}
		public void setFalsecolor(String falsecolor) {
			this.falsecolor = falsecolor;
		}
		public String getTruecolor() {
			return truecolor;
		}
		public void setTruecolor(String truecolor) {
			this.truecolor = truecolor;
		}		
	}
	private Tile tile;
	public class Tile {
		private String truecolor;
		private String falsecolor;
		private String nvdi;
		private String evi;
		public String getEvi() {
			return evi;
		}
		public void setEvi(String evi) {
			this.evi = evi;
		}
		public String getNvdi() {
			return nvdi;
		}
		public void setNvdi(String nvdi) {
			this.nvdi = nvdi;
		}
		public String getFalsecolor() {
			return falsecolor;
		}
		public void setFalsecolor(String falsecolor) {
			this.falsecolor = falsecolor;
		}
		public String getTruecolor() {
			return truecolor;
		}
		public void setTruecolor(String truecolor) {
			this.truecolor = truecolor;
		}		
	}
	private Stats stats;
	public class Stats {
		private String ndvi;
		private String evi;
		public String getEvi() {
			return evi;
		}
		public void setEvi(String evi) {
			this.evi = evi;
		}
		public String getNdvi() {
			return ndvi;
		}
		public void setNdvi(String ndvi) {
			this.ndvi = ndvi;
		}
	}
	private Data data;
	public class Data {
		private String truecolor;
		private String falsecolor;
		private String ndvi;
		private String evi;
		public String getEvi() {
			return evi;
		}
		public void setEvi(String evi) {
			this.evi = evi;
		}
		public String getNdvi() {
			return ndvi;
		}
		public void setNdvi(String ndvi) {
			this.ndvi = ndvi;
		}
		public String getFalsecolor() {
			return falsecolor;
		}
		public void setFalsecolor(String falsecolor) {
			this.falsecolor = falsecolor;
		}
		public String getTruecolor() {
			return truecolor;
		}
		public void setTruecolor(String truecolor) {
			this.truecolor = truecolor;
		}
	}
	public long getDt() {
		return dt;
	}
	public void setDt(long dt) {
		this.dt = dt;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public float getDc() {
		return dc;
	}
	public void setDc(float dc) {
		this.dc = dc;
	}
	public float getCl() {
		return cl;
	}
	public void setCl(float cl) {
		this.cl = cl;
	}
	public Sun getSun() {
		return sun;
	}
	public void setSun(Sun sun) {
		this.sun = sun;
	}
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	public Stats getStats() {
		return stats;
	}
	public void setStats(Stats stats) {
		this.stats = stats;
	}
	public Tile getTile() {
		return tile;
	}
	public void setTile(Tile tile) {
		this.tile = tile;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
}
