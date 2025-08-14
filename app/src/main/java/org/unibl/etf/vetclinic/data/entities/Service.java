package org.unibl.etf.vetclinic.data.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Services")
public class Service {

    @PrimaryKey(autoGenerate = true)
    public int ID;

    @NonNull
    public String Name;

    @Nullable
    public String Description;

    public double Price;

    @Nullable
    public Integer DurationMinutes;

    @Nullable
    public Date Deleted;
}

