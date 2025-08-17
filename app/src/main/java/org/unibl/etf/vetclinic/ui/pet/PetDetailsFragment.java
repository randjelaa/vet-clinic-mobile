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
        petViewModel = new ViewModelProvider(this).get(PetViewModel.class);

        if (getArguments() != null) {
            petId = getArguments().getInt("petId", -1);
            if (petId != -1) {
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
            }
        }

        textViewMedicalRecords = view.findViewById(R.id.textViewMedicalRecords);
        recordViewModel = new ViewModelProvider(this).get(MedicalRecordViewModel.class);

        recordViewModel.getMedicalRecordsByPetId(petId).observe(getViewLifecycleOwner(), records -> {
            if (records != null && !records.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (MedicalRecord r : records) {
                    sb.append("Dijagnoza: ").append(r.Diagnosis != null ? r.Diagnosis : "N/A").append("\n")
                            .append("Terapija: ").append(r.Treatment != null ? r.Treatment : "N/A").append("\n")
                            .append("Lijekovi: ").append(r.Medications != null ? r.Medications : "N/A").append("\n")
                            .append("Bilješke: ").append(r.Notes != null ? r.Notes : "N/A").append("\n\n");
                }
                textViewMedicalRecords.setText(sb.toString());
            } else {
                textViewMedicalRecords.setText("Nema medicinskih zapisa za ovog ljubimca.");
            }
        });
    }

}

