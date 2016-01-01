package dk.eazyit.halalguide.ui.location_details.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Map;

import butterknife.Bind;
import dk.eazyit.halalguide.R;
import dk.eazyit.halalguide.domain.HGLocation;
import dk.eazyit.halalguide.extensions.HGActivity;
import dk.eazyit.halalguide.ui.common.activity.HGBaseActivity;
import dk.eazyit.halalguide.ui.common.models.HGBaseViewModel;
import dk.eazyit.halalguide.ui.location_details.adapter.HGReviewAdapter;
import dk.eazyit.halalguide.ui.location_details.models.HGLocationDetailsViewModel;
import nl.changer.polypicker.Config;
import nl.changer.polypicker.ImagePickerActivity;
import rx.functions.Action1;

@HGActivity(layout = R.layout.activity_location_details, swipeEnabled = false, screenName = "location_details")
public class HGLocationDetailsActivityHG extends HGBaseActivity {

    private static final int INTENT_REQUEST_GET_IMAGES = 100;

    HGLocationDetailsViewModel viewModel = HGLocationDetailsViewModel.create();

    @Bind(R.id.reviews)
    ListView reviews;

    HGReviewAdapter adapter;
    private Menu mOptionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected Map<String, String> getScreenDimensions() {
        Map<String, String> dimensions = super.getScreenDimensions();
        dimensions.put("locationId", getLocationId());
        return dimensions;
    }

    @Override
    protected void onResume() {
        super.onResume();

        viewModel.setLocationId(getLocationId());

        reviews.setAdapter(adapter = new HGReviewAdapter(this, -1, viewModel));

        subscribers.add(viewModel.locationSubject.subscribe(new Action1<HGLocation>() {
            @Override
            public void call(HGLocation hgLocation) {
                mOptionsMenu.findItem(R.id.action_favorite).setTitle(viewModel.isFavorite() ? R.string.favorite_unmark : R.string.favorite);
                mOptionsMenu.findItem(R.id.action_call).setVisible(viewModel.hasTelephone());
                mOptionsMenu.findItem(R.id.action_website).setVisible(viewModel.hasWebsite());
            }
        }));
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.onPause();
    }

    @Override
    public void onRefresh() {
        String locationId = getIntent().getStringExtra(HGLocation.INTENT);
        viewModel.setLocationId(locationId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location_details, menu);
        mOptionsMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_favorite:
                viewModel.toogleFavorite();
                mOptionsMenu.findItem(R.id.action_favorite).setTitle(viewModel.isFavorite() ? R.string.favorite_unmark : R.string.favorite);
                Toast.makeText(this, viewModel.isFavorite() ? getString(R.string.favorite_mark_toast) : getString(R.string.favorite_unmark_toast), Toast.LENGTH_SHORT).show();
                return true;
//            case R.id.action_suggest_change:
//                //TODO
//                return true;
//            case R.id.action_add_review:
//                //TODO
//                return true;
            case R.id.action_add_picture:
                Intent imagePickerIntent = new Intent(this, ImagePickerActivity.class);
                Config config = new Config.Builder()
                        .setTabBackgroundColor(R.color.greenTintColor)    // set tab background color. Default white.
                        .setTabSelectionIndicatorColor(R.color.darkGreenTintColor)
                        .setCameraButtonColor(R.color.greenTintColor)
                        .setSelectionLimit(5)    // set photo selection limit. Default unlimited selection.
                        .build();
                ImagePickerActivity.setConfig(config);
                startActivityForResult(imagePickerIntent, INTENT_REQUEST_GET_IMAGES);
                return true;
            case R.id.action_directions:
                Uri geo = Uri.parse("geo:" + viewModel.getLatitude() + "," + viewModel.getLongitude());
                Intent directionsIntent = new Intent(android.content.Intent.ACTION_VIEW, geo);
                if (directionsIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(directionsIntent);
                } else {
                    Toast.makeText(this, getString(R.string.navigation_unavailable), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_call:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + viewModel.telephone()));
                startActivity(callIntent);
                return true;
            case R.id.action_website:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.website()));
                startActivity(browserIntent);
                return true;
            case R.id.action_share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, viewModel.shareText());
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.share)));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_IMAGES) {
                Parcelable[] parcelableUris = intent.getParcelableArrayExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
                if (parcelableUris == null) {
                    return;
                }
                // Java doesn't allow array casting, this is a little hack
                Uri[] uris = new Uri[parcelableUris.length];
                System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);
                if (uris != null && uris.length > 0) {
                    try {
                        viewModel.saveMultiplePictures(uris);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected HGBaseViewModel getViewModel() {
        return viewModel;
    }

}
