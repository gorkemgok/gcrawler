package com.gorkemgok.gcrawler;

import java.net.URL;

public interface VisitorTaskFactory {

    VisitorTask create(URL url);

}
