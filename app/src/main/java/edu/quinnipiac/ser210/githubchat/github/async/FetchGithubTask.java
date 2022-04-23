package edu.quinnipiac.ser210.githubchat.github.async;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;

public abstract class FetchGithubTask extends AsyncTask<String,Void,String> {

    private GithubWrapper githubWrapper;

    public FetchGithubTask(GithubWrapper githubWrapper) {
        this.githubWrapper = githubWrapper;
    }

    protected abstract String createURL(String[] strings);

    protected void addHeaders(HttpsURLConnection connection) {

    }

    @Override
    protected String doInBackground(String... strings) {
        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;
        StringBuffer jsonString = new StringBuffer();

        while(githubWrapper.getGithubToken() == null);
        System.out.println("TOKEN ALIVE");

        try {
            URL url = new URL(createURL(strings));
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.addRequestProperty("Accept","application/vnd.github.v3+json");
            urlConnection.addRequestProperty("Authorization","token " + githubWrapper.getGithubToken());
            addHeaders(urlConnection);
            InputStream stream = urlConnection.getInputStream();

            if(stream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(stream));

            for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                jsonString.append(line);
            }
            reader.close();


            githubWrapper.getFetchAPIListener().onFetchAPI(url.toString(),jsonString.toString());

        } catch(Exception e) {
            e.printStackTrace();
        }


        return jsonString.toString();
    }

    public interface Listener {
        void onFetchAPI(String url, String api);
    }
}
