package org.unibl.etf.vetclinic.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Roles")
public class Role {

    @PrimaryKey(autoGenerate = true)
    public int ID;

    @NonNull
    public String Name;
}
