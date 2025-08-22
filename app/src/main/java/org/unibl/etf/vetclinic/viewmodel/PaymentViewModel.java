package org.unibl.etf.vetclinic.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.unibl.etf.vetclinic.data.entities.Payment;
import org.unibl.etf.vetclinic.data.entities.relations.PaymentWithAppointment;
import org.unibl.etf.vetclinic.repository.PaymentRepository;

import java.util.List;
import java.util.function.Consumer;

public class PaymentViewModel extends AndroidViewModel {

    private final PaymentRepository repository;

    public PaymentViewModel(@NonNull Application application) {
        super(application);
        repository = new PaymentRepository(application);
    }

    public void getPaymentsWithAppointmentsForUser(int userId, Consumer<List<PaymentWithAppointment>> callback) {
        repository.getPaymentsWithAppointmentsForUser(userId, callback);
    }

    public void insert(Payment payment) {
        repository.insert(payment);
    }

    public void update(Payment payment) {
        repository.update(payment);
    }

    public void delete(Payment payment) {
        repository.delete(payment);
    }
}
