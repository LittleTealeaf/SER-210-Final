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

/*
If you're on Android Studio, you should be able to hover over the first /** line and click the icon that shows up to on the left to show the rendered javadoc
 */
/**
 * <p>The DatabaseWrapper is a class that wraps the required functions into simple methods that the front-end can use. Using this method allows the organization of database
 * operations, as well as easy implementations of asynchronous tasks.</p>
 * <p>Firstly, let's make the distinction between the types of methods in this class. There are the regular methods {@link #getGithubCache(String)}, and the start methods
 * {@link #startGetGithubCache(String, OnFetchGithubCache)}. These methods, in the end, perform the same action on the repository, but do it in different ways.</p>
 * <ul>
 *     <li>{@link #getGithubCache(String)} will immediately perform the action to get the {@link GithubCache} from the database on whatever thread it's currently on. Once
 *     it's done, it will then return the value</li>
 *     <li>{@link #startGetGithubCache(String, OnFetchGithubCache, int)} instead starts {@link #getGithubCache(String)} on an asynchronous thread. It returns the
 *     "channel" that the listener will be notified on (See {@link ThreadManager}). Once {@link #getGithubCache(String)} completes on the other thread, it will return
 *     back to the main thread and notify the provided listener</li>
 * </ul>
 * <p>This allows any action required to be taken either on the current thread (useful in cases where it's already being done on an async thread, such as
 * {@link edu.quinnipiac.ser210.githubchat.github.GithubWrapper}, or on a new thread.</p>
 * @author Thomas Kwashnak
 * @see #executeDatabaseOperation(DatabaseOperation) 
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

    /**
     * Attempts to get a DatabaseWrapper from a holder.
     * @param object The object to attempt to get the DatabaseWrapper from
     * @return The database wrapper, returns null if the object does not implement the {@link DatabaseHolder} interface.
     */
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
     * <p>Because tasks are being computed on asynchronous threads, it's impossible to know when one will start and another will end. In order to ensure this structure is
     * stable, this method is used whenever a database operation is required. Since this method is synchronized, there can only be one instance of this method running at a
     * given time.</p>
     *
     * <p>This method manages any database operations. First, it opens a new writable database using {@link #getWritableDatabase()}. Then, it executes the provided operation,
     * passing in the opened database. Once the operation is completed, it closes the database and allowing the next operation to be completed.</p>
     * @param operation The database operation that needs to be executed.
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

    public interface DatabaseOperation {

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
