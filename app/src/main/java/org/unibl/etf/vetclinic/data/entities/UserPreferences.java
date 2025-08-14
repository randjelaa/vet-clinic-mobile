package org.unibl.etf.vetclinic.data.entities;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "UserPreferences",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "ID",
                childColumns = "UserID",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("UserID")}
)
public class UserPreferences {

    @PrimaryKey(autoGenerate = true)
    public int ID;

    public int UserID;

    @Nullable
    public String Theme;

    @Nullable
    public String Language;
}

