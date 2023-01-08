package it.itsar.fooding;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private ArrayList<Prodotto> prodotti;
    private Context context;
    private ActivityResultLauncher activityResultLauncher;

    public ProductAdapter(ArrayList<Prodotto> prodotti, Context context, ActivityResultLauncher activityResultLauncher) {
        this.prodotti = prodotti;
        this.context = context;
        this.activityResultLauncher = activityResultLauncher;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prodotto_riga, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(prodotti.get(position));
    }

    @Override
    public int getItemCount() {
        return prodotti.size();
    }

    public void setProdotti(ArrayList<Prodotto> prodotti) {
        this.prodotti = prodotti;
    }

     class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView productName;
        private TextView productBrand;
        private ImageView productImage;
        private TextView productStock;
        private TextView productWeight;
        private TextView productExpirationDate;
        private CardView imageCardView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            productName = itemView.findViewById(R.id.productName);
            productBrand = itemView.findViewById(R.id.productBrand);
            productImage = itemView.findViewById(R.id.productImage);
            productStock = itemView.findViewById(R.id.productStock);
            productWeight = itemView.findViewById(R.id.productWeight);
            productExpirationDate = itemView.findViewById(R.id.productExpirationDate);
            imageCardView = itemView.findViewById(R.id.productImageCard);
        }


        void bind(Prodotto prodotto) {
            if(prodotto.getNome().length() > 20) {
                productName.setText(prodotto.getNome().substring(0, 20) + "...");
            }
            else {
                productName.setText(prodotto.getNome());
            }
            productBrand.setText(prodotto.getMarca());
            productImage.setImageResource(prodotto.getImage());
            productStock.setText(prodotto.getAmountOfUnits() + "");
            productWeight.setText(prodotto.getPeso() + prodotto.getUnità());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                productExpirationDate.setText(prodotto.getCloserExpirationdate().getExpirationDate().format(dateTimeFormatter) + " (" + prodotto.getCloserExpirationdate().getAmount() + ")");
            }
            imageCardView.setBackgroundColor(prodotto.getColore());
        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            Prodotto prodotto = prodotti.get(position);
            Intent intent = new Intent(context, ProductDetails.class);
            intent.putExtra("prodotto", prodotto);
            intent.putExtra("position", position);
            activityResultLauncher.launch(intent);
        }
    }
}
