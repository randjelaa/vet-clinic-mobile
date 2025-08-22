package org.unibl.etf.vetclinic.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

import org.unibl.etf.vetclinic.R;
import org.unibl.etf.vetclinic.data.entities.User;
import org.unibl.etf.vetclinic.viewmodel.UserViewModel;

public class EditProfileFragment extends Fragment {

    private EditText editName, editEmail, editCurrentPassword, editNewPassword;
    private MaterialButton btnChangePassword, btnSave;
    private boolean showPasswordFields = false;

    private UserViewModel userViewModel;
    private SharedPreferences prefs;
    private int userId;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        editName = view.findViewById(R.id.editName);
        editEmail = view.findViewById(R.id.editEmail);
        editCurrentPassword = view.findViewById(R.id.editCurrentPassword);
        editNewPassword = view.findViewById(R.id.editNewPassword);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnSave = view.findViewById(R.id.btnSave);

        prefs = requireActivity().getSharedPreferences("VetClinicPrefs", Context.MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUserById(userId, user -> {
            if (user != null) {
                currentUser = user;

                requireActivity().runOnUiThread(() -> {
                    editName.setText(user.Name);
                    editEmail.setText(user.Email);
                });
            }
        });

        btnChangePassword.setOnClickListener(v -> {
            showPasswordFields = !showPasswordFields;
            editCurrentPassword.setVisibility(showPasswordFields ? View.VISIBLE : View.GONE);
            editNewPassword.setVisibility(showPasswordFields ? View.VISIBLE : View.GONE);
        });

        btnSave.setOnClickListener(v -> {
            if (currentUser == null) return;

            String name = editName.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String currentPass = editCurrentPassword.getText().toString();
            String newPass = editNewPassword.getText().toString();

            if (name.isEmpty()) {
                editName.setError("Name is required");
                return;
            }

            if (email.isEmpty()) {
                editEmail.setError("Email is required");
                return;
            }

            if (showPasswordFields) {
                if (currentPass.isEmpty() || newPass.isEmpty()) {
                    Toast.makeText(getContext(), "Enter both current and new password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!currentPass.equals(currentUser.Password)) {
                    Toast.makeText(getContext(), "Incorrect current password", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Potvrda prije spremanja
            new AlertDialog.Builder(requireContext())
                    .setTitle("Confirm Changes")
                    .setMessage("Are you sure you want to save these changes?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        currentUser.Name = name;
                        currentUser.Email = email;
                        if (showPasswordFields && !newPass.isEmpty())
                            currentUser.Password = newPass;

                        userViewModel.updateUser(currentUser,
                                () -> requireActivity().runOnUiThread(() -> {
                                    Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                                    requireActivity().getSupportFragmentManager().popBackStack();
                                }),
                                () -> requireActivity().runOnUiThread(() ->
                                        Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show()
                                )
                        );
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        return view;
    }
}


