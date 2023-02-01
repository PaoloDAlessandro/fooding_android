package it.itsar.fooding;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private ArrayList<Ricetta> ricette;
    private Context context;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    public RecipeAdapter(ArrayList<Ricetta> ricette, Context context, ActivityResultLauncher<Intent> activityResultLauncher) {
        this.ricette = ricette;
        this.context = context;
        this.activityResultLauncher = activityResultLauncher;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ricetta_row, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(ricette.get(position));
    }

    @Override
    public int getItemCount() {
        return ricette.size();
    }

    public void setRicette(ArrayList<Ricetta> ricette) {
        this.ricette = ricette;
    }

     class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView ricettaNome;
        private TextView ricettaAutore;
        private TextView ricettaDifficolta;
        private TextView ricettaDurata;
        private TextView ricettaKcal;
        private ImageView ricettaImage;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ricettaNome = itemView.findViewById(R.id.ricettaName);
            ricettaAutore = itemView.findViewById(R.id.ricettaAuthor);
            ricettaDifficolta = itemView.findViewById(R.id.ricettaDifficolta );
            ricettaDurata = itemView.findViewById(R.id.ricettaDurata);
            ricettaKcal = itemView.findViewById(R.id.ricettaKcal);
            ricettaImage = itemView.findViewById(R.id.ricettaImage);
        }


        void bind(Ricetta ricetta) {
            ricettaNome.setText(ricetta.getNome());
            ricettaAutore.setText("By " + ricetta.getAutore());
            ricettaDifficolta.setText(ricetta.getDifficolta().toString().toLowerCase());
            ricettaDurata.setText(ricetta.getTempoPreparazione() + "min.");
            ricettaKcal.setText(ricetta.getKcal() + "kcal");
            downloadImage(ricetta.getImage());


        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            Ricetta ricetta = ricette.get(position);
            Intent intent = new Intent(context, RicettaDetails.class);
            intent.putExtra("ricetta", ricetta);
            intent.putExtra("position", position);
            activityResultLauncher.launch(intent);
        }

         private void downloadImage(String imageUri) {
             Picasso.get()
                     .load(imageUri)
                     .into(ricettaImage, new com.squareup.picasso.Callback() {
                         @Override
                         public void onSuccess(){
                         }

                         @Override
                         public void onError(Exception e) {
                         }
                     });
         }
    }
}
