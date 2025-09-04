package org.unibl.etf.vetclinic.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.unibl.etf.vetclinic.data.entities.Appointment;
import org.unibl.etf.vetclinic.data.entities.relations.AppointmentWithDetails;

import java.util.List;

@Dao
public interface AppointmentDao {

    @Insert
    long insert(Appointment appointment);

    @Update
    void update(Appointment appointment);

    @Delete
    void delete(Appointment appointment);

    @Query("SELECT * FROM Appointments WHERE ID = :id LIMIT 1")
    Appointment getById(int id);

    @Query("SELECT a.ID, a.Date, p.Name AS PetName, s.Name AS ServiceName, s.Price, " +
            "u.Name AS VetName, " +
            "CASE WHEN pay.AppointmentID IS NOT NULL THEN 1 ELSE 0 END AS IsPaid " +
            "FROM Appointments a " +
            "JOIN Pets p ON a.PetID = p.ID " +
            "JOIN Services s ON a.ServiceID = s.ID " +
            "JOIN Users u ON a.VetID = u.ID " +
            "LEFT JOIN Payments pay ON pay.AppointmentID = a.ID " +
            "WHERE a.Deleted IS NULL AND p.OwnerID = :userId " +
            "ORDER BY a.Date DESC")
    LiveData<List<AppointmentWithDetails>> getAppointmentsByUserId(int userId);

    @Query("SELECT a.ID, a.Date, p.Name AS PetName, s.Name AS ServiceName, s.Price, " +
            "CASE WHEN EXISTS (SELECT 1 FROM Payments WHERE AppointmentID = a.ID) THEN 1 ELSE 0 END AS IsPaid " +
            "FROM Appointments a " +
            "JOIN Pets p ON a.PetID = p.ID " +
            "JOIN Services s ON a.ServiceID = s.ID " +
            "WHERE a.ID = :id AND a.Deleted IS NULL LIMIT 1")
    AppointmentWithDetails getDetailsById(int id);

    @Query("SELECT * FROM Appointments WHERE VetID = :vetId AND Deleted IS NULL")
    LiveData<List<Appointment>> getAppointmentsByVetId(int vetId);
}

