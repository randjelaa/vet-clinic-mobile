package org.unibl.etf.vetclinic.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import org.unibl.etf.vetclinic.data.dao.UserDao;
import org.unibl.etf.vetclinic.data.dao.UserPreferencesDao;
import org.unibl.etf.vetclinic.data.database.AppDatabase;
import org.unibl.etf.vetclinic.data.entities.User;
import org.unibl.etf.vetclinic.data.entities.UserPreferences;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import android.os.Handler;
import android.os.Looper;

public class UserRepository {

    private final UserDao userDao;

    private final UserPreferencesDao userPreferencesDao;
    private final ExecutorService executorService;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        userDao = db.userDao();
        userPreferencesDao = db.userPreferencesDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(User user, Runnable onSuccess, Runnable onFailure) {
        executorService.execute(() -> {
            try {
                User existing = userDao.getUserByEmail(user.Email);
                if (existing != null) {
                    onFailure.run();
                    return;
                }

                long userId = userDao.insert(user);

                UserPreferences prefs = new UserPreferences();
                prefs.UserID = (int) userId;
                prefs.Language = "English";
                prefs.Theme = "Light";

                userPreferencesDao.insert(prefs);

                onSuccess.run();
            } catch (Exception e) {
                onFailure.run();
            }
        });
    }

    public int insertUserAndPreferencesSync(User user) {
        try {
            User existing = userDao.getUserByEmail(user.Email);
            if (existing != null) {
                return -1;
            }

            long userId = userDao.insert(user);

            UserPreferences prefs = new UserPreferences();
            prefs.UserID = (int) userId;
            prefs.Language = "English";
            prefs.Theme = "Light";

            userPreferencesDao.insert(prefs);
            return (int) userId;
        } catch (Exception e) {
            return -1;
        }
    }


    public void login(String email, String password, Consumer<User> onResult) {
        executorService.execute(() -> {
            User user = userDao.login(email, password);
            onResult.accept(user);
        });
    }

    public LiveData<List<User>> getAllVets() {
        return userDao.getAllVets();
    }

    public LiveData<List<User>> getAllClients() {
        return userDao.getAllClients();
    }

    public void getUserByIdAsync(int id, Consumer<User> callback) {
        executorService.execute(() -> {
            User user = userDao.getUserById(id);
            new Handler(Looper.getMainLooper()).post(() -> callback.accept(user));
        });
    }

    public void update(User user, Runnable onSuccess, Runnable onFailure) {
        executorService.execute(() -> {
            try {
                userDao.update(user);
                onSuccess.run();
            } catch (Exception e) {
                onFailure.run();
            }
        });
    }
}
