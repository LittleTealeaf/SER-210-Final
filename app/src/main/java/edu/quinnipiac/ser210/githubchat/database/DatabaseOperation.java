package edu.quinnipiac.ser210.githubchat.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * TODO put this inside DatabaseWrapper
 * @author Thomas Kwashnak
 */
public interface DatabaseOperation {
    void execute(SQLiteDatabase database);
}
