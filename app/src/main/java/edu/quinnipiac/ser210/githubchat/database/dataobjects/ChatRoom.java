package edu.quinnipiac.ser210.githubchat.database.dataobjects;

public class ChatRoom {

    private int id = -1;
    private String repoName;
    private boolean favorite;

    public ChatRoom() {}

    public ChatRoom(int id, String repoName, boolean favorite) {
        this.id = id;
        this.repoName = repoName;
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
