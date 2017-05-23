package com.ixxus.hastalavista.datastore.rest;

/**
 *  Bean used to send data.
 *
 *  Created by alexneatu on 22/05/2017.
 */
public class SearchResult {

    private String uuid;
    private String url;
    private int occurrences;
    private int distance;

    public SearchResult(String uuid, String url, int occurrences, int distance) {
        this.uuid = uuid;
        this.url = url;
        this.occurrences = occurrences;
        this.distance = distance;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(int occurrences) {
        this.occurrences = occurrences;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
