package org.unibl.etf.vetclinic.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.unibl.etf.vetclinic.data.entities.Appointment;

import java.util.Date;
import java.util.List;

@Dao
public interface AppointmentDao {

    @Insert
    long insert(Appointment appointment);

    @Update
    void update(Appointment appointment);

    @Delete
    void delete(Appointment appointment);

    @Query("SELECT * FROM Appointments WHERE PetID = :petId AND Deleted IS NULL ORDER BY Date ASC")
    List<Appointment> getAppointmentsForPet(int petId);

    @Query("SELECT * FROM Appointments WHERE Date >= :from AND Date <= :to AND Deleted IS NULL ORDER BY Date ASC")
    List<Appointment> getAppointmentsInRange(Date from, Date to);

    @Query("SELECT * FROM Appointments WHERE ID = :id LIMIT 1")
    Appointment getAppointmentById(int id);
}

