package com.gorkemgok.gcrawler;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.protobuf.ByteString;
import com.gorkemgok.gcrawler.grpc.ScreenShot;
import com.gorkemgok.gcrawler.grpc.VisitorResult;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.net.URL;
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
            Dimension resolution = driver.manage().window().getSize();
            VisitorResult result = VisitorResult.newBuilder()
                    .setPageSource(driver.getPageSource())
                    .addScreenshots(
                            ScreenShot.newBuilder()
                                    .setWidth(resolution.getWidth())
                                    .setHeight(resolution.getHeight())
                                    .setImg(ByteString.copyFrom(ss))
                                    .build()
                    )
                    .build();
            return result;
        }
    }

}
