package org.unibl.etf.vetclinic.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.unibl.etf.vetclinic.data.entities.MedicalRecord;

import java.util.List;

@Dao
public interface MedicalRecordDao {

    @Insert
    long insert(MedicalRecord record);

    @Update
    void update(MedicalRecord record);

    @Delete
    void delete(MedicalRecord record);

    @Query("SELECT * FROM MedicalRecords WHERE AppointmentID = :appointmentId LIMIT 1")
    MedicalRecord getRecordForAppointment(int appointmentId);

    @Query("SELECT * FROM MedicalRecords ORDER BY ID DESC")
    List<MedicalRecord> getAllRecords();
}

