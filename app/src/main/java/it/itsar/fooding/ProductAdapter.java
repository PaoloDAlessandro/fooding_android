package it.itsar.fooding;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Prodotto[] prodotti;

    public ProductAdapter(Prodotto[] prodotti) {
        this.prodotti = prodotti;
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

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        private TextView productName;
        private TextView productBrand;
        private ImageView productImage;
        private TextView productStock;
        private TextView productWeight;
        private TextView productTimer;
        private CardView imageCardView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
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
    }
}
