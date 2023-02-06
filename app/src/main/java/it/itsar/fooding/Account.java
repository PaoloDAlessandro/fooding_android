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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.util.ArrayList;

public class Account extends Fragment {

    private LocalStorageManager localStorageManager = new LocalStorageManager();


    public FragmentManager fragmentManager;
    private MyProperties myProperties = MyProperties.getInstance();

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private ImageButton logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentManager = getParentFragmentManager();
        logoutButton = getActivity().findViewById(R.id.logoutIcon);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                localStorageManager.deleteFile(new File(getActivity().getFilesDir() + LocalStorageManager.USER_PRODUCT_FILE_NAME));
                localStorageManager.deleteFile(new File(getActivity().getFilesDir() + LocalStorageManager.ULTIME_AGGIUNTE_FILE_NAME));
                myProperties.setUserUsername(null);
                myProperties.setUserProdotti(new ArrayList<>());
                myProperties.setUltimeAggiunte(new ArrayList<>());
                configFragmentManager(Login.class);
            }
        });

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