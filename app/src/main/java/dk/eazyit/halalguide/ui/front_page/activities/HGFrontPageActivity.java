package dk.eazyit.halalguide.ui.front_page.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import dk.eazyit.halalguide.R;
import dk.eazyit.halalguide.domain.HGLocation;
import dk.eazyit.halalguide.domain.HGLocationType;
import dk.eazyit.halalguide.extensions.HGActivity;
import dk.eazyit.halalguide.ui.common.activity.HGBaseActivity;
import dk.eazyit.halalguide.ui.common.adapters.HGLocationAdapter;
import dk.eazyit.halalguide.ui.common.models.HGBaseViewModel;
import dk.eazyit.halalguide.ui.front_page.models.HGFrontPageViewModel;
import dk.eazyit.halalguide.ui.location.activties.HGLocationActivityHG;
import dk.eazyit.halalguide.ui.location_details.activities.HGLocationDetailsActivityHG;
import dk.eazyit.halalguide.ui.settings.activity.HGSettingsActivityHG;
import rx.functions.Action1;

@HGActivity(layout = R.layout.activity_front_page, swipeEnabled = true, screenName = "frontpage")
public class HGFrontPageActivity extends HGBaseActivity implements AdapterView.OnItemClickListener {

    @Bind(R.id.shop_container)
    LinearLayout shop_container;

    @Bind(R.id.mosque_container)
    LinearLayout chat_container;

    @Bind(R.id.dining_container)
    LinearLayout dining_container;

    @Bind(R.id.chat_container)
    LinearLayout mosque_container;

    @Bind(R.id.favorites_container)
    LinearLayout favorites_container;

    @Bind(R.id.latest_updates_list)
    ListView latest_updates_list;

    HGFrontPageViewModel viewModel = HGFrontPageViewModel.create();

    HGLocationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
        } else {
            // Probably initialize members with default values for a new instance
        }

        latest_updates_list.setAdapter(adapter = new HGLocationAdapter(this, new ArrayList<HGLocation>()));
        latest_updates_list.setOnItemClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        subscribers.add(viewModel.locationsSubject.subscribe(new Action1<List<HGLocation>>() {
            @Override
            public void call(List<HGLocation> hgLocations) {
                adapter.clear();
                adapter.addAll(hgLocations);
                adapter.notifyDataSetChanged();
            }
        }));

        viewModel.refreshLocation();
    }

    @Override
    public void onRefresh() {
        viewModel.refreshLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_front_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings: {
                Intent activity = new Intent(this, HGSettingsActivityHG.class);
                startActivity(activity);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    public void goToLocations(View v) {

        Intent activity = new Intent(this, HGLocationActivityHG.class);
        int locationType = 0;
        switch (v.getId()) {
            case R.id.shop_container: {
                locationType = HGLocationType.Shop.getId().intValue();
                break;
            }
            case R.id.dining_container: {
                locationType = HGLocationType.Dining.getId().intValue();
                break;
            }
            case R.id.chat_container: {
                locationType = HGLocationType.Mosque.getId().intValue();
                break;
            }
            default: {

            }
        }
        activity.putExtra(HGLocationType.intent, locationType);
        startActivity(activity);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HGLocation location = adapter.getItem(position);
        Intent activity = new Intent(this, HGLocationDetailsActivityHG.class);
        activity.putExtra(HGLocation.INTENT, location.getObjectId());
        startActivity(activity);
    }

    @Override
    protected HGBaseViewModel getViewModel() {
        return viewModel;
    }
}
