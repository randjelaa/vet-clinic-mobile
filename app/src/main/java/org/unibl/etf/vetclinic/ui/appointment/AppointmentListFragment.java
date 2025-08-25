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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.unibl.etf.vetclinic.R;
import org.unibl.etf.vetclinic.data.entities.relations.AppointmentWithDetails;
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

        upcomingAdapter = new AppointmentListAdapter(new AppointmentListAdapter.OnItemActionListener() {
            @Override
            public void onCancel(AppointmentWithDetails appointment) {
                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle(R.string.cancel_confirmation_title)
                        .setMessage(R.string.confirm_cancel_appointment)
                        .setPositiveButton(R.string.yes, (dialog, which) -> {
                            appointmentViewModel.cancelAppointment(appointment.ID);
                            Toast.makeText(getContext(), getString(R.string.success_appointment_cancelled), Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
            }

            @Override
            public void onPay(AppointmentWithDetails appointment) {
                appointmentViewModel.markAppointmentAsPaid(appointment.ID);
                Toast.makeText(getContext(), getString(R.string.success_appointment_paid), Toast.LENGTH_SHORT).show();
            }
        });

        pastAdapter = new AppointmentListAdapter(new AppointmentListAdapter.OnItemActionListener() {
            @Override
            public void onCancel(AppointmentWithDetails appointment) {
                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle(R.string.cancel_confirmation_title)
                        .setMessage(R.string.confirm_cancel_appointment)
                        .setPositiveButton(R.string.yes, (dialog, which) -> {
                            appointmentViewModel.cancelAppointment(appointment.ID);
                            Toast.makeText(getContext(), getString(R.string.success_appointment_cancelled), Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
            }

            @Override
            public void onPay(AppointmentWithDetails appointment) {
                appointmentViewModel.markAppointmentAsPaid(appointment.ID);
                Toast.makeText(getContext(), getString(R.string.success_appointment_paid), Toast.LENGTH_SHORT).show();
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
        fab.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_appointmentListFragment_to_addAppointmentFragment);
        });

        return view;
    }
}
