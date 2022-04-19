package edu.quinnipiac.ser210.githubchat.database;

import android.content.Context;

public class DatabaseWrapper {

    private DatabaseHelper databaseHelper;

    public DatabaseWrapper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }
}
