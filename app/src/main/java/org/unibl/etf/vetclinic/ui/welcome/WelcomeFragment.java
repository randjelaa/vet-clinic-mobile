package org.unibl.etf.vetclinic.ui.welcome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import org.unibl.etf.vetclinic.R;

public class WelcomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.buttonLogin).setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_welcomeFragment_to_loginFragment)
        );

        view.findViewById(R.id.buttonRegister).setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_welcomeFragment_to_registerFragment)
        );
    }
}
