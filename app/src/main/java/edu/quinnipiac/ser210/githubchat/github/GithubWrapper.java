package edu.quinnipiac.ser210.githubchat.github;

import android.os.Handler;
import android.os.Looper;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import edu.quinnipiac.ser210.githubchat.database.DatabaseHolder;
import edu.quinnipiac.ser210.githubchat.database.DatabaseWrapper;
import edu.quinnipiac.ser210.githubchat.database.dataobjects.GithubCache;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubAttachable;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubIssue;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubPull;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubUser;
import edu.quinnipiac.ser210.githubchat.github.listeners.OnFetchGithubAttachbleList;
import edu.quinnipiac.ser210.githubchat.github.listeners.OnFetchGithubPulls;
import edu.quinnipiac.ser210.githubchat.github.listeners.OnFetchGithubRepo;
import edu.quinnipiac.ser210.githubchat.github.listeners.OnFetchGithubRepoList;
import edu.quinnipiac.ser210.githubchat.github.listeners.OnFetchGithubUser;
import edu.quinnipiac.ser210.githubchat.threads.ThreadManager;

/**
 * @author Thomas Kwashnak
 */
public class GithubWrapper implements GithubHolder, DatabaseHolder {

    public static final String AUTH_TOKEN = "Github Token";
    private static final int PER_PAGE = 100;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final DatabaseWrapper databaseWrapper;
    private String token;
    private GithubUser githubUser;

    public GithubWrapper(DatabaseHolder databaseHolder) {
        this.databaseWrapper = databaseHolder.getDatabaseWrapper();
    }

    public static GithubWrapper from(Object object) {
        if (object instanceof GithubHolder) {
            return ((GithubHolder) object).getGithubWrapper();
        } else {
            return null;
        }
    }

    public void setToken(String token) {
        this.token = token;
        startFetchGithubUser(null, (user, channel) -> githubUser = user);
    }

    public int startFetchGithubUser(String username, OnFetchGithubUser listener) {
        return ThreadManager.startThread(() -> fetchGithubUser(username), listener::onFetchGithubUser);
    }

    public GithubUser fetchGithubUser(String username) {
        try {
            return new GithubUser(new JSONObject(
                    Objects.requireNonNull(fetchURL(username == null ? "https://api.github.com/user" : "https://api.github.com/users/" + username))));
        } catch (Exception e) {
            return null;
        }
    }

    protected final String fetchURL(String url) {

        System.out.println("Fetching from url:" + url);

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

        databaseWrapper.updateGithubCache(new GithubCache(url, Instant.now().getEpochSecond(), json.toString()));

        return json.toString();
    }

    @Override
    public GithubWrapper getGithubWrapper() {
        return this;
    }

    @Override
    public DatabaseWrapper getDatabaseWrapper() {
        return databaseWrapper;
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

    public GithubUser getGithubUser() {
        return githubUser;
    }

    public int startFetchGithubUser(String username, OnFetchGithubUser listener, int channel) {
        return ThreadManager.startThread(() -> fetchGithubUser(username), listener::onFetchGithubUser, channel);
    }

    public int startFetchGithubRepo(String repoName, OnFetchGithubRepo listener) {
        return ThreadManager.startThread(() -> fetchGithubRepo(repoName), listener::onFetchGithubRepo);
    }

    public GithubRepo fetchGithubRepo(String fullName) {
        try {
            return new GithubRepo(new JSONObject(Objects.requireNonNull(fetchURL("https://api.github.com/repos/" + fullName))));
        } catch (Exception e) {
            return null;
        }
    }

    public int startFetchGithubRepo(String repoName, OnFetchGithubRepo listener, int channel) {
        return ThreadManager.startThread(() -> fetchGithubRepo(repoName), listener::onFetchGithubRepo, channel);
    }

    public int startFetchGithubRepoList(String username, OnFetchGithubRepoList listener) {
        return ThreadManager.startThread(() -> fetchGithubRepoList(username), listener::onFetchGithubRepoList);
    }

    public List<GithubRepo> fetchGithubRepoList(String username) {
        JSONArray array = fetchList(username == null ? "https://api.github.com/user/repos" : "https://api.github.com/users/" + username + "/repos");
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

    public int startFetchGithubRepoList(String username, OnFetchGithubRepoList listener, int channel) {
        return ThreadManager.startThread(() -> fetchGithubRepoList(username), listener::onFetchGithubRepoList, channel);
    }

    public int startFetchGithubPulls(String repoName, OnFetchGithubPulls listener) {
        return ThreadManager.startThread(() -> fetchGithubPulls(repoName), listener::onFetchGithubPulls);
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

    public int startFetchGithubPulls(String repoName, OnFetchGithubPulls listener, int channel) {
        return ThreadManager.startThread(() -> fetchGithubPulls(repoName), listener::onFetchGithubPulls, channel);
    }

    public int startFetchGithubAttachableList(String repoName, OnFetchGithubAttachbleList listener) {
        return ThreadManager.startThread(() -> fetchGithubAttachableList(repoName), listener::onFetchMessageAttachableList);
    }

    public int startFetchGithubAttachableList(String repoName, OnFetchGithubAttachbleList listener, int channel) {
        return ThreadManager.startThread(() -> fetchGithubAttachableList(repoName), listener::onFetchMessageAttachableList, channel);
    }
}
