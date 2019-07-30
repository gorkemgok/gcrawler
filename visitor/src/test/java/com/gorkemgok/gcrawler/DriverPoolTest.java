package com.gorkemgok.gcrawler;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import static org.testng.Assert.*;

public class DriverPoolTest {

    Injector injector;

    @BeforeTest
    public void setup() {
        Module module = Modules.override(new VisitorModule(3))
                .with(new DriverPoolTestModule());
        injector = Guice.createInjector(module);
    }

    @Test
    public void testGet() throws InterruptedException {
        DriverPool driverPool = injector.getInstance(DriverPool.class);

        driverPool.get();
        driverPool.get();
        CrawlerDriver driver3 = driverPool.get();

        ForkJoinPool.commonPool().execute(() -> {
            try {
                Thread.sleep(1000);
                driver3.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        long s = System.currentTimeMillis();
        CrawlerDriver driver4 = driverPool.get();
        long f = System.currentTimeMillis();
        assertTrue((f-s) >= 1000);
    }

    @Test
    public void testShutdown() {
    }
}