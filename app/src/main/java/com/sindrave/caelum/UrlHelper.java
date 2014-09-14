package com.sindrave.caelum;

/**
 * Created by Yanik on 15/08/2014.
 */
public class UrlHelper {

    private String url;

    public UrlHelper(String url) {
        this.url = url;
    }

    public void addGetParameter(String parameterName, String parameterValue) {
        int queryStartIndex = url.indexOf('?');
        char queryCharToAdd = queryStartIndex == -1 ? '?' : '&';
        url = String.format("%s%c%s=%s", url, queryCharToAdd, parameterName, parameterValue);
    }

    public String getUrl() {
        return url;
    }
}
