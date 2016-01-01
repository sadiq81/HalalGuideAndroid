package dk.eazyit.halalguide;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.ParseObject;

import dk.eazyit.halalguide.domain.HGLocation;
import dk.eazyit.halalguide.domain.HGLocationPicture;
import dk.eazyit.halalguide.domain.HGReview;
import dk.eazyit.halalguide.domain.HGUser;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Privat on 07/07/15.
 */
public class HGApplication extends Application {

    private static Context context;

    public static Context getAppContext() {
        return HGApplication.context;
    }

    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());

        HGApplication.context = getApplicationContext();

        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(HGLocation.class);
        ParseObject.registerSubclass(HGLocationPicture.class);
        ParseObject.registerSubclass(HGReview.class);
        ParseObject.registerSubclass(HGUser.class);
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));

    }

}
