package dk.eazyit.halalguide.ui.common.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.parse.ParseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import dk.eazyit.halalguide.R;
import dk.eazyit.halalguide.domain.HGLocation;
import dk.eazyit.halalguide.extensions.HGActivity;
import dk.eazyit.halalguide.ui.common.models.HGBaseViewModel;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Privat on 19/08/15.
 */
public abstract class HGBaseActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    protected List<Subscription> subscribers = new ArrayList<Subscription>();

    @Nullable
    @Bind(R.id.swipe_container)
    protected SwipeRefreshLayout swipeRefreshLayout;

    HGActivity activity = this.getClass().getAnnotation(HGActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity.layout());
        ButterKnife.bind(this);

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(activity.swipeEnabled());
            swipeRefreshLayout.setOnRefreshListener(this);
            swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        }

    }

    protected Map<String, String> getScreenDimensions() {
        Map<String, String> dimensions = new HashMap<>();
        return dimensions;
    }

    protected String getLocationId() {
        return getIntent().getStringExtra(HGLocation.INTENT);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Map<String, String> dimensions = getScreenDimensions();
        dimensions.put("name", activity.screenName());
        ParseAnalytics.trackEventInBackground("screen", dimensions);

        if (getViewModel() != null && swipeRefreshLayout != null) {
            subscribers.add(getViewModel().fetchCountSubject.subscribe(new Subscriber<Integer>() {
                @Override
                public void onCompleted() {
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onError(Throwable e) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onNext(Integer integer) {
                    if (integer == 0) {
                        swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.setEnabled(activity.swipeEnabled());
                    } else if (integer == 1) {
                        swipeRefreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(true);
                            }
                        });
                    }
                }
            }));

            subscribers.add(getViewModel().exceptionSubject.subscribe(new Subscriber<com.parse.ParseException>() {
                @Override
                public void onCompleted() {
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(activity.swipeEnabled());
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(HGBaseActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNext(com.parse.ParseException e) {
                    Toast.makeText(HGBaseActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        for (Subscription sub : subscribers) {
            sub.unsubscribe();
        }
    }

    @Override
    public void onRefresh() {

    }

    protected HGBaseViewModel getViewModel() {
        return null;
    }

}
