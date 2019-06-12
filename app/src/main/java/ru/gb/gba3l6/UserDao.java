package ru.gb.gba3l6;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users")
    List<Users> getAll();

    @Query("DELETE FROM users")
    int deleteAll();

    @Insert
    void insert(Users employee);
}
