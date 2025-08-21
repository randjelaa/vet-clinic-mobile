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
import android.util.Log;


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
                    onFailure.run(); // Email already exists
                    return;
                }

                long userId = userDao.insert(user); // dobavi ID novog usera

                // Kreiraj default preferences
                UserPreferences prefs = new UserPreferences();
                prefs.UserID = (int) userId;
                prefs.Language = "English";
                prefs.Theme = "Light";

                userPreferencesDao.insert(prefs);

                // ✅ LOGOVANJE:
                Log.d("UserRepository", "UserPreferences saved: " +
                        "UserID = " + prefs.UserID +
                        ", Language = " + prefs.Language +
                        ", Theme = " + prefs.Theme);

                onSuccess.run();
            } catch (Exception e) {
                Log.e("UserRepository", "Error while inserting user or preferences", e);
                onFailure.run(); // Any other error
            }
        });
    }

    public int insertUserAndPreferencesSync(User user) {
        try {
            User existing = userDao.getUserByEmail(user.Email);
            if (existing != null) {
                return -1; // email already exists
            }

            long userId = userDao.insert(user);

            UserPreferences prefs = new UserPreferences();
            prefs.UserID = (int) userId;
            prefs.Language = "English";
            prefs.Theme = "Light";

            userPreferencesDao.insert(prefs);

            Log.d("UserRepository", "UserPreferences saved for userId = " + prefs.UserID);

            return (int) userId;
        } catch (Exception e) {
            Log.e("UserRepository", "Failed to insert user and preferences", e);
            return -1;
        }
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
