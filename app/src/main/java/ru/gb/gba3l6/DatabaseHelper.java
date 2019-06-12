package ru.gb.gba3l6;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;
    static final String TABLE_USERS = "users";

    // столбцы
    static final String COLUMN_ID = "user_id";
    static final String COLUMN_LOGIN = "login";
    static final String COLUMN_AVATAR = "avatar";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " TEXT PRIMARY KEY," +
                COLUMN_LOGIN + " TEXT, " +
                COLUMN_AVATAR + " TEXT " + ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
