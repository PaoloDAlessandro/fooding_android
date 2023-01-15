package it.itsar.fooding;

import android.content.Context;
import android.database.sqlite.SQLiteStatement;
import android.graphics.text.TextRunShaper;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductNameAutoCompleteAdapter extends ArrayAdapter<Prodotto> {

    private Context context;
    private List<Prodotto> prodotti = new ArrayList<>();
    private ImageView productImage;

    public ProductNameAutoCompleteAdapter (@NonNull Context context, ArrayList<Prodotto> prodotti) {
        super(context, R.layout.autocomplete_product_name, prodotti);
        this.context = context;
        this.prodotti = prodotti;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertedView, @NonNull ViewGroup parent) {
        View listItem = convertedView;
        if(listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.autocomplete_product_name, parent, false);
        }

        Prodotto prodotto = prodotti.get(position);
        productImage = listItem.findViewById(R.id.productImage);
        downloadImage(prodotto.getImage());

        TextView productName = listItem.findViewById(R.id.productName);
        productName.setText(prodotto.getNome());

        TextView productBrand = listItem.findViewById(R.id.productBrand);
        productBrand.setText(prodotto.getMarca());

        return listItem;
    }

    public void setProdotti(List<Prodotto> prodotti) {
        this.prodotti = prodotti;
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
