package edu.quinnipiac.ser210.githubchat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import edu.quinnipiac.ser210.githubchat.database.dataobjects.GithubCache;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_GITHUB_CACHE = "GITHUB_CACHE";
    private static final String KEY_ID = "_id";
    private static final String KEY_URL = "URL";
    private static final String KEY_CONTENT = "CONTENT";
    private static final String KEY_FETCH_TIME = "FETCH_TIME";

    private static final int VERSION = 1;

    private final SQLiteDatabase database;

    public DatabaseHelper(Context context) {
        super(context, "GithubChatDatabase", null, VERSION);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        onUpgrade(sqLiteDatabase,0,VERSION);
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

    public long addGithubCache(GithubCache cache) throws SQLiteException {

        ContentValues values = new ContentValues();
        values.put(KEY_URL, cache.getUrl());
        values.put(KEY_CONTENT, cache.getContent());
        values.put(KEY_FETCH_TIME, cache.getFetchTime());

        long id = database.insert(TABLE_GITHUB_CACHE, null, values);


        return id;
    }

    public GithubCache getGithubCache(String url) {
        String[] columns = {KEY_URL, KEY_CONTENT, KEY_FETCH_TIME};
        Cursor cursor = database.query(TABLE_GITHUB_CACHE, columns, KEY_URL + " = ?", new String[]{url}, null, null, null);

        GithubCache cache = null;

        if (cursor.getCount() > 0) {
            cursor.moveToNext();

            cache = new GithubCache();
            cache.setUrl(cursor.getString(0));
            cache.setContent(cursor.getString(1));
            cache.setFetchTime(cursor.getLong(2));

            cursor.close();
        }
        return cache;
    }

    public void updateGithubCache(GithubCache cache) throws SQLiteException {

        ContentValues values = new ContentValues();
        values.put(KEY_CONTENT, cache.getContent());
        values.put(KEY_URL, cache.getUrl());
        values.put(KEY_FETCH_TIME, cache.getFetchTime());

        database.update(TABLE_GITHUB_CACHE, values, KEY_URL + " = ?", new String[]{cache.getUrl()});

    }

    @Override
    public synchronized void close() {
        database.close();
        super.close();
    }
}
