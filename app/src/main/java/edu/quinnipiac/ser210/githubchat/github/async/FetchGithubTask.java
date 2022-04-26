package edu.quinnipiac.ser210.githubchat.github.async;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import edu.quinnipiac.ser210.githubchat.database.DatabaseHelper;
import edu.quinnipiac.ser210.githubchat.database.dataobjects.GithubCache;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;

/**
 * @author Thomas Kwashnak
 * An abstract AsyncTask that handles fetching data from the API endpoint, storing it into the database, and returning that value
 */
public abstract class FetchGithubTask extends AsyncTask<String, Void, String> {

    protected final GithubWrapper githubWrapper;
    protected final DatabaseHelper databaseHelper;

    public FetchGithubTask(GithubWrapper githubWrapper, DatabaseHelper databaseHelper) {
        this.githubWrapper = githubWrapper;
        this.databaseHelper = databaseHelper;
    }

    private final String compileHeaders(String url) {
        Map<String, String> headers = buildHeaders();
        if (headers.size() > 0) {
            StringBuilder builder = new StringBuilder(url).append("?");

            for (String key : headers.keySet()) {
                builder.append("&").append(key).append("=").append(headers.get(key));
            }

            return builder.toString().replace("?&", "?");
        } else {
            return url;
        }
    }

    public Map<String, String> buildHeaders() {
        return new HashMap<>();
    }

    @Override
    protected String doInBackground(String... strings) {

        String address = compileHeaders(getURL(strings));

        GithubCache cache = databaseHelper.getGithubCache(address);
        if (cache != null && cache.getFetchTime() > Instant.now().getEpochSecond() - 24 * 60 * 60) {
            return cache.getContent();
        }

        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;
        StringBuilder jsonString = new StringBuilder();

        try {
            Log.i(FetchGithubTask.class.toString(),"Fetching url: " + address);
            URL url = new URL(address);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.addRequestProperty("Accept", "application/vnd.github.v3+json");
            urlConnection.setUseCaches(true);

            if (githubWrapper.getGithubToken() != null) {
                urlConnection.addRequestProperty("Authorization", "token " + githubWrapper.getGithubToken());
            }
            InputStream stream = urlConnection.getInputStream();

            if (stream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(stream));

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                jsonString.append(line);
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        cache = new GithubCache();
        cache.setUrl(address);
        cache.setContent(jsonString.toString());
        cache.setFetchTime(Instant.now().getEpochSecond());

        databaseHelper.insertGithubCache(cache);

        return jsonString.toString();
    }

    @Deprecated
    protected String getURLKey(String... strings) {
        return getURL(strings);
    }

    protected abstract String getURL(String[] strings);

    @Deprecated
    protected void addHeaders(HttpsURLConnection connection) {

    }
}
