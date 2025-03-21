package com.learn.WeatherApp.models;

import java.util.List;

public record WeatherResponse(
        int queryCost,
        double latitude,
        double longitude,
        String resolvedAddress,
        String address,
        String timezone,
        double tzoffset,
        List<WeatherDay> days
) {}
