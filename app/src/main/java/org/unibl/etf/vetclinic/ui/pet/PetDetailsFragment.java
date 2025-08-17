package org.unibl.etf.vetclinic.ui.pet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.vetclinic.R;
import org.unibl.etf.vetclinic.viewmodel.MedicalRecordViewModel;
import org.unibl.etf.vetclinic.viewmodel.PetViewModel;

import android.app.AlertDialog;
import android.widget.Button;
import android.widget.Toast;


public class PetDetailsFragment extends Fragment {

    private int petId;
    private PetViewModel petViewModel;
    private TextView textViewPetDetails;
    private MedicalRecordViewModel recordViewModel;
    private RecyclerView recyclerViewMedicalRecords;
    private MedicalRecordAdapter medicalRecordAdapter;
    private Button buttonDeletePet;

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

        textViewPetDetails = view.findViewById(R.id.textViewPetDetails);
        recyclerViewMedicalRecords = view.findViewById(R.id.recyclerViewMedicalRecords);
        recyclerViewMedicalRecords.setLayoutManager(new LinearLayoutManager(requireContext()));
        medicalRecordAdapter = new MedicalRecordAdapter();
        recyclerViewMedicalRecords.setAdapter(medicalRecordAdapter);

        buttonDeletePet = view.findViewById(R.id.buttonDeletePet);

        petViewModel = new ViewModelProvider(this).get(PetViewModel.class);
        recordViewModel = new ViewModelProvider(this).get(MedicalRecordViewModel.class);

        if (getArguments() != null) {
            petId = getArguments().getInt("petId", -1);
            if (petId != -1) {
                petViewModel.getPetById(petId).observe(getViewLifecycleOwner(), pet -> {
                    if (pet != null) {
                        String details = getString(R.string.pet_name) + ": " + pet.Name + "\n" +
                                getString(R.string.species) + ": " + (pet.Species != null ? pet.Species : getString(R.string.unknown)) + "\n" +
                                getString(R.string.breed) + ": " + (pet.Breed != null ? pet.Breed : getString(R.string.unknown));
                        textViewPetDetails.setText(details);
                    } else {
                        textViewPetDetails.setText(getString(R.string.not_found));
                    }
                });

                recordViewModel.getMedicalRecordsWithAppointmentByPetId(petId).observe(getViewLifecycleOwner(), records -> {
                    if (records != null && !records.isEmpty()) {
                        medicalRecordAdapter.setRecordsWithAppointments(records);
                    } else {
                        // Opcionalno: prikaži empty state poruku
                    }
                });

                buttonDeletePet.setOnClickListener(v -> showDeleteConfirmationDialog());
            } else {
                textViewPetDetails.setText(R.string.not_found);
                buttonDeletePet.setVisibility(View.GONE);
            }
        }
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.delete_confirmation_title))
                .setMessage(getString(R.string.delete_confirmation_message))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> deletePet())
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }

    private void deletePet() {
        petViewModel.getPetById(petId).observe(getViewLifecycleOwner(), pet -> {
            if (pet != null) {
                petViewModel.delete(pet);
                Toast.makeText(requireContext(), getString(R.string.pet_deleted), Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
            } else {
                Toast.makeText(requireContext(), getString(R.string.pet_delete_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
