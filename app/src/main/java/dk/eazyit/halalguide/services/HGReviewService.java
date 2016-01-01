package dk.eazyit.halalguide.services;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.eazyit.halalguide.domain.HGCreationStatus;
import dk.eazyit.halalguide.domain.HGLocation;
import dk.eazyit.halalguide.domain.HGReview;
import dk.eazyit.halalguide.extensions.HGQuery;

/**
 * Created by Privat on 10/08/15.
 */
public class HGReviewService {

    private static HGReviewService instance = null;

    protected HGReviewService() {
    }

    public static HGReviewService getInstance() {
        if (instance == null) {
            instance = new HGReviewService();
        }
        return instance;
    }

    public void reviewsForLocation(HGLocation location, final FindCallback<HGReview> callback) {
        HGQuery<HGReview> query = new HGQuery(HGReview.class);
        query.whereEqualTo("creationStatus", HGCreationStatus.Approved.getId());
        query.whereEqualTo("locationId", location.getObjectId());
        query.findInBackground(new FindCallback<HGReview>() {
            @Override
            public void done(List<HGReview> list, ParseException e) {
                if (e == null) {
                    ParseObject.pinAllInBackground("reviewsForLocation", list);
                } else {
                    //TODO Log error
                }
                callback.done(list, e);
            }
        });
        Map<String, String> map = new HashMap<>();
        map.put("location_id", location.getObjectId());
        ParseAnalytics.trackEventInBackground("reviews_for_location", map);
    }

}


