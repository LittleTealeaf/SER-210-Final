package edu.quinnipiac.ser210.githubchat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_GITHUB_CACHE = "GITHUB_CACHE";
    private static final String KEY_ID = "_id";
    private static final String KEY_URL = "URL";
    private static final String KEY_CONTENT = "CONTENT";
    private static final String KEY_FETCH_TIME = "FETCH_TIME";

    private static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, "GithubChatDatabase", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            sqLiteDatabase.execSQL(
                    String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s LONG, %s TEXT);", TABLE_GITHUB_CACHE,
                                  KEY_ID, KEY_URL, KEY_FETCH_TIME, KEY_CONTENT
                                 ));
        }
    }
}
