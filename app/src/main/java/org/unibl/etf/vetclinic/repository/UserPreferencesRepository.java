package org.unibl.etf.vetclinic.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import org.unibl.etf.vetclinic.data.dao.UserPreferencesDao;
import org.unibl.etf.vetclinic.data.database.AppDatabase;
import org.unibl.etf.vetclinic.data.entities.UserPreferences;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserPreferencesRepository {

    private final UserPreferencesDao userPreferencesDao;
    private final ExecutorService executorService;

    public UserPreferencesRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        userPreferencesDao = db.userPreferencesDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<UserPreferences> getPreferencesForUser(int userId) {
        return userPreferencesDao.getPreferencesForUser(userId);
    }

    public void insert(UserPreferences prefs) {
        executorService.execute(() -> userPreferencesDao.insert(prefs));
    }

    public void update(UserPreferences prefs) {
        executorService.execute(() -> userPreferencesDao.update(prefs));
    }

    public void delete(UserPreferences prefs) {
        executorService.execute(() -> userPreferencesDao.delete(prefs));
    }
}
