package org.unibl.etf.vetclinic.data.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import org.unibl.etf.vetclinic.data.entities.Appointment;
import org.unibl.etf.vetclinic.data.entities.Payment;

public class PaymentWithAppointment {

    @Embedded
    public Payment payment;

    public AppointmentWithDetails appointment;
}

