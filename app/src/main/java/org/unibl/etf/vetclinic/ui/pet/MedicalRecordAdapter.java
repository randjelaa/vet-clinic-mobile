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
import org.unibl.etf.vetclinic.util.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MedicalRecordAdapter extends RecyclerView.Adapter<MedicalRecordAdapter.MedicalRecordViewHolder> {
    private List<MedicalRecordWithAppointment> recordsWithAppointment = new ArrayList<>();

    public void setRecordsWithAppointments(List<MedicalRecordWithAppointment> records) {
        this.recordsWithAppointment = records;
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

        View contextView = holder.itemView;
        String na = contextView.getContext().getString(R.string.value_na);
        String unknown = contextView.getContext().getString(R.string.unknown);

        holder.diagnosis.setText(contextView.getContext().getString(
                R.string.label_diagnosis, r.Diagnosis != null ? r.Diagnosis : na));

        holder.treatment.setText(contextView.getContext().getString(
                R.string.label_treatment, r.Treatment != null ? r.Treatment : na));

        holder.medications.setText(contextView.getContext().getString(
                R.string.label_medications, r.Medications != null ? r.Medications : na));

        holder.notes.setText(contextView.getContext().getString(
                R.string.label_notes, r.Notes != null ? r.Notes : na));

        if (a != null && a.Date != null) {
            String dateStr = DateUtils.formatDate(a.Date);
            holder.date.setText(contextView.getContext().getString(R.string.label_date, dateStr));
        } else {
            holder.date.setText(contextView.getContext().getString(R.string.label_date,
                    contextView.getContext().getString(R.string.unknown)));
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

