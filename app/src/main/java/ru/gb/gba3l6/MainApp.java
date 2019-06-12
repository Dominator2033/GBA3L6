package ru.gb.gba3l6;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.orm.SugarContext;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MainApp extends Application {
    private static AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);

        Realm.init(this);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded().build();

        Realm.setDefaultConfiguration(realmConfiguration);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database").fallbackToDestructiveMigration().build();
    }

    public static AppDatabase getDb() {
        return db;
    }
}
