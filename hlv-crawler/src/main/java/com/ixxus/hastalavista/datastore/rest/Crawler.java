package com.ixxus.hastalavista.datastore.rest;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

/**
 * Created by alexneatu on 22/05/2017.
 */
public class Crawler {

    @NotBlank(message = "Web site value cannot be empty")
    private String website;

    @Min(10)
    private int noLinks;

   public Crawler() {}

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getNoLinks() {
        return noLinks;
    }

    public void setNoLinks(int noLinks) {
        this.noLinks = noLinks;
    }
}
