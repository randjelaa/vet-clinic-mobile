package org.unibl.etf.vetclinic.ui.pet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        adapter = new PetListAdapter();
        recyclerView.setAdapter(adapter);

        petViewModel = new ViewModelProvider(this).get(PetViewModel.class);

        petViewModel.getAllPets().observe(getViewLifecycleOwner(), (List<Pet> pets) -> {
            adapter.submitList(pets);
        });

        return view;
    }
}

