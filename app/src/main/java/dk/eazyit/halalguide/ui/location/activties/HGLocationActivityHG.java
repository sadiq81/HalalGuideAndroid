package dk.eazyit.halalguide.ui.location.activties;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import dk.eazyit.halalguide.R;
import dk.eazyit.halalguide.domain.HGLocation;
import dk.eazyit.halalguide.domain.HGLocationType;
import dk.eazyit.halalguide.extensions.HGActivity;
import dk.eazyit.halalguide.ui.common.activity.HGBaseActivity;
import dk.eazyit.halalguide.ui.common.adapters.HGLocationAdapter;
import dk.eazyit.halalguide.ui.common.models.HGBaseViewModel;
import dk.eazyit.halalguide.ui.location.models.HGLocationViewModel;
import dk.eazyit.halalguide.ui.location_details.activities.HGLocationDetailsActivityHG;
import rx.functions.Action1;

@HGActivity(layout = R.layout.activity_location, swipeEnabled = true, screenName = "location")
public class HGLocationActivityHG extends HGBaseActivity implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private static int threshold = 0;
    HGLocationViewModel viewModel = HGLocationViewModel.create();
    @Bind(R.id.add_new)
    FloatingActionButton add_new;
    @Bind(R.id.locations)
    ListView locations;
    HGLocationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            viewModel.setLocationType(HGLocationType.getLocation(savedInstanceState.getInt(HGLocationType.intent, -1)));
        } else {
            viewModel.setLocationType(getType());
        }
        setTitle(viewModel.getLocationType().getResource_id());

        locations.setAdapter(adapter = new HGLocationAdapter(this, new ArrayList<HGLocation>()));
        locations.setOnItemClickListener(this);
        locations.setOnScrollListener(this);

        add_new.attachToListView(locations);

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
    protected Map<String, String> getScreenDimensions() {
        Map<String, String> dimensions = super.getScreenDimensions();
        dimensions.put("type", getType().toString());
        return dimensions;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(HGLocationType.intent, viewModel.getLocationType().getId().intValue());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_filter: {
                Intent activity = new Intent(this, HGFilterActivityHG.class);
                activity.putExtra(HGLocationType.intent, viewModel.getLocationType().getId().intValue());
                startActivity(activity);
                break;
            }
//            case R.id.action_categories: {
//                break;
//            }
//            case R.id.action_map: {
//                break;
//            }
        }
        return super.onOptionsItemSelected(item);
    }

    private HGLocationType getType() {
        return HGLocationType.getLocation(getIntent().getIntExtra(HGLocationType.intent, 0));
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == SCROLL_STATE_IDLE) {
            if (locations.getLastVisiblePosition() >= locations.getCount() - 1 - threshold) {
                viewModel.nextPage();
                viewModel.refreshLocation();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    @Override
    public void onRefresh() {
        viewModel.resetPage();
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
