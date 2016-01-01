package dk.eazyit.halalguide.ui.location_details.adapter;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import dk.eazyit.halalguide.R;
import dk.eazyit.halalguide.domain.HGLocation;
import dk.eazyit.halalguide.domain.HGLocationPicture;
import dk.eazyit.halalguide.domain.HGSmiley;
import dk.eazyit.halalguide.domain.HGUser;
import dk.eazyit.halalguide.services.HGGPSService;
import dk.eazyit.halalguide.ui.common.views.HGBaseViewHolder;
import dk.eazyit.halalguide.ui.location_details.models.HGLocationDetailsViewModel;
import rx.functions.Action1;

/**
 * Created by Privat on 09/08/15.
 */
public class HGLocationDetailsHeaderViewHolder extends HGBaseViewHolder {

    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.addressStreet)
    TextView addressStreet;
    @Bind(R.id.addressCity)
    TextView addressCity;
    @Bind(R.id.reviewRating)
    RatingBar reviewRating;
    @Bind(R.id.category)
    TextView category;

    @Bind(R.id.distance)
    TextView distance;
    @Bind(R.id.distanceKm)
    TextView distanceKm;

    @Bind(R.id.pork)
    ImageView pork;
    @Bind(R.id.alcohol)
    ImageView alcohol;
    @Bind(R.id.non_halal)
    ImageView non_halal;

    @Bind(R.id.submitterImage)
    ImageView submitterImage;
    @Bind(R.id.submitterLabel)
    TextView submitterLabel;
    @Bind(R.id.submitterName)
    TextView submitterName;


    @Bind(R.id.no_pictures)
    TextView no_pictures;

    @Bind(R.id.pictures)
    RecyclerView pictures;

    @Bind(R.id.smileys)
    RecyclerView smileys;

    @Bind(R.id.no_smileys)
    TextView no_smileys;

    HGLocationDetailsViewModel viewModel;

    public HGLocationDetailsHeaderViewHolder(final View convertView, HGLocationDetailsViewModel viewModel) {
        super(convertView);
        ButterKnife.bind(this, convertView);
        this.viewModel = viewModel;

        pictures.setHasFixedSize(true);
        pictures.setAdapter(new HGLocationPictureAdapter());
        LinearLayoutManager manager = new LinearLayoutManager(convertView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        manager.scrollToPosition(0);
        pictures.setLayoutManager(manager);
        pictures.setItemAnimator(new DefaultItemAnimator());

        smileys.setHasFixedSize(true);
        smileys.setAdapter(new HGLocationSmileyAdapter());
        LinearLayoutManager manager2 = new LinearLayoutManager(convertView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        manager2.scrollToPosition(0);
        smileys.setLayoutManager(manager2);
        smileys.setItemAnimator(new DefaultItemAnimator());

        subscriptions.add(viewModel.locationSubject.subscribe(new Action1<HGLocation>() {
            @Override
            public void call(HGLocation location) {
                if (location != null) {
                    name.setText(location.getName());
                    addressStreet.setText(location.getAddressRoad() + " " + location.getAddressRoadNumber());
                    addressCity.setText(location.getAddressPostalCode() + " " + location.getAddressCity());

                    category.setText(location.getCategoryString(convertView.getContext()));
                    distance.setText(HGGPSService.getInstance().distanceFromLocation(location.getLocation()));
                    pork.setVisibility(location.getPork() ? View.VISIBLE : View.GONE);
                    alcohol.setVisibility(location.getAlcohol() ? View.VISIBLE : View.GONE);
                    non_halal.setVisibility(location.getNonHalal() ? View.VISIBLE : View.GONE);
                }
            }
        }));
        subscriptions.add(this.viewModel.ratingSubject.subscribe(new Action1<Number>() {
            @Override
            public void call(Number number) {
                if (number != null) {
                    reviewRating.setRating(number.floatValue());
                }
            }
        }));
        subscriptions.add(this.viewModel.userSubject.subscribe(new Action1<HGUser>() {
            @Override
            public void call(HGUser hgUser) {
                if (hgUser != null) {
                    Picasso.with(convertView.getContext()).load(String.valueOf(hgUser.getFacebookProfileUrlSmall())).into(submitterImage);
                    submitterName.setText(hgUser.getFacebookName());
                }
            }
        }));
        subscriptions.add(this.viewModel.picturesSubject.subscribe(new Action1<List<HGLocationPicture>>() {
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
        subscriptions.add(this.viewModel.smileysSubject.subscribe(new Action1<List<HGSmiley>>() {
            @Override
            public void call(List<HGSmiley> hgSmileys) {
                if (hgSmileys == null || hgSmileys.size() == 0) {
                    no_smileys.setVisibility(View.VISIBLE);
                } else {
                    HGLocationSmileyAdapter adapter = (HGLocationSmileyAdapter) smileys.getAdapter();
                    adapter.setSmileys(hgSmileys);
                }
            }
        }));
    }
}
