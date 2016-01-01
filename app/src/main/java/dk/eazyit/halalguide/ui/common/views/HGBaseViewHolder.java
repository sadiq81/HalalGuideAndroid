package dk.eazyit.halalguide.ui.common.views;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Created by Privat on 29/12/2015.
 */
public class HGBaseViewHolder {

    //TODO unsubscribe when views parent is destroyed

    protected View convertView;
    protected List<Subscription> subscriptions = new ArrayList<Subscription>();

    public HGBaseViewHolder(View convertView) {
        this.convertView = convertView;
    }

    public void onPause() {
        for (Subscription sub : subscriptions) {
            sub.unsubscribe();
        }
    }
}
