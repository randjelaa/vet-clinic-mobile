package org.unibl.etf.vetclinic.repository;

import android.app.Application;
import android.util.Log;

import org.unibl.etf.vetclinic.data.dao.AppointmentDao;
import org.unibl.etf.vetclinic.data.dao.PaymentDao;
import org.unibl.etf.vetclinic.data.dao.PetDao;
import org.unibl.etf.vetclinic.data.dao.UnpaidServiceDao;
import org.unibl.etf.vetclinic.data.database.AppDatabase;
import org.unibl.etf.vetclinic.data.entities.Appointment;
import org.unibl.etf.vetclinic.data.entities.Payment;
import org.unibl.etf.vetclinic.data.entities.Pet;
import org.unibl.etf.vetclinic.data.entities.Service;
import org.unibl.etf.vetclinic.data.entities.UnpaidService;
import org.unibl.etf.vetclinic.data.entities.relations.AppointmentWithDetails;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppointmentRepository {

    Application app;
    private final AppointmentDao appointmentDao;
    private final UnpaidServiceDao unpaidServiceDao;
    private final PaymentDao paymentDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public AppointmentRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        appointmentDao = db.appointmentDao();
        unpaidServiceDao = db.unpaidServiceDao();
        paymentDao = db.paymentDao();
        app = application;
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
        executor.execute(() -> {
            long insertedId = appointmentDao.insert(appointment);
            if (insertedId != -1) {
                PetRepository petRepository = new PetRepository(app);
                Pet pet = petRepository.getPetByIdSync(appointment.PetID);
                if (pet != null) {
                    ServiceRepository serviceRepository = new ServiceRepository(app);
                    Service service = serviceRepository.getServiceByIdSync(appointment.ServiceID);
                    if (service != null) {
                        UnpaidService unpaidService = new UnpaidService();
                        unpaidService.AppointmentID = (int) insertedId;
                        unpaidService.UserID = pet.OwnerID;
                        unpaidService.Amount = service.Price;
                        unpaidService.status = UnpaidService.Status.pending;

                        unpaidServiceDao.insert(unpaidService);
                        Log.d("Repo", "UnpaidService created with amount " + service.Price + " for appointment ID: " + insertedId);
                    } else {
                        Log.e("Repo", "Service not found for ServiceID: " + appointment.ServiceID);
                    }
                } else {
                    Log.e("Repo", "Pet not found for PetID: " + appointment.PetID);
                }
            } else {
                Log.e("Repo", "Failed to insert appointment");
            }
        });
    }


}
