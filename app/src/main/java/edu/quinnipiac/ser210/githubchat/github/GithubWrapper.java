package edu.quinnipiac.ser210.githubchat.github;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import edu.quinnipiac.ser210.githubchat.database.DatabaseHolder;
import edu.quinnipiac.ser210.githubchat.database.DatabaseWrapper;
import edu.quinnipiac.ser210.githubchat.database.dataobjects.GithubCache;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubAttachment;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubIssue;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubPull;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubUser;
import edu.quinnipiac.ser210.githubchat.threads.ThreadManager;

/**
 * <p>Wraps all functionality with fetching data from the Github REST API.</p>
 * <p>Similarly to {@link DatabaseWrapper}, this class contains two main types of methods: regular methods ({@link #fetchGithubUser(String)}) and start methods
 * ({@link #startFetchGithubUser(String, OnFetchGithubUser)})</p>
 * <ul>
 *  <li>
 *      {@link #fetchGithubUser(String)}will immediately retrieve the {@link GithubUser} from the database on whatever thread it's currently on. Once it's done, the method will
 *      return the fetched value
 *  </li>
 *  <li>
 *      {@link #startFetchGithubUser(String, OnFetchGithubUser)} instead starts {@link #fetchGithubUser(String)} on an asynchronous thread. It returns the "channel" that
 *      the listener will be notified on (See {@link ThreadManager}). Once {@link #fetchGithubUser(String)} completes on the asynchronous thread, it will then return to the
 *      main thread and send a notification to the provided listener.
 *  </li>
 * </ul>
 * <p>
 *     This class also stores the user's github token, which is used to access API requests (to take advantage of the higher number of requests per hour) if it's provided.
 * </p>
 * <p>
 *     In order to keep from sending multiple requests to an API endpoint, the GithubWrapper will store any data it's fetched into the {@link DatabaseWrapper} as
 *     {@link GithubCache GithubCaches}. Then, whenever the GithubWrapper needs to make a request, it first checks in with the DatabaseWrapper to see if a cached value of
 *     that request has been made. If it has been, and it's not too old, then instead of making a new request, it will just return the cached value. This significantly
 *     reduces the number of API calls made, especially if the user keeps opening and closing something such as a fragment, requesting new api calls every time.
 * </p>
 * @author Thomas Kwashnak
 */
public class GithubWrapper implements GithubHolder, DatabaseHolder {

    public static final String AUTH_TOKEN = "Github Token";
    private static final int PER_PAGE = 100;

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
            return new GithubUser(
                    new JSONObject(Objects.requireNonNull(fetchURL(username == null ? "https://api.github.com/user" : "https://api.github.com/users/" + username))));
        } catch (Exception e) {
            return null;
        }
    }

    protected final String fetchURL(String url) {
        return fetchURL(url, 10 * 60);
    }

    protected final String fetchURL(String url, int keepTime) {

        //This checks the cached value. This is done on the same thread because it needs to wait for the execution to complete.
        GithubCache urlCache = databaseWrapper.getGithubCache(url);
        if (urlCache != null && urlCache.getFetchTime() > Instant.now().getEpochSecond() - keepTime) {
            System.out.println("Fetching from Cache: " + url);
            return urlCache.getContent();
        }

        System.out.println("Fetching from url: " + url);

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
            }
        }
        return issues;
    }

    public List<GithubAttachment> fetchGithubAttachmentList(String repoName) {
        List<GithubAttachment> attachableList = new LinkedList<>();
        attachableList.addAll(fetchGithubPulls(repoName));
        attachableList.addAll(fetchGithubIssues(repoName));
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
            } catch (Exception ignored) {
            }
        }

        return repos;
    }

    /**
     * <p>Wraps fetching a JsonArray from a GithubAPI list.</p>
     * <p>This method makes api requests for each page of api until it retrieves less than the number of items per page it requested.</p>
     * @param baseURL The base url to request
     * @return The JSONArray from that endpoint
     */
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
        } catch (Exception ignored) {
        }

        return jsonArray;
    }

    public int startFetchGithubRepoList(String username, OnFetchGithubRepoList listener, int channel) {
        return ThreadManager.startThread(() -> fetchGithubRepoList(username), listener::onFetchGithubRepoList, channel);
    }

    public int startFetchGithubPulls(String repoName, OnFetchGithubPullList listener) {
        return ThreadManager.startThread(() -> fetchGithubPulls(repoName), listener::onFetchGithubPullList);
    }

    public List<GithubPull> fetchGithubPulls(String repoName) {
        JSONArray array = fetchList("https://api.github.com/repos/" + repoName + "/pulls");
        List<GithubPull> pulls = new LinkedList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                pulls.add(new GithubPull(array.getJSONObject(i)));
            } catch (Exception ignored) {
            }
        }

        return pulls;
    }

    public int startFetchGithubPulls(String repoName, OnFetchGithubPullList listener, int channel) {
        return ThreadManager.startThread(() -> fetchGithubPulls(repoName), listener::onFetchGithubPullList, channel);
    }

    public int startFetchGithubAttachmentList(String repoName, OnFetchGithubAttachmentList listener) {
        return ThreadManager.startThread(() -> fetchGithubAttachmentList(repoName), listener::onFetchGithubAttachmentList);
    }

    public int startFetchGithubAttachmentList(String repoName, OnFetchGithubAttachmentList listener, int channel) {
        return ThreadManager.startThread(() -> fetchGithubAttachmentList(repoName), listener::onFetchGithubAttachmentList, channel);
    }

    public int startFetchGithubPull(String repoName, int number, OnFetchGithubPull listener) {
        return ThreadManager.startThread(() -> fetchGithubPull(repoName, number), listener::onFetchGithubPull);
    }

    public GithubPull fetchGithubPull(String repoName, int number) {
        try {
            return new GithubPull(new JSONObject(Objects.requireNonNull(fetchURL("https://api.github.com/repos/" + repoName + "/pulls/" + number))));
        } catch (Exception e) {
            return null;
        }
    }

    public int startFetchGithubPull(String repoName, int number, OnFetchGithubPull listener, int channel) {
        return ThreadManager.startThread(() -> fetchGithubPull(repoName, number), listener::onFetchGithubPull, channel);
    }

    public int startFetchGithubIssue(String repoName, int number, OnFetchGithubIssue listener) {
        return startFetchGithubIssue(repoName, number, listener, ThreadManager.registerChannel());
    }

    public int startFetchGithubIssue(String repoName, int number, OnFetchGithubIssue listener, int channel) {
        return ThreadManager.startThread(() -> fetchGithubIssue(repoName, number), listener::onFetchGithubIssue, channel);
    }

    public GithubIssue fetchGithubIssue(String repoName, int number) {
        try {
            return new GithubIssue(new JSONObject(Objects.requireNonNull(fetchURL("https://api.github.com/repos/" + repoName + "/issues/" + number))));
        } catch (Exception e) {
            return null;
        }
    }

    public GithubAttachment fetchGithubAttachment(String repoName, int number) {
        GithubPull issue = fetchGithubPull(repoName, number);
        if (issue != null) {
            return issue;
        } else {
            return fetchGithubIssue(repoName, number);
        }
    }

    public int startFetchGithubLanguages(String repoName, OnFetchGithubLanguages listener, int channel) {
        return ThreadManager.startThread(() -> fetchGithubLanguages(repoName), listener::onFetchGithubLanguages, channel);
    }

    public Map<String, Integer> fetchGithubLanguages(String repoName) {

        Map<String, Integer> values = new HashMap<>();

        try {
            JSONObject jsonObject = new JSONObject(fetchURL("https://api.github.com/repos/" + repoName + "/languages"));
            for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                String key = it.next();
                values.put(key, jsonObject.getInt(key));
            }
        } catch (Exception ignored) {
            return null;
        }
        return values;
    }

    public int startFetchGithubLanguages(String repoName, OnFetchGithubLanguages listener) {
        return ThreadManager.startThread(() -> fetchGithubLanguages(repoName), listener::onFetchGithubLanguages);
    }

    public int startFetchGithubAttachment(String repoName, int number, OnFetchGithubAttachment listener) {
        return ThreadManager.startThread(() -> fetchGithubAttachment(repoName, number), listener::onFetchGithubAttachment);
    }

    public int startFetchGithubAttachment(String repoName, int number, OnFetchGithubAttachment listener, int channel) {
        return ThreadManager.startThread(() -> fetchGithubAttachment(repoName, number), listener::onFetchGithubAttachment, channel);
    }

    public interface OnFetchGithubAttachment {

        OnFetchGithubAttachment NONE = (a, b) -> {};

        void onFetchGithubAttachment(GithubAttachment githubAttachment, int channel);
    }

    public interface OnFetchGithubAttachmentList {

        OnFetchGithubAttachmentList NONE = (a, b) -> {};

        void onFetchGithubAttachmentList(List<GithubAttachment> attachments, int channel);
    }

    public interface OnFetchGithubIssue {

        OnFetchGithubIssue NONE = (a, b) -> {};

        void onFetchGithubIssue(GithubIssue issue, int channel);
    }

    public interface OnFetchGithubIssueList {

        OnFetchGithubIssueList NONE = (a, b) -> {};

        void onFetchGithubIssueList(List<GithubIssue> issues, int channel);
    }

    public interface OnFetchGithubPull {

        OnFetchGithubPull NONE = (a, b) -> {};

        void onFetchGithubPull(GithubPull pull, int channel);
    }

    public interface OnFetchGithubPullList {

        OnFetchGithubPullList NONE = (a, b) -> {};

        void onFetchGithubPullList(List<GithubPull> pulls, int channel);
    }

    public interface OnFetchGithubRepo {

        OnFetchGithubPull NONE = (a, b) -> {};

        void onFetchGithubRepo(GithubRepo repo, int channel);
    }

    public interface OnFetchGithubRepoList {

        OnFetchGithubRepoList NONE = (a, b) -> {};

        void onFetchGithubRepoList(List<GithubRepo> repos, int channel);
    }

    public interface OnFetchGithubUser {

        OnFetchGithubUser NONE = (a, b) -> {};

        void onFetchGithubUser(GithubUser user, int channel);
    }

    public interface OnFetchGithubLanguages {

        OnFetchGithubLanguages NONE = (a, b) -> {};

        void onFetchGithubLanguages(Map<String, Integer> languages, int channel);
    }
}
