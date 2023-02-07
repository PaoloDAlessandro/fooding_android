package it.itsar.fooding;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class IngredientiRicettaAdapter extends RecyclerView.Adapter<IngredientiRicettaAdapter.IngredientiRicettaViewHolder> {

    private ArrayList<Ingrediente> ingredienti;
    private MyProperties myProperties = MyProperties.getInstance();
    private ArrayList<Prodotto> userProducts = myProperties.getUserProdotti();

    public IngredientiRicettaAdapter(ArrayList<Ingrediente> ingredienti) {
        this.ingredienti = ingredienti;
    }

    @NonNull
    @Override
    public IngredientiRicettaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ricetta_ingrediente_row, parent, false);
        return new IngredientiRicettaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientiRicettaViewHolder holder, int position) {
        holder.bind(ingredienti.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredienti.size();
    }

    class IngredientiRicettaViewHolder extends RecyclerView.ViewHolder {

        private ImageView ingredienteImage;
        private ImageView ingredienteStatus;
        private TextView ingredienteName;

        public IngredientiRicettaViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredienteImage = itemView.findViewById(R.id.ingredienteImage);
            ingredienteName = itemView.findViewById(R.id.ingredienteName);
            ingredienteStatus = itemView.findViewById(R.id.ingredienteStatus);
        }

        public void bind(Ingrediente ingrediente) {
            downloadImage(ingrediente.getProdotto().getImage());
            String ingredienteText = ingrediente.getQuantità() + ingrediente.getProdotto().getUnità() + " ";

            /*
            if (ingrediente.getProdotto().getNome().length() > 16) {
                ingredienteText += ingrediente.getProdotto().getNome().substring(0, 15) + "...";
            }
            else {
                ingredienteText += ingrediente.getProdotto().getNome();
            }

             */

            ingredienteText += ingrediente.getProdotto().getNome();

            ingredienteName.setText(ingredienteText);
            if(userProducts.stream().anyMatch(prodotto -> prodotto.getNome().equals(ingrediente.getProdotto().getNome()))) {
                ingredienteStatus.setImageResource(R.drawable.ingredient_present);
                DrawableCompat.setTint(
                        DrawableCompat.wrap(ingredienteStatus.getDrawable()),
                        Color.parseColor("#159e1e")
                );
            }
            else {
                ingredienteStatus.setImageResource(R.drawable.ingredient_absent);
                DrawableCompat.setTint(
                        DrawableCompat.wrap(ingredienteStatus.getDrawable()),
                        Color.parseColor("#b51b1b")
                );
            }
        }

        private void downloadImage(String imageUri) {
            Picasso.get()
                    .load(imageUri)
                    .into(ingredienteImage, new com.squareup.picasso.Callback() {
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
