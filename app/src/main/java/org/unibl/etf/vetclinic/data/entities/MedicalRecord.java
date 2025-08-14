package org.unibl.etf.vetclinic.data.entities;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "MedicalRecords",
        foreignKeys = @ForeignKey(
                entity = Appointment.class,
                parentColumns = "ID",
                childColumns = "AppointmentID",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("AppointmentID")}
)
public class MedicalRecord {

    @PrimaryKey(autoGenerate = true)
    public int ID;

    public int AppointmentID;

    @Nullable
    public String Diagnosis;

    @Nullable
    public String Treatment;

    @Nullable
    public String Medications;

    @Nullable
    public String Notes;
}

