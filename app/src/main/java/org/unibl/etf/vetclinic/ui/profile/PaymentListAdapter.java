package org.unibl.etf.vetclinic.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.vetclinic.R;
import org.unibl.etf.vetclinic.data.entities.relations.PaymentWithAppointment;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.PaymentViewHolder> {

    private List<PaymentWithAppointment> payments;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    public void setPayments(List<PaymentWithAppointment> payments) {
        this.payments = payments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        PaymentWithAppointment payment = payments.get(position);

        // Prikaži datum plaćanja
        holder.textDatePaid.setText("Paid on: " + dateFormat.format(payment.payment.Date));

        // Prikaži informacije o zakazanom terminu, ako postoji
        if (payment.appointment != null) {
            holder.textDateAppointment.setText("Appointment on: " + dateFormat.format(payment.appointment.Date));

            // Prikaži osnovne detalje o terminu (npr. ID)
            holder.textDetails.setText("Pet: " + payment.appointment.PetName + "\n" +
                                        "Service: " + payment.appointment.ServiceName);
        } else {
            holder.textDateAppointment.setText("No appointment associated");
            holder.textDetails.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return payments == null ? 0 : payments.size();
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView textDatePaid, textDateAppointment, textDetails;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            textDatePaid = itemView.findViewById(R.id.textDatePaid);
            textDateAppointment = itemView.findViewById(R.id.textDateAppointment);
            textDetails = itemView.findViewById(R.id.textDetails);
        }
    }
}
