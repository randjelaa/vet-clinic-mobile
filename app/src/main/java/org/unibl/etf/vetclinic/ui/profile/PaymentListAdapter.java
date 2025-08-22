package org.unibl.etf.vetclinic.ui.profile;

import android.content.Context;
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
        Context context = holder.itemView.getContext();

        holder.textDatePaid.setText(
                context.getString(R.string.paid_on) + dateFormat.format(payment.payment.Date)
        );

        if (payment.appointment != null) {
            holder.textDateAppointment.setText(
                    context.getString(R.string.appointment_on) + dateFormat.format(payment.appointment.Date)
            );

            holder.textDetails.setText(
                    context.getString(R.string.appointment_pet_prefix) + payment.appointment.PetName + "\n" +
                            context.getString(R.string.appointment_service_prefix) + payment.appointment.ServiceName
            );
        } else {
            holder.textDateAppointment.setText(R.string.no_appointment_associated);
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
