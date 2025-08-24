package org.unibl.etf.vetclinic.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.unibl.etf.vetclinic.data.entities.UnpaidService;

@Dao
public interface UnpaidServiceDao {

    @Insert
    long insert(UnpaidService unpaidService);

    @Update
    void update(UnpaidService unpaidService);

    @Delete
    void delete(UnpaidService unpaidService);

    @Query("SELECT * FROM UnpaidServices WHERE AppointmentID = :appointmentId LIMIT 1")
    UnpaidService getByAppointmentId(int appointmentId);
}

