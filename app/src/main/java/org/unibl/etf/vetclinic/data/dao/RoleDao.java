package org.unibl.etf.vetclinic.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import org.unibl.etf.vetclinic.data.entities.Role;

@Dao
public interface RoleDao {

    @Insert
    long insert(Role role);

    @Update
    void update(Role role);

    @Delete
    void delete(Role role);

}

