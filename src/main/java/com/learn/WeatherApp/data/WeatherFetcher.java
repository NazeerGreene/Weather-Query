package com.learn.WeatherApp.data;

import com.learn.WeatherApp.models.WeatherResponse;

public interface WeatherFetcher {
    WeatherResponse fetch();
}
