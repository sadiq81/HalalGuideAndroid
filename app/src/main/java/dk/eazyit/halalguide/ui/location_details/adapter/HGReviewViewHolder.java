package dk.eazyit.halalguide.ui.location_details.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import dk.eazyit.halalguide.R;
import dk.eazyit.halalguide.domain.HGLocationPicture;
import dk.eazyit.halalguide.domain.HGUser;
import dk.eazyit.halalguide.ui.common.views.HGBaseViewHolder;
import dk.eazyit.halalguide.ui.location_details.models.HGReviewModel;
import rx.functions.Action1;

/**
 * Created by Privat on 16/08/15.
 */
public class HGReviewViewHolder extends HGBaseViewHolder {

    @Bind(R.id.profileImage)
    ImageView profileImage;
    @Bind(R.id.profileName)
    TextView profileName;
    @Bind(R.id.reviewRating)
    RatingBar reviewRating;
    @Bind(R.id.review)
    TextView review;
    @Bind(R.id.reviewDate)
    TextView reviewDate;
    @Bind(R.id.reviewImage1)
    ImageView reviewImage1;
    @Bind(R.id.reviewImage2)
    ImageView reviewImage2;
    @Bind(R.id.reviewImage3)
    ImageView reviewImage3;

    private HGReviewModel viewModel;

    public HGReviewViewHolder(View convertView) {
        super(convertView);
        ButterKnife.bind(this, convertView);
    }

    public void setViewModel(HGReviewModel viewModel) {
        this.viewModel = viewModel;

        subscriptions.add(viewModel.userSubject.subscribe(new Action1<HGUser>() {
            @Override
            public void call(HGUser hgUser) {
                profileImage.setImageBitmap(null);

                Picasso.with(profileImage.getContext()).cancelRequest(profileImage);
                Picasso.with(profileImage.getContext()).load(hgUser.getFacebookProfileUrlSmall().toString()).into(profileImage);

                profileName.setText(hgUser.getFacebookName());
            }
        }));

        reviewRating.setRating(viewModel.getReview().getRating().intValue());
        review.setText(viewModel.getReview().getReview());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        reviewDate.setText(formatter.format(viewModel.getReview().getCreatedAt()));

        subscriptions.add(viewModel.locationPicturesSubject.subscribe(new Action1<List<HGLocationPicture>>() {
            @Override
            public void call(List<HGLocationPicture> hgLocationPictures) {
                if (hgLocationPictures != null) {
                    if (hgLocationPictures.size() > 0) {
                        reviewImage1.setImageBitmap(null);
                        Picasso.with(reviewImage1.getContext()).cancelRequest(reviewImage1);
                        Picasso.with(reviewImage1.getContext()).load(hgLocationPictures.get(0).getThumbnail().getUrl()).into(reviewImage1);
                    }
                    if (hgLocationPictures.size() > 1) {
                        reviewImage2.setImageBitmap(null);
                        Picasso.with(reviewImage2.getContext()).cancelRequest(reviewImage2);
                        Picasso.with(reviewImage2.getContext()).load(hgLocationPictures.get(1).getThumbnail().getUrl()).into(reviewImage2);
                    }
                    if (hgLocationPictures.size() > 2) {
                        reviewImage3.setImageBitmap(null);
                        Picasso.with(reviewImage3.getContext()).cancelRequest(reviewImage3);
                        Picasso.with(reviewImage3.getContext()).load(hgLocationPictures.get(2).getThumbnail().getUrl()).into(reviewImage3);
                    }
                }
            }
        }));

    }


}
