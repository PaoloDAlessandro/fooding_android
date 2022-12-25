package it.itsar.fooding;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Prodotto[] prodotti;
    private Context context;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    public ProductAdapter(Prodotto[] prodotti, Context context, ActivityResultLauncher activityResultLauncher) {
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
        holder.bind(prodotti[position]);
    }

    @Override
    public int getItemCount() {
        return prodotti.length;
    }

    public void setProdotti(Prodotto[] prodotti) {
        this.prodotti = prodotti;
    }

     class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView productName;
        private TextView productBrand;
        private ImageView productImage;
        private TextView productStock;
        private TextView productWeight;
        private TextView productTimer;
        private CardView imageCardView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            productName = itemView.findViewById(R.id.productName);
            productBrand = itemView.findViewById(R.id.productBrand);
            productImage = itemView.findViewById(R.id.productImage);
            productStock = itemView.findViewById(R.id.productStock);
            productWeight = itemView.findViewById(R.id.productWeight);
            productTimer = itemView.findViewById(R.id.productTimer);
            imageCardView = itemView.findViewById(R.id.productImageCard);
        }


        void bind(Prodotto prodotto) {
            productName.setText(prodotto.getNome());
            productBrand.setText(prodotto.getMarca());
            productImage.setImageResource(prodotto.getImage());
            productStock.setText(prodotto.getGiacenza() + "");
            productWeight.setText(prodotto.getPeso() + "g");
            productTimer.setText(prodotto.getPreparazione() + "min");
        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            Prodotto prodotto = prodotti[position];
            Intent intent = new Intent(context, ProductDetails.class);
            intent.putExtra("prodotto", prodotto);
            intent.putExtra("position", position);
            activityResultLauncher.launch(intent);
        }
    }
}
