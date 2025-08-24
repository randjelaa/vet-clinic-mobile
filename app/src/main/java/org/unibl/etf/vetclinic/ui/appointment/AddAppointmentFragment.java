package org.unibl.etf.vetclinic.ui.appointment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import org.unibl.etf.vetclinic.R;
import org.unibl.etf.vetclinic.data.entities.Appointment;
import org.unibl.etf.vetclinic.data.entities.Pet;
import org.unibl.etf.vetclinic.data.entities.Service;
import org.unibl.etf.vetclinic.data.entities.User;
import org.unibl.etf.vetclinic.viewmodel.AppointmentViewModel;
import org.unibl.etf.vetclinic.viewmodel.PetViewModel;
import org.unibl.etf.vetclinic.viewmodel.ServiceViewModel;
import org.unibl.etf.vetclinic.viewmodel.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.*;

public class AddAppointmentFragment extends Fragment {

    private Spinner petSpinner, vetSpinner, serviceSpinner;
    private TextView dateTextView;
    private Button selectDateButton, selectTimeButton, submitButton;

    private Date selectedDate;
    private AppointmentViewModel appointmentViewModel;
    private PetViewModel petViewModel;
    private UserViewModel userViewModel;
    private ServiceViewModel serviceViewModel;

    private List<Pet> userPets = new ArrayList<>();
    private List<User> vets = new ArrayList<>();
    private List<Service> services = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_appointment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        petSpinner = view.findViewById(R.id.spinnerPets);
        vetSpinner = view.findViewById(R.id.spinnerVets);
        serviceSpinner = view.findViewById(R.id.spinnerServices);
        dateTextView = view.findViewById(R.id.textViewSelectedDate);
        selectDateButton = view.findViewById(R.id.buttonSelectDate);
        selectTimeButton = view.findViewById(R.id.buttonSelectTime);
        submitButton = view.findViewById(R.id.buttonAddAppointment);

        appointmentViewModel = new ViewModelProvider(this).get(AppointmentViewModel.class);
        petViewModel = new ViewModelProvider(this).get(PetViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        serviceViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);

        SharedPreferences prefs = requireActivity().getSharedPreferences("VetClinicPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Load pets
        petViewModel.getPetsByUserId(userId).observe(getViewLifecycleOwner(), pets -> {
            userPets = pets;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item,
                    convertPetsToNames(pets));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            petSpinner.setAdapter(adapter);
        });

        // Load vets
        userViewModel.getAllVets().observe(getViewLifecycleOwner(), allVets -> {
            vets = allVets;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item,
                    convertVetsToNames(vets));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            vetSpinner.setAdapter(adapter);
        });

        // Load services
        serviceViewModel.getAllServices().observe(getViewLifecycleOwner(), allServices -> {
            services = allServices;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item,
                    convertServicesToNames(services));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            serviceSpinner.setAdapter(adapter);
        });

        selectDateButton.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(getContext(), (view1, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                selectedDate = calendar.getTime();
                updateDateText();
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        selectTimeButton.setOnClickListener(v -> {
            if (selectedDate == null) {
                Toast.makeText(getContext(), "Please select date first", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedDate.before(new Date())) {
                Toast.makeText(getContext(), "Datum i vrijeme moraju biti u budućnosti", Toast.LENGTH_SHORT).show();
                return;
            }
            final Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(getContext(), (view12, hourOfDay, minute) -> {
                Calendar timeCal = Calendar.getInstance();
                timeCal.setTime(selectedDate);
                timeCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                timeCal.set(Calendar.MINUTE, minute);
                selectedDate = timeCal.getTime();
                updateDateText();
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        });

        submitButton.setOnClickListener(v -> {
            if (selectedDate == null) {
                Toast.makeText(getContext(), "Please select appointment date/time", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedDate.before(new Date())) {
                Toast.makeText(getContext(), "Datum i vrijeme moraju biti u budućnosti", Toast.LENGTH_SHORT).show();
                return;
            }

            int petIndex = petSpinner.getSelectedItemPosition();
            int vetIndex = vetSpinner.getSelectedItemPosition();
            int serviceIndex = serviceSpinner.getSelectedItemPosition();

            if (petIndex < 0 || vetIndex < 0 || serviceIndex < 0) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Appointment appointment = new Appointment();
            appointment.Date = selectedDate;
            appointment.PetID = userPets.get(petIndex).ID;
            appointment.VetID = vets.get(vetIndex).ID;
            appointment.ServiceID = services.get(serviceIndex).ID;

            appointmentViewModel.insert(appointment);

            Toast.makeText(getContext(), "Appointment scheduled", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).navigateUp();
        });
    }

    private void updateDateText() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy. HH:mm", Locale.getDefault());
        dateTextView.setText(sdf.format(selectedDate));
    }

    private List<String> convertPetsToNames(List<Pet> pets) {
        List<String> names = new ArrayList<>();
        for (Pet p : pets) names.add(p.Name);
        return names;
    }

    private List<String> convertVetsToNames(List<User> vets) {
        List<String> names = new ArrayList<>();
        for (User v : vets) names.add(v.Name); // assuming `Name` field
        return names;
    }

    private List<String> convertServicesToNames(List<Service> services) {
        List<String> names = new ArrayList<>();
        for (Service s : services) names.add(s.Name + " (" + s.Price + " KM)");
        return names;
    }
}
