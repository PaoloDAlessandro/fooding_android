package it.itsar.fooding;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Signin extends Fragment {

    private TextView accediButton;
    private Button registratiButton;
    private EditText usernameInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private TextView usernameError;
    private TextView passwordError;
    private TextView confirmPasswordError;
    private MaterialCardView usernameInputCard;
    private MaterialCardView passwordInputCard;
    private MaterialCardView confirmPasswordInputCard;

    private final AuthStorageManager authStorageManager = new AuthStorageManager();
    private User user;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference userCollection = db.collection("user");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accediButton = view.findViewById(R.id.accediTextClickable);
        registratiButton = view.findViewById(R.id.registratiButton);
        usernameInput = view.findViewById(R.id.usernameInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        confirmPasswordInput = view.findViewById(R.id.confirmPasswordInput);
        usernameError = view.findViewById(R.id.usernameError);
        passwordError = view.findViewById(R.id.passwordError);
        confirmPasswordError = view.findViewById(R.id.confirmPasswordError);
        usernameInputCard = view.findViewById(R.id.usernameInputCard);
        passwordInputCard = view.findViewById(R.id.passwordInputCard);
        confirmPasswordInputCard = view.findViewById(R.id.confirmPasswordInputCard);

        fragmentManager = getParentFragmentManager();
        transaction = fragmentManager.beginTransaction();

        registratiButton.setOnClickListener(view1 -> {

            if (usernameInput.getText().length() < 5) {
                displayInputError(usernameInputCard, usernameError);
            }

            if (passwordInput.getText().length() < 8) {
                displayInputError(passwordInputCard, passwordError);
            }

            if (!confirmPasswordInput.getText().toString().equals(passwordInput.getText().toString())) {
                displayInputError(confirmPasswordInputCard, confirmPasswordError);
            }

            if (usernameInput.getText().length() >= 5 && passwordInput.getText().length() >= 8 && passwordInput.getText().toString().equals(confirmPasswordInput.getText().toString())) {
                addUserToDb();
                user = new User(usernameInput.getText().toString(), passwordInput.getText().toString());
                try {
                    writeOnAuthStorage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        accediButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                returnToLogin();
                return true;
            }
        });

        setTextChangeListenerForInput(usernameInput, usernameInputCard, usernameError);
        setTextChangeListenerForInput(passwordInput, passwordInputCard, passwordError);
        setTextChangeListenerForInput(confirmPasswordInput, confirmPasswordInputCard, confirmPasswordError);

    }

    void writeOnAuthStorage() throws IOException {
        File file = new File(getActivity().getFilesDir(), AuthStorageManager.AUTH_FILE_NAME);
        authStorageManager.backupToFile(file, user);
        User user1 = authStorageManager.backupFromFile(getActivity().getFilesDir() + AuthStorageManager.AUTH_FILE_NAME);
        Log.d("USER FROM FILE: ", user1.toString());
    }

    void setTextChangeListenerForInput(EditText input, MaterialCardView inputCard, TextView inputError) {
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hideInputError(inputCard, inputError);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    void displayInputError(MaterialCardView inputCard, TextView inputError) {
        inputCard.setStrokeColor(Color.RED);
        inputError.setVisibility(View.VISIBLE);
    }

    void hideInputError(MaterialCardView inputCard, TextView inputError) {
        inputCard.setStrokeColor(Color.parseColor("#d4d4d4"));
        inputError.setVisibility(View.GONE);
    }

    void addUserToDb() {
        Map<String , Object> data = new HashMap<>();
        data.put("username", usernameInput.getText().toString());
        data.put("password", passwordInput.getText().toString());

        userCollection.add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Log.d("Result: ", "SUCCESS");
                        returnToLogin();
                    }
                }).addOnFailureListener(e -> {
                    Log.d("Result: ", e.toString());
                });
    }



    void returnToLogin() {
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, Login.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }
}