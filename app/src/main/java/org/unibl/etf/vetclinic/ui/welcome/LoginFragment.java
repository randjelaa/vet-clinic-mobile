package org.unibl.etf.vetclinic.ui.welcome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.unibl.etf.vetclinic.MainActivity;
import org.unibl.etf.vetclinic.R;
import org.unibl.etf.vetclinic.viewmodel.UserPreferencesViewModel;
import org.unibl.etf.vetclinic.viewmodel.UserViewModel;

public class LoginFragment extends Fragment {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ImageButton backButton;
    private TextView registerLink;

    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailEditText = view.findViewById(R.id.editTextEmail);
        passwordEditText = view.findViewById(R.id.editTextPassword);
        loginButton = view.findViewById(R.id.buttonLoginSubmit);
        backButton = view.findViewById(R.id.buttonBack);
        registerLink = view.findViewById(R.id.textViewRegisterLink);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), getString(R.string.error_fields_required), Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show();
                return;
            }

            userViewModel.login(email, password, user -> requireActivity().runOnUiThread(() -> {
                if (user != null) {
                    UserPreferencesViewModel preferencesViewModel = new ViewModelProvider(requireActivity()).get(UserPreferencesViewModel.class);

                    preferencesViewModel.getPreferencesForUser(user.ID).observe(getViewLifecycleOwner(), prefs -> {
                        if (prefs != null) {
                            SharedPreferences sharedPrefs = requireActivity().getSharedPreferences("VetClinicPrefs", Context.MODE_PRIVATE);
                            sharedPrefs.edit()
                                    .putBoolean("isLoggedIn", true)
                                    .putInt("userId", user.ID)
                                    .putString("language", prefs.Language)
                                    .putString("theme", prefs.Theme)
                                    .apply();

                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            requireActivity().finish();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), getString(R.string.error_invalid_credentials), Toast.LENGTH_SHORT).show();
                }
            }));

        });

        backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp()
        );

        registerLink.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_RegisterFragment)
        );
    }
}
