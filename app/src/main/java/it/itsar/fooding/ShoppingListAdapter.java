package it.itsar.fooding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder> {

    ArrayList<Prodotto> shoppingList;

    public ShoppingListAdapter(ArrayList<Prodotto> shoppingList) {
        this.shoppingList = shoppingList;
    }

    @NonNull
    @Override
    public ShoppingListAdapter.ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoppinglist_row, parent, false);
        return new ShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListAdapter.ShoppingListViewHolder holder, int position) {
        holder.bind(shoppingList.get(position));
    }

    @Override
    public int getItemCount() {
       return shoppingList.size();
    }

    protected static class ShoppingListViewHolder extends RecyclerView.ViewHolder {

        private TextView productName;
        private TextView productMarca;
        private ImageView productImage;
        private CheckBox productStatus;


        public ShoppingListViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productMarca = itemView.findViewById(R.id.productBrand);
            productStatus = itemView.findViewById(R.id.productStatus);
        }

        void bind(Prodotto prodotto) {
            productName.setText(prodotto.getNome());
            productMarca.setText(prodotto.getMarca());
            downloadImage(prodotto.getImage());
            productStatus.setChecked(false);
        }

        private void downloadImage(String imageUri) {
            Picasso.get()
                    .load(imageUri)
                    .into(productImage, new com.squareup.picasso.Callback() {
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
