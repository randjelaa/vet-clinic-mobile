package org.unibl.etf.vetclinic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

public class WelcomeActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "VetClinicPrefs";
    public static final String KEY_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("VetClinicPrefs", MODE_PRIVATE);
        if (prefs.getBoolean("isLoggedIn", false)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_welcome);
    }
}
