package edu.quinnipiac.ser210.githubchat.database.dataobjects;

import android.content.ContentValues;

public class GithubCache {
    private String url;
    private String content;
    private long fetchTime;

    public GithubCache() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getFetchTime() {
        return fetchTime;
    }

    public void setFetchTime(long fetchTime) {
        this.fetchTime = fetchTime;
    }
}
