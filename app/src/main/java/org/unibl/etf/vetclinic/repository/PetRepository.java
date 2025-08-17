package org.unibl.etf.vetclinic.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import org.unibl.etf.vetclinic.data.dao.UserDao;
import org.unibl.etf.vetclinic.data.database.AppDatabase;
import org.unibl.etf.vetclinic.data.dao.PetDao;
import org.unibl.etf.vetclinic.data.entities.Pet;
import org.unibl.etf.vetclinic.data.entities.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PetRepository {
    private final PetDao petDao;
    private final LiveData<List<Pet>> allPets;
    private final ExecutorService executorService;
    private final Application application;

    public PetRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.application = application;
        petDao = db.petDao();
        allPets = petDao.getAllPets();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<Pet> getPetById(int id) {
        return petDao.getPetById(id);
    }

    public LiveData<List<Pet>> getAllPets() {
        return allPets;
    }

    public void insert(Pet pet) {
        executorService.execute(() -> petDao.insert(pet));
    }

    public void update(Pet pet) {
        executorService.execute(() -> petDao.update(pet));
    }

    public void delete(Pet pet) {
        executorService.execute(() -> petDao.delete(pet));
    }

    public LiveData<List<Pet>> getPetsByUserId(int userId) {
        return petDao.getPetsByUserId(userId);
    }
}

