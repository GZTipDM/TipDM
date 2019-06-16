package com.tipdm.framework.dmserver.interceptor.model;

/**
 * Created by TipDM on 2018/1/28.
 * E-mail:devp@tipdm.com
 */
public class URLLimitMapping {

    private String[] urls;
    private int rate=0;

    public URLLimitMapping() {
    }

    public URLLimitMapping(String[] urls, int rate) {
        this.urls = urls;
        this.rate = rate;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
