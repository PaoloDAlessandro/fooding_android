package it.itsar.fooding;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class UltimeAggiunteAdapter extends RecyclerView.Adapter<UltimeAggiunteAdapter.ultimeAggiunteViewHolder>{

    private Prodotto[] ultimeAggiunte;
    private Context context;
    private ActivityResultLauncher activityResultLauncher;

    public UltimeAggiunteAdapter(Prodotto[] prodotti, Context context, ActivityResultLauncher activityResultLauncher) {
        this.ultimeAggiunte = prodotti;
        this.context = context;
        this.activityResultLauncher = activityResultLauncher;
    }

    @NonNull
    @Override
    public ultimeAggiunteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ultime_aggiunte_row, parent, false);
        return new ultimeAggiunteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ultimeAggiunteViewHolder holder, int position) {
        holder.bind(ultimeAggiunte[position]);
    }

    @Override
    public int getItemCount() {
        return ultimeAggiunte.length;
    }

    public void setUltimeAggiunte(Prodotto[] ultimeAggiunte) {
        this.ultimeAggiunte = ultimeAggiunte;
    }

    class ultimeAggiunteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView productName;
        private ImageView productImage;
        private TextView productExpirationDate;

        public ultimeAggiunteViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productExpirationDate = itemView.findViewById(R.id.productExpirationDate);
        }

        void bind(Prodotto prodotto) {
            if(prodotto.getNome().length() > 17) {
                productName.setText(prodotto.getNome().substring(0, 16) + "...");
            }
            else {
                productName.setText(prodotto.getNome());
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d-M-yyyy");
            productExpirationDate.setText(prodotto.getCloserExpirationdate().getExpirationDate().format(dateTimeFormatter));
            }
            productImage.setImageResource(prodotto.getImage());
        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            Prodotto prodotto = ultimeAggiunte[position];
            Intent intent = new Intent(context, ProductDetails.class);
            intent.putExtra("prodotto", prodotto);
            intent.putExtra("position", position);
            activityResultLauncher.launch(intent);
        }
    }
}
