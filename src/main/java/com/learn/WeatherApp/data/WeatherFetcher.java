package com.learn.WeatherApp.data;

import com.learn.WeatherApp.models.VcWeatherResponse;

/**
 * Interface for fetching weather data from a weather API.
 */
public interface WeatherFetcher {

    /**
     * Fetches the weather data from the configured API.
     *
     * @return A {@link VcWeatherResponse} object containing the weather data.
     */
    VcWeatherResponse fetch();
}
