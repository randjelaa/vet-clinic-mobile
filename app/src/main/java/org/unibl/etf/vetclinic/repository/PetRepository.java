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

    public void insertTestPets() {
        executorService.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(application);

            UserDao userDao = db.userDao();

            // Provjeri da li već postoji neki korisnik
            List<User> users = userDao.getAll(); // Napravi getAll() ako ne postoji

            int userId;

            if (users == null || users.isEmpty()) {
                // Napravi testnog korisnika
                User user = new User();
                user.Name = "Testni Korisnik";
                user.Email = "test@example.com";
                user.Password = "password123";
                user.RoleID = null;

                userId = (int) userDao.insert(user);
            } else {
                userId = users.get(0).ID;
            }

            // Sada možemo ubaciti ljubimce sa validnim OwnerID
            Pet pet1 = new Pet();
            pet1.Name = "Maza";
            pet1.Species = "Pas";
            pet1.Breed = "Labrador";
            pet1.OwnerID = userId;

            Pet pet2 = new Pet();
            pet2.Name = "Cica";
            pet2.Species = "Mačka";
            pet2.Breed = "Persijska";
            pet2.OwnerID = userId;

            petDao.insert(pet1);
            petDao.insert(pet2);
        });
    }


}

