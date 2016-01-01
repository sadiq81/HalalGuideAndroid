package dk.eazyit.halalguide.ui.location_details.activities;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import dk.eazyit.halalguide.R;
import dk.eazyit.halalguide.domain.HGLocationPicture;
import dk.eazyit.halalguide.extensions.HGActivity;
import dk.eazyit.halalguide.ui.common.activity.HGBaseActivity;
import dk.eazyit.halalguide.ui.common.models.HGBaseViewModel;
import dk.eazyit.halalguide.ui.location_details.adapter.HGLocationPictureAdapter;
import dk.eazyit.halalguide.ui.location_details.models.HGLocationDetailsViewModel;
import rx.functions.Action1;


@HGActivity(layout = R.layout.activity_hglocation_images, swipeEnabled = false, screenName = "location_images")
public class HGLocationImagesActivity extends HGBaseActivity {

    @Bind(R.id.pictures)
    RecyclerView pictures;

    @Bind(R.id.no_pictures)
    TextView no_pictures;

    HGLocationDetailsViewModel viewModel = HGLocationDetailsViewModel.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        pictures.setHasFixedSize(true);
        pictures.setAdapter(new HGLocationPictureAdapter());
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        manager.scrollToPosition(0);
        pictures.setLayoutManager(manager);
        pictures.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        viewModel.setLocationId(getLocationId());

        subscribers.add(viewModel.picturesSubject.subscribe(new Action1<List<HGLocationPicture>>() {
            @Override
            public void call(List<HGLocationPicture> hgLocationPictures) {
                if (hgLocationPictures == null || hgLocationPictures.size() == 0) {
                    no_pictures.setVisibility(View.VISIBLE);
                } else {
                    HGLocationPictureAdapter adapter = (HGLocationPictureAdapter) pictures.getAdapter();
                    adapter.setPictures(hgLocationPictures);
                }
            }
        }));
    }

    @Override
    protected Map<String, String> getScreenDimensions() {
        Map<String, String> dimensions = super.getScreenDimensions();
        dimensions.put("locationId", getLocationId());
        return dimensions;
    }


    @Override
    protected HGBaseViewModel getViewModel() {
        return viewModel;
    }
}
