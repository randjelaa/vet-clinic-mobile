package org.unibl.etf.vetclinic.ui.pet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.unibl.etf.vetclinic.R;
import org.unibl.etf.vetclinic.data.entities.Pet;
import org.unibl.etf.vetclinic.viewmodel.PetViewModel;

import java.util.List;

public class PetListFragment extends Fragment {
    private PetViewModel petViewModel;
    private PetListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPets);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PetListAdapter(pet -> {
            Bundle bundle = new Bundle();
            bundle.putInt("petId", pet.ID);
            NavHostFragment.findNavController(this).navigate(R.id.action_petListFragment_to_petDetailsFragment, bundle);
        });
        recyclerView.setAdapter(adapter);

        petViewModel = new ViewModelProvider(this).get(PetViewModel.class);

        SharedPreferences prefs = requireActivity().getSharedPreferences("VetClinicPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId != -1) {
            petViewModel.getPetsByUserId(userId).observe(getViewLifecycleOwner(), (List<Pet> pets) -> {
                adapter.submitList(pets);
            });
        }

        FloatingActionButton fab = view.findViewById(R.id.fabAddPet);
        fab.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_petListFragment_to_addPetFragment)
        );

        adapter.setOnItemClickListener(pet -> {
            Bundle bundle = new Bundle();
            bundle.putInt("petId", pet.ID);
            NavHostFragment.findNavController(this).navigate(R.id.action_petListFragment_to_petDetailsFragment, bundle);
        });

        return view;
    }
}
