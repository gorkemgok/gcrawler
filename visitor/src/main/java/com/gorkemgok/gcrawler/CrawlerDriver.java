package com.gorkemgok.gcrawler;

import org.openqa.selenium.WebDriver;

public interface CrawlerDriver extends WebDriver, AutoCloseable {

    WebDriver unwrap();

    <T> T cast(Class<T> clazz);
}
