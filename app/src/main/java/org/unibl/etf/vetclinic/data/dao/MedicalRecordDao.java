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

    @Transaction
    @Query("SELECT * FROM MedicalRecords WHERE AppointmentID IN (SELECT ID FROM Appointments WHERE PetID = :petId)")
    LiveData<List<MedicalRecordWithAppointment>> getMedicalRecordsWithAppointmentByPetId(int petId);

}

