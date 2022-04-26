package edu.quinnipiac.ser210.githubchat.database.dataobjects;

public class ChatRepository {
    private String fullName;
    private boolean favorite;

    public  String getFullName() {
        return fullName;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
