package dk.eazyit.halalguide.services;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.eazyit.halalguide.domain.HGCreationStatus;
import dk.eazyit.halalguide.domain.HGLocation;
import dk.eazyit.halalguide.extensions.HGQuery;

/**
 * Created by Privat on 07/07/15.
 */
public class HGLocationService {

    private static HGLocationService instance = null;

    protected HGLocationService() {
    }

    public static HGLocationService getInstance() {
        if (instance == null) {
            instance = new HGLocationService();
        }
        return instance;
    }

    public static List<HGLocation> getDummyLocations() {
        List<HGLocation> locations = new ArrayList<>(3);

        HGLocation loc1 = HGLocation.createWithoutData(HGLocation.class, "1");
        loc1.setName("test1");
        loc1.setAddressRoad("road");
        loc1.setAddressRoadNumber("1");
        loc1.setAddressCity("by");
        loc1.setAddressPostalCode("1");
        loc1.setAlcohol(true);
        loc1.setPork(true);
        loc1.setNonHalal(true);
        loc1.setCreationStatus(1);
        loc1.setLocationType(0);
        loc1.setSubmitterId("1");
        loc1.setHomePage("http://www.test.dk");
        loc1.setLanguage(0);
        loc1.setPoint(new ParseGeoPoint(55.0000, 12.0000));

        return locations;

    }

    public void locationById(String id, GetCallback<HGLocation> callBack) {
        HGQuery<HGLocation> query = new HGQuery(HGLocation.class);
        query.getInBackground(id, callBack);

        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("id", id);
        ParseAnalytics.trackEventInBackground("location_by_id", dimensions);
    }

    /*public void saveSuggestion(HGChangeSuggestion suggestion, SaveCallback callback) {
        suggestion.saveInBackground(callback);
    }*/

    public void saveLocation(HGLocation location, SaveCallback callback) {
        location.saveInBackground(callback);
        ParseAnalytics.trackEventInBackground("save_location");
    }

    public void locationsByQuery(HGQuery<HGLocation> query, final FindCallback<HGLocation> callback) {
        query.findInBackground(new FindCallback<HGLocation>() {
            @Override
            public void done(List<HGLocation> list, ParseException e) {
                if (e == null) {
                    ParseObject.pinAllInBackground("locationsByQuery", list);
                }
                callback.done(list, e);
            }
        });
        ParseAnalytics.trackEventInBackground("locations_by_query");
    }

    public void lastTenLocations(final FindCallback<HGLocation> callback) {
        HGQuery<HGLocation> query = new HGQuery<>(HGLocation.class);
        query.whereEqualTo("creationStatus", HGCreationStatus.Approved.getId());
        query.orderByDescending("updatedAt");
        query.setLimit(10);

        query.findInBackground(new FindCallback<HGLocation>() {
            @Override
            public void done(List<HGLocation> list, ParseException e) {
                if (e == null) {
                    ParseObject.pinAllInBackground("lastTenLocations", list);
                }
                callback.done(list, e);
            }
        });
        ParseAnalytics.trackEventInBackground("last_ten_locations");
    }

    public void findExistingLocationsWithName(String name, final FindCallback<HGLocation> callback) {
        HGQuery<HGLocation> query = new HGQuery<>(HGLocation.class);
        query.whereEqualTo("name", name);

        query.findInBackground(new FindCallback<HGLocation>() {
            @Override
            public void done(List<HGLocation> list, ParseException e) {
                if (e == null) {
                    ParseObject.pinAllInBackground("findExistingLocationsWithName", list);
                }
                callback.done(list, e);
            }
        });
        ParseAnalytics.trackEventInBackground("find_existing_locations_with_name");
    }
}
