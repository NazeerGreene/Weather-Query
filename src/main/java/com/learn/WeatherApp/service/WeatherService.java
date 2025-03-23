package com.learn.WeatherApp.service;

import com.learn.WeatherApp.models.WeatherResponse;

public interface WeatherService {
    WeatherResponse atZipCode(String zip);
    WeatherResponse atZipCodeBetweenDates(String zip, String start, String end);
}
