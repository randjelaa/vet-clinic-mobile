package org.unibl.etf.vetclinic.data.dao;

import androidx.lifecycle.LiveData;
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

    @Query("SELECT * FROM Users WHERE ID = :id LIMIT 1")
    User getUserById(int id);

    @Query("SELECT * FROM Users WHERE Email = :email AND Password = :password LIMIT 1")
    User login(String email, String password);

    @Query("SELECT * FROM Users WHERE Email = :email LIMIT 1")
    User getUserByEmail(String email);

    @Query("SELECT u.* FROM Users u " +
            "JOIN Roles r ON u.RoleID = r.ID " +
            "WHERE r.Name = 'Veterinarian' AND u.Deleted IS NULL")
    LiveData<List<User>> getAllVets();

    @Query("SELECT u.* FROM Users u " +
            "JOIN Roles r ON u.RoleID = r.ID " +
            "WHERE r.Name = 'Client' AND u.Deleted IS NULL")
    LiveData<List<User>> getAllClients();

}

