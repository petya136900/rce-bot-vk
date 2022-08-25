package com.petya136900.rcebot.geoapi;

public class WeatherData {
	private Coord coord;
	public class Coord {
		private float lon;
		private float lat;
		public float getLat() {
			return lat;
		}
		public void setLat(float lat) {
			this.lat = lat;
		}
		public float getLon() {
			return lon;
		}
		public void setLon(float lon) {
			this.lon = lon;
		}
	}
	private Weather[] weather;
	public class Weather {
		private Integer id;
		private String main;
		private String description;
		private String icon;
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getMain() {
			return main;
		}
		public void setMain(String main) {
			this.main = main;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
	}
	private String base;
	private Main main;
	public class Main {
		private float temp;
		private float feels_like;
		private float temp_min;
		private float temp_max;
		private Integer pressure;
		private Integer humidity;
		private Integer sea_level;
		private Integer grnd_level;
		public Integer getGrnd_level() {
			return grnd_level;
		}
		public void setGrnd_level(Integer grnd_level) {
			this.grnd_level = grnd_level;
		}
		public Integer getSea_level() {
			return sea_level;
		}
		public void setSea_level(Integer sea_level) {
			this.sea_level = sea_level;
		}
		public Integer getHumidity() {
			return humidity;
		}
		public void setHumidity(Integer humidity) {
			this.humidity = humidity;
		}
		public Integer getPressure() {
			return pressure;
		}
		public void setPressure(Integer pressure) {
			this.pressure = pressure;
		}
		public float getTemp_max() {
			return temp_max;
		}
		public void setTemp_max(float temp_max) {
			this.temp_max = temp_max;
		}
		public float getTemp_min() {
			return temp_min;
		}
		public void setTemp_min(float temp_min) {
			this.temp_min = temp_min;
		}
		public float getFeels_like() {
			return feels_like;
		}
		public void setFeels_like(float feels_like) {
			this.feels_like = feels_like;
		}
		public float getTemp() {
			return temp;
		}
		public void setTemp(float temp) {
			this.temp = temp;
		}
	}
	private Integer visibility;
	private Wind wind;
	public class Wind {
		private float speed;
		private Integer deg;
		private float gust;
		public float getGust() {
			return gust;
		}
		public void setGust(float gust) {
			this.gust = gust;
		}
		public Integer getDeg() {
			return deg;
		}
		public void setDeg(Integer deg) {
			this.deg = deg;
		}
		public float getSpeed() {
			return speed;
		}
		public void setSpeed(float speed) {
			this.speed = speed;
		}
	}
	private Clouds clouds;
	public class Clouds {
		private Integer all;

		public Integer getAll() {
			return all;
		}

		public void setAll(Integer all) {
			this.all = all;
		}
	}
	private Rain rain;
	public class Rain {
		private Float _1h;
		private Float _3h;
		public Float get_3h() {
			return _3h;
		}
		public void set_3h(Float _3h) {
			this._3h = _3h;
		}
		public Float get_1h() {
			return _1h;
		}
		public void set_1h(Float _1h) {
			this._1h = _1h;
		}
	}	
	private Snow snow;
	public class Snow {
		private Float _1h;
		private Float _3h;
		public Float get_3h() {
			return _3h;
		}
		public void set_3h(Float _3h) {
			this._3h = _3h;
		}
		public Float get_1h() {
			return _1h;
		}
		public void set_1h(Float _1h) {
			this._1h = _1h;
		}
	}
	private Long dt;
	private Sys sys;
	public class Sys {
		private String country;
		private Long sunrise;
		private Long sunset;
		public Long getSunset() {
			return sunset;
		}
		public void setSunset(Long sunset) {
			this.sunset = sunset;
		}
		public Long getSunrise() {
			return sunrise;
		}
		public void setSunrise(Long sunrise) {
			this.sunrise = sunrise;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
	}
	private Long timezone;
	private Integer id;
	private String name;
	private Integer cod;
	private String message;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getCod() {
		return cod;
	}
	public void setCod(Integer cod) {
		this.cod = cod;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getTimezone() {
		return timezone;
	}
	public void setTimezone(Long timezone) {
		this.timezone = timezone;
	}
	public Sys getSys() {
		return sys;
	}
	public void setSys(Sys sys) {
		this.sys = sys;
	}
	public Long getDt() {
		return dt;
	}
	public void setDt(Long dt) {
		this.dt = dt;
	}
	public Snow getSnow() {
		return snow;
	}
	public void setSnow(Snow snow) {
		this.snow = snow;
	}
	public Rain getRain() {
		return rain;
	}
	public void setRain(Rain rain) {
		this.rain = rain;
	}
	public Clouds getClouds() {
		return clouds;
	}
	public void setClouds(Clouds clouds) {
		this.clouds = clouds;
	}
	public Wind getWind() {
		return wind;
	}
	public void setWind(Wind wind) {
		this.wind = wind;
	}
	public Integer getVisibility() {
		return visibility;
	}
	public void setVisibility(Integer visibility) {
		this.visibility = visibility;
	}
	public Main getMain() {
		return main;
	}
	public void setMain(Main main) {
		this.main = main;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public Weather[] getWeather() {
		return weather;
	}
	public void setWeather(Weather[] weather) {
		this.weather = weather;
	}
	public Coord getCoord() {
		return coord;
	}
	public void setCoord(Coord coord) {
		this.coord = coord;
	}
}
