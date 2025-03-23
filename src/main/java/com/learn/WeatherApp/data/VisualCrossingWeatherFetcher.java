package com.learn.WeatherApp.data;

import com.learn.WeatherApp.models.WeatherResponse;
import lombok.NonNull;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VisualCrossingWeatherFetcher implements WeatherFetcher {
    private final String baseUrl;
    private final String apiKey;
    private String location;

    // additional api parameters
    public static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private String startDate;
    private String endDate;

    public VisualCrossingWeatherFetcher(
            @NonNull String baseUrl,
            @NonNull String apiKey
    )
    {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + '/';
        this.apiKey = apiKey;
    }

    public VisualCrossingWeatherFetcher onLocation(@NonNull String newLocation) {
        if (!newLocation.isEmpty()) {
            location = newLocation;
        }
        return this;
    }

    public VisualCrossingWeatherFetcher onLocation(double lon, double lat) {
        location = String.format("%s,%s", lon, lat);
        return this;
    }

    public VisualCrossingWeatherFetcher betweenDates(@NonNull LocalDate start, LocalDate end) {
        startDate = start.format(dateTimeFormat);
        if (end != null) {
            endDate = end.format(dateTimeFormat);
        }

        return this;
    }

    @Override
    public WeatherResponse fetch() {
        String requestingUrl = buildUrl();
        return new RestTemplate().getForObject(requestingUrl, WeatherResponse.class);
    }

    private String buildUrl() {
        String url = baseUrl;

        if (location == null) {
            throw new IllegalArgumentException("location cannot be null");
        }

        // add location - mandatory
        url += location;

        // add dates - optional
        if (startDate != null) {
            url = url + "/" + startDate;
        }

        if (endDate != null) {
            url = url + "/" + endDate;
        }

        // add the remaining query parameters
        return UriComponentsBuilder.fromUriString(url)
                .queryParam("key", apiKey)
                .toUriString();
    }
}
