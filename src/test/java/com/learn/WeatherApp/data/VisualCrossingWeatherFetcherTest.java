package com.learn.WeatherApp.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VisualCrossingWeatherFetcherTest {
    private static final String BASE_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
    private static final String API_KEY = "test-api-key";
    private VisualCrossingWeatherFetcher fetcher;

    @BeforeEach
    void setUp() {
        fetcher = new VisualCrossingWeatherFetcher(BASE_URL, API_KEY);
    }

    @Test
    void shouldBuildUrlWithLocationOnly() {
        String expectedUrl = UriComponentsBuilder.fromUriString(BASE_URL + "/Houston")
                .queryParam("key", API_KEY)
                .toUriString();

        fetcher.onLocation("Houston");
        assertEquals(expectedUrl, fetcher.buildUrl());
    }

    @Test
    void shouldBuildUrlWithLatLon() {
        String expectedUrl = UriComponentsBuilder.fromUriString(BASE_URL + "/29.76,-95.36")
                .queryParam("key", API_KEY)
                .toUriString();

        fetcher.onLocation(29.76, -95.36);
        assertEquals(expectedUrl, fetcher.buildUrl());
    }

    @Test
    void shouldBuildUrlWithDateRange() {
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 5);

        String expectedUrl = UriComponentsBuilder.fromUriString(BASE_URL + "/Houston/2025-01-01/2025-01-05")
                .queryParam("key", API_KEY)
                .toUriString();

        fetcher.onLocation("Houston").betweenDates(start, end);
        assertEquals(expectedUrl, fetcher.buildUrl());
    }

    @Test
    void shouldBuildUrlWithSingleIncludeOption() {
        String expectedUrl = UriComponentsBuilder.fromUriString(BASE_URL + "/Houston")
                .queryParam("include", "fcst")
                .queryParam("key", API_KEY)
                .toUriString();

        fetcher.onLocation("Houston").include("fcst");
        assertEquals(expectedUrl, fetcher.buildUrl());
    }

    @Test
    void shouldBuildUrlWithMultipleIncludeOptions() {
        String expectedUrl = UriComponentsBuilder.fromUriString(BASE_URL + "/Houston")
                .queryParam("include", "fcst,stats")
                .queryParam("key", API_KEY)
                .toUriString();

        fetcher.onLocation("Houston").include(new String[]{"fcst", "stats"});
        assertEquals(expectedUrl, fetcher.buildUrl());
    }

    @Test
    void shouldThrowExceptionIfLocationNotSet() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, fetcher::buildUrl);
        assertEquals("location cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForInvalidIncludeOption() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> fetcher.include("invalid_option"));
        assertEquals("invalid_option is not a query option", exception.getMessage());
    }
}
