package edu.quinnipiac.ser210.githubchat.database.dataobjects;

/**
 * Represents a repository stored in the Database
 * @author Thomas Kwashnak
 */
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
