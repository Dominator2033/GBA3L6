package ru.gb.gba3l6;

import io.realm.RealmObject;

public class RealmModel extends RealmObject {
    private String login;
    private String userId;
    private String avatarUrl;

    public RealmModel() {
    }

    public void setLogin(String login) {
        this.login = login;
    }

    void setUserId(String userId) {
        this.userId = userId;
    }

    void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
