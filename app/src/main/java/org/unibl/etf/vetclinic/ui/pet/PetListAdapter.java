package org.unibl.etf.vetclinic.ui.pet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.vetclinic.R;
import org.unibl.etf.vetclinic.data.entities.Pet;

public class PetListAdapter extends ListAdapter<Pet, PetListAdapter.PetViewHolder> {

    private static OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Pet pet);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public PetListAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Pet> DIFF_CALLBACK = new DiffUtil.ItemCallback<Pet>() {
        @Override
        public boolean areItemsTheSame(@NonNull Pet oldItem, @NonNull Pet newItem) {
            return oldItem.ID == newItem.ID;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Pet oldItem, @NonNull Pet newItem) {
            return oldItem.Name.equals(newItem.Name)
                    && ((oldItem.Breed == null && newItem.Breed == null) || (oldItem.Breed != null && oldItem.Breed.equals(newItem.Breed)))
                    && ((oldItem.Species == null && newItem.Species == null) || (oldItem.Species != null && oldItem.Species.equals(newItem.Species)));
        }
    };

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pet, parent, false);
        return new PetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        Pet pet = getItem(position);
        holder.bind(pet, listener);
    }

    class PetViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;
        private final TextView textViewSpecies;
        private final TextView textViewBreed;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewPetName);
            textViewSpecies = itemView.findViewById(R.id.textViewPetSpecies);
            textViewBreed = itemView.findViewById(R.id.textViewPetBreed);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }

        public void bind(Pet pet, OnItemClickListener listener) {
            textViewName.setText(pet.Name);
            textViewSpecies.setText(pet.Species != null ? pet.Species : "Nepoznata vrsta");
            textViewBreed.setText(pet.Breed != null ? pet.Breed : "Nepoznata rasa");

            itemView.setOnClickListener(v -> listener.onItemClick(pet));
        }
    }
}

