package org.unibl.etf.vetclinic.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.unibl.etf.vetclinic.data.entities.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    long insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM Users ORDER BY Name ASC")
    List<User> getAllUsers();

    @Query("SELECT * FROM Users WHERE ID = :id LIMIT 1")
    User getUserById(int id);

    @Query("SELECT * FROM Users WHERE Email = :email AND Password = :password LIMIT 1")
    User login(String email, String password);
}

