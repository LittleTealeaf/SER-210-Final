package edu.quinnipiac.ser210.githubchat.database.dataobjects;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Thomas Kwashnak
 */
public class GithubCacheTest {

    GithubCache githubCache;

    @Before
    public void setUp() throws Exception {
        githubCache = new GithubCache();
    }

    @Test
    public void getUrl() {
        assertNull(githubCache.getUrl());
        githubCache.setUrl("test");
        assertNotNull(githubCache.getUrl());
    }

    @Test
    public void setUrl() {
        githubCache.setUrl("https://api.github.com");
        assertEquals("https://api.github.com",githubCache.getUrl());
    }

    @Test
    public void getContent() {
        assertNull(githubCache.getContent());
        githubCache.setContent("CONTENT");
        assertNotNull(githubCache.getContent());
    }

    @Test
    public void setContent() {
        githubCache.setContent("test content");
        assertEquals("test content",githubCache.getContent());
    }

    @Test
    public void getFetchTime() {
        assertEquals(0,githubCache.getFetchTime());
        githubCache.setFetchTime(100);
        assertNotEquals(0,githubCache.getFetchTime());
    }

    @Test
    public void setFetchTime() {
        githubCache.setFetchTime(1000);
        assertEquals(1000,githubCache.getFetchTime());
    }
}
