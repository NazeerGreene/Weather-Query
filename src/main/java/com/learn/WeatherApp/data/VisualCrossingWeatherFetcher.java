package com.learn.WeatherApp.data;

import com.learn.WeatherApp.models.WeatherResponse;
import lombok.NonNull;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class VisualCrossingWeatherFetcher implements WeatherFetcher {
    private final String baseUrl;
    private final String apiKey;
    private String location;

    // additional api path parameters - date
    public static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private String startDate;
    private String endDate;

    // limit data returned
    public static final Set<String> queryOptions;
    private final List<String> includeList;

    static {
        queryOptions = Set.of(
                "days",     // daily data
                "hours",    // hourly data
                "minutes",  // minutely data
                "alerts",   // weather alerts
                "current",  // conditions at requested time
                "events",   // historical natural disasters
                "obs",      // historical observations from weather stations
                "remote",   // historical observations from remote source such as satellite or radar
                "fcst",     // forecast based on 16 day models
                "stats",    // historical statistical normals and daily statistical forecast
                "statsfcst" // full statistical forecast information for dates in the future
        );
    }

    public VisualCrossingWeatherFetcher(
            @NonNull String baseUrl,
            @NonNull String apiKey
    )
    {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.includeList = new ArrayList<>();
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

    public VisualCrossingWeatherFetcher include(@NonNull String option) {
        if (queryOptions.contains(option)) {
            includeList.add(option);
        } else {
            throw new IllegalArgumentException(option + " is not a query option");
        }

        return this;
    }

    public VisualCrossingWeatherFetcher include(@NonNull String[] options) {
        for(String option: options) {
            if (queryOptions.contains(option)) {
                includeList.add(option);
            } else {
                throw new IllegalArgumentException(option + " is not a query option");
            }
        }

        return this;
    }

    @Override
    public WeatherResponse fetch() {
        String requestingUrl = buildUrl();
        return new RestTemplate().getForObject(requestingUrl, WeatherResponse.class);
    }

    public String buildUrl() {
        if (location == null) {
            throw new IllegalStateException("location cannot be null");
        }

        StringBuilder urlBuilder = new StringBuilder(baseUrl);

        if (!baseUrl.endsWith("/")) {
            urlBuilder.append("/");
        }

        // add location - mandatory
        urlBuilder.append(location);

        // add dates - optional
        if (startDate != null) {
            urlBuilder.append("/").append(startDate);
        }

        if (endDate != null) {
            urlBuilder.append("/").append(endDate);
        }

        var includeQuery = includeList.isEmpty() ? Optional.empty() : Optional.of(String.join(",", includeList));

        // add the remaining query parameters
        return UriComponentsBuilder.fromUriString(urlBuilder.toString())
                .queryParamIfPresent("include", includeQuery)
                .queryParam("key", apiKey)
                .toUriString();
    }
}
