package it.itsar.fooding;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends Fragment {

    private TextView registratiButton;
    private EditText emailInput;
    private EditText passwordInput;
    private MaterialCardView emailInputCard;
    private MaterialCardView passwordInputCard;
    private Button accediButton;
    private TextView loginError;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

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

        loginError = view.findViewById(R.id.loginError);

        fragmentManager = getParentFragmentManager();
        transaction = fragmentManager.beginTransaction();
        setTextChangeListenerForInput(emailInput);
        setTextChangeListenerForInput(passwordInput);
        //checkUserAlreadyLogged();

        accediButton.setOnClickListener(view1 -> {
            String emailFromInput = emailInput.getText().toString();
            String passwordFromInput = passwordInput.getText().toString();

            emailInputCard.setStrokeColor(Color.parseColor("#d4d4d4"));
            passwordInputCard.setStrokeColor(Color.parseColor("#d4d4d4"));

            if (emailFromInput.length() > 5 && passwordFromInput.length() > 5) {
                auth.signInWithEmailAndPassword(emailFromInput.toLowerCase(), passwordFromInput)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (auth.getCurrentUser().isEmailVerified()) {
                                        Log.d("EMAIL: ", auth.getCurrentUser().getEmail());
                                        goToHome();
                                    } else {
                                        displayLoginError("Per favore verifica il tuo indirizzo email");
                                    }
                                } else {
                                    displayLoginError("Email o password non corretti");
                                }
                            }
                        });
            } else {
                displayLoginError("Inserisci email e password");
            }

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

    void setTextChangeListenerForInput(EditText input) {
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hideInputError();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    void hideInputError() {
        emailInputCard.setStrokeColor(Color.parseColor("#d4d4d4"));
        passwordInputCard.setStrokeColor(Color.parseColor("#d4d4d4"));
        loginError.setVisibility(View.GONE);
    }

    void displayLoginError(String erroreText) {
        loginError.setText(erroreText);
        loginError.setVisibility(View.VISIBLE);
        emailInputCard.setStrokeColor(Color.RED);
        passwordInputCard.setStrokeColor(Color.RED);
    }

    void goToHome() {
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, Home.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }
}
