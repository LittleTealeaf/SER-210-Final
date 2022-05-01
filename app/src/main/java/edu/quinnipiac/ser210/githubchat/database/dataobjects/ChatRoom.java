package edu.quinnipiac.ser210.githubchat.database.dataobjects;

/**
 * Represents a chat room
 *
 * @author Thomas Kwashnak
 */
public class ChatRoom {

    private String repoName;
    private boolean favorite;

    public ChatRoom() {}

    public ChatRoom(String repoName, boolean favorite) {
        this.repoName = repoName;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
