package org.unibl.etf.vetclinic.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.unibl.etf.vetclinic.data.entities.UserPreferences;
import org.unibl.etf.vetclinic.repository.UserPreferencesRepository;

public class UserPreferencesViewModel extends AndroidViewModel {
    private final UserPreferencesRepository repository;

    public UserPreferencesViewModel(@NonNull Application application) {
        super(application);
        repository = new UserPreferencesRepository(application);
    }

    public void insert(UserPreferences prefs) {
        repository.insert(prefs);
    }

    public void update(UserPreferences prefs) {
        repository.update(prefs);
    }

    public void delete(UserPreferences prefs) {
        repository.delete(prefs);
    }

    public void getPreferencesForUser(int userId, UserPreferencesRepository.PreferencesCallback callback) {
        repository.getPreferencesForUser(userId, callback);
    }
}
