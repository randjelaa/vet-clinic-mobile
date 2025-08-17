package org.unibl.etf.vetclinic.ui.pet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.unibl.etf.vetclinic.R;
import org.unibl.etf.vetclinic.data.entities.Pet;
import org.unibl.etf.vetclinic.viewmodel.PetViewModel;

public class AddPetFragment extends Fragment {

    private EditText nameEditText, speciesEditText, breedEditText;
    private Button submitButton;
    private PetViewModel petViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_pet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameEditText = view.findViewById(R.id.editTextPetName);
        speciesEditText = view.findViewById(R.id.editTextPetSpecies);
        breedEditText = view.findViewById(R.id.editTextPetBreed);
        submitButton = view.findViewById(R.id.buttonAddPet);

        petViewModel = new ViewModelProvider(this).get(PetViewModel.class);

        submitButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String species = speciesEditText.getText().toString().trim();
            String breed = breedEditText.getText().toString().trim();

            // Provjera imena (obavezno)
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(getContext(), "Unesite ime ljubimca", Toast.LENGTH_SHORT).show();
                return;
            }
            if (name.length() < 2) {
                Toast.makeText(getContext(), "Ime ljubimca mora imati najmanje 2 karaktera", Toast.LENGTH_SHORT).show();
                return;
            }

            // Opcione dodatne provjere za vrstu i rasu (možeš ih ukloniti ako nisu obavezne)
            if (!TextUtils.isEmpty(species) && species.length() < 2) {
                Toast.makeText(getContext(), "Vrsta mora imati najmanje 2 karaktera ili ostavite prazno", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!TextUtils.isEmpty(breed) && breed.length() < 2) {
                Toast.makeText(getContext(), "Rasa mora imati najmanje 2 karaktera ili ostavite prazno", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = requireActivity().getSharedPreferences("VetClinicPrefs", Context.MODE_PRIVATE);
            int userId = prefs.getInt("userId", -1);
            if (userId == -1) {
                Toast.makeText(getContext(), "Greška: Korisnik nije prijavljen", Toast.LENGTH_SHORT).show();
                return;
            }

            Pet pet = new Pet();
            pet.Name = name;
            pet.Species = species;
            pet.Breed = breed;
            pet.OwnerID = userId;

            petViewModel.insert(pet);
            Toast.makeText(getContext(), "Ljubimac dodat", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).navigateUp();
        });
    }
}
