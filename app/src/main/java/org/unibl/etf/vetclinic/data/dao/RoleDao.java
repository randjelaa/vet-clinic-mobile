package org.unibl.etf.vetclinic.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.unibl.etf.vetclinic.data.entities.Role;

import java.util.List;

@Dao
public interface RoleDao {

    @Insert
    long insert(Role role);

    @Update
    void update(Role role);

    @Delete
    void delete(Role role);

    @Query("SELECT * FROM Roles ORDER BY Name ASC")
    List<Role> getAllRoles();

    @Query("SELECT * FROM Roles WHERE ID = :id LIMIT 1")
    Role getRoleById(int id);
}

