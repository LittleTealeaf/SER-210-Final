package edu.quinnipiac.ser210.githubchat.github.async;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import edu.quinnipiac.ser210.githubchat.database.DatabaseHelper;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;

public abstract class FetchGithubTask extends AsyncTask<String,Void,String> {

    protected final GithubWrapper githubWrapper;
    protected final DatabaseHelper databaseHelper;

    public FetchGithubTask(GithubWrapper githubWrapper, DatabaseHelper databaseHelper) {
        this.githubWrapper = githubWrapper;
        this.databaseHelper = databaseHelper;

    }

    protected abstract String createURL(String[] strings);

    protected void addHeaders(HttpsURLConnection connection) {

    }

    @Override
    protected String doInBackground(String... strings) {
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


        return jsonString.toString();
    }
}
