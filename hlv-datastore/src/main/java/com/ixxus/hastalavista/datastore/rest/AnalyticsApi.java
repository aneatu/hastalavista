package com.ixxus.hastalavista.datastore.rest;

import java.util.Map;

/**
 * Created by alexneatu on 22/05/2017.
 */
public class AnalyticsApi extends ResponseApi {

    private String words;
    private String terms;
    private String urls;

    public AnalyticsApi(String status, String message) {
        super(status, message);
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }
}
