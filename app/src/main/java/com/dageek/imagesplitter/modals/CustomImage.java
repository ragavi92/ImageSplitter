package com.dageek.imagesplitter.modals;

import java.io.Serializable;

public class CustomImage implements Serializable {
    private String contentUrl;
    private String url;
    private Boolean isSelected;

    public CustomImage(String contentUrl, String url) {
        this.contentUrl = contentUrl;
        this.url = url;
        this.isSelected = false;
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

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

}
