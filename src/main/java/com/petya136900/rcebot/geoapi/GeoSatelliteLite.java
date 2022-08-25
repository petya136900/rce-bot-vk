package com.petya136900.rcebot.geoapi;

public class GeoSatelliteLite {
	private long dt; // UnixTime
	private String type; // Satellite name
	private float dc; // Approximate %
	private float cl; // clouds %
	private Image image;
	public class Image {
		private String truecolor;
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
	public void updateUrls() {
		this.getImage().setTruecolor("<img src=\""+this.getImage().getTruecolor()+"\"/>");
		this.getTile().setTruecolor("<a href=\""+this.getTile().getTruecolor()+"\">Тык</a>");
	}
}
