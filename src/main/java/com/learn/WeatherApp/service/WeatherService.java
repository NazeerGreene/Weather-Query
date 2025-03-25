package com.learn.WeatherApp.service;

import com.learn.WeatherApp.models.WeatherResponse;

public interface WeatherService {
    WeatherResponse atLocation(String zip);
    WeatherResponse atLocationBetweenDates(String zip, String start, String end);
}
