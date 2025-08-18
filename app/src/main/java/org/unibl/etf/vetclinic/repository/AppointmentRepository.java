package org.unibl.etf.vetclinic.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import org.unibl.etf.vetclinic.data.database.AppDatabase;
import org.unibl.etf.vetclinic.data.dao.AppointmentDao;
import org.unibl.etf.vetclinic.data.entities.relations.AppointmentWithDetails;

import java.util.List;

public class AppointmentRepository {
    private final AppointmentDao appointmentDao;

    public AppointmentRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        appointmentDao = db.appointmentDao();
    }

    public LiveData<List<AppointmentWithDetails>> getAppointmentsByUserId(int userId) {
        return appointmentDao.getAppointmentsByUserId(userId);
    }
}
