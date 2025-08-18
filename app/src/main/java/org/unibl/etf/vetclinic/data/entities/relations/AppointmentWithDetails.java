package org.unibl.etf.vetclinic.data.entities.relations;

import androidx.annotation.NonNull;
import java.util.Date;

public class AppointmentWithDetails {
    public int ID;
    @NonNull
    public Date Date;
    @NonNull
    public String PetName;
    @NonNull
    public String ServiceName;
    public double Price;
}

