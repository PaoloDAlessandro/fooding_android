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

public class ResetPasswordComplete extends Fragment {

    private Button accediButton;

    private FragmentManager fragmentManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_password_complete, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentManager = getParentFragmentManager();

        accediButton = view.findViewById(R.id.successButton);

        accediButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotToFragment(Login.class);
            }
        });
    }

    void gotToFragment(Class destinationClass) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, destinationClass, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }
}