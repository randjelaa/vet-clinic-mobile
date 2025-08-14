package org.unibl.etf.vetclinic.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.unibl.etf.vetclinic.data.entities.User;
import org.unibl.etf.vetclinic.repository.UserRepository;
import java.util.function.Consumer;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository repository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public void register(User user, Runnable onSuccess, Runnable onFailure) {
        repository.insert(user, onSuccess, onFailure);
    }

    public void login(String email, String password, Consumer<User> onResult) {
        repository.login(email, password, onResult);
    }
}
