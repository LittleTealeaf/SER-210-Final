package edu.quinnipiac.ser210.githubchat.database.dataobjects;

/**
 * Represents a Chat Room stored in the Database. Contains the repository name (as listed on Github), and whether the user has favorited this repository
 *
 * @author Thomas Kwashnak
 */
public class ChatRoom {

    private String repoName;
    private boolean favorite;

    /**
     * Creates an empty ChatRoom reference
     */
    public ChatRoom() {}

    /**
     * Creates A Chat Room reference with a repository name and a favorite value
     *
     * @param repoName The Repository name as it appears on Github
     * @param favorite True if the user has marked this room as a favorite, false otherwise
     */
    public ChatRoom(String repoName, boolean favorite) {
        this.repoName = repoName;
        this.favorite = favorite;
    }

    /**
     * Gets the repository name as indicated on Github.
     *
     * @return Repository Name, in the format "username/name". Ex: "LittleTealeaf/SER-210-Final"
     */
    public String getRepoName() {
        return repoName;
    }

    /**
     * Sets the repository name as indicated on Github
     *
     * @param repoName The repository name as on Github. Must be in the format "owner/name", such as "LittleTealeaf/SER-210-Final"
     */
    public void setRepoName(String repoName) {
        assert repoName.contains("/");
        this.repoName = repoName;
    }

    /**
     * Indicates whether or not the user has indicated that this GithubRepo is a favorite
     *
     * @return true if the user indicates this as a favorite chat room, false otherwise.
     */
    public boolean isFavorite() {
        return favorite;
    }

    /**
     * Updates the favorite value for this github repository
     *
     * @param favorite true if the this chat room is a favorite, false otherwise
     */
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
