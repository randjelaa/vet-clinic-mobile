package org.unibl.etf.vetclinic.data.entities.relations;

import androidx.room.Embedded;

import org.unibl.etf.vetclinic.data.entities.Payment;

public class PaymentWithAppointment {

    @Embedded
    public Payment payment;

    public AppointmentWithDetails appointment;

}

