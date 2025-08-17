package org.unibl.etf.vetclinic.ui.pet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.vetclinic.R;
import org.unibl.etf.vetclinic.data.entities.Appointment;
import org.unibl.etf.vetclinic.data.entities.MedicalRecord;
import org.unibl.etf.vetclinic.data.entities.relations.MedicalRecordWithAppointment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MedicalRecordAdapter extends RecyclerView.Adapter<MedicalRecordAdapter.MedicalRecordViewHolder> {

    private List<MedicalRecord> records;

    private List<MedicalRecordWithAppointment> recordsWithAppointment = new ArrayList<>();

    public void setRecordsWithAppointments(List<MedicalRecordWithAppointment> records) {
        this.recordsWithAppointment = records;
        notifyDataSetChanged();
    }


    public void setRecords(List<MedicalRecord> records) {
        this.records = records;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MedicalRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medical_record, parent, false);
        return new MedicalRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicalRecordViewHolder holder, int position) {
        MedicalRecordWithAppointment item = recordsWithAppointment.get(position);
        MedicalRecord r = item.record;
        Appointment a = item.appointment;

        holder.diagnosis.setText("Dijagnoza: " + (r.Diagnosis != null ? r.Diagnosis : "N/A"));
        holder.treatment.setText("Terapija: " + (r.Treatment != null ? r.Treatment : "N/A"));
        holder.medications.setText("Lijekovi: " + (r.Medications != null ? r.Medications : "N/A"));
        holder.notes.setText("Bilješke: " + (r.Notes != null ? r.Notes : "N/A"));

        if (a != null && a.Date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            holder.date.setText("Datum: " + sdf.format(a.Date));
        } else {
            holder.date.setText("Datum: Nepoznat");
        }
    }

    @Override
    public int getItemCount() {
        return recordsWithAppointment != null ? recordsWithAppointment.size() : 0;
    }

    static class MedicalRecordViewHolder extends RecyclerView.ViewHolder {
        TextView diagnosis, treatment, medications, notes, date;

        MedicalRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            diagnosis = itemView.findViewById(R.id.textViewDiagnosis);
            treatment = itemView.findViewById(R.id.textViewTreatment);
            medications = itemView.findViewById(R.id.textViewMedications);
            notes = itemView.findViewById(R.id.textViewNotes);
            date = itemView.findViewById(R.id.textViewDate);
        }
    }

}

