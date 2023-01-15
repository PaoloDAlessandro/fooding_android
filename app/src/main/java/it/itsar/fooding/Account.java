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
import android.widget.ImageButton;
import android.widget.ImageView;

import org.checkerframework.checker.units.qual.A;

import java.io.File;

public class Account extends Fragment {

    private LocalStorageManager localStorageManager = new LocalStorageManager();

    private final AuthStorageManager authStorageManager = new AuthStorageManager();

    public FragmentManager fragmentManager;


    private ImageButton logoutButton;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentManager = getParentFragmentManager();
        logoutButton = getActivity().findViewById(R.id.logoutIcon);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authStorageManager.deleteFile(new File(getActivity().getFilesDir() + AuthStorageManager.AUTH_FILE_NAME));
                MainActivity.isLogged = false;
                localStorageManager.deleteFile(new File(getActivity().getFilesDir() + LocalStorageManager.LOCAL_FILE_NAME));
                configFragmentManager(Login.class);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    private void configFragmentManager(Class fragmentClass) {
        getActivity().findViewById(R.id.logoutIcon).setVisibility(View.INVISIBLE);
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragmentClass, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }

}