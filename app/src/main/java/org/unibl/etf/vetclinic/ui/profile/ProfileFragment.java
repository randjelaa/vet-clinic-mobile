package org.unibl.etf.vetclinic.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;

import org.unibl.etf.vetclinic.MainActivity;
import org.unibl.etf.vetclinic.R;
import org.unibl.etf.vetclinic.WelcomeActivity;
import org.unibl.etf.vetclinic.viewmodel.UserPreferencesViewModel;
import org.unibl.etf.vetclinic.viewmodel.UserViewModel;

public class ProfileFragment extends Fragment {

    private TextView textUserName, textUserEmail;
    private Spinner spinnerLanguage;
    private SwitchCompat switchTheme;
    private MaterialButton btnEditProfile, btnPayments, btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        textUserName = view.findViewById(R.id.textUserName);
        textUserEmail = view.findViewById(R.id.textUserEmail);
        spinnerLanguage = view.findViewById(R.id.spinnerLanguage);
        switchTheme = view.findViewById(R.id.switchTheme);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnPayments = view.findViewById(R.id.btnPayments);
        btnLogout = view.findViewById(R.id.btnLogout);

        SharedPreferences prefs = requireActivity().getSharedPreferences("VetClinicPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUserById(userId, user -> {
            if (user != null) {
                requireActivity().runOnUiThread(() -> {
                    textUserName.setText(user.Name);
                    textUserEmail.setText(user.Email);
                });
            }
        });

        // === Postavi adapter za jezike ===
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.language_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter);

        // === ViewModel za preferences ===
        UserPreferencesViewModel prefsViewModel = new ViewModelProvider(requireActivity()).get(UserPreferencesViewModel.class);

        // === Ucitaj user preferences iz baze ===
        prefsViewModel.getPreferencesForUser(userId).observe(getViewLifecycleOwner(), userPrefs -> {
            if (userPrefs != null) {
                // Postavi jezik
                String[] languageCodes = getResources().getStringArray(R.array.language_codes);
                String savedLangCode = userPrefs.Language != null ? userPrefs.Language : "en";

// Pronađi poziciju jezika na osnovu koda (npr. "sr")
                int langPos = 0;
                for (int i = 0; i < languageCodes.length; i++) {
                    if (languageCodes[i].equals(savedLangCode)) {
                        langPos = i;
                        break;
                    }
                }

                spinnerLanguage.setSelection(langPos);


                // Postavi temu
                boolean isDark = "Dark".equalsIgnoreCase(userPrefs.Theme);
                switchTheme.setChecked(isDark);

                // === Promjena jezika ===
                spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String[] languageCodes = getResources().getStringArray(R.array.language_codes);
                        String selectedLanguage = languageCodes[position];
                        if (!selectedLanguage.equals(userPrefs.Language)) {
                            userPrefs.Language = selectedLanguage;
                            prefsViewModel.update(userPrefs);
                            prefs.edit().putString("language", selectedLanguage).apply();
                            restartApp(); // promjena jezika zahtijeva restart
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) { }
                });

                // === Promjena teme ===
                switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    String newTheme = isChecked ? "Dark" : "Light";
                    if (!newTheme.equalsIgnoreCase(userPrefs.Theme)) {
                        userPrefs.Theme = newTheme;
                        prefsViewModel.update(userPrefs);
                        prefs.edit().putString("theme", newTheme).putBoolean("darkTheme", isChecked).apply();
                        restartApp(); // restart da bi se tema primijenila
                    }
                });
            }
        });

        btnEditProfile.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_profileFragment_to_editProfileFragment);
        });


        btnPayments.setOnClickListener(v ->
                Toast.makeText(getContext(), "Open payments screen", Toast.LENGTH_SHORT).show()
        );

        btnLogout.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            Intent intent = new Intent(getActivity(), WelcomeActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }

    private void restartApp() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}
