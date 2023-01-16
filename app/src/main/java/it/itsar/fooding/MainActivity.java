package it.itsar.fooding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    public FragmentManager fragmentManager = getSupportFragmentManager();
    private final MyProperties myProperties = MyProperties.getInstance();
    private LocalStorageManager localStorageManager = new LocalStorageManager();
    private final AuthStorageManager authStorageManager = new AuthStorageManager();

    public static boolean isLogged;

    User userFromFile;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userCollection = db.collection("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomMenu);
        userFromFile = authStorageManager.backupFromFile(getFilesDir() + AuthStorageManager.AUTH_FILE_NAME);
        checkUserIsLogged();

        //setLocalStorageManager();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.homeButton:
                    if (isLogged) {
                        configFragmentManager(Home.class);
                    } else {
                        configFragmentManager(Login.class);
                    }
                    break;

                case R.id.pantryButton:
                    if (isLogged) {
                        configFragmentManager(Pantry.class);
                    } else {
                        configFragmentManager(Login.class);
                        Toast.makeText(this, "wow", Toast.LENGTH_SHORT);
                    }
                    break;

                case R.id.userButton:
                    if (isLogged) {
                        configFragmentManager(Account.class);
                    } else {
                        configFragmentManager(Login.class);
                    }
                    break;
            }

            return true;
        });
    }

    private void setLocalStorageManager() {
        try {
            localStorageManager.backupFromFile(getFilesDir() + "/storage.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(myProperties.getUserProdotti().size() == 0) {
            /*
            try {
                localStorageManager.backupToFile(new File(getFilesDir() + "/storage.txt"), prod);
            } catch (IOException e) {
                e.printStackTrace();
            }

             */
        }
    }

    private void configFragmentManager(Class fragmentClass) {
        if (fragmentClass == Account.class) {
            findViewById(R.id.logoutIcon).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.logoutIcon).setVisibility(View.INVISIBLE);
        }
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragmentClass, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserIsLogged();
    }

    void checkUserIsLogged() {
        String usernameFromFile = userFromFile.getUsername();
        String passwordFromFile = userFromFile.getPassword();
            userCollection.whereEqualTo("username", usernameFromFile)
                    .whereEqualTo("password", passwordFromFile)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            if (task.getResult().size() == 0) {
                                isLogged = false;
                                configFragmentManager(Login.class);
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                isLogged = true;
                                Log.d("Result: ", document.getId() + " ==> " + document.getData());
                            }
                        } else {
                            Log.d("Error:", "Error getting documents: " + task.getException());
                        }
                    }
                });
    }
}