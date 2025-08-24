package org.unibl.etf.vetclinic.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.vetclinic.R;
import org.unibl.etf.vetclinic.data.entities.Service;

public class ServiceListAdapter extends ListAdapter<Service, ServiceListAdapter.ViewHolder> {

    public ServiceListAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Service> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull Service oldItem, @NonNull Service newItem) {
                    return oldItem.ID == newItem.ID;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Service oldItem, @NonNull Service newItem) {
                    return oldItem.Name.equals(newItem.Name)
                            && oldItem.Price == newItem.Price
                            && ((oldItem.Description == null && newItem.Description == null)
                            || (oldItem.Description != null && oldItem.Description.equals(newItem.Description)));
                }
            };

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textName;
        private final TextView textDescription;
        private final TextView textPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textViewServiceName);
            textDescription = itemView.findViewById(R.id.textViewServiceDescription);
            textPrice = itemView.findViewById(R.id.textViewServicePrice);
        }

        public void bind(Service service) {
            textName.setText(service.Name);
            textDescription.setText(service.Description != null ? service.Description : "");
            textPrice.setText(itemView.getContext().getString(R.string.price_format, service.Price));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
}
