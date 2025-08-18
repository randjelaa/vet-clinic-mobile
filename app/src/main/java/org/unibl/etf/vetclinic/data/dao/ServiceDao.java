package org.unibl.etf.vetclinic.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.unibl.etf.vetclinic.data.entities.Service;

import java.util.List;

@Dao
public interface ServiceDao {

    @Insert
    long insert(Service service);

    @Update
    void update(Service service);

    @Delete
    void delete(Service service);

    @Query("SELECT * FROM Services WHERE Deleted IS NULL ORDER BY Name ASC")
    LiveData<List<Service>> getAllServices();

    @Query("SELECT * FROM Services WHERE ID = :id LIMIT 1")
    Service getServiceById(int id);
}

