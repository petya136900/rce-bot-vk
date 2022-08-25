package com.petya136900.rcebot.geoapi;

public class GeoJson {
	private String name="Polygon Sample";
	private Geo_Json geo_json= new Geo_Json();
	public class Geo_Json {
		private String type="Feature";
		private Properties properties=new Properties();
		public class Properties {
			
		}
		private Geometry geometry=new Geometry();
		public class Geometry {
			private String type="Polygon";
			private float[][][] coordinates;
			public float[][][] getCoordinates() {
				return coordinates;
			}
			public void setCoordinates(float[][][] coordinates) {
				this.coordinates = coordinates;
			}
			public String getType() {
				return type;
			}
			public void setType(String type) {
				this.type = type;
			}			
		}
		public Geometry getGeometry() {
			return geometry;
		}
		public void setGeometry(Geometry geometry) {
			this.geometry = geometry;
		}
		public Properties getProperties() {
			return properties;
		}
		public void setProperties(Properties properties) {
			this.properties = properties;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	}
	public GeoJson(float lat, float lon, float d) {
		this.getGeo_json().getGeometry().setCoordinates(
			new float[][][] {{
				{lon-d,lat+d},
				{lon-d,lat-d},
				{lon+d,lat-d},
				{lon+d,lat+d},
				{lon-d,lat+d}
			}}
		);
	}
	public GeoJson() {

	}
	public Geo_Json getGeo_json() {
		return geo_json;
	}
	public void setGeo_json(Geo_Json geo_json) {
		this.geo_json = geo_json;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
