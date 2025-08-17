package org.unibl.etf.vetclinic.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.unibl.etf.vetclinic.data.entities.Pet;
import org.unibl.etf.vetclinic.repository.PetRepository;

import java.util.List;

public class PetViewModel extends AndroidViewModel {
    private final PetRepository repository;
    private final LiveData<List<Pet>> allPets;

    public PetViewModel(@NonNull Application application) {
        super(application);
        repository = new PetRepository(application);
        allPets = repository.getAllPets();
    }

    public LiveData<Pet> getPetById(int id) {
        return repository.getPetById(id);
    }

    public LiveData<List<Pet>> getAllPets() {
        return allPets;
    }

    public LiveData<List<Pet>> getPetsByUserId(int userId) {
        return repository.getPetsByUserId(userId);
    }

    public void insert(Pet pet) {
        repository.insert(pet);
    }

    public void update(Pet pet) {
        repository.update(pet);
    }

    public void delete(Pet pet) {
        repository.delete(pet);
    }
}
