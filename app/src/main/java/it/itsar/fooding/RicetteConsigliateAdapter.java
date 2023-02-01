package it.itsar.fooding;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RicetteConsigliateAdapter extends RecyclerView.Adapter<RicetteConsigliateAdapter.RicetteConsigliateViewHolder> {

    private ArrayList<Ricetta> ricette;
    private Context context;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    public RicetteConsigliateAdapter(ArrayList<Ricetta> ricette, Context context, ActivityResultLauncher<Intent> activityResultLauncher) {
        this.ricette = ricette;
        this.context = context;
        this.activityResultLauncher = activityResultLauncher;
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

    class RicetteConsigliateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ricettaImage;
        private TextView ricettaName;

        public RicetteConsigliateViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ricettaImage = itemView.findViewById(R.id.ricettaImage);
            ricettaName = itemView.findViewById(R.id.ricettaName);
        }

        void bind(Ricetta ricetta) {
            downloadImage(ricetta.getImage());
            if(ricetta.getNome().length() > 20) {
                ricettaName.setText(ricetta.getNome().substring(0, 17) + "..");
            }
            else {
                ricettaName.setText(ricetta.getNome());
            }
        }

         @Override
         public void onClick(View view) {
             int position = this.getAdapterPosition();
             Ricetta ricetta = ricette.get(position);
             Intent intent = new Intent(context, RicettaDetails.class);
             intent.putExtra("ricetta", ricetta);
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
