package edu.quinnipiac.ser210.githubchat.database;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.*;

import android.app.Instrumentation;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import edu.quinnipiac.ser210.githubchat.database.dataobjects.ChatRoom;
import edu.quinnipiac.ser210.githubchat.database.dataobjects.GithubCache;

@RunWith(AndroidJUnit4.class)
public class DatabaseWrapperTest {

    DatabaseWrapper databaseWrapper;

    @Before
    public void setUp() throws Exception {
        getApplicationContext().deleteDatabase("GithubChatDatabase");
        databaseWrapper = new DatabaseWrapper(getApplicationContext());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getChatRoom() {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRepoName("LittleTealeaf/SER-210-Final");
        databaseWrapper.setChatRoom(chatRoom);
        ChatRoom fetched = databaseWrapper.getChatRoom("LittleTealeaf/SER-210-Final");
        assertEquals(chatRoom.getRepoName(),fetched.getRepoName());
    }

    @Test
    public void getChatRooms() {
        String[] strings = new String[] {"a","b","c","d","e"};
        for(String name : strings) {
            databaseWrapper.setChatRoom(new ChatRoom(name,false));
        }
        List<ChatRoom> chatRoomList = databaseWrapper.getChatRooms();
        assertEquals(strings.length,chatRoomList.size());
    }

    @Test
    public void getGithubCache() {
        assertNull(databaseWrapper.getGithubCache("test"));
        GithubCache githubCache = new GithubCache();
        githubCache.setUrl("test");
        databaseWrapper.setGithubCache(githubCache);
        assertNotNull(databaseWrapper.getGithubCache("test"));
    }
}