package it.itsar.fooding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ProductNameAutoCompleteAdapter extends ArrayAdapter<Prodotto> {

    private Context context;
    private List<Prodotto> prodotti;

    public ProductNameAutoCompleteAdapter(Context context, int resourceId, @NonNull List<Prodotto> prodotti) {
        super(context, resourceId, prodotti);
        this.context = context;
        this.prodotti = prodotti;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.autocomplete_product_name, parent, false);
        ImageView icon = row.findViewById(R.id.productImage);
        TextView productName = row.findViewById(R.id.productName);
        TextView productBrand = row.findViewById(R.id.productBrand);

        icon.setImageResource(prodotti.get(position).getImage());
        productName.setText(prodotti.get(position).getNome());
        productBrand.setText(prodotti.get(position).getMarca());
        return row;
    }

    public void setProdotti(List<Prodotto> prodotti) {
        this.prodotti = prodotti;
    }

    public List<Prodotto> getProdotti() {
        return prodotti;
    }
}
