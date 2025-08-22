package org.unibl.etf.vetclinic.repository;

import android.app.Application;

import org.unibl.etf.vetclinic.data.database.AppDatabase;
import org.unibl.etf.vetclinic.data.dao.AppointmentDao;
import org.unibl.etf.vetclinic.data.dao.PaymentDao;
import org.unibl.etf.vetclinic.data.entities.Appointment;
import org.unibl.etf.vetclinic.data.entities.Payment;
import org.unibl.etf.vetclinic.data.entities.relations.AppointmentWithDetails;
import org.unibl.etf.vetclinic.data.entities.relations.PaymentWithAppointment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class PaymentRepository {

    private final PaymentDao paymentDao;
    private final AppointmentDao appointmentDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public PaymentRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.paymentDao = db.paymentDao();
        this.appointmentDao = db.appointmentDao();
    }

    public void getPaymentsWithAppointmentsForUser(int userId, Consumer<List<PaymentWithAppointment>> callback) {
        executor.execute(() -> {
            List<Payment> payments = paymentDao.getPaymentsForUser(userId);
            List<PaymentWithAppointment> result = new ArrayList<>();

            for (Payment payment : payments) {
                AppointmentWithDetails details = null;
                if (payment.AppointmentID != null) {
                    details = appointmentDao.getDetailsById(payment.AppointmentID);
                }

                PaymentWithAppointment pwa = new PaymentWithAppointment();
                pwa.payment = payment;
                pwa.appointment = details;

                result.add(pwa);
            }

            callback.accept(result);
        });
    }



    public void insert(Payment payment) {
        executor.execute(() -> paymentDao.insert(payment));
    }

    public void update(Payment payment) {
        executor.execute(() -> paymentDao.update(payment));
    }

    public void delete(Payment payment) {
        executor.execute(() -> paymentDao.delete(payment));
    }
}
