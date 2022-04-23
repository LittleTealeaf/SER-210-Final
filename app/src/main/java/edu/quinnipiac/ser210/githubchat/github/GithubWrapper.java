package edu.quinnipiac.ser210.githubchat.github;

public class GithubWrapper {

    public static String TOKEN = "githubToken";

    private String githubToken;

    public GithubWrapper() {

    }

    public GithubWrapper(String githubToken) {
        this.githubToken = githubToken;
    }

    public void setGithubToken(String githubToken) {
        this.githubToken = githubToken;
    }

    public String getGithubToken() {
        return githubToken;
    }


    public interface Holder {
        GithubWrapper getGithubWrapper();
    }
}
