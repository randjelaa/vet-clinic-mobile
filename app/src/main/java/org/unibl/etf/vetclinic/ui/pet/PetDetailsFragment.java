package org.unibl.etf.vetclinic.ui.pet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.vetclinic.R;
import org.unibl.etf.vetclinic.data.entities.Pet;
import org.unibl.etf.vetclinic.viewmodel.MedicalRecordViewModel;
import org.unibl.etf.vetclinic.viewmodel.PetViewModel;

import android.app.AlertDialog;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class PetDetailsFragment extends Fragment {

    private int petId;
    private PetViewModel petViewModel;
    private EditText editTextPetName, editTextSpecies, editTextBreed;
    private Button buttonSavePet;
    private MedicalRecordViewModel recordViewModel;
    private RecyclerView recyclerViewMedicalRecords;
    private MedicalRecordAdapter medicalRecordAdapter;
    private Button buttonDeletePet;
    private Pet currentPet;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pet_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextPetName = view.findViewById(R.id.editTextPetName);
        editTextSpecies = view.findViewById(R.id.editTextSpecies);
        editTextBreed = view.findViewById(R.id.editTextBreed);
        buttonSavePet = view.findViewById(R.id.buttonSavePet);
        recyclerViewMedicalRecords = view.findViewById(R.id.recyclerViewMedicalRecords);
        recyclerViewMedicalRecords.setLayoutManager(new LinearLayoutManager(requireContext()));
        medicalRecordAdapter = new MedicalRecordAdapter();
        recyclerViewMedicalRecords.setAdapter(medicalRecordAdapter);
        TextView textNoRecords = view.findViewById(R.id.textNoRecords);

        buttonDeletePet = view.findViewById(R.id.buttonDeletePet);

        petViewModel = new ViewModelProvider(this).get(PetViewModel.class);
        recordViewModel = new ViewModelProvider(this).get(MedicalRecordViewModel.class);

        if (getArguments() != null) {
            petId = getArguments().getInt("petId", -1);
            if (petId != -1) {
                petViewModel.getPetById(petId).observe(getViewLifecycleOwner(), pet -> {
                    if (pet != null && currentPet == null) {
                        currentPet = pet;
                        editTextPetName.setText(pet.Name);
                        editTextSpecies.setText(pet.Species != null ? pet.Species : "");
                        editTextBreed.setText(pet.Breed != null ? pet.Breed : "");
                    }
                });

                recordViewModel.getMedicalRecordsWithAppointmentByPetId(petId).observe(getViewLifecycleOwner(), records -> {
                    if (records != null && !records.isEmpty()) {
                        textNoRecords.setVisibility(View.GONE);
                        medicalRecordAdapter.setRecordsWithAppointments(records);
                    } else {
                        textNoRecords.setVisibility(View.VISIBLE);
                    }
                });

                buttonDeletePet.setOnClickListener(v -> showDeleteConfirmationDialog());
            } else {
                buttonDeletePet.setVisibility(View.GONE);
            }
        }

        buttonSavePet.setOnClickListener(v -> {
            String name = editTextPetName.getText().toString().trim();
            String species = editTextSpecies.getText().toString().trim();
            String breed = editTextBreed.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), R.string.error_pet_name_required, Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(requireContext())
                    .setTitle(R.string.save)
                    .setMessage(R.string.confirm_save_changes)
                    .setPositiveButton(R.string.save, (dialog, which) -> {
                        if (currentPet != null) {
                            currentPet.Name = name;
                            currentPet.Species = species.isEmpty() ? null : species;
                            currentPet.Breed = breed.isEmpty() ? null : breed;
                            petViewModel.update(currentPet);
                            Toast.makeText(requireContext(), R.string.success_pet_updated, Toast.LENGTH_SHORT).show();

                            requireActivity()
                                    .runOnUiThread(() -> {
                                        androidx.navigation.NavController navController =
                                                androidx.navigation.Navigation.findNavController(requireView());
                                        navController.popBackStack();
                                    });
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        });
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.delete_confirmation_title))
                .setMessage(getString(R.string.confirm_delete_pet))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> deletePet())
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }

    private void deletePet() {
        petViewModel.getPetById(petId).observe(getViewLifecycleOwner(), pet -> {
            if (pet != null) {
                petViewModel.delete(pet);
                Toast.makeText(requireContext(), getString(R.string.success_pet_deleted), Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
            } else {
                Toast.makeText(requireContext(), getString(R.string.error_pet_not_found), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
