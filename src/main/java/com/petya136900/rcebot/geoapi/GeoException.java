package com.petya136900.rcebot.geoapi;

public class GeoException extends Exception {
    public GeoException(String name) {
        this.name=name;
    }

    @Override
    public String getMessage() {
        return message;
    }
    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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

    private String message;
    private Integer cod;
    public GeoException(Integer cod, String message) {
        this.cod=cod;
        this.message=message;
    }
}
