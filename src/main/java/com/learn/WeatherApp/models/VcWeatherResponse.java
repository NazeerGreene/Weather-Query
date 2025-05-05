package com.learn.WeatherApp.models;

import org.springframework.data.redis.core.RedisHash;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@RedisHash(timeToLive = 3600_000) // one hour
public record VcWeatherResponse(
        int queryCost,
        double latitude,
        double longitude,
        String resolvedAddress,
        String address,
        String timezone,
        double tzoffset,
        List<VcWeatherDay> days
) implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
}
