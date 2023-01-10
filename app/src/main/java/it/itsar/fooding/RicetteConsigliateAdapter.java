package it.itsar.fooding;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RicetteConsigliateAdapter extends RecyclerView.Adapter<RicetteConsigliateAdapter.RicetteConsigliateViewHolder> {

    private ArrayList<Ricetta> ricette;

    public RicetteConsigliateAdapter(ArrayList<Ricetta> ricette) {
        this.ricette = ricette;
    }

    @NonNull
    @Override
    public RicetteConsigliateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ricetta_consigliata_riga, parent, false);
        return new RicetteConsigliateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RicetteConsigliateViewHolder holder, int position) {
        holder.bind(ricette.get(position));
    }

    @Override
    public int getItemCount() {
        return ricette.size() ;
    }

     static class RicetteConsigliateViewHolder extends RecyclerView.ViewHolder {

        private ImageView ricettaImage;
        private TextView ricettaName;

        public RicetteConsigliateViewHolder(@NonNull View itemView) {
            super(itemView);
            ricettaImage = itemView.findViewById(R.id.ricettaImage);
            ricettaName = itemView.findViewById(R.id.ricettaName);
        }

        void bind(Ricetta ricetta) {
            ricettaImage.setImageResource(ricetta.getImage());
            if(ricetta.getNome().length() > 20) {
                ricettaName.setText(ricetta.getNome().substring(0, 17) + "..");
            }
            else {
                ricettaName.setText(ricetta.getNome());
            }
        }
    }
}
