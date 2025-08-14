package org.unibl.etf.vetclinic.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.unibl.etf.vetclinic.R;
import org.unibl.etf.vetclinic.data.entities.User;
import org.unibl.etf.vetclinic.viewmodel.UserViewModel;

public class RegisterFragment extends Fragment {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private Button registerButton;

    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameEditText = view.findViewById(R.id.editTextUsername);
        emailEditText = view.findViewById(R.id.editTextEmail);
        passwordEditText = view.findViewById(R.id.editTextPassword);
        registerButton = view.findViewById(R.id.buttonRegisterSubmit);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        registerButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = new User();
            user.Name = name;
            user.Email = email;
            user.Password = password;

            userViewModel.register(user,
                    () -> requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this).navigate(R.id.action_registerFragment_to_loginFragment);
                    }),
                    () -> requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Email already exists or error occurred", Toast.LENGTH_SHORT).show()
                    )
            );
        });
    }
}
