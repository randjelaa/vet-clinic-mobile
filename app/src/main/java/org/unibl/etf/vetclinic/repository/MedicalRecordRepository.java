package org.unibl.etf.vetclinic.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import org.unibl.etf.vetclinic.data.database.AppDatabase;
import org.unibl.etf.vetclinic.data.dao.MedicalRecordDao;
import org.unibl.etf.vetclinic.data.entities.MedicalRecord;

import java.util.List;

public class MedicalRecordRepository {
    private final MedicalRecordDao medicalRecordDao;

    public MedicalRecordRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        medicalRecordDao = db.medicalRecordDao();
    }

    public LiveData<List<MedicalRecord>> getMedicalRecordsByPetId(int petId) {
        return medicalRecordDao.getMedicalRecordsByPetId(petId);
    }
}
