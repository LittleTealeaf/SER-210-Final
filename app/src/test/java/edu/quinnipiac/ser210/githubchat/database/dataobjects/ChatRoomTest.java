package edu.quinnipiac.ser210.githubchat.database.dataobjects;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;

/**
 * @author Thomas Kwashnak
 */
public class ChatRoomTest {

    private ChatRoom chatRoom;

    @Before
    public void setUp() throws Exception {
        chatRoom = new ChatRoom();
    }

    @Test
    public void getRepoName() {
        chatRoom.setRepoName("Testing");
        assertEquals("Testing",chatRoom.getRepoName());
    }

    @Test
    public void setRepoName() {
        assertNull(chatRoom.getRepoName());
        chatRoom.setRepoName("test");
        assertEquals("test",chatRoom.getRepoName());
    }

    @Test
    public void isFavorite() {
        assertFalse(chatRoom.isFavorite());
    }

    @Test
    public void setFavorite() {
        chatRoom.setFavorite(true);
        assertTrue(chatRoom.isFavorite());
        chatRoom.setFavorite(false);
        assertFalse(chatRoom.isFavorite());
    }
}