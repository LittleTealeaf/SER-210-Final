package edu.quinnipiac.ser210.githubchat.database;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    String repoName;

    @Before
    public void setUp() throws Exception {
        getApplicationContext().deleteDatabase("GithubChatDatabase");
        databaseWrapper = new DatabaseWrapper(getApplicationContext());
        repoName = "LittleTealeaf/SER-210";
    }

    @After
    public void tearDown() throws Exception {
        getApplicationContext().deleteDatabase("GithubChatDatabase");
    }

    @Test
    public void getChatRoom() {
        assertNull(databaseWrapper.getChatRoom(repoName));

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRepoName(repoName);
        chatRoom.setFavorite(true);
        databaseWrapper.updateChatRoom(chatRoom);

        ChatRoom chatRoom1 = databaseWrapper.getChatRoom(repoName);
        assertEquals(repoName,chatRoom1.getRepoName());
        assertTrue(chatRoom1.isFavorite());
    }

    @Test
    public void removeChatRoom() {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRepoName(repoName);
        chatRoom.setFavorite(true);
        databaseWrapper.updateChatRoom(chatRoom);
        databaseWrapper.removeChatRoom(chatRoom);
        assertNull(databaseWrapper.getChatRoom(repoName));
        databaseWrapper.updateChatRoom(chatRoom);
        databaseWrapper.removeChatRoom(repoName);
        assertNull(databaseWrapper.getChatRoom(repoName));
    }

    @Test
    public void updateChatRoom() {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRepoName(repoName);
        chatRoom.setFavorite(false);
        databaseWrapper.updateChatRoom(chatRoom);
        assertFalse(databaseWrapper.getChatRoom(repoName).isFavorite());
        chatRoom.setFavorite(true);
        databaseWrapper.updateChatRoom(chatRoom);
        assertTrue(databaseWrapper.getChatRoom(repoName).isFavorite());
    }

}