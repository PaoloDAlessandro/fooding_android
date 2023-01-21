package it.itsar.fooding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    public FragmentManager fragmentManager = getSupportFragmentManager();
    private final AuthStorageManager authStorageManager = new AuthStorageManager();

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    private User userFromFile;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userCollection = db.collection("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomMenu);
        userFromFile = authStorageManager.backupFromFile(getFilesDir() + AuthStorageManager.AUTH_FILE_NAME);
        if (firebaseUser == null) {
            configFragmentManager(Login.class);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.homeButton:
                    if (firebaseAuth.getCurrentUser() != null) {
                        configFragmentManager(Home.class);
                    } else {
                        configFragmentManager(Login.class);
                    }
                    break;

                case R.id.pantryButton:
                    if (firebaseAuth.getCurrentUser() != null) {
                        configFragmentManager(Pantry.class);
                    } else {
                        configFragmentManager(Login.class);
                    }
                    break;

                case R.id.reciptsButton:
                    if (firebaseAuth.getCurrentUser() != null) {
                        configFragmentManager(Recipes.class);
                    } else {
                        configFragmentManager(Login.class);
                    }
                    break;

                case R.id.userButton:
                    if (firebaseAuth.getCurrentUser() != null) {
                        configFragmentManager(Account.class);
                    } else {
                        configFragmentManager(Login.class);
                    }
                    break;
            }

            return true;
        });
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
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }
}