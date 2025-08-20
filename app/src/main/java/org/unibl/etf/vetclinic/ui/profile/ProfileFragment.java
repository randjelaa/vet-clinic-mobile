package org.unibl.etf.vetclinic.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import org.unibl.etf.vetclinic.R;
import org.unibl.etf.vetclinic.WelcomeActivity;

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
        String name = prefs.getString("userName", "Unknown User");
        String email = prefs.getString("userEmail", "unknown@email.com");
        boolean darkTheme = prefs.getBoolean("darkTheme", false);

        textUserName.setText(name);
        textUserEmail.setText(email);
        switchTheme.setChecked(darkTheme);

        btnEditProfile.setOnClickListener(v ->
                        Toast.makeText(getContext(), "Edit profile clicked", Toast.LENGTH_SHORT).show()
                // TODO: open edit profile fragment/dialog
        );

        btnPayments.setOnClickListener(v ->
                        Toast.makeText(getContext(), "Open payments screen", Toast.LENGTH_SHORT).show()
                // TODO: navigate to PaymentsFragment
        );

        btnLogout.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            Intent intent = new Intent(getActivity(), WelcomeActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("darkTheme", isChecked).apply();
            Toast.makeText(getContext(), "Theme changed (apply restart)", Toast.LENGTH_SHORT).show();
            // TODO: apply theme switch properly
        });

        // TODO: postavi adapter za jezike i sacuvaj izbor u SharedPreferences

        return view;
    }
}
