package com.gorkemgok.gcrawler;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class VisitorTask implements Callable<VisitorResult>{

    private final DriverPool driverPool;

    private final URL url;

    @Inject
    public VisitorTask(DriverPool driverPool, @Assisted URL url) {
        this.driverPool = driverPool;
        this.url = url;
    }

    public VisitorResult call() throws Exception {
        try (CrawlerDriver driver = driverPool.get()){
            driver.get(url.toString());
            byte[] ss = driver.cast(TakesScreenshot.class).getScreenshotAs(OutputType.BYTES);
            VisitorResult result = new VisitorResult(driver.getPageSource(), Arrays.asList(ss));
            return result;
        }
    }

}
