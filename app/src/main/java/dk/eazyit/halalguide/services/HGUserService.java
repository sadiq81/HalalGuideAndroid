package dk.eazyit.halalguide.services;

import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;

import java.util.HashMap;
import java.util.Map;

import dk.eazyit.halalguide.domain.HGUser;
import dk.eazyit.halalguide.extensions.HGQuery;

/**
 * Created by Privat on 15/08/15.
 */
public class HGUserService {

    private static HGUserService instance = null;

    protected HGUserService() {
    }

    public static HGUserService getInstance() {
        if (instance == null) {
            instance = new HGUserService();
        }
        return instance;
    }

    public static HGUser getDummyData() {
        HGUser user = HGUser.createWithoutData(HGUser.class, "1");
        Map<String, String> facebookData = new HashMap<>();
        facebookData.put("name", "Test");
        return user;
    }

    public void getUser(String id, final GetCallback<HGUser> callback) {
        HGQuery<HGUser> query = new HGQuery(HGUser.class);
        query.getInBackground(id, new GetCallback<HGUser>() {
            @Override
            public void done(HGUser hgUser, ParseException e) {
                if (e == null) {
                    hgUser.pinInBackground("getUser");
                } else {
                    //TODO Log error
                }
                callback.done(hgUser, e);
            }
        });
        Map<String, String> map = new HashMap<>();
        map.put("user_id", id);
        ParseAnalytics.trackEventInBackground("get_user", map);
    }
}
