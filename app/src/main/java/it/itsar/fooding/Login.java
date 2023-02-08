package it.itsar.fooding;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.IntentSender;
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

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Login extends Fragment {

    private TextView registratiButton;
    private EditText emailInput;
    private EditText passwordInput;
    private MaterialCardView emailInputCard;
    private MaterialCardView passwordInputCard;
    private MaterialCardView googleLoginButton;
    private TextView resetPasswordClickable;
    private Button accediButton;
    private TextView loginError;
    private BottomNavigationView bottomNavigationView;


    private FirebaseAuth mAuth;

    private static final int REQ_ONE_TAP = 2;


    FirebaseAuth auth = FirebaseAuth.getInstance();

    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

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
    public void onStart() {
        super.onStart();
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
        googleLoginButton = view.findViewById(R.id.loginGoogleBox);
        resetPasswordClickable = view.findViewById(R.id.resetPasswordTextClickable);
        bottomNavigationView = getActivity().findViewById(R.id.bottomMenu);

        mAuth = FirebaseAuth.getInstance();

        oneTapClient = Identity.getSignInClient(getContext());
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

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
                                        bottomNavigationView.setVisibility(View.VISIBLE);
                                        gotToFragment(Home.class);
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

        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneTapClient.beginSignIn(signInRequest)
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<BeginSignInResult>() {
                            @Override
                            public void onSuccess(BeginSignInResult result) {
                                try {
                                    startIntentSenderForResult(result.getPendingIntent().getIntentSender(), REQ_ONE_TAP, null, 0, 0, 0, null);
                                } catch (IntentSender.SendIntentException e) {
                                    Log.d("ERROR: ", e.getLocalizedMessage());
                                }
                            }
                        })
                        .addOnFailureListener(getActivity(), new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("ERROR: ", e.getLocalizedMessage());
                            }
                        });
            }
        });

        resetPasswordClickable.setOnTouchListener((view12, motionEvent) -> {
            gotToFragment(ResetPassword.class);
            return true;
        });

        registratiButton.setOnTouchListener((view13, motionEvent) -> {
            gotToFragment(Signin.class);
            return true;
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_ONE_TAP:
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();
                    if (idToken != null) {
                        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                        mAuth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Log.d("CREDENTIAL: ", credential.getDisplayName() + " " + user.getEmail());
                                            addUserToUsersCollection(credential.getDisplayName(), user.getEmail());
                                            bottomNavigationView.setVisibility(View.VISIBLE);
                                            gotToFragment(Home.class);

                                            Log.d("STATUS: ", "LOGGED");
                                        } else {
                                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                                        }
                                    }
                                });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }

                break;
        }
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

    void addUserToUsersCollection(String username, String email) {
        Map<String , Object> data = new HashMap<>();
        data.put("username", username);
        data.put("email", email);

        userCollection.whereEqualTo("username", username)
                        .whereEqualTo("email", email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && task.getResult().size() != 0) {
                                    Log.d("STATUS: ", "USER ALREADY EXIST");
                                } else {
                                    Log.d("STATUS: ", "NEW USER");
                                    userCollection.add(data)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.d("Result: ", "Success");
                                                }
                                            });
                                }
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

    void gotToFragment(Class destinationClass) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, destinationClass, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }
}
