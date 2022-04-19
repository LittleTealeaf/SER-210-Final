package edu.quinnipiac.ser210.githubchat.github.async;

import android.os.AsyncTask;

import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.callbacks.OnAPIFetched;

public class APIFetchTask extends AsyncTask<String,Void,String> {

    private final OnAPIFetched listener;

    public APIFetchTask(OnAPIFetched listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(listener != null) {
            listener.onAPIFetched(s);
        }
    }
}
