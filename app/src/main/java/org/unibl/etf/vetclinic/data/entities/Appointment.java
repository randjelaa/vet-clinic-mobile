package org.unibl.etf.vetclinic.data.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(
        tableName = "Appointments",
        foreignKeys = {
                @ForeignKey(
                        entity = Pet.class,
                        parentColumns = "ID",
                        childColumns = "PetID",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "ID",
                        childColumns = "VetID"
                ),
                @ForeignKey(
                        entity = Service.class,
                        parentColumns = "ID",
                        childColumns = "ServiceID"
                )
        },
        indices = {@Index("PetID"), @Index("VetID"), @Index("ServiceID")}
)
public class Appointment {

    @PrimaryKey(autoGenerate = true)
    public int ID;

    @NonNull
    public Date Date;

    @Nullable
    public Date Deleted;

    public int PetID;

    public int VetID;

    public int ServiceID;
}

