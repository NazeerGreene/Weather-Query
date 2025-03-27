package com.learn.WeatherApp.service;

import com.learn.WeatherApp.models.WeatherResponse;

/**
 * The interface that any service layer fetchers should conform to.
 */
public interface WeatherService {
    WeatherResponse atLocation(String zip);
    WeatherResponse atLocationBetweenDates(String zip, String start, String end);
}
