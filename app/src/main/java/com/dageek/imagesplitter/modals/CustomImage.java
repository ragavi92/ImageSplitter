package com.dageek.imagesplitter.modals;

import java.io.Serializable;

public class CustomImage implements Serializable {
    private String contentUrl;
    private String url;

    public CustomImage(String contentUrl, String url) {
        this.contentUrl = contentUrl;
        this.url = url;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
