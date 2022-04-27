package edu.quinnipiac.ser210.githubchat.github;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import edu.quinnipiac.ser210.githubchat.database.DatabaseHolder;
import edu.quinnipiac.ser210.githubchat.database.DatabaseWrapper;
import edu.quinnipiac.ser210.githubchat.database.dataobjects.GithubCache;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubAttachable;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubIssue;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubPull;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubUser;
import edu.quinnipiac.ser210.githubchat.github.listeners.OnFetchAttachableList;
import edu.quinnipiac.ser210.githubchat.github.listeners.OnFetchGithubIssues;
import edu.quinnipiac.ser210.githubchat.github.listeners.OnFetchGithubPulls;
import edu.quinnipiac.ser210.githubchat.github.listeners.OnFetchGithubRepo;
import edu.quinnipiac.ser210.githubchat.github.listeners.OnFetchGithubRepoList;
import edu.quinnipiac.ser210.githubchat.github.listeners.OnFetchGithubUser;

public class GithubWrapper implements GithubHolder, DatabaseHolder {

    public static final int CHANNEL_DEFAULT = -1;
    public static final String AUTH_TOKEN = "Github Token";
    private static final int PER_PAGE = 100;
    private final DatabaseWrapper databaseWrapper;
    private String token;

    public GithubWrapper(DatabaseHolder databaseHolder) {
        this.databaseWrapper = databaseHolder.getDatabaseWrapper();
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public GithubWrapper getGithubWrapper() {
        return this;
    }

    @Override
    public DatabaseWrapper getDatabaseWrapper() {
        return databaseWrapper;
    }

    public void startFetchGithubUser(String username, OnFetchGithubUser listener) {
        startFetchGithubUser(username, listener, CHANNEL_DEFAULT);
    }

    public void startFetchGithubUser(String username, OnFetchGithubUser listener, int channel) {
        new Thread(() -> listener.onFetchGithubUser(fetchGithubUser(username), channel)).start();
    }

    public GithubUser fetchGithubUser(String username) {
        try {
            return new GithubUser(new JSONObject(Objects.requireNonNull(fetchURL("https://api.github.com/users/" + username))));
        } catch (Exception e) {
            return null;
        }
    }

    protected final String fetchURL(String url) {

        GithubCache urlCache = databaseWrapper.getGithubCache(url);
        if (urlCache != null && urlCache.getFetchTime() > Instant.now().getEpochSecond() - 24 * 60 * 60) {
            return urlCache.getContent();
        }

        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;
        StringBuilder json = new StringBuilder();

        try {
            urlConnection = (HttpsURLConnection) new URL(url).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.addRequestProperty("Accept", "application/vnd.github.v3+json");
            urlConnection.setUseCaches(true);

            if (token != null) {
                urlConnection.addRequestProperty("Authorization", "token " + token);
            }

            InputStream stream = urlConnection.getInputStream();

            if (stream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(stream));

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                json.append(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return json.toString();
        }
        return json.toString();
    }

    public void startFetchGithubRepo(String fullName, OnFetchGithubRepo listener) {
        startFetchGithubRepo(fullName, listener, CHANNEL_DEFAULT);
    }

    public void startFetchGithubRepo(String fullName, OnFetchGithubRepo listener, int channel) {
        new Thread(() -> listener.onFetchGithubRepo(fetchGithubRepo(fullName), channel)).start();
    }

    public GithubRepo fetchGithubRepo(String fullName) {
        try {
            return new GithubRepo(new JSONObject(Objects.requireNonNull(fetchURL("https://api.github.com/repos/" + fullName))));
        } catch (Exception e) {
            return null;
        }
    }

    public void startFetchGithubRepoList(String username, OnFetchGithubRepoList listener) {
        startFetchGithubRepoList(username, listener, CHANNEL_DEFAULT);
    }

    public void startFetchGithubRepoList(String username, OnFetchGithubRepoList listener, int channel) {
        new Thread(() -> listener.onFetchGithubRepoList(fetchGithubRepoList(username), channel));
    }

    public List<GithubRepo> fetchGithubRepoList(String username) {
        JSONArray array = fetchList("https://api.github.com/users/" + username + "/repos");
        List<GithubRepo> repos = new LinkedList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                repos.add(new GithubRepo(array.getJSONObject(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return repos;
    }

    protected final JSONArray fetchList(String baseURL) {
        JSONArray jsonArray = new JSONArray();
        JSONArray tmp = null;
        int page = 1;
        try {
            do {
                tmp = new JSONArray(fetchURL(baseURL + "?per_page=" + PER_PAGE + "&page=" + page));

                for (int i = 0; i < tmp.length(); i++) {
                    jsonArray.put(tmp.getJSONObject(i));
                }

                page++;
            } while (tmp.length() == PER_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    public void startFetchGithubPulls(String repoName, OnFetchGithubPulls listener) {
        startFetchGithubPulls(repoName, listener, CHANNEL_DEFAULT);
    }

    public void startFetchGithubPulls(String repoName, OnFetchGithubPulls listener, int channel) {
        new Thread(() -> listener.onFetchGithubPulls(fetchGithubPulls(repoName), channel)).start();
    }

    public List<GithubPull> fetchGithubPulls(String repoName) {
        JSONArray array = fetchList("https://api.github.com/repos/" + repoName + "/pulls");
        List<GithubPull> pulls = new LinkedList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                pulls.add(new GithubPull(array.getJSONObject(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return pulls;
    }

    public void startFetchGithubIssues(String repoName, OnFetchGithubIssues listener) {
        startFetchGithubIssues(repoName, listener, CHANNEL_DEFAULT);
    }

    public void startFetchGithubIssues(String repoName, OnFetchGithubIssues listener, int channel) {
        new Thread(() -> listener.onFetchGithubIssues(fetchGithubIssues(repoName), channel)).start();
    }

    public List<GithubIssue> fetchGithubIssues(String repoName) {
        JSONArray array = fetchList("https://api.github.com/repos/" + repoName + "/issues");
        List<GithubIssue> issues = new LinkedList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                issues.add(new GithubIssue(array.getJSONObject(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return issues;
    }

    public List<GithubAttachable> fetchGithubAttachableList(String repoName) {
        List<GithubAttachable> attachableList = new LinkedList<>();
        attachableList.addAll(fetchGithubIssues(repoName));
        attachableList.addAll(fetchGithubPulls(repoName));
        return attachableList;
    }

    public void startFetchGithubAttachableList(String repoName, OnFetchAttachableList listener, int channel) {
        new Thread(() -> listener.onFetchMessageAttachableList(fetchGithubAttachableList(repoName), channel)).start();
    }

    public void startFetchGithubAttachableList(String repoName, OnFetchAttachableList listener) {
        startFetchGithubAttachableList(repoName, listener, CHANNEL_DEFAULT);
    }
}
