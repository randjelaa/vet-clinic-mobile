package org.unibl.etf.vetclinic.ui.appointment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.unibl.etf.vetclinic.R;

public class AppointmentFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);

        TextView label = view.findViewById(R.id.textViewLabel);
        label.setText("Appointments Fragment");

        return view;
    }
}
