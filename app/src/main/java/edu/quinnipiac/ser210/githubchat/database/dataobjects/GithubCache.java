package edu.quinnipiac.ser210.githubchat.database.dataobjects;

/**
 * Represents a Github Cache, or a cached API fetch
 *
 * @author Thomas Kwashnak
 */
public class GithubCache {

    private String url;
    private String content;
    private long fetchTime;

    public GithubCache() {

    }

    public GithubCache(String url, long fetchTime, String content) {
        this.url = url;
        this.fetchTime = fetchTime;
        this.content = content;
    }

    /**
     * The URL that the content is fetched from
     * @return The URL key
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL key that the content is fetched from
     * @param url The url that the data is fetched from
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * The content cached from the url endpoint
     * @return Complete content as a string
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the cached content from the url
     * @param content Cached Content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Returns the birth-date of the cached content
     * @return The time, in epoch seconds, when the content was cached
     */
    public long getFetchTime() {
        return fetchTime;
    }

    public void setFetchTime(long fetchTime) {
        this.fetchTime = fetchTime;
    }
}
