package pl.kuc_industries.warsawnavihelper.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.leakcanary.LeakCanary;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.kuc_industries.warsawnavihelper.Fragments.FirstOpenFragment;
import pl.kuc_industries.warsawnavihelper.R;


public class FirstOpeningActivity extends AppCompatActivity {
    @BindView(R.id.bank_spinner) Spinner mBankSpinner;
    @BindView(R.id.stop_spinner) Spinner mStopSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LeakCanary.install(getApplication());

        setContentView(R.layout.activity_first_open);
        ButterKnife.bind(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if(!previouslyStarted) {
            Toast.makeText(getApplicationContext(), "First start!", Toast.LENGTH_LONG);
        }
        else {
            Toast.makeText(getApplicationContext(), "App was previously started!", Toast.LENGTH_LONG);
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void confirmButtonHandler(View view) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = prefs.edit();

        edit.putString(getString(R.string.pref_default_bank), mBankSpinner.getSelectedItem().toString());
        edit.putString(getString(R.string.pref_default_stop), mStopSpinner.getSelectedItem().toString());
        //TODO: uncomment this for release build
        //edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
        edit.commit();

        Toast.makeText(getApplicationContext(), "Settings saved", Toast.LENGTH_LONG);
        startActivity(new Intent(this, MainActivity.class));
    }
}
