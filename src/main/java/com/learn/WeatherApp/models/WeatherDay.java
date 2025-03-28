package com.learn.WeatherApp.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public record WeatherDay(
        String datetime,
        long datetimeEpoch,
        double tempmax,
        double tempmin,
        double temp,
        double feelslikemax,
        double feelslikemin,
        double feelslike,
        double dew,
        double humidity,
        double precip,
        double precipprob,
        double precipcover,
        List<String> preciptype,
        double snow,
        double snowdepth,
        double windgust,
        double windspeed,
        double winddir,
        double pressure,
        double cloudcover,
        double visibility,
        double solarradiation,
        double solarenergy,
        int uvindex,
        int severerisk,
        String sunrise,
        long sunriseEpoch,
        String sunset,
        long sunsetEpoch,
        double moonphase,
        String conditions,
        String description,
        String icon,
        List<String> stations,
        String source
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
