package edu.quinnipiac.ser210.githubchat.github.async;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.time.Instant;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import edu.quinnipiac.ser210.githubchat.database.DatabaseHelper;
import edu.quinnipiac.ser210.githubchat.database.dataobjects.GithubCache;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;

/**
 * @author Thomas Kwashnak
 * An abstract AsyncTask that handles fetching data from the API endpoint, storing it into the database, and returning that value
 */
public abstract class FetchGithubTask extends AsyncTask<String,Void,String> {

    protected final GithubWrapper githubWrapper;
    protected final DatabaseHelper databaseHelper;

    public FetchGithubTask(GithubWrapper githubWrapper, DatabaseHelper databaseHelper) {
        this.githubWrapper = githubWrapper;
        this.databaseHelper = databaseHelper;

    }

    protected abstract String createURL(String[] strings);

    protected String getURLKey(String... strings) {
        return createURL(strings);
    }

    protected void addHeaders(HttpsURLConnection connection) {

    }

    @Override
    protected String doInBackground(String... strings) {

        GithubCache cache = databaseHelper.getGithubCache(getURLKey(strings));
        if(cache != null && cache.getFetchTime() > Instant.now().getEpochSecond() - 24 * 60 * 60) {
            return cache.getContent();
        }

        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;
        StringBuilder jsonString = new StringBuilder();

        try {
            URL url = new URL(createURL(strings));
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.addRequestProperty("Accept","application/vnd.github.v3+json");
            addHeaders(urlConnection);
            if(githubWrapper.getGithubToken() != null) {
                urlConnection.addRequestProperty("Authorization","token " + githubWrapper.getGithubToken());
            }
            InputStream stream = urlConnection.getInputStream();

            if(stream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(stream));

            for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                jsonString.append(line);
            }
            reader.close();


//            githubWrapper.getFetchAPIListener().onFetchAPI(url.toString(),jsonString.toString());

        } catch(Exception e) {
            e.printStackTrace();
        }

        cache = new GithubCache();
        cache.setUrl(getURLKey(strings));
        cache.setContent(jsonString.toString());
        cache.setFetchTime(Instant.now().getEpochSecond());

        databaseHelper.insertGithubCache(cache);


        return jsonString.toString();
    }
}
