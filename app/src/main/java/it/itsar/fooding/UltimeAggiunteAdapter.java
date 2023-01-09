package it.itsar.fooding;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;

public class UltimeAggiunteAdapter extends RecyclerView.Adapter<UltimeAggiunteAdapter.ultimeAggiunteViewHolder>{

    private Prodotto[] ultimeAggiunte;

    public UltimeAggiunteAdapter(Prodotto[] prodotti) {
        this.ultimeAggiunte = prodotti;
    }

    @NonNull
    @Override
    public ultimeAggiunteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ultime_aggiunte_row, parent, false);
        return new ultimeAggiunteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ultimeAggiunteViewHolder holder, int position) {
        holder.bind(ultimeAggiunte[position]);
    }

    @Override
    public int getItemCount() {
        return ultimeAggiunte.length;
    }

    static class ultimeAggiunteViewHolder extends RecyclerView.ViewHolder {

        private TextView productName;
        private ImageView productImage;

        public ultimeAggiunteViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
        }

        void bind(Prodotto prodotto) {
            if(prodotto.getNome().length() > 17) {
                productName.setText(prodotto.getNome().substring(0, 16) + "...");
            }
            else {
                productName.setText(prodotto.getNome());
            }
            productImage.setImageResource(prodotto.getImage());
        }
    }
}
