package it.itsar.fooding;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class ResetPassword extends Fragment {

    private EditText emailInput;
    private MaterialCardView emailInputCard;
    private Button recuperaButton;
    private TextView emailInputError;
    private TextView loginTextClickable;

    private FirestoreManager firestoreManager = new FirestoreManager();

    private FragmentManager fragmentManager;

    private FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_reset_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailInput = view.findViewById(R.id.emailInput);
        recuperaButton = view.findViewById(R.id.recureButton);
        emailInputCard = view.findViewById(R.id.emailInputCard);
        emailInputError = view.findViewById(R.id.emailError);
        loginTextClickable = view.findViewById(R.id.loginTextClickable);

        fragmentManager = getParentFragmentManager();


        recuperaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmailValid(emailInput.getText().toString())) {
                    hideInputError(emailInputCard, emailInputError);
                    firestoreManager.searchUserEmail(emailInput.getText().toString(), new FirestoreManager.SearchUserEmail() {
                        @Override
                        public void onSuccess() {
                            auth.sendPasswordResetEmail(emailInput.getText().toString());
                            gotToFragment(ResetPasswordComplete.class);
                        }

                        @Override
                        public void onFailure() {
                            displayInputError(emailInputCard, emailInputError);
                        }
                    });
                    auth.sendPasswordResetEmail(emailInput.getText().toString());
                } else {
                    displayInputError(emailInputCard, emailInputError);
                }
            }
        });

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hideInputError(emailInputCard, emailInputError);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        loginTextClickable.setOnTouchListener((view1, event)-> {
            gotToFragment(Login.class);
            return true;
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

    void gotToFragment(Class destinationClass) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, destinationClass, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }

    boolean isEmailValid(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }
}