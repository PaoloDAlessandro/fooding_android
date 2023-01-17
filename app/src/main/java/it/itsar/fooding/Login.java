package it.itsar.fooding;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.IOException;

public class Login extends Fragment {

    private TextView registratiButton;
    private EditText emailInput;
    private EditText passwordInput;
    private MaterialCardView emailInputCard;
    private MaterialCardView passwordInputCard;
    private Button accediButton;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private final AuthStorageManager authStorageManager = new AuthStorageManager();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userCollection = db.collection("user");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailInput = view.findViewById(R.id.emailInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        accediButton = view.findViewById(R.id.accediButton);
        emailInputCard = view.findViewById(R.id.emailInputCard);
        passwordInputCard = view.findViewById(R.id.passwordInputCard);
        registratiButton = view.findViewById(R.id.registratiTextClickable);

        fragmentManager = getParentFragmentManager();
        transaction = fragmentManager.beginTransaction();

        //checkUserAlreadyLogged();

        accediButton.setOnClickListener(view1 -> {
            String emailFromInput = emailInput.getText().toString();
            String passwordFromInput = passwordInput.getText().toString();

            emailInputCard.setStrokeColor(Color.parseColor("#d4d4d4"));
            passwordInputCard.setStrokeColor(Color.parseColor("#d4d4d4"));


            auth.signInWithEmailAndPassword(emailFromInput, passwordFromInput)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if (auth.getCurrentUser().isEmailVerified()) {
                                            Log.d("EMAIL: ", auth.getCurrentUser().getEmail());
                                            goToHome();
                                        } else {
                                            Toast.makeText(getActivity(), "Please verify your email address", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

            /*
            userCollection.whereEqualTo("username", usernameFromInput)
                    .whereEqualTo("password", passwordFromInput)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                if(task.getResult().size() == 0) {
                                    Snackbar.make(view, "Utente non trovato!", Snackbar.LENGTH_SHORT).show();
                                    emailInputCard.setStrokeColor(Color.parseColor("#ff0303"));
                                    passwordInputCard.setStrokeColor(Color.parseColor("#ff0303"));
                                }
                                else {
                                    emailInputCard.setStrokeColor(Color.parseColor("#d4d4d4"));
                                    passwordInputCard.setStrokeColor(Color.parseColor("#d4d4d4"));
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("Result: ", document.getId() + " ==> " + document.getData());
                                        Snackbar.make(view, "Loggato!", Snackbar.LENGTH_SHORT).show();
                                        User loggedUser = new User(usernameFromInput, passwordFromInput);
                                        try {
                                            authStorageManager.backupToFile(new File(getActivity().getFilesDir(), AuthStorageManager.AUTH_FILE_NAME), loggedUser);
                                            MainActivity.isLogged = true;
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        goToHome();
                                    }
                                }
                            } else {
                                Log.d("Error:", "Error getting documents: " + task.getException());
                            }
                        }
                    });

             */
        });

        registratiButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, Signin.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
                return true;
            }
        });
    }
    /*
    void checkUserAlreadyLogged() {
        User user = authStorageManager.backupFromFile(getActivity().getFilesDir() + AuthStorageManager.AUTH_FILE_NAME);

        userCollection.whereEqualTo("username", user.getUsername())
                .whereEqualTo("password", user.getPassword())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("STATUS: ", "USER ALREADY LOGGED");
                                goToHome();
                                Log.d("Result: ", document.getId() + " ==> " + document.getData());
                            }
                        } else {
                            Log.d("Error:", "Error getting documents: " + task.getException());
                        }
                    }
                });
    }

     */

    void goToHome() {
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, Home.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }
}
