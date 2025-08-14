package org.unibl.etf.vetclinic.data.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(
        tableName = "Users",
        foreignKeys = @ForeignKey(
                entity = Role.class,
                parentColumns = "ID",
                childColumns = "RoleID",
                onDelete = ForeignKey.NO_ACTION
        ),
        indices = {@Index("RoleID"), @Index(value = {"Email"}, unique = true)}
)
public class User {

    @PrimaryKey(autoGenerate = true)
    public int ID;

    @NonNull
    public String Name;

    @NonNull
    public String Email;

    @NonNull
    public String Password;

    @Nullable
    public Date Deleted;

    @Nullable
    public Integer RoleID;
}

