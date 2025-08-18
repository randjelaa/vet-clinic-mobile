package org.unibl.etf.vetclinic.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import org.unibl.etf.vetclinic.data.dao.UserDao;
import org.unibl.etf.vetclinic.data.database.AppDatabase;
import org.unibl.etf.vetclinic.data.entities.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class UserRepository {

    private final UserDao userDao;
    private final ExecutorService executorService;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        userDao = db.userDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(User user, Runnable onSuccess, Runnable onFailure) {
        executorService.execute(() -> {
            try {
                User existing = userDao.getUserByEmail(user.Email);
                if (existing != null) {
                    onFailure.run(); // Email already exists
                    return;
                }
                userDao.insert(user);
                onSuccess.run();
            } catch (Exception e) {
                onFailure.run(); // Any other error
            }
        });
    }

    public void login(String email, String password, Consumer<User> onResult) {
        executorService.execute(() -> {
            User user = userDao.login(email, password);
            onResult.accept(user); // može biti null
        });
    }

    public LiveData<List<User>> getAllVets() {
        return userDao.getAllVets(); // Pretpostavka: samo veterinari
    }
}
