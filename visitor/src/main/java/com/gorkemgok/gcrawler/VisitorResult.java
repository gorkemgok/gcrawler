package com.gorkemgok.gcrawler;

import java.util.List;

public class VisitorResult {

    private final String source;

    private final List<byte[]> screenShots;

    public VisitorResult(String source, List<byte[]> screenShots) {
        this.source = source;
        this.screenShots = screenShots;
    }

    public String getSource() {
        return source;
    }

    public List<byte[]> getScreenShots() {
        return screenShots;
    }
}
