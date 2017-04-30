package pl.kuc_industries.warsawnavihelper;

import android.os.Build;
import android.widget.Spinner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import pl.kuc_industries.warsawnavihelper.Activities.FirstOpeningActivity;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
        packageName = "pl.kuc_industries.warsawnavihelper")
@RunWith(RobolectricTestRunner.class)
public class FirstOpeningActivityTest {
    private FirstOpeningActivity activity;

    @Before
    public void setup() {
        activity = Robolectric.setupActivity(FirstOpeningActivity.class);
    }

    @Test
    public void validateTextViewContent() {
        Spinner bankSpinner = (Spinner) activity.findViewById(R.id.bank_spinner);
        assertNotNull("Spinner could not be found", bankSpinner);
        assertTrue("Spinner's first element should be Alior Bank",
                "Alior Bank".equals(bankSpinner.getItemAtPosition(0)));
    }
}
