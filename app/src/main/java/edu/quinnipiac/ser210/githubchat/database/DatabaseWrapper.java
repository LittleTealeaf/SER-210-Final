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
import edu.quinnipiac.ser210.githubchat.database.listeners.OnFetchChatRoom;
import edu.quinnipiac.ser210.githubchat.database.listeners.OnFetchChatRooms;

public class DatabaseWrapper extends SQLiteOpenHelper implements DatabaseHolder {

    public static final int CHANNEL_DEFAULT = -1;

    private static final int VERSION = 1;

    private static final String TABLE_GITHUB_CACHE = "GITHUB_CACHE";
    private static final String TABLE_CHAT_ROOM = "CHAT_ROOM";

    private static final String COL_URL = "URL";
    private static final String COL_CONTENT = "CONTENT";
    private static final String COL_FETCH_TIME = "FETCH_TIME";
    private static final String COL_REPO_NAME = "REPO_NAME";
    private static final String COL_FAVORITE = "FAVORITE";
    private static final String COL_ID = "ID";

    public DatabaseWrapper(Context context) {
        super(context,"GithubChatDatabase",null,VERSION);
    }

    @Override
    public DatabaseWrapper getDatabaseWrapper() {
        return this;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        onUpgrade(sqLiteDatabase,0,VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old, int cur) {
        if(old < 1) {
            sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_GITHUB_CACHE + " (" + COL_URL + " TEXT PRIMARY KEY, " + COL_FETCH_TIME + " LONG, " + COL_CONTENT +
                                   " TEXT);");
            sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CHAT_ROOM + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_REPO_NAME + " " +
                                           "TEXT NOT NULL, " + COL_FAVORITE + " INTEGER)");
        }
    }

    public synchronized void executeDatabaseOperation(DatabaseOperation operation) {
        SQLiteDatabase database = getWritableDatabase();
        operation.execute(database);
        database.close();
    }

    public void setGithubCache(GithubCache githubCache) {
        ContentValues values = new ContentValues();
        values.put(COL_URL,githubCache.getUrl());
        values.put(COL_FETCH_TIME,githubCache.getFetchTime());
        values.put(COL_CONTENT,githubCache.getContent());

        executeDatabaseOperation((db) -> {
            if(db.update(TABLE_GITHUB_CACHE,values,COL_URL + " = ?",new String[] {githubCache.getUrl()}) == 0) {
                db.insert(TABLE_GITHUB_CACHE,null,values);
            }
        });
    }

    public GithubCache getGithubCache(String url) {
        String[] columns = {COL_FETCH_TIME, COL_CONTENT};

        AtomicReference<GithubCache> cache = new AtomicReference<>();

        executeDatabaseOperation((db) -> {
            Cursor cursor = db.query(TABLE_GITHUB_CACHE,columns,COL_URL + " = ?",new String[] {url},null,null,null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                cache.set(new GithubCache(url,cursor.getLong(0),cursor.getString(1)));
            }
            cursor.close();
        });
        return cache.get();
    }

    public void setChatRoom(ChatRoom chatRoom) {
        ContentValues values = new ContentValues();
        values.put(COL_REPO_NAME,chatRoom.getRepoName());
        values.put(COL_FAVORITE,chatRoom.isFavorite() ? 1 : 0);

        executeDatabaseOperation((db) -> {
            if(db.update(TABLE_CHAT_ROOM,values,COL_ID + " = ?",new String[]{Integer.toString(chatRoom.getId())}) == 0) {
                db.insert(TABLE_CHAT_ROOM,null,values);
            }
        });
    }

    public List<ChatRoom> getChatRooms() {
        String[] columns = {COL_ID,COL_REPO_NAME,COL_FAVORITE};
        List<ChatRoom> chatRooms = new LinkedList<>();

        executeDatabaseOperation((db) -> {
            Cursor cursor = db.query(TABLE_CHAT_ROOM,columns,null,null,null,null,null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                ChatRoom chatRoom = new ChatRoom();
                chatRoom.setId(cursor.getInt(0));
                chatRoom.setRepoName(cursor.getString(1));
                chatRoom.setFavorite(cursor.getInt(2) == 1);
                chatRooms.add(chatRoom);
                cursor.moveToNext();
            }
            cursor.close();
        });
        return chatRooms;
    }

    public ChatRoom getChatRoom(int id) {
        String[] columns = {COL_ID, COL_REPO_NAME, COL_FAVORITE};

        AtomicReference<ChatRoom> room = new AtomicReference<>();

        executeDatabaseOperation((db) -> {
            Cursor cursor = db.query(TABLE_CHAT_ROOM,columns,COL_ID + " = ?",new String[] {Integer.toString(id)},null,null,null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                room.set(new ChatRoom(cursor.getInt(0),cursor.getString(1),cursor.getInt(2) == 1));
            }
            cursor.close();
        });
        return room.get();
    }

    public void startGetChatRoom(int id, OnFetchChatRoom listener) {
        startGetChatRoom(id, listener, CHANNEL_DEFAULT);
    }

    public void startGetChatRoom(int id, OnFetchChatRoom listener, int channel) {
        new Thread(() -> listener.onFetchChatRoom(getChatRoom(id),channel)).start();
    }

    public void startGetChatRooms(OnFetchChatRooms listener) {
        startGetChatRooms(listener, CHANNEL_DEFAULT);
    }

    public void startGetChatRooms(OnFetchChatRooms listener, int channel) {
        new Thread(() -> listener.onFetchChatRooms(getChatRooms(),channel)).start();
    }

    public void startSetGithubCache(GithubCache githubCache) {
        new Thread(() -> setGithubCache(githubCache)).start();
    }


    public static DatabaseWrapper from(Object object) {
        if(object instanceof DatabaseHolder) {
            return ((DatabaseHolder) object).getDatabaseWrapper();
        } else {
            return null;
        }
    }
}
