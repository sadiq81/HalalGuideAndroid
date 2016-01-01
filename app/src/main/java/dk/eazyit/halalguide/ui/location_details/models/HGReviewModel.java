package dk.eazyit.halalguide.ui.location_details.models;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import dk.eazyit.halalguide.domain.HGLocationPicture;
import dk.eazyit.halalguide.domain.HGReview;
import dk.eazyit.halalguide.domain.HGUser;
import dk.eazyit.halalguide.services.HGPictureService;
import dk.eazyit.halalguide.services.HGUserService;
import dk.eazyit.halalguide.ui.common.models.HGBaseViewModel;
import rx.subjects.BehaviorSubject;

/**
 * Created by Privat on 26/08/15.
 */
public class HGReviewModel extends HGBaseViewModel {

    public BehaviorSubject<HGUser> userSubject = BehaviorSubject.create();
    public BehaviorSubject<List<HGLocationPicture>> locationPicturesSubject = BehaviorSubject.create();
    HGReview review;
    HGUser user = null;
    List<HGLocationPicture> locationPictures = new ArrayList<>();

    public HGReviewModel(HGReview review) {
        this.review = review;

        increaseCount();
        HGUserService.getInstance().getUser(review.getSubmitterId(), new GetCallback<HGUser>() {
            @Override
            public void done(HGUser hgUser, ParseException e) {
                decreaseCount();
                if (e != null) {
                    exceptionSubject.onNext(e);
                }
                userSubject.onNext(HGReviewModel.this.user = hgUser);
            }
        });

        increaseCount();
        HGPictureService.getInstance().locationPicturesForReview(review, new FindCallback<HGLocationPicture>() {
            @Override
            public void done(List<HGLocationPicture> list, ParseException e) {
                decreaseCount();
                if (e != null) {
                    exceptionSubject.onNext(e);
                }
                locationPicturesSubject.onNext(locationPictures = list);
            }
        });
    }

    public HGReview getReview() {
        return review;
    }
}
