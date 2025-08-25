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
                editName.setError(getString(R.string.error_name_required));
                return;
            }

            if (email.isEmpty()) {
                editEmail.setError(getString(R.string.error_email_required));
                return;
            }

            if (showPasswordFields) {
                if (currentPass.isEmpty() || newPass.isEmpty()) {
                    Toast.makeText(getContext(), getString(R.string.error_enter_passwords), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!currentPass.equals(currentUser.Password)) {
                    Toast.makeText(getContext(), getString(R.string.error_incorrect_password), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            new AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.save_confirmation_title))
                    .setMessage(getString(R.string.confirm_save_changes))
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        currentUser.Name = name;
                        currentUser.Email = email;
                        if (showPasswordFields && !newPass.isEmpty())
                            currentUser.Password = newPass;

                        userViewModel.updateUser(currentUser,
                                () -> requireActivity().runOnUiThread(() -> {
                                    Toast.makeText(getContext(), getString(R.string.success_profile_updated), Toast.LENGTH_SHORT).show();
                                    requireActivity().getSupportFragmentManager().popBackStack();
                                }),
                                () -> requireActivity().runOnUiThread(() ->
                                        Toast.makeText(getContext(), getString(R.string.error_profile_update), Toast.LENGTH_SHORT).show()
                                )
                        );
                    })
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show();
        });

        return view;
    }
}


