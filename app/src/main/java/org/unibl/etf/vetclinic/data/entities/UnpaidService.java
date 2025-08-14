package org.unibl.etf.vetclinic.data.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "UnpaidServices",
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "ID",
                        childColumns = "UserID"
                ),
                @ForeignKey(
                        entity = Appointment.class,
                        parentColumns = "ID",
                        childColumns = "AppointmentID"
                )
        },
        indices = {@Index("UserID"), @Index("AppointmentID")}
)
public class UnpaidService {

    public enum Status {
        pending,
        overdue,
        partial
    }

    @PrimaryKey(autoGenerate = true)
    public int ID;

    public int UserID;

    @Nullable
    public Integer AppointmentID;

    public double Amount;

    @NonNull
    public Status status;

    public UnpaidService() {
        this.status = Status.pending;
    }
}
