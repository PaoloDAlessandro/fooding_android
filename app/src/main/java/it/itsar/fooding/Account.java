package it.itsar.fooding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class Account extends Fragment {

    private LocalStorageManager localStorageManager = new LocalStorageManager();


    public FragmentManager fragmentManager;
    private MyProperties myProperties = MyProperties.getInstance();

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private ImageButton logoutButton;
    private TextView userGreating;
    private ImageView userImage;

    private ImageButton shoppingListButton;

    private BottomNavigationView bottomNavigationView;


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
        bottomNavigationView = getActivity().findViewById(R.id.bottomMenu);
        userGreating = view.findViewById(R.id.userName);
        userImage = view.findViewById(R.id.userImage);
        shoppingListButton = view.findViewById(R.id.listaSpesa);

        if (firebaseAuth.getCurrentUser().getPhotoUrl() != null) {
            downloadImage(Objects.requireNonNull(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getPhotoUrl()).toString());
        }


        String username = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName();

        if (username != null) {
            int spaceIndex = username.indexOf(" ");
            if (spaceIndex != -1) {
                userGreating.setText("Ciao " + username.substring(0, spaceIndex) + "!");
            } else if (myProperties.getUserUsername() != null) {
                username = myProperties.getUserUsername();
                userGreating.setText("Bentornato " + username + "!");
            }
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                localStorageManager.deleteFile(new File(getActivity().getFilesDir() + LocalStorageManager.USER_PRODUCT_FILE_NAME));
                localStorageManager.deleteFile(new File(getActivity().getFilesDir() + LocalStorageManager.ULTIME_AGGIUNTE_FILE_NAME));
                myProperties.setUserUsername(null);
                myProperties.setUserProdotti(new ArrayList<>());
                myProperties.setUltimeAggiunte(new ArrayList<>());
                bottomNavigationView.setVisibility(View.GONE);
                configFragmentManager(Login.class);
            }
        });

        shoppingListButton.setOnClickListener(view1 -> {
            configFragmentManager(ShoppingList.class);
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

    private void downloadImage(String imageUri) {
        Picasso.get()
                .load(imageUri)
                .into(userImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess(){
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
    }

}