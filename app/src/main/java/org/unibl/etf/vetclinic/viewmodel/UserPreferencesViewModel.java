package org.unibl.etf.vetclinic.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.unibl.etf.vetclinic.data.entities.UserPreferences;
import org.unibl.etf.vetclinic.repository.UserPreferencesRepository;

public class UserPreferencesViewModel extends AndroidViewModel {

    private final UserPreferencesRepository repository;

    public UserPreferencesViewModel(@NonNull Application application) {
        super(application);
        repository = new UserPreferencesRepository(application);
    }

    public LiveData<UserPreferences> getPreferencesForUser(int userId) {
        return repository.getPreferencesForUser(userId);
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
}
