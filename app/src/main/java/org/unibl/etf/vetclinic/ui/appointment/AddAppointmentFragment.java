package org.unibl.etf.vetclinic.ui.appointment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
            return;
        }

        petViewModel.getPetsByUserId(userId).observe(getViewLifecycleOwner(), pets -> {
            userPets = pets;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item,
                    convertPetsToNames(pets));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            petSpinner.setAdapter(adapter);
        });

        userViewModel.getAllVets().observe(getViewLifecycleOwner(), allVets -> {
            vets = allVets;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item,
                    convertVetsToNames(vets));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            vetSpinner.setAdapter(adapter);
        });

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
                Toast.makeText(getContext(), getString(R.string.error_date_first), Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedDate.before(new Date())) {
                Toast.makeText(getContext(), getString(R.string.error_future), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), getString(R.string.error_select_date_time), Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedDate.before(new Date())) {
                Toast.makeText(getContext(), getString(R.string.error_future), Toast.LENGTH_SHORT).show();
                return;
            }

            int petIndex = petSpinner.getSelectedItemPosition();
            int vetIndex = vetSpinner.getSelectedItemPosition();
            int serviceIndex = serviceSpinner.getSelectedItemPosition();

            if (petIndex < 0 || vetIndex < 0 || serviceIndex < 0) {
                Toast.makeText(getContext(), getString(R.string.error_fields_required), Toast.LENGTH_SHORT).show();
                return;
            }

            int vetId = vets.get(vetIndex).ID;
            Service selectedService = services.get(serviceIndex);

            // Dobavljamo sve zakazane termine za ovog veterinara
            appointmentViewModel.getAppointmentsByVetId(vetId).observe(getViewLifecycleOwner(), appointments -> {
                boolean isOccupied = false;

                Calendar selectedStart = Calendar.getInstance();
                selectedStart.setTime(selectedDate);

                Calendar selectedEnd = (Calendar) selectedStart.clone();
                selectedEnd.add(Calendar.MINUTE, selectedService.DurationMinutes + 10); // dodaj buffer

                for (Appointment a : appointments) {
                    Calendar existingStart = Calendar.getInstance();
                    existingStart.setTime(a.Date);

                    Service serviceForExisting = null;
                    for (Service s : services) {
                        if (s.ID == a.ServiceID) {
                            serviceForExisting = s;
                            break;
                        }
                    }
                    if (serviceForExisting == null) continue;

                    Calendar existingEnd = (Calendar) existingStart.clone();
                    existingEnd.add(Calendar.MINUTE, serviceForExisting.DurationMinutes + 10); // dodaj buffer

                    // Provjera preklapanja
                    if (selectedStart.before(existingEnd) && existingStart.before(selectedEnd)) {
                        isOccupied = true;
                        break;
                    }
                }

                if (isOccupied) {
                    Toast.makeText(getContext(), getString(R.string.error_vet_busy), Toast.LENGTH_SHORT).show();
                } else {
                    // Veterinar je slobodan, zakazujemo
                    Appointment appointment = new Appointment();
                    appointment.Date = selectedDate;
                    appointment.PetID = userPets.get(petIndex).ID;
                    appointment.VetID = vetId;
                    appointment.ServiceID = selectedService.ID;

                    appointmentViewModel.insert(appointment);

                    Toast.makeText(getContext(), getString(R.string.success_appointment_scheduled), Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(this).navigateUp();
                }
            });
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
        for (User v : vets) names.add(v.Name);
        return names;
    }

    private List<String> convertServicesToNames(List<Service> services) {
        List<String> names = new ArrayList<>();
        for (Service s : services) names.add(s.Name + " (" + s.Price + " KM)");
        return names;
    }
}
