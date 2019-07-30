package com.gorkemgok.gcrawler;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DriverPool {

    public static final String POOL_SIZE_NAME = "driverPoolSize";

    private class CrawlerDriverImpl implements CrawlerDriver {

        private final WebDriver webDriver;

        @Inject
        public CrawlerDriverImpl(WebDriver webDriver) {
            this.webDriver = webDriver;
        }

        @Override
        public WebDriver unwrap() {
            return this.webDriver;
        }

        @Override
        public <T> T cast(Class<T> clazz) {
            return (T) webDriver;
        }

        @Override
        public void close() {
            returnToPool(this);
        }

        @Override
        public void get(String s) {
            webDriver.get(s);
        }

        @Override
        public String getCurrentUrl() {
            return webDriver.getCurrentUrl();
        }

        @Override
        public String getTitle() {
            return webDriver.getTitle();
        }

        @Override
        public List<WebElement> findElements(By by) {
            return webDriver.findElements(by);
        }

        @Override
        public WebElement findElement(By by) {
            return webDriver.findElement(by);
        }

        @Override
        public String getPageSource() {
            return webDriver.getPageSource();
        }

        @Override
        public void quit() {
            webDriver.quit();
        }

        @Override
        public Set<String> getWindowHandles() {
            return webDriver.getWindowHandles();
        }

        @Override
        public String getWindowHandle() {
            return webDriver.getWindowHandle();
        }

        @Override
        public TargetLocator switchTo() {
            return webDriver.switchTo();
        }

        @Override
        public Navigation navigate() {
            return webDriver.navigate();
        }

        @Override
        public Options manage() {
            return webDriver.manage();
        }
    }

    private final int poolSize;

    private final Set<CrawlerDriver> idleDrivers;

    private final Provider<WebDriver> webDriverProvider;

    @Inject
    public DriverPool(@Named(POOL_SIZE_NAME) int poolSize, Provider<WebDriver> webDriverProvider) {
        this.poolSize = poolSize;
        idleDrivers = new HashSet<>(poolSize);
        this.webDriverProvider = webDriverProvider;
        for (int i = 0; i < poolSize; i++) {
            idleDrivers.add(new CrawlerDriverImpl(webDriverProvider.get()));
        }
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    //TODO: refactor synchronized methods
    public synchronized CrawlerDriver get() throws InterruptedException {
        CrawlerDriver crawlerDriver = null;
        while (!Thread.interrupted()) {
            Iterator<CrawlerDriver> it = idleDrivers.iterator();
            if (it.hasNext()) {
                crawlerDriver = it.next();
                it.remove();
                break;
            } else {
                wait();
            }
        }
        return crawlerDriver;
    }

    public void shutdown() {
        idleDrivers.forEach(CrawlerDriver::quit);
    }

    private synchronized void returnToPool(CrawlerDriver driver) {
        idleDrivers.add(driver);
        notifyAll();
    }
}
