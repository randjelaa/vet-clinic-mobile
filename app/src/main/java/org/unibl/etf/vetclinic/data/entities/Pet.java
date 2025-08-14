package org.unibl.etf.vetclinic.data.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(
        tableName = "Pets",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "ID",
                childColumns = "OwnerID",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("OwnerID")}
)
public class Pet {

    @PrimaryKey(autoGenerate = true)
    public int ID;

    @NonNull
    public String Name;

    @Nullable
    public String Species;

    @Nullable
    public String Breed;

    public int OwnerID;

    @Nullable
    public Date Deleted;
}

