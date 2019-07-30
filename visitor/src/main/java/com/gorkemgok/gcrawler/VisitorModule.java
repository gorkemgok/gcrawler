package com.gorkemgok.gcrawler;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class VisitorModule extends AbstractModule {

    private final int driverPoolSize;

    public VisitorModule(int driverPoolSize) {
        this.driverPoolSize = driverPoolSize;
    }

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(VisitorTask.class, VisitorTask.class)
                .build(VisitorTaskFactory.class));
        bind(DriverPool.class).in(Scopes.SINGLETON);
        bindConstant().annotatedWith(Names.named(DriverPool.POOL_SIZE_NAME)).to(driverPoolSize);
    }

    @Provides
    WebDriver provideWebDriver() {
        ChromeOptions options = new ChromeOptions()
                .setHeadless(true);
        WebDriver webDriver = new ChromeDriver(options);
        webDriver.manage().window().setSize(new Dimension(1920, 1080));
        return webDriver;
    }
}
