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
import org.unibl.etf.vetclinic.data.entities.MedicalRecord;
import org.unibl.etf.vetclinic.viewmodel.MedicalRecordViewModel;
import org.unibl.etf.vetclinic.viewmodel.PetViewModel;

public class PetDetailsFragment extends Fragment {

    private int petId;

    private PetViewModel petViewModel;
    private TextView textViewPetDetails;

    private MedicalRecordViewModel recordViewModel;
    private TextView textViewMedicalRecords;

    private RecyclerView recyclerViewMedicalRecords;
    private MedicalRecordAdapter medicalRecordAdapter;



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

        petViewModel = new ViewModelProvider(this).get(PetViewModel.class);
        recordViewModel = new ViewModelProvider(this).get(MedicalRecordViewModel.class); // <--- MORA biti ovde!

        if (getArguments() != null) {
            petId = getArguments().getInt("petId", -1);
            if (petId != -1) {
                // PET DETALJI
                petViewModel.getPetById(petId).observe(getViewLifecycleOwner(), pet -> {
                    if (pet != null) {
                        String details = "Ime: " + pet.Name + "\n" +
                                "Vrsta: " + (pet.Species != null ? pet.Species : "Nepoznata") + "\n" +
                                "Rasa: " + (pet.Breed != null ? pet.Breed : "Nepoznata");
                        textViewPetDetails.setText(details);
                    } else {
                        textViewPetDetails.setText("Ljubimac nije pronađen.");
                    }
                });

                // MEDICINSKI ZAPISI
                recordViewModel.getMedicalRecordsWithAppointmentByPetId(petId).observe(getViewLifecycleOwner(), records -> {
                    if (records != null && !records.isEmpty()) {
                        medicalRecordAdapter.setRecordsWithAppointments(records);
                    } else {
                        // Opcionalno: prikaži empty state poruku
                    }
                });
            } else {
                textViewPetDetails.setText("Nepoznat ID ljubimca.");
            }
        }
    }


}

