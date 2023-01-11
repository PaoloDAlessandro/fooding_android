package it.itsar.fooding;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends Fragment {

    private EditText usernameInput;
    private EditText passwordInput;
    private MaterialCardView usernameInputCard;
    private MaterialCardView passwordInputCard;
    private Button accediButton;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference user = db.collection("user");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usernameInput = view.findViewById(R.id.usernameInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        accediButton = view.findViewById(R.id.accediButton);
        usernameInputCard = view.findViewById(R.id.usernameInputCard);
        passwordInputCard = view.findViewById(R.id.passwordInputCard);


        accediButton.setOnClickListener(view1 -> {
            usernameInputCard.setStrokeColor(Color.parseColor("#d4d4d4"));
            passwordInputCard.setStrokeColor(Color.parseColor("#d4d4d4"));
            user.whereEqualTo("username", usernameInput.getText().toString())
                    .whereEqualTo("password", passwordInput.getText().toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                if(task.getResult().size() == 0) {
                                    Log.d("ERROR:", "Utente non trovato!");
                                    Snackbar.make(view, "Utente non trovato!", Snackbar.LENGTH_SHORT).show();
                                    usernameInputCard.setStrokeColor(Color.parseColor("#ff0303"));
                                    passwordInputCard.setStrokeColor(Color.parseColor("#ff0303"));
                                }
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("Result: ", document.getId() + " ==> " + document.getData());
                                    Snackbar.make(view, "Loggato!", Snackbar.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d("Error:", "Error getting documents: " + task.getException());
                            }
                        }
                    });
        });
    }


}