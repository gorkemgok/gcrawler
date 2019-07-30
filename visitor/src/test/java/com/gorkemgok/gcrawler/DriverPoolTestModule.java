package com.gorkemgok.gcrawler;

import com.google.inject.AbstractModule;
import org.openqa.selenium.WebDriver;

import static org.mockito.Mockito.mock;

public class DriverPoolTestModule extends AbstractModule {

    @Override
    protected void configure() {
        WebDriver mockWebDriver = mock(WebDriver.class);
        bind(WebDriver.class).toInstance(mockWebDriver);
    }
}
