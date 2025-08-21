package org.unibl.etf.vetclinic.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.unibl.etf.vetclinic.data.entities.UserPreferences;

@Dao
public interface UserPreferencesDao {

    @Insert
    long insert(UserPreferences prefs);

    @Update
    void update(UserPreferences prefs);

    @Delete
    void delete(UserPreferences prefs);

    @Query("SELECT * FROM UserPreferences WHERE UserID = :userId LIMIT 1")
    LiveData<UserPreferences> getPreferencesForUser(int userId);
}

