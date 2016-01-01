package dk.eazyit.halalguide.ui.common.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import dk.eazyit.halalguide.HGSettings;
import dk.eazyit.halalguide.R;
import dk.eazyit.halalguide.domain.HGCategory;
import dk.eazyit.halalguide.domain.HGDiningCategory;
import dk.eazyit.halalguide.domain.HGLocationType;
import dk.eazyit.halalguide.domain.HGShopCategory;
import dk.eazyit.halalguide.extensions.HGActivity;
import dk.eazyit.halalguide.ui.common.adapters.HGCategoryListAdapter;

@HGActivity(layout = R.layout.activity_hgcategory, swipeEnabled = false, screenName = "category")
public class HGCategoryActivityHG extends HGBaseActivity {

    @Bind(R.id.category_list)
    ListView category_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        List<HGCategory> categories = new ArrayList<>();
        List<HGCategory> selected = new ArrayList<>();
        switch (getType()) {
            case Dining: {
                Collections.addAll(categories, HGDiningCategory.values());
                selected.addAll(HGSettings.getInstance().getCategoriesFilter());
                break;
            }
            case Shop: {
                Collections.addAll(categories, HGShopCategory.values());
                selected.addAll(HGSettings.getInstance().getShopCategoriesFilter());
                break;
            }
        }
        HGCategoryListAdapter adapter = new HGCategoryListAdapter(this, categories, selected);
        category_list.setAdapter(adapter);

    }

    @Override
    protected Map<String, String> getScreenDimensions() {
        Map<String, String> dimensions = super.getScreenDimensions();
        dimensions.put("type", getType().toString());
        return dimensions;
    }

    private HGLocationType getType() {
        return HGLocationType.getLocation(getIntent().getIntExtra(HGLocationType.intent, 0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hgcategory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
