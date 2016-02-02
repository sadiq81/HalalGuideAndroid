package dk.eazyit.halalguide.ui.settings.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import dk.eazyit.halalguide.R;
import dk.eazyit.halalguide.extensions.HGActivity;
import dk.eazyit.halalguide.ui.common.activity.HGBaseActivity;

@HGActivity(layout = R.layout.activity_hgsettings, swipeEnabled = false, screenName = "settings")
public class HGSettingsActivityHG extends HGBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hgsettings, menu);
        return true;
    }

}
