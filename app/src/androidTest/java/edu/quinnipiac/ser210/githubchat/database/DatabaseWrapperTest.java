package edu.quinnipiac.ser210.githubchat.database;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.*;

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
        getApplicationContext().deleteDatabase("GithubChatDatabase");

    }

    @Test
    public void getChatRoom() {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRepoName("LittleTealeaf/SER-210-Final");
        databaseWrapper.updateChatRoom(chatRoom);
        ChatRoom fetched = databaseWrapper.getChatRoom("LittleTealeaf/SER-210-Final");
        assertEquals(chatRoom.getRepoName(),fetched.getRepoName());
    }

    @Test
    public void getChatRooms() {
        String[] strings = new String[] {"a","b","c","d","e"};
        for(String name : strings) {
            databaseWrapper.updateChatRoom(new ChatRoom(name, false));
        }
        List<ChatRoom> chatRoomList = databaseWrapper.getChatRoomList();
        assertEquals(strings.length,chatRoomList.size());
    }

    @Test
    public void getGithubCache() {
        assertNull(databaseWrapper.getGithubCache("test"));
        GithubCache githubCache = new GithubCache();
        githubCache.setUrl("test");
        databaseWrapper.updateGithubCache(githubCache);
        assertNotNull(databaseWrapper.getGithubCache("test"));
    }
}