package it.itsar.fooding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SigninSuccess extends Fragment {

    private Button successButton;
    FragmentManager fragmentManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_signin_success, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentManager = getParentFragmentManager();
        successButton = view.findViewById(R.id.successButton);

        successButton.setOnClickListener(view1 -> {
            returnToLogin();
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