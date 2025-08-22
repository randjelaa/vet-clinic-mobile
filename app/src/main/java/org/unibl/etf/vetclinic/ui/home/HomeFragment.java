package org.unibl.etf.vetclinic.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.vetclinic.R;
import org.unibl.etf.vetclinic.databinding.FragmentHomeBinding;
import org.unibl.etf.vetclinic.viewmodel.UserViewModel;
import org.unibl.etf.vetclinic.viewmodel.ServiceViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("VetClinicPrefs", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        if (userId != -1) {
            userViewModel.getUserById(userId, user -> {
                if (user != null) {
                    String welcomeText = getString(R.string.welcome_user, user.Name);
                    binding.textHome.setText(welcomeText);
                } else {
                    binding.textHome.setText(getString(R.string.welcome_default));
                }
            });
        } else {
            binding.textHome.setText(getString(R.string.welcome_default));
        }

        userViewModel.getAllVets().observe(getViewLifecycleOwner(), vets -> {
            int vetCount = vets != null ? vets.size() : 0;
            binding.textVeterinarians.setText(getString(R.string.veterinarians_count_format, vetCount));
        });

        userViewModel.getAllClients().observe(getViewLifecycleOwner(), clients -> {
            int clientCount = clients != null ? clients.size() : 0;
            binding.textClients.setText(getString(R.string.clients_count_format, clientCount));
        });

        RecyclerView recyclerViewServices = binding.recyclerViewServices;
        ServiceListAdapter serviceAdapter = new ServiceListAdapter();
        recyclerViewServices.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewServices.setAdapter(serviceAdapter);

        ServiceViewModel serviceViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);
        serviceViewModel.getAllServices().observe(getViewLifecycleOwner(), services -> {
            serviceAdapter.submitList(services);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
