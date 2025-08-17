package org.unibl.etf.vetclinic.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import org.unibl.etf.vetclinic.data.entities.MedicalRecord;
import org.unibl.etf.vetclinic.data.entities.relations.MedicalRecordWithAppointment;

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

    @Transaction
    @Query("SELECT * FROM MedicalRecords WHERE AppointmentID IN (SELECT ID FROM Appointments WHERE PetID = :petId)")
    LiveData<List<MedicalRecordWithAppointment>> getMedicalRecordsWithAppointmentByPetId(int petId);


    @Query("SELECT * FROM MedicalRecords WHERE AppointmentID IN (SELECT ID FROM Appointments WHERE PetID = :petId)")
    LiveData<List<MedicalRecord>> getMedicalRecordsByPetId(int petId);
}

