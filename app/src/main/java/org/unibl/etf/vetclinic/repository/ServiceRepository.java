package org.unibl.etf.vetclinic.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import org.unibl.etf.vetclinic.data.database.AppDatabase;
import org.unibl.etf.vetclinic.data.dao.ServiceDao;
import org.unibl.etf.vetclinic.data.entities.Service;

import java.util.List;

public class ServiceRepository {
    private final ServiceDao serviceDao;

    public ServiceRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        serviceDao = db.serviceDao();
    }

    public LiveData<List<Service>> getAllServices() {
        return serviceDao.getAllServices();
    }

    public Service getServiceByIdSync(int serviceId) {
        return serviceDao.getServiceByIdSync(serviceId);
    }
}
