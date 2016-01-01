package dk.eazyit.halalguide.ui.location_details.models;

import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.util.Patterns;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dk.eazyit.halalguide.HGSettings;
import dk.eazyit.halalguide.domain.HGLocation;
import dk.eazyit.halalguide.domain.HGLocationPicture;
import dk.eazyit.halalguide.domain.HGReview;
import dk.eazyit.halalguide.domain.HGSmiley;
import dk.eazyit.halalguide.domain.HGUser;
import dk.eazyit.halalguide.services.HGLocationService;
import dk.eazyit.halalguide.services.HGPictureService;
import dk.eazyit.halalguide.services.HGReviewService;
import dk.eazyit.halalguide.services.HGSmileyService;
import dk.eazyit.halalguide.services.HGUserService;
import dk.eazyit.halalguide.ui.common.models.HGBaseViewModel;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by Privat on 08/07/15.
 */
public class HGLocationDetailsViewModel extends HGBaseViewModel {

    public BehaviorSubject<HGLocation> locationSubject = BehaviorSubject.create();
    public BehaviorSubject<Number> ratingSubject = BehaviorSubject.create();
    public BehaviorSubject<List<HGLocationPicture>> picturesSubject = BehaviorSubject.create();
    public BehaviorSubject<List<HGReviewModel>> reviewsModelsSubject = BehaviorSubject.create();
    public BehaviorSubject<List<HGSmiley>> smileysSubject = BehaviorSubject.create();
    public BehaviorSubject<HGUser> userSubject = BehaviorSubject.create();
    HGLocation location = null;
    Number rating = 0;
    List<HGLocationPicture> pictures = new ArrayList<>();
    List<HGReviewModel> reviewModels = new ArrayList<>();
    List<HGSmiley> smileys = new ArrayList<>();
    HGUser user = null;

    public HGLocationDetailsViewModel() {
    }

    public static HGLocationDetailsViewModel create() {
        return new HGLocationDetailsViewModel();
    }

    public void setLocationId(String locationId) {
        increaseCount();
        HGLocationService.getInstance().locationById(locationId, new GetCallback<HGLocation>() {
            @Override
            public void done(HGLocation hgLocation, ParseException e) {
                setLocation(hgLocation);
                decreaseCount();
            }
        });
    }

    private void setLocation(HGLocation location) {

        this.locationSubject.onNext(this.location = location);

        increaseCount(4);

        HGReviewService.getInstance().reviewsForLocation(this.location, new FindCallback<HGReview>() {
            @Override
            public void done(List<HGReview> list, ParseException e) {
                decreaseCount();

                for (HGReview review : list) {
                    reviewModels.add(new HGReviewModel(review));
                }
                calculateAverageRating();
                HGLocationDetailsViewModel.this.reviewsModelsSubject.onNext(HGLocationDetailsViewModel.this.reviewModels);
            }
        });

        HGUserService.getInstance().getUser(this.location.getSubmitterId(), new GetCallback<HGUser>() {
            @Override
            public void done(HGUser hgUser, ParseException e) {
                decreaseCount();
                userSubject.onNext(HGLocationDetailsViewModel.this.user = hgUser);
            }
        });

        HGPictureService.getInstance().locationPicturesForLocation(this.location, new FindCallback<HGLocationPicture>() {
            @Override
            public void done(List<HGLocationPicture> list, ParseException e) {
                decreaseCount();
                picturesSubject.onNext(HGLocationDetailsViewModel.this.pictures = list);
            }
        });

        HGSmileyService.getInstance().getSmileysObservable(location.getNavneloebenummer()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<HGSmiley>>() {
            @Override
            public void call(List<HGSmiley> smileys) {
                decreaseCount();
                smileysSubject.onNext(HGLocationDetailsViewModel.this.smileys = smileys);
            }
        });
    }

    public void setSmileys(List<HGSmiley> smileys) {
        smileysSubject.onNext(this.smileys = smileys);
    }

    private void calculateAverageRating() {
        if (this.reviewModels != null && this.reviewModels.size() > 0) {
            float average = 0;
            for (HGReviewModel reviewModel : this.reviewModels) {
                average += reviewModel.getReview().getRating().floatValue();
            }
            ratingSubject.onNext(rating = average);
        } else {
            ratingSubject.onNext(rating = 0);
        }
    }

    public String telephone() {
        return location.getTelephone();
    }

    public String website() {
        return location.getHomePage();
    }

    public boolean hasTelephone() {
        return location.getTelephone() != null && PhoneNumberUtils.isGlobalPhoneNumber(location.getTelephone());
    }

    public boolean hasWebsite() {
        return location.getHomePage() != null && Patterns.WEB_URL.matcher(location.getHomePage()).matches();
    }

    public String shareText() {
        StringBuilder sb = new StringBuilder();
        sb.append(location.getName() + System.getProperty("line.separator"));
        sb.append(location.getAddressRoad() + " " + location.getAddressRoadNumber() + System.getProperty("line.separator"));
        sb.append(location.getAddressPostalCode() + " " + location.getAddressCity() + System.getProperty("line.separator"));
        sb.append(System.getProperty("line.separator"));
        if (hasWebsite()) {
            sb.append(location.getHomePage() + System.getProperty("line.separator"));
            sb.append(System.getProperty("line.separator"));
        }

        if (hasTelephone()) {
            sb.append(location.getTelephone() + System.getProperty("line.separator"));
            sb.append(System.getProperty("line.separator"));
        }

        sb.append("halalguide://location/" + location.getObjectId() + System.getProperty("line.separator"));
        sb.append(System.getProperty("line.separator"));

        sb.append("Appstore: https://appsto.re/dk/lQQP4.i" + System.getProperty("line.separator"));
        sb.append(System.getProperty("line.separator"));

        sb.append("Google Play Store:: https://play.google.com/store/apps/details?id=halalguide.dk.eazyit.halalguide");

        return sb.toString();
    }

    public void toogleFavorite() {
        List<String> favorites = HGSettings.getInstance().getFavorites();
        if (isFavorite()) {
            favorites.remove(location.getObjectId());
        } else {
            favorites.add(location.getObjectId());
        }
    }

    public boolean isFavorite() {
        return HGSettings.getInstance().getFavorites().contains(location.getObjectId());
    }

    public void saveMultiplePictures(Uri[] uris) throws IOException {
        fetchCountSubject.onNext(fetchCount = 1);
        HGPictureService.getInstance().saveMultiplePictures(uris, location, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                fetchCountSubject.onNext(fetchCount = 0);
            }
        });
    }

    public String getLatitude() {
        return String.valueOf(location.getPoint().getLatitude());
    }

    public String getLongitude() {
        return String.valueOf(location.getPoint().getLongitude());
    }
}
