package org.unibl.etf.vetclinic.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.unibl.etf.vetclinic.data.entities.relations.AppointmentWithDetails;
import org.unibl.etf.vetclinic.repository.AppointmentRepository;

import java.util.List;

public class AppointmentViewModel extends AndroidViewModel {
    private final AppointmentRepository repository;

    public AppointmentViewModel(@NonNull Application application) {
        super(application);
        repository = new AppointmentRepository(application);
    }

    public LiveData<List<AppointmentWithDetails>> getAppointmentsByUserId(int userId) {
        return repository.getAppointmentsByUserId(userId);
    }
}
