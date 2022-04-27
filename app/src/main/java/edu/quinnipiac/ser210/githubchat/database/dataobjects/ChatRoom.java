package edu.quinnipiac.ser210.githubchat.database.dataobjects;

public class ChatRoom {

    private String repoName;
    private boolean favorite;

    public ChatRoom() {}

    public ChatRoom(String repoName, boolean favorite) {
        this.repoName = repoName;
        this.favorite = favorite;
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
