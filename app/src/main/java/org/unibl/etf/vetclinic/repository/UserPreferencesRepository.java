package org.unibl.etf.vetclinic.repository;

import android.app.Application;

import org.unibl.etf.vetclinic.data.database.AppDatabase;
import org.unibl.etf.vetclinic.data.dao.UserPreferencesDao;
import org.unibl.etf.vetclinic.data.entities.UserPreferences;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserPreferencesRepository {
    private final UserPreferencesDao dao;
    private final ExecutorService executorService;

    public UserPreferencesRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        dao = db.userPreferencesDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(UserPreferences prefs) {
        executorService.execute(() -> dao.insert(prefs));
    }

    public void update(UserPreferences prefs) {
        executorService.execute(() -> dao.update(prefs));
    }

    public void delete(UserPreferences prefs) {
        executorService.execute(() -> dao.delete(prefs));
    }

    public interface PreferencesCallback {
        void onResult(UserPreferences prefs);
    }

    public void getPreferencesForUser(int userId, PreferencesCallback callback) {
        executorService.execute(() -> {
            UserPreferences prefs = dao.getPreferencesForUser(userId);
            callback.onResult(prefs);
        });
    }
}
