package com.learn.WeatherApp.data;

import com.learn.WeatherApp.models.WeatherResponse;

/**
 * Interface for fetching weather data from a weather API.
 */
public interface WeatherFetcher {

    /**
     * Fetches the weather data from the configured API.
     *
     * @return A {@link WeatherResponse} object containing the weather data.
     */
    WeatherResponse fetch();
}
