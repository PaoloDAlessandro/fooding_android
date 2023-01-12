package it.itsar.fooding;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signin extends Fragment {

    private TextView accediButton;
    private Button registratiButton;
    private EditText usernameInput;
    private EditText passwordInput;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference user = db.collection("user");

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

        fragmentManager = getParentFragmentManager();
        transaction = fragmentManager.beginTransaction();

        registratiButton.setOnClickListener(view1 -> {
            Map<String , Object> data = new HashMap<>();
            data.put("username", usernameInput.getText().toString());
            data.put("password", passwordInput.getText().toString());

            user.add(data)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Log.d("Result: ", "SUCCESS");
                            returnToLogin();
                        }
                    }).addOnFailureListener(e -> {
                        Log.d("Result: ", e.toString());
                    });
        });

        accediButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                returnToLogin();
                return true;
            }
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