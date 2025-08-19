package org.unibl.etf.vetclinic.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.unibl.etf.vetclinic.data.entities.Pet;

import java.util.List;

@Dao
public interface PetDao {

    @Insert
    long insert(Pet pet);

    @Update
    void update(Pet pet);

    @Delete
    void delete(Pet pet);

    @Query("SELECT * FROM Pets WHERE OwnerID = :ownerId AND Deleted IS NULL ORDER BY Name ASC")
    LiveData<List<Pet>> getPetsByUserId(int ownerId);

    @Query("SELECT * FROM Pets WHERE ID = :id")
    LiveData<Pet> getPetById(int id);

    @Query("SELECT * FROM Pets WHERE ID = :id LIMIT 1")
    Pet getPetByIdSync(int id);
}

