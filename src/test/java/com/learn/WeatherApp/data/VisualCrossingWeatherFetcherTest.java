package com.learn.WeatherApp.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@PropertySource("classpath:application.properties")
class VisualCrossingWeatherFetcherTest {
    @Value("${weather.api.base-url}")
    private String baseUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldAddLocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // set up
        var fetcher = new VisualCrossingWeatherFetcher(baseUrl, apiKey);
        fetcher.onLocation("77471");

        Class<?> clazz = fetcher.getClass();
        Method buildUrl = clazz.getDeclaredMethod("buildUrl");
        buildUrl.setAccessible(true);

        // retrieve
        String url = (String) buildUrl.invoke(fetcher);

        // test
        assertEquals("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/77471",
        url);
    }

    @Test
    void shouldThrowErrorWithoutLocation() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            var fetcher = new VisualCrossingWeatherFetcher(baseUrl, apiKey);
            fetcher.fetchWeather();
        });

        assertEquals("location cannot be null", ex.getLocalizedMessage());
    }

    @Test
    void shouldAddBothDates() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // set up
        var fetcher = new VisualCrossingWeatherFetcher(baseUrl, apiKey);
        fetcher.onLocation("77471");
        fetcher.betweenDates(LocalDate.of(2025, 01, 01), LocalDate.of(2025, 01, 02));

        Class<?> clazz = fetcher.getClass();
        Method buildUrl = clazz.getDeclaredMethod("buildUrl");
        buildUrl.setAccessible(true);

        // retrieve
        String url = (String) buildUrl.invoke(fetcher);

        // test
        assertEquals("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/77471/2025-01-01/2025-01-02",
                url);
    }

    @Test
    void shouldAddStartDate() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // set up
        var fetcher = new VisualCrossingWeatherFetcher(baseUrl, apiKey);
        fetcher.onLocation("77471");
        fetcher.betweenDates(LocalDate.of(2025, 01, 01), null);

        Class<?> clazz = fetcher.getClass();
        Method buildUrl = clazz.getDeclaredMethod("buildUrl");
        buildUrl.setAccessible(true);

        // retrieve
        String url = (String) buildUrl.invoke(fetcher);

        // test
        assertEquals("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/77471/2025-01-01",
                url);
    }
}