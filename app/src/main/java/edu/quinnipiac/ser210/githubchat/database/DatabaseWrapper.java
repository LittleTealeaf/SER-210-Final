package edu.quinnipiac.ser210.githubchat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import edu.quinnipiac.ser210.githubchat.database.dataobjects.ChatRoom;
import edu.quinnipiac.ser210.githubchat.database.dataobjects.GithubCache;
import edu.quinnipiac.ser210.githubchat.database.listeners.OnFetchChatRoom;
import edu.quinnipiac.ser210.githubchat.database.listeners.OnFetchChatRooms;
import edu.quinnipiac.ser210.githubchat.database.listeners.OnSetChatRoom;

public class DatabaseWrapper extends SQLiteOpenHelper implements DatabaseHolder {

    public static final int CHANNEL_DEFAULT = -1;

    public static final String KEY_REPO_NAME = "RepoName";

    private static final int VERSION = 1;

    private static final String TABLE_GITHUB_CACHE = "GITHUB_CACHE";
    private static final String TABLE_CHAT_ROOM = "CHAT_ROOM";

    private static final String COL_URL = "URL";
    private static final String COL_CONTENT = "CONTENT";
    private static final String COL_FETCH_TIME = "FETCH_TIME";
    private static final String COL_REPO_NAME = "REPO_NAME";
    private static final String COL_FAVORITE = "FAVORITE";
    private static final String COL_ID = "ID";

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public DatabaseWrapper(Context context) {
        super(context, "GithubChatDatabase", null, VERSION);
    }

    public static DatabaseWrapper from(Object object) {
        if (object instanceof DatabaseHolder) {
            return ((DatabaseHolder) object).getDatabaseWrapper();
        } else {
            return null;
        }
    }

    @Override
    public DatabaseWrapper getDatabaseWrapper() {
        return this;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        onUpgrade(sqLiteDatabase, 0, VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old, int cur) {
        if (old < 1) {
            sqLiteDatabase.execSQL(
                    "CREATE TABLE " + TABLE_GITHUB_CACHE + " (" + COL_URL + " TEXT PRIMARY KEY, " + COL_FETCH_TIME + " LONG, " + COL_CONTENT + " " + "TEXT);");
            sqLiteDatabase.execSQL(
                    "CREATE TABLE " + TABLE_CHAT_ROOM + "(" + COL_REPO_NAME + " TEXT PRIMARY KEY NOT NULL, " + COL_FAVORITE + " INTEGER)");
        }
    }

    public GithubCache getGithubCache(String url) {
        String[] columns = {COL_FETCH_TIME, COL_CONTENT};

        AtomicReference<GithubCache> cache = new AtomicReference<>();

        executeDatabaseOperation((db) -> {
            Cursor cursor = db.query(TABLE_GITHUB_CACHE, columns, COL_URL + " = ?", new String[]{url}, null, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                cache.set(new GithubCache(url, cursor.getLong(0), cursor.getString(1)));
            }
            cursor.close();
        });
        return cache.get();
    }

    public synchronized void executeDatabaseOperation(DatabaseOperation operation) {
        SQLiteDatabase database = getWritableDatabase();
        operation.execute(database);
        database.close();
    }

    public void setChatRoom(ChatRoom chatRoom) {
        ContentValues values = new ContentValues();
        values.put(COL_REPO_NAME, chatRoom.getRepoName());
        values.put(COL_FAVORITE, chatRoom.isFavorite() ? 1 : 0);

        executeDatabaseOperation((db) -> {
            if (db.update(TABLE_CHAT_ROOM, values, COL_REPO_NAME + " = ?", new String[]{chatRoom.getRepoName()}) == 0) {
                db.insert(TABLE_CHAT_ROOM, null, values);
            }
        });
    }

    public void startSetChatRoom(ChatRoom chatRoom) {
        startThread(() -> setChatRoom(chatRoom));
    }

    public void startSetChatRoom(ChatRoom chatRoom, OnSetChatRoom listener) {
        startSetChatRoom(chatRoom,listener,CHANNEL_DEFAULT);
    }

    public void startSetChatRoom(ChatRoom chatRoom, OnSetChatRoom listener, int channel) {
        startThread(() -> {
            setChatRoom(chatRoom);
            return chatRoom;
        },listener::onSetChatRoom,channel);
    }

    public void startGetChatRoom(String repoName, OnFetchChatRoom listener) {
        startGetChatRoom(repoName, listener, CHANNEL_DEFAULT);
    }

    public void startGetChatRoom(String repoName, OnFetchChatRoom listener, int channel) {
        startThread(() -> getChatRoom(repoName), listener::onFetchChatRoom, channel);
    }

    private synchronized <T> void startThread(Callable<T> callable, Notifier<T> notifier, int channel) {
        executorService.execute(() -> {
            try {
                T item = callable.call();
                handler.post(() -> notifier.notify(item, channel));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ChatRoom getChatRoom(String repoName) {
        String[] columns = {COL_REPO_NAME, COL_FAVORITE};

        AtomicReference<ChatRoom> room = new AtomicReference<>();

        executeDatabaseOperation((db) -> {
            Cursor cursor = db.query(TABLE_CHAT_ROOM, columns, COL_REPO_NAME + " = ?", new String[]{repoName}, null, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                room.set(new ChatRoom(cursor.getString(0), cursor.getInt(1) == 1));
            }
            cursor.close();
        });
        return room.get();
    }

    public void startGetChatRooms(OnFetchChatRooms listener) {
        startGetChatRooms(listener, CHANNEL_DEFAULT);
    }

    public void startGetChatRooms(OnFetchChatRooms listener, int channel) {
        startThread(this::getChatRooms, listener::onFetchChatRooms, channel);
    }

    public List<ChatRoom> getChatRooms() {
        String[] columns = {COL_REPO_NAME, COL_FAVORITE};
        List<ChatRoom> chatRooms = new LinkedList<>();

        executeDatabaseOperation((db) -> {
            Cursor cursor = db.query(TABLE_CHAT_ROOM, columns, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                chatRooms.add(new ChatRoom(cursor.getString(0), cursor.getInt(1) == 1));
                cursor.moveToNext();
            }
            cursor.close();
        });
        return chatRooms;
    }

    public void startSetGithubCache(GithubCache githubCache) {
        startThread(() -> setGithubCache(githubCache));
    }

    private void startThread(Runnable runnable) {
        executorService.execute(runnable);
    }

    public void setGithubCache(GithubCache githubCache) {
        ContentValues values = new ContentValues();
        values.put(COL_URL, githubCache.getUrl());
        values.put(COL_FETCH_TIME, githubCache.getFetchTime());
        values.put(COL_CONTENT, githubCache.getContent());

        executeDatabaseOperation((db) -> {
            if (db.update(TABLE_GITHUB_CACHE, values, COL_URL + " = ?", new String[]{githubCache.getUrl()}) == 0) {
                db.insert(TABLE_GITHUB_CACHE, null, values);
            }
        });
    }

    private interface Notifier<T> {

        void notify(T item, int channel);
    }
}
