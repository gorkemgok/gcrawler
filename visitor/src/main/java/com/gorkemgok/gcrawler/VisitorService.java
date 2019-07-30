package com.gorkemgok.gcrawler;

import com.google.inject.Inject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class VisitorService implements Runnable {

    private final VisitorTaskFactory taskFactory;

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    @Inject
    public VisitorService(VisitorTaskFactory taskFactory) {
        this.taskFactory = taskFactory;
    }

    @Override
    public void run() {
        try {
            VisitorTask visitorTask = taskFactory.create(new URL("https://www.google.com"));
            Future<VisitorResult> resultFuture = executor.submit(visitorTask);
            VisitorResult result = resultFuture.get();
            System.out.println(result.getSource());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
