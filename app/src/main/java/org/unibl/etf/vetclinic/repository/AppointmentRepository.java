package org.unibl.etf.vetclinic.repository;

import android.app.Application;

import org.unibl.etf.vetclinic.data.dao.AppointmentDao;
import org.unibl.etf.vetclinic.data.dao.PaymentDao;
import org.unibl.etf.vetclinic.data.dao.UnpaidServiceDao;
import org.unibl.etf.vetclinic.data.database.AppDatabase;
import org.unibl.etf.vetclinic.data.entities.Appointment;
import org.unibl.etf.vetclinic.data.entities.Payment;
import org.unibl.etf.vetclinic.data.entities.UnpaidService;
import org.unibl.etf.vetclinic.data.entities.relations.AppointmentWithDetails;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppointmentRepository {

    private final AppointmentDao appointmentDao;
    private final UnpaidServiceDao unpaidServiceDao;
    private final PaymentDao paymentDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public AppointmentRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        appointmentDao = db.appointmentDao();
        unpaidServiceDao = db.unpaidServiceDao();
        paymentDao = db.paymentDao();
    }

    public LiveData<List<AppointmentWithDetails>> getAppointmentsByUserId(int userId) {
        return appointmentDao.getAppointmentsByUserId(userId);
    }

    public void cancelAppointment(int appointmentId) {
        executor.execute(() -> {
            Appointment appointment = appointmentDao.getById(appointmentId);
            if (appointment != null && appointment.Deleted == null) {
                appointment.Deleted = new Date();
                appointmentDao.update(appointment);
            }
        });
    }

    public void markAppointmentAsPaid(int appointmentId) {
        executor.execute(() -> {
            UnpaidService unpaid = unpaidServiceDao.getByAppointmentId(appointmentId);
            if (unpaid != null) {
                Payment payment = new Payment();
                payment.AppointmentID = appointmentId;
                payment.UserID = unpaid.UserID;
                payment.Amount = unpaid.Amount;
                payment.Date = new Date();
                paymentDao.insert(payment);

                unpaidServiceDao.delete(unpaid);
            }
        });
    }

    public void insert(Appointment appointment) {
        executor.execute(() -> appointmentDao.insert(appointment));
    }
}
