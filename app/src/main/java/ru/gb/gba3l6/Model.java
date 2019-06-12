package ru.gb.gba3l6;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class Model {

    private String login;

    private String id;

    @SerializedName("avatar_url")
    private String avatar;

    @Nullable
    String getAvatar() {
        return avatar;
    }

    @Nullable
    public String getLogin() {
        return login;
    }

    String getUserId() {
        return id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    void setId(String id) {
        this.id = id;
    }

    void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
