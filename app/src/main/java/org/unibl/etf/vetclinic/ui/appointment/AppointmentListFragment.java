package org.unibl.etf.vetclinic.ui.appointment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.unibl.etf.vetclinic.R;
import org.unibl.etf.vetclinic.data.entities.relations.AppointmentWithDetails;
import org.unibl.etf.vetclinic.ui.appointment.AppointmentListAdapter;
import org.unibl.etf.vetclinic.viewmodel.AppointmentViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentListFragment extends Fragment {

    private AppointmentViewModel appointmentViewModel;
    private AppointmentListAdapter upcomingAdapter;
    private AppointmentListAdapter pastAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_list, container, false);

        RecyclerView recyclerUpcoming = view.findViewById(R.id.recyclerViewUpcoming);
        RecyclerView recyclerPast = view.findViewById(R.id.recyclerViewPast);

        recyclerUpcoming.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerPast.setLayoutManager(new LinearLayoutManager(getContext()));

        // Adapteri sa listenerima za dugmad
        upcomingAdapter = new AppointmentListAdapter(new AppointmentListAdapter.OnItemActionListener() {
            @Override
            public void onCancel(AppointmentWithDetails appointment) {
                Toast.makeText(getContext(), "Cancel upcoming " + appointment.ID, Toast.LENGTH_SHORT).show();
                // TODO: implement cancel logic
            }

            @Override
            public void onPay(AppointmentWithDetails appointment) {
                Toast.makeText(getContext(), "Pay upcoming " + appointment.ID, Toast.LENGTH_SHORT).show();
                // TODO: implement payment logic
            }
        });

        pastAdapter = new AppointmentListAdapter(new AppointmentListAdapter.OnItemActionListener() {
            @Override
            public void onCancel(AppointmentWithDetails appointment) {
                Toast.makeText(getContext(), "Cancel past " + appointment.ID, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPay(AppointmentWithDetails appointment) {
                Toast.makeText(getContext(), "Pay past " + appointment.ID, Toast.LENGTH_SHORT).show();
            }
        });

        recyclerUpcoming.setAdapter(upcomingAdapter);
        recyclerPast.setAdapter(pastAdapter);

        appointmentViewModel = new ViewModelProvider(this).get(AppointmentViewModel.class);

        SharedPreferences prefs = requireActivity().getSharedPreferences("VetClinicPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId != -1) {
            appointmentViewModel.getAppointmentsByUserId(userId).observe(getViewLifecycleOwner(), appointments -> {
                List<AppointmentWithDetails> upcoming = new ArrayList<>();
                List<AppointmentWithDetails> past = new ArrayList<>();
                Date now = new Date();

                for (AppointmentWithDetails app : appointments) {
                    if (app.Date.after(now)) {
                        upcoming.add(app);
                    } else {
                        past.add(app);
                    }
                }

                upcomingAdapter.submitList(upcoming);
                pastAdapter.submitList(past);
            });
        }

        FloatingActionButton fab = view.findViewById(R.id.fabAddAppointment);
        fab.setOnClickListener(v ->
                        Toast.makeText(getContext(), "Navigate to Add Appointment", Toast.LENGTH_SHORT).show()
                // TODO: navigate to AddAppointmentFragment
        );

        return view;
    }
}
