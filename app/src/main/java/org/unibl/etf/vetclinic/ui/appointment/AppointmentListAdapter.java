package org.unibl.etf.vetclinic.ui.appointment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.vetclinic.R;
import org.unibl.etf.vetclinic.data.entities.relations.AppointmentWithDetails;

import java.util.Date;

public class AppointmentListAdapter extends ListAdapter<AppointmentWithDetails, AppointmentListAdapter.ViewHolder> {

    public interface OnItemActionListener {
        void onCancel(AppointmentWithDetails appointment);
        void onPay(AppointmentWithDetails appointment);
    }

    private final OnItemActionListener listener;

    public AppointmentListAdapter(OnItemActionListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<AppointmentWithDetails> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<AppointmentWithDetails>() {
                @Override
                public boolean areItemsTheSame(@NonNull AppointmentWithDetails oldItem, @NonNull AppointmentWithDetails newItem) {
                    return oldItem.ID == newItem.ID;
                }

                @Override
                public boolean areContentsTheSame(@NonNull AppointmentWithDetails oldItem, @NonNull AppointmentWithDetails newItem) {
                    return oldItem.Date.equals(newItem.Date)
                            && oldItem.PetName.equals(newItem.PetName)
                            && oldItem.ServiceName.equals(newItem.ServiceName)
                            && oldItem.Price == newItem.Price
                            && oldItem.IsPaid == newItem.IsPaid; // Dodaj poređenje IsPaid
                }
            };

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewPet;
        private final TextView textViewService;
        private final TextView textViewDate;
        private final Button buttonCancel;
        private final Button buttonPay;
        private final TextView textViewPaid;

        ViewHolder(View itemView) {
            super(itemView);
            textViewPet = itemView.findViewById(R.id.textViewPetName);
            textViewService = itemView.findViewById(R.id.textViewService);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            buttonCancel = itemView.findViewById(R.id.buttonCancel);
            buttonPay = itemView.findViewById(R.id.buttonPay);
            textViewPaid = itemView.findViewById(R.id.textViewPaid);
        }

        void bind(AppointmentWithDetails appointment, OnItemActionListener listener) {
            textViewPet.setText(appointment.PetName);
            textViewService.setText(appointment.ServiceName + " (" + appointment.Price + " KM)");
            textViewDate.setText(appointment.Date.toString());

            Date now = new Date();

            // Početno sakrij sve akcije
            buttonCancel.setVisibility(View.GONE);
            buttonPay.setVisibility(View.GONE);
            textViewPaid.setVisibility(View.GONE);

            if (appointment.Date.after(now)) {
                // Budući termin - vidi dugme Cancel
                buttonCancel.setVisibility(View.VISIBLE);
                buttonCancel.setOnClickListener(v -> listener.onCancel(appointment));
            } else {
                // Prošli termin
                if (appointment.IsPaid) {
                    // Plaćeno - prikaži oznaku Paid
                    textViewPaid.setVisibility(View.VISIBLE);
                    textViewPaid.setText(itemView.getContext().getString(R.string.paid));
                } else {
                    // Nije plaćeno - dugme Pay
                    buttonPay.setVisibility(View.VISIBLE);
                    buttonPay.setOnClickListener(v -> listener.onPay(appointment));
                }
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppointmentWithDetails appointment = getItem(position);
        holder.bind(appointment, listener);
    }
}
