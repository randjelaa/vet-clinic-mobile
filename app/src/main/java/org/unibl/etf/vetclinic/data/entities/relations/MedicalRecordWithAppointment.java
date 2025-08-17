package org.unibl.etf.vetclinic.data.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import org.unibl.etf.vetclinic.data.entities.Appointment;
import org.unibl.etf.vetclinic.data.entities.MedicalRecord;

public class MedicalRecordWithAppointment {
    @Embedded
    public MedicalRecord record;

    @Relation(
            parentColumn = "AppointmentID",
            entityColumn = "ID"
    )
    public Appointment appointment;
}
