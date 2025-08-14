package org.unibl.etf.vetclinic.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.unibl.etf.vetclinic.data.entities.UnpaidService;

import java.util.List;

@Dao
public interface UnpaidServiceDao {

    @Insert
    long insert(UnpaidService unpaidService);

    @Update
    void update(UnpaidService unpaidService);

    @Delete
    void delete(UnpaidService unpaidService);

    @Query("SELECT * FROM UnpaidServices WHERE UserID = :userId ORDER BY ID DESC")
    List<UnpaidService> getUnpaidServicesForUser(int userId);

    @Query("SELECT * FROM UnpaidServices WHERE Status = :status ORDER BY ID DESC")
    List<UnpaidService> getUnpaidServicesByStatus(String status);

    @Query("SELECT * FROM UnpaidServices WHERE ID = :id LIMIT 1")
    UnpaidService getUnpaidServiceById(int id);
}

