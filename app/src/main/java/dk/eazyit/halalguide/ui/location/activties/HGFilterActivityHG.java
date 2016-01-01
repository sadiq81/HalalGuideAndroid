package dk.eazyit.halalguide.ui.location.activties;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import dk.eazyit.halalguide.HGSettings;
import dk.eazyit.halalguide.R;
import dk.eazyit.halalguide.domain.HGDiningCategory;
import dk.eazyit.halalguide.domain.HGLanguage;
import dk.eazyit.halalguide.domain.HGLocationType;
import dk.eazyit.halalguide.domain.HGShopCategory;
import dk.eazyit.halalguide.extensions.HGActivity;
import dk.eazyit.halalguide.ui.common.activity.HGBaseActivity;
import dk.eazyit.halalguide.ui.common.activity.HGCategoryActivityHG;
import dk.eazyit.halalguide.ui.common.adapters.HGLanguageAdapter;

@HGActivity(layout = R.layout.activity_hgfilter, swipeEnabled = false, screenName = "filter")
public class HGFilterActivityHG extends HGBaseActivity {

    @Bind(R.id.maximum_distance_value)
    TextView maximum_distance_value;

    @Bind(R.id.maximum_distance_slider)
    SeekBar maximum_distance_slider;

    @Bind(R.id.switch_view)
    RelativeLayout switch_view;

    @Bind(R.id.category_view)
    RelativeLayout category_view;

    @Bind(R.id.non_halal_switch)
    Switch non_halal_switch;
    @Bind(R.id.alcohol_switch)
    Switch alcohol_switch;
    @Bind(R.id.pork_switch)
    Switch pork_switch;

    @Bind(R.id.categories_label)
    TextView categories_label;

    @Bind(R.id.categories_count)
    TextView categories_count;

    @Bind(R.id.choose)
    Button choose;
    @Bind(R.id.reset)
    Button reset;

    @Bind(R.id.language)
    Spinner language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupLocationType();
        setupUpCountLabels();
        setupSliderProgress();
    }

    @Override
    protected Map<String, String> getScreenDimensions() {
        Map<String, String> dimensions = super.getScreenDimensions();
        dimensions.put("type", getType().toString());
        return dimensions;
    }

    public void showCategoryView(View view) {
        Intent activity = new Intent(this, HGCategoryActivityHG.class);
        activity.putExtra(HGLocationType.intent, getType().getId().intValue());
        startActivity(activity);
    }

    public void resetCategory(View view) {
        switch (getType()) {
            case Mosque: {
                HGSettings.getInstance().setLanguage(HGLanguage.None);
                break;
            }
            case Dining: {
                HGSettings.getInstance().setCategoriesFilter(new ArrayList<HGDiningCategory>());
                break;
            }
            case Shop: {
                HGSettings.getInstance().setShopCategoriesFilter(new ArrayList<HGShopCategory>());
                break;
            }
        }
        setupUpCountLabels();
    }

    private void setupLocationType() {
        int maximumDistance = 0;
        switch (getType()) {
            case Mosque: {
                categories_label.setText(R.string.language);
                categories_count.setText(HGSettings.getInstance().getLanguage().getResource_id());
                maximumDistance = HGSettings.getInstance().getMaximumDistanceMosque().intValue();
                setupLanguageView();
                hideSwitchView();
                hideCategoryView();
                break;
            }
            case Dining: {
                maximumDistance = HGSettings.getInstance().getMaximumDistanceDining().intValue();
                categories_count.setText(HGSettings.getInstance().getLanguage().getResource_id());
                hideLanguageView();
                setupSwitchView();
                break;
            }
            case Shop: {
                maximumDistance = HGSettings.getInstance().getMaximumDistanceShop().intValue();
                hideLanguageView();
                hideSwitchView();
                break;
            }
        }
        maximum_distance_slider.setProgress(maximumDistance);
        if (maximumDistance < 21) {
            maximum_distance_value.setText(maximumDistance + " km");
        } else {
            maximum_distance_value.setText(R.string.unlimited);
        }

    }

    private void setupUpCountLabels() {
        switch (getType()) {
            case Mosque: {
                categories_label.setText(R.string.language);
                categories_count.setText(String.valueOf(HGSettings.getInstance().getLanguage().getResource_id()));
                break;
            }
            case Dining: {
                categories_count.setText(String.valueOf(HGSettings.getInstance().getCategoriesFilter().size()));
                break;
            }
            case Shop: {
                categories_count.setText(String.valueOf(HGSettings.getInstance().getShopCategoriesFilter().size()));
                break;
            }
        }
    }

    private void hideLanguageView() {
        language.setVisibility(View.GONE);
    }

    private void setupLanguageView() {
        HGLanguageAdapter languageAdapter = new HGLanguageAdapter(this);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        language.setAdapter(languageAdapter);
        language.setSelection(HGSettings.getInstance().getLanguage().getId().intValue());

        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HGSettings.getInstance().setLanguage(HGLanguage.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                HGSettings.getInstance().setLanguage(HGLanguage.None);
            }
        });
    }

    private void setupSliderProgress() {
        maximum_distance_slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 20) {
                    maximum_distance_value.setText(R.string.unlimited);
                } else {
                    maximum_distance_value.setText(progress + 1 + " km");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                switch (getType()) {
                    case Mosque: {
                        HGSettings.getInstance().setMaximumDistanceMosque(seekBar.getProgress() + 1);
                        break;
                    }
                    case Dining: {
                        HGSettings.getInstance().setMaximumDistanceDining(seekBar.getProgress() + 1);
                        break;
                    }
                    case Shop: {
                        HGSettings.getInstance().setMaximumDistanceShop(seekBar.getProgress() + 1);
                        break;
                    }
                }
            }
        });

    }

    private HGLocationType getType() {
        return HGLocationType.getLocation(getIntent().getIntExtra(HGLocationType.intent, 0));
    }

    private void setupSwitchView() {
        non_halal_switch.setChecked(HGSettings.getInstance().isHalalFilter());
        non_halal_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                HGSettings.getInstance().setHalalFilter(isChecked);
            }
        });

        alcohol_switch.setChecked(HGSettings.getInstance().isAlcoholFilter());
        alcohol_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                HGSettings.getInstance().setAlcoholFilter(isChecked);
            }
        });

        pork_switch.setChecked(HGSettings.getInstance().isPorkFilter());
        pork_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                HGSettings.getInstance().setPorkFilter(isChecked);
            }
        });
    }

    private void hideSwitchView() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) switch_view.getLayoutParams();
        params.height = 0;
        switch_view.setLayoutParams(params);
    }

    private void hideCategoryView() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) category_view.getLayoutParams();
        params.height = 0;
        category_view.setLayoutParams(params);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hgfilter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                HGSettings.getInstance().persistFilters();
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HGSettings.getInstance().persistFilters();
    }

}
