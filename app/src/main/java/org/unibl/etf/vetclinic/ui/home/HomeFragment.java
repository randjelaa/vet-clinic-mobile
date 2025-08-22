package org.unibl.etf.vetclinic.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.unibl.etf.vetclinic.data.entities.User;
import org.unibl.etf.vetclinic.databinding.FragmentHomeBinding;
import org.unibl.etf.vetclinic.viewmodel.UserViewModel;

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
                    String welcomeText = "Dobrodošli, " + user.Name + "!";
                    binding.textHome.setText(welcomeText);
                } else {
                    binding.textHome.setText("Dobrodošli!");
                }
            });
        } else {
            binding.textHome.setText("Dobrodošli!");
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
