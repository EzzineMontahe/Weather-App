package com.montahe.weatherapp;

import com.google.gson.annotations.SerializedName;

public class WeatherData {
    @SerializedName("name")
    public String cityName;

    @SerializedName("main")
    public Main main;

    @SerializedName("weather")
    public Weather[] weather;

    @SerializedName("wind")
    public Wind wind;

    public static class Main {
        @SerializedName("temp")
        public double temperature;

        @SerializedName("humidity")
        public int humidity;
    }

    public static class Weather {
        @SerializedName("description")
        public String description;

        @SerializedName("icon")
        public String icon;
    }

    public static class Wind {
        @SerializedName("speed")
        public double speed;
    }
}