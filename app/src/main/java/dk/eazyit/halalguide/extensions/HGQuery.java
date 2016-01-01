package dk.eazyit.halalguide.extensions;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import dk.eazyit.halalguide.HGApplication;

/**
 * Created by Privat on 07/07/15.
 */
public class HGQuery<T extends ParseObject> extends ParseQuery<T> {

    public HGQuery(Class<T> subclass) {
        super(subclass);
        init();
    }

    public HGQuery(String theClassName) {
        super(theClassName);
        init();
    }

    private void init() {
        if (!isConnected()) {
            fromLocalDatastore();
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) HGApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


}
