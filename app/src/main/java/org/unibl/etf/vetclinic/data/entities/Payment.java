package org.unibl.etf.vetclinic.data.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(
        tableName = "Payments",
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
public class Payment {

    @PrimaryKey(autoGenerate = true)
    public int ID;

    public double Amount;

    @NonNull
    public Date Date;

    public int UserID;

    @Nullable
    public Integer AppointmentID;
}

