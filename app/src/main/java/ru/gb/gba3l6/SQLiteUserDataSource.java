package ru.gb.gba3l6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

class SQLiteUserDataSource {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    private String[] allColumn = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_LOGIN,
            DatabaseHelper.COLUMN_AVATAR
    };

    SQLiteUserDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    void close() {
        dbHelper.close();
    }

    void addUser(Model user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_ID, user.getUserId());
        contentValues.put(DatabaseHelper.COLUMN_LOGIN, user.getLogin());
        contentValues.put(DatabaseHelper.COLUMN_AVATAR, user.getAvatar());
        database.insert(DatabaseHelper.TABLE_USERS, null, contentValues);
    }

    int deleteAll() {
        return database.delete(DatabaseHelper.TABLE_USERS, null, null);
    }

    List<Model> getAllUsers() {
        List<Model> notes = new ArrayList<>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_USERS,
                allColumn, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Model user = cursorToUser(cursor);
            notes.add(user);
            cursor.moveToNext();
        }
        cursor.close();
        return notes;
    }

    private Model cursorToUser(Cursor cursor) {
        Model user = new Model();
        user.setId(cursor.getString(0));
        user.setLogin(cursor.getString(1));
        user.setAvatar(cursor.getString(2));
        return user;
    }
}
