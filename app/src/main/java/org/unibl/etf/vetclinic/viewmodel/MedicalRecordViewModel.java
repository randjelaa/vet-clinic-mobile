package org.unibl.etf.vetclinic.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.unibl.etf.vetclinic.data.entities.MedicalRecord;
import org.unibl.etf.vetclinic.data.entities.relations.MedicalRecordWithAppointment;
import org.unibl.etf.vetclinic.repository.MedicalRecordRepository;

import java.util.List;

public class MedicalRecordViewModel extends AndroidViewModel {

    private final MedicalRecordRepository repository;

    public MedicalRecordViewModel(@NonNull Application application) {
        super(application);
        repository = new MedicalRecordRepository(application);
    }

    public LiveData<List<MedicalRecord>> getMedicalRecordsByPetId(int petId) {
        return repository.getMedicalRecordsByPetId(petId);
    }

    public LiveData<List<MedicalRecordWithAppointment>> getMedicalRecordsWithAppointmentByPetId(int petId) {
        return repository.getMedicalRecordsWithAppointmentByPetId(petId);
    }
}
