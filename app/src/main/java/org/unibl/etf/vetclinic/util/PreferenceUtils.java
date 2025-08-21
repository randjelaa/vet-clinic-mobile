package org.unibl.etf.vetclinic.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import org.unibl.etf.vetclinic.data.entities.UserPreferences;
import org.unibl.etf.vetclinic.viewmodel.UserPreferencesViewModel;

import java.util.Locale;

public class PreferenceUtils {

    public static final String PREFS_NAME = "VetClinicPrefs";

    /**
     * Pozvati npr. u LoginActivity kad korisnik uspješno uđe u aplikaciju
     */
    public static void loadPreferencesFromDb(Activity activity, int userId, Runnable onDone) {
        UserPreferencesViewModel viewModel = new ViewModelProvider((ViewModelStoreOwner) activity)
                .get(UserPreferencesViewModel.class);

        viewModel.getPreferencesForUser(userId, prefs -> {
            if (prefs != null) {
                SharedPreferences sp = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                sp.edit()
                        .putString("theme", prefs.Theme != null ? prefs.Theme : "light")
                        .putString("language", prefs.Language != null ? prefs.Language : "en")
                        .apply();

                activity.runOnUiThread(() -> {
                    applyUserPreferences(activity);
                    if (onDone != null) onDone.run();
                });
            }
        });
    }

    /**
     * Pozvati da se primijene preferencije iz SharedPreferences (npr. u MainActivity.onCreate)
     */
    public static void applyUserPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String theme = prefs.getString("theme", "light");
        String language = prefs.getString("language", "en");

        // Tema
        if ("dark".equals(theme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Jezik
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    /**
     * Ažurira preference u bazi i SharedPreferences
     */
    public static void updatePreferences(Activity activity, int userId, String theme, String language) {
        // Update SharedPreferences
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putString("theme", theme)
                .putString("language", language)
                .apply();

        // Update u bazi
        UserPreferencesViewModel viewModel = new ViewModelProvider((ViewModelStoreOwner) activity)
                .get(UserPreferencesViewModel.class);

        viewModel.getPreferencesForUser(userId, prefsFromDb -> {
            if (prefsFromDb != null) {
                prefsFromDb.Theme = theme;
                prefsFromDb.Language = language;
                viewModel.update(prefsFromDb);
            }
        });
    }
}
