package edu.quinnipiac.ser210.githubchat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import edu.quinnipiac.ser210.githubchat.database.dataobjects.ChatRoom;
import edu.quinnipiac.ser210.githubchat.database.dataobjects.GithubCache;
import edu.quinnipiac.ser210.githubchat.threads.ThreadManager;

/**
 * @author Thomas Kwashnak
 */
public class DatabaseWrapper extends SQLiteOpenHelper implements DatabaseHolder {

    private static final int VERSION = 1;

    private static final String TABLE_GITHUB_CACHE = "GITHUB_CACHE";
    private static final String TABLE_CHAT_ROOM = "CHAT_ROOM";

    private static final String COL_URL = "URL";
    private static final String COL_CONTENT = "CONTENT";
    private static final String COL_FETCH_TIME = "FETCH_TIME";
    private static final String COL_REPO_NAME = "REPO_NAME";
    private static final String COL_FAVORITE = "FAVORITE";

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
            sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_GITHUB_CACHE + " (" + COL_URL + " TEXT PRIMARY KEY, " + COL_FETCH_TIME + " LONG, " + COL_CONTENT + " " + "TEXT);");
            sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CHAT_ROOM + "(" + COL_REPO_NAME + " TEXT PRIMARY KEY NOT NULL, " + COL_FAVORITE + " INTEGER)");
        }
    }

    /**
     * Starts the {@link #getGithubCache(String)} method in an alternate thread, notifying the listener on completion
     *
     * @param url      URL of the cache to get
     * @param listener Listener to notify once the value has been retrieved
     *
     * @return The channel that the listener will be notified on
     */
    public int startGetGithubCache(String url, OnFetchGithubCache listener) {
        return ThreadManager.startThread(() -> getGithubCache(url), listener::onFetchGithubCache);
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

    /**
     * Wraps opening and closing a writable database to ensure that asynchronous tasks do not overlap with database executions
     *
     * @param operation The operation that needs to be executed
     */
    public synchronized void executeDatabaseOperation(DatabaseOperation operation) {
        //Opens the database
        SQLiteDatabase database = getWritableDatabase();
        //Runs the operation
        operation.execute(database);
        //Closes the database
        database.close();
    }

    public int startGetGithubCache(String url, OnFetchGithubCache listener, int channel) {
        return ThreadManager.startThread(() -> getGithubCache(url), listener::onFetchGithubCache, channel);
    }

    public int startGetChatRoom(String repoName, OnFetchChatRoom listener) {
        return ThreadManager.startThread(() -> getChatRoom(repoName), listener::onFetchChatRoom);
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

    public int startGetChatRoom(String repoName, OnFetchChatRoom listener, int channel) {
        return ThreadManager.startThread(() -> getChatRoom(repoName), listener::onFetchChatRoom, channel);
    }

    public int startRemoveChatRoom(ChatRoom chatRoom, OnRemoveChatRoom listener) {
        return ThreadManager.startThread(() -> removeChatRoom(chatRoom), listener::onRemoveChatRoom);
    }

    public String removeChatRoom(ChatRoom chatRoom) {
        return removeChatRoom(chatRoom.getRepoName());
    }

    public String removeChatRoom(String repoName) {
        executeDatabaseOperation((db) -> db.delete(TABLE_CHAT_ROOM, COL_REPO_NAME + " = ?", new String[]{repoName}));
        return repoName;
    }

    public int startRemoveChatRoom(String repoName, OnRemoveChatRoom listener) {
        return ThreadManager.startThread(() -> removeChatRoom(repoName), listener::onRemoveChatRoom);
    }

    public int startRemoveChatRoom(String repoName, OnRemoveChatRoom listener, int channel) {
        return ThreadManager.startThread(() -> removeChatRoom(repoName), listener::onRemoveChatRoom, channel);
    }

    public int startRemoveChatRoom(ChatRoom chatRoom, OnRemoveChatRoom listener, int channel) {
        return ThreadManager.startThread(() -> removeChatRoom(chatRoom), listener::onRemoveChatRoom, channel);
    }

    public int startUpdateChatRoom(ChatRoom chatRoom, OnUpdateChatRoom listener) {
        return ThreadManager.startThread(() -> updateChatRoom(chatRoom), listener::onUpdateChatRoom);
    }

    public ChatRoom updateChatRoom(ChatRoom chatRoom) {
        //Create content values
        ContentValues values = new ContentValues();
        values.put(COL_REPO_NAME, chatRoom.getRepoName());
        values.put(COL_FAVORITE, chatRoom.isFavorite() ? 1 : 0);

        executeDatabaseOperation((db) -> {
            //Updates the table with the values
            if (db.update(TABLE_CHAT_ROOM, values, COL_REPO_NAME + " = ?", new String[]{chatRoom.getRepoName()}) == 0) {

                //If no rows were updated, then insert a new row
                db.insert(TABLE_CHAT_ROOM, null, values);
            }
        });
        return chatRoom;
    }

    public int startUpdateChatRoom(ChatRoom chatRoom, OnUpdateChatRoom listener, int channel) {
        return ThreadManager.startThread(() -> updateChatRoom(chatRoom), listener::onUpdateChatRoom, channel);
    }

    public int startGetChatRoomList(OnFetchChatRoomList listener) {
        return ThreadManager.startThread(this::getChatRoomList, listener::onFetchChatRoomList);
    }

    public List<ChatRoom> getChatRoomList() {
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

    public int startGetChatRoomList(OnFetchChatRoomList listener, int channel) {
        return ThreadManager.startThread(this::getChatRoomList, listener::onFetchChatRoomList, channel);
    }

    public int startUpdateGithubCache(GithubCache githubCache, OnUpdateGithubCache listener) {
        return ThreadManager.startThread(() -> updateGithubCache(githubCache), listener::onUpdateGithubCache);
    }

    public GithubCache updateGithubCache(GithubCache githubCache) {
        ContentValues values = new ContentValues();
        values.put(COL_URL, githubCache.getUrl());
        values.put(COL_FETCH_TIME, githubCache.getFetchTime());
        values.put(COL_CONTENT, githubCache.getContent());

        executeDatabaseOperation((db) -> {
            if (db.update(TABLE_GITHUB_CACHE, values, COL_URL + " = ?", new String[]{githubCache.getUrl()}) == 0) {
                db.insert(TABLE_GITHUB_CACHE, null, values);
            }
        });
        return githubCache;
    }

    public int startUpdateGithubCache(GithubCache githubCache, OnUpdateGithubCache listener, int channel) {
        return ThreadManager.startThread(() -> updateGithubCache(githubCache), listener::onUpdateGithubCache, channel);
    }

    private interface DatabaseOperation {

        void execute(SQLiteDatabase database);
    }

    public interface OnFetchChatRoom {

        OnFetchChatRoom NONE = (a, b) -> {};

        void onFetchChatRoom(ChatRoom chatRoom, int channel);
    }

    public interface OnFetchChatRoomList {

        OnFetchChatRoomList NONE = (a, b) -> {};

        void onFetchChatRoomList(List<ChatRoom> chatRoomList, int channel);
    }

    public interface OnFetchGithubCache {

        OnFetchGithubCache NONE = (a, b) -> {};

        void onFetchGithubCache(GithubCache githubCache, int channel);
    }

    public interface OnRemoveChatRoom {

        OnRemoveChatRoom NONE = (a, b) -> {};

        void onRemoveChatRoom(String repoName, int channel);
    }

    public interface OnUpdateChatRoom {

        OnUpdateChatRoom NONE = (a, b) -> {};

        void onUpdateChatRoom(ChatRoom chatRoom, int channel);
    }

    public interface OnUpdateGithubCache {

        OnUpdateChatRoom NONE = (a, b) -> {};

        void onUpdateGithubCache(GithubCache githubCache, int channel);
    }
}
