package org.unibl.etf.vetclinic.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.unibl.etf.vetclinic.data.entities.Service;
import org.unibl.etf.vetclinic.repository.ServiceRepository;

import java.util.List;

public class ServiceViewModel extends AndroidViewModel {
    private final ServiceRepository repository;

    public ServiceViewModel(@NonNull Application application) {
        super(application);
        repository = new ServiceRepository(application);
    }

    public LiveData<List<Service>> getAllServices() {
        return repository.getAllServices();
    }
}
