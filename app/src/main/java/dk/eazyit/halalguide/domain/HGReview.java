package dk.eazyit.halalguide.domain;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Privat on 10/08/15.
 */
@ParseClassName("Review")
public class HGReview extends ParseObject {

    public Number getCreationStatus() {
        return getNumber("creationStatus");
    }

    public void setCreationStatus(Number creationStatus) {
        put("creationStatus", creationStatus);
    }

    public String getLocationId() {
        return getString("locationId");
    }

    public void setLocationId(String locationId) {
        put("locationId", locationId);
    }

    public Number getRating() {
        return getNumber("rating");
    }

    public void setRating(Number rating) {
        put("rating", rating);
    }

    public String getReview() {
        return getString("review");
    }

    public void setReview(String review) {
        put("review", review);
    }

    public String getSubmitterId() {
        return getString("submitterId");
    }

    public void setSubmitterId(String submitterId) {
        put("submitterId", submitterId);
    }
}
