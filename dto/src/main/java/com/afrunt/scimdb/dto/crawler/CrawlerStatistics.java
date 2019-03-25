package com.afrunt.scimdb.dto.crawler;

import java.io.Serializable;
import java.util.Map;

import static java.util.Map.entry;

/**
 * @author Andrii Frunt
 */
public class CrawlerStatistics implements Serializable {
    private boolean available = true;
    private boolean running;

    public boolean isAvailable() {
        return available;
    }

    public CrawlerStatistics setAvailable(boolean available) {
        this.available = available;
        return this;
    }

    public boolean isRunning() {
        return running;
    }

    public CrawlerStatistics setRunning(boolean running) {
        this.running = running;
        return this;
    }

    public Map<String, Object> asMap() {
        return Map.ofEntries(
                entry("available", available),
                entry("running", running)
        );
    }
}
