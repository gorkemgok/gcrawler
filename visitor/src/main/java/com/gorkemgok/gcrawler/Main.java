package com.gorkemgok.gcrawler;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Injector injector = Guice.createInjector(new VisitorModule(3));
        VisitorService visitorService = injector.getInstance(VisitorService.class);
        visitorService.run();
    }
}
