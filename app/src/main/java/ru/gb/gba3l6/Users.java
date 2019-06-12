package ru.gb.gba3l6;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Users {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "login")
    private String login;

    @ColumnInfo(name = "userId")
    private String userId;

    @ColumnInfo(name = "avatar_url")
    private String avatarUrl;

    int getUid() {
        return uid;
    }

    void setUid(int uid) {
        this.uid = uid;
    }

    public String getLogin() {
        return login;
    }

    String getUserId() {
        return userId;
    }

    String getAvatarUrl() {
        return avatarUrl;
    }

    Users(String login, @NonNull String userId, String avatarUrl) {
        this.login = login;
        this.userId = userId;
        this.avatarUrl = avatarUrl;
    }
}