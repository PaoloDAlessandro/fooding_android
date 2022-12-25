package it.itsar.fooding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Pantry extends Fragment {

    private RecyclerView recyclerView;
    public Prodotto[] prodotti = {
            new Prodotto("Crema carciofi", "Knorr", "Farina di FRUMENTO, LATTE scremato, fecola di patate, PANNA, sale iodato, farina di mais*, aromi, carciofi 3,1%, olio di mais, estratto per brodo, zucchero, estratto di lievito, succo di limone, spinaci, sale, prezzemolo 0,2%, spezie (pepe nero*, aglio, radice di levistico). Può contenere uova, soia, sedaano e senape. *Da agricoltura sostenibile", 250, 4, 3, R.drawable.crema_carciofi, new ValoriNutrizionali(724.00, 9, 11.0, 16, 6.3, 32, 17.0, 7, 1.3, 1, 1.3, 0, 2.5, 5, 0, 0)),
            /*
            new Prodotto("Ragù Contadino", "Barilla", "Carote 4% Rosmarino Oli vegetali (oliva, girasole) Aglio Aromi Sale Zucchero Amido di mais Cipolle Vino rosso 6% Concentrato di pomodoro 16,5% Polpa di pomodoro 18% Acqua Carne suina 25%",400, 1, 0, R.drawable.ragu_contadino),
            new Prodotto("Leerdammer", "Leerdammer", "Latte, sale, fermenti lattici, caglio microbico.", 350, 2, 0, R.drawable.leerdammer),
            new Prodotto("Rosette","Schär", "amido di mais , acqua , pasta madre (farina di riso, acqua) 14% , fibra vegetale (psillio) , farina di miglio , olio di girasole 3,8% , farina di riso , proteine di soia , amido di riso , addensante: idrossipropilmetilcellulosa ; sciroppo di riso , sale , lievito , zucchero . Può contenere tracce di nocciole, uova e di senape. SENZA FRUMENTO.", 500, 5, 8, R.drawable.rosette),
            new Prodotto("Crema carciofi", "Knorr", "Farina di FRUMENTO, LATTE scremato, fecola di patate, PANNA, sale iodato, farina di mais*, aromi, carciofi 3,1%, olio di mais, estratto per brodo, zucchero, estratto di lievito, succo di limone, spinaci, sale, prezzemolo 0,2%, spezie (pepe nero*, aglio, radice di levistico). Può contenere uova, soia, sedaano e senape. *Da agricoltura sostenibile", 250, 4, 3, R.drawable.crema_carciofi),
            new Prodotto("Ragù Contadino", "Barilla", "Carote 4% Rosmarino Oli vegetali (oliva, girasole) Aglio Aromi Sale Zucchero Amido di mais Cipolle Vino rosso 6% Concentrato di pomodoro 16,5% Polpa di pomodoro 18% Acqua Carne suina 25%",400, 1, 0, R.drawable.ragu_contadino),
            new Prodotto("Leerdammer", "Leerdammer", "Latte, sale, fermenti lattici, caglio microbico.", 350, 2, 0, R.drawable.leerdammer),
            new Prodotto("Rosette","Schär", "amido di mais , acqua , pasta madre (farina di riso, acqua) 14% , fibra vegetale (psillio) , farina di miglio , olio di girasole 3,8% , farina di riso , proteine di soia , amido di riso , addensante: idrossipropilmetilcellulosa ; sciroppo di riso , sale , lievito , zucchero . Può contenere tracce di nocciole, uova e di senape. SENZA FRUMENTO.", 500, 5, 8, R.drawable.rosette),
            new Prodotto("Crema carciofi", "Knorr", "Farina di FRUMENTO, LATTE scremato, fecola di patate, PANNA, sale iodato, farina di mais*, aromi, carciofi 3,1%, olio di mais, estratto per brodo, zucchero, estratto di lievito, succo di limone, spinaci, sale, prezzemolo 0,2%, spezie (pepe nero*, aglio, radice di levistico). Può contenere uova, soia, sedaano e senape. *Da agricoltura sostenibile", 250, 4, 3, R.drawable.crema_carciofi),
            new Prodotto("Ragù Contadino", "Barilla", "Carote 4% Rosmarino Oli vegetali (oliva, girasole) Aglio Aromi Sale Zucchero Amido di mais Cipolle Vino rosso 6% Concentrato di pomodoro 16,5% Polpa di pomodoro 18% Acqua Carne suina 25%",400, 1, 0, R.drawable.ragu_contadino),
            new Prodotto("Leerdammer", "Leerdammer", "Latte, sale, fermenti lattici, caglio microbico.", 350, 2, 0, R.drawable.leerdammer),
            new Prodotto("Rosette","Schär", "amido di mais , acqua , pasta madre (farina di riso, acqua) 14% , fibra vegetale (psillio) , farina di miglio , olio di girasole 3,8% , farina di riso , proteine di soia , amido di riso , addensante: idrossipropilmetilcellulosa ; sciroppo di riso , sale , lievito , zucchero . Può contenere tracce di nocciole, uova e di senape. SENZA FRUMENTO.", 500, 5, 8, R.drawable.rosette),
            new Prodotto("Crema carciofi", "Knorr", "Farina di FRUMENTO, LATTE scremato, fecola di patate, PANNA, sale iodato, farina di mais*, aromi, carciofi 3,1%, olio di mais, estratto per brodo, zucchero, estratto di lievito, succo di limone, spinaci, sale, prezzemolo 0,2%, spezie (pepe nero*, aglio, radice di levistico). Può contenere uova, soia, sedaano e senape. *Da agricoltura sostenibile", 250, 4, 3, R.drawable.crema_carciofi),
            new Prodotto("Ragù Contadino", "Barilla", "Carote 4% Rosmarino Oli vegetali (oliva, girasole) Aglio Aromi Sale Zucchero Amido di mais Cipolle Vino rosso 6% Concentrato di pomodoro 16,5% Polpa di pomodoro 18% Acqua Carne suina 25%",400, 1, 0, R.drawable.ragu_contadino),
            new Prodotto("Leerdammer", "Leerdammer", "Latte, sale, fermenti lattici, caglio microbico.", 350, 2, 0, R.drawable.leerdammer),
            new Prodotto("Rosette","Schär", "amido di mais , acqua , pasta madre (farina di riso, acqua) 14% , fibra vegetale (psillio) , farina di miglio , olio di girasole 3,8% , farina di riso , proteine di soia , amido di riso , addensante: idrossipropilmetilcellulosa ; sciroppo di riso , sale , lievito , zucchero . Può contenere tracce di nocciole, uova e di senape. SENZA FRUMENTO.", 500, 5, 8, R.drawable.rosette)


             */
    };

    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                switch (result.getResultCode()){
                    case Activity.RESULT_OK:
                        updateRecycleView(result);
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.d("Edit: ", "NO");
                        break;
                }
            }
    );

    private ProductAdapter productAdapter;
    private EditText searchProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pantry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My pantry");

        productAdapter = new ProductAdapter(prodotti, getContext(), activityLauncher);
        searchProduct = view.findViewById(R.id.searchProduct);
        recyclerView = view.findViewById(R.id.productsRecyclerView);
        recyclerView.setAdapter(productAdapter);
        searchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<Prodotto> prodottiFiltered = Arrays.asList(prodotti);
                prodottiFiltered = prodottiFiltered.stream()
                        .filter(prodotto -> prodotto.getNome().toLowerCase().contains(charSequence.toString().toLowerCase()))
                        .collect(Collectors.toList());
                Log.d("Input: ", charSequence.toString());
                Log.d("Filtered product: ", prodottiFiltered.toString());
                productAdapter.setProdotti(prodottiFiltered.toArray(new Prodotto[prodottiFiltered.size()]));
                recyclerView.setAdapter(productAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    void updateRecycleView(ActivityResult result) {
        Intent intent = result.getData();
        Prodotto prodotto = (Prodotto) intent.getSerializableExtra("prodotto");
        int position = intent.getIntExtra("position", 0);
        prodotti[position].setGiacenza(prodotto.getGiacenza());
        productAdapter.setProdotti(prodotti);
        recyclerView.setAdapter(productAdapter);

    }

}