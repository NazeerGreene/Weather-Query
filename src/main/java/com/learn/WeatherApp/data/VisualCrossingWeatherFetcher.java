package com.learn.WeatherApp.data;

import com.learn.WeatherApp.models.VcWeatherResponse;
import lombok.NonNull;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * VisualCrossingWeatherFetcher is responsible for constructing and executing API requests
 * to fetch weather data from the Visual Crossing Weather API.
 *
 * This class allows users to specify location, date range, and query options before
 * making an API call. It builds the request URL dynamically based on provided parameters.
 */
public class VisualCrossingWeatherFetcher implements WeatherFetcher {
    private final String baseUrl = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
    private final String apiKey;
    private String location;

    /**
     * Date format used for API request parameters.
     */
    public static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private String startDate;
    private String endDate;

    /**
     * Set of valid query options that can be included in the API request.
     */
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
                "remote",   // historical observations from remote sources such as satellite or radar
                "fcst",     // forecast based on 16-day models
                "stats",    // historical statistical normals and daily statistical forecast
                "statsfcst" // full statistical forecast information for future dates
        );
    }

    /**
     * Constructs a new VisualCrossingWeatherFetcher.
     *
     * @param apiKey the API key for authentication
     */
    public VisualCrossingWeatherFetcher(@NonNull String apiKey) {
        this.apiKey = apiKey;
        this.includeList = new ArrayList<>();
    }

    /**
     * Sets the location for the weather data request.
     *
     * @param newLocation the location in string format
     * @return the current instance of {@code VisualCrossingWeatherFetcher}
     */
    public VisualCrossingWeatherFetcher onLocation(@NonNull String newLocation) {
        if (!newLocation.isEmpty()) {
            location = newLocation;
        }
        return this;
    }

    /**
     * Sets the location for the weather data request using latitude and longitude.
     *
     * @param lon longitude value
     * @param lat latitude value
     * @return the current instance of {@code VisualCrossingWeatherFetcher}
     */
    public VisualCrossingWeatherFetcher onLocation(double lon, double lat) {
        location = String.format("%s,%s", lon, lat);
        return this;
    }

    /**
     * Sets the date range for the weather data request.
     *
     * @param start the start date
     * @param end the end date (optional)
     * @return the current instance of {@code VisualCrossingWeatherFetcher}
     */
    public VisualCrossingWeatherFetcher betweenDates(@NonNull LocalDate start, LocalDate end) {
        startDate = start.format(dateTimeFormat);
        if (end != null) {
            endDate = end.format(dateTimeFormat);
        }
        return this;
    }

    /**
     * Includes additional query options in the API request.
     *
     * @param option a valid query option
     * @return the current instance of {@code VisualCrossingWeatherFetcher}
     * @throws IllegalArgumentException if the option is not valid
     */
    public VisualCrossingWeatherFetcher include(@NonNull String option) {
        if (queryOptions.contains(option)) {
            includeList.add(option);
        } else {
            throw new IllegalArgumentException(option + " is not a query option");
        }
        return this;
    }

    /**
     * Includes multiple query options in the API request.
     *
     * @param options an array of valid query options
     * @return the current instance of {@code VisualCrossingWeatherFetcher}
     * @throws IllegalArgumentException if any option is not valid
     */
    public VisualCrossingWeatherFetcher include(@NonNull String[] options) {
        for (String option : options) {
            if (queryOptions.contains(option)) {
                includeList.add(option);
            } else {
                throw new IllegalArgumentException(option + " is not a query option");
            }
        }
        return this;
    }

    /**
     * Fetches weather data using the constructed API request URL.
     *
     * @return a {@code WeatherResponse} object containing weather data
     */
    @Override
    public VcWeatherResponse fetch() {
        String requestingUrl = buildUrl();
        return new RestTemplate().getForObject(requestingUrl, VcWeatherResponse.class);
    }

    /**
     * Builds the API request URL based on provided parameters.
     *
     * @return the constructed URL as a string
     * @throws IllegalStateException if location is not set
     */
    public String buildUrl() {
        if (location == null) {
            throw new IllegalStateException("location cannot be null");
        }

        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        if (!baseUrl.endsWith("/")) {
            urlBuilder.append("/");
        }

        // Add location - mandatory
        urlBuilder.append(location);

        // Add dates - optional
        if (startDate != null) {
            urlBuilder.append("/").append(startDate);
        }
        if (endDate != null) {
            urlBuilder.append("/").append(endDate);
        }

        var includeQuery = includeList.isEmpty() ? Optional.empty() : Optional.of(String.join(",", includeList));

        // Add the remaining query parameters
        return UriComponentsBuilder.fromUriString(urlBuilder.toString())
                .queryParamIfPresent("include", includeQuery)
                .queryParam("key", apiKey)
                .toUriString();
    }
}
