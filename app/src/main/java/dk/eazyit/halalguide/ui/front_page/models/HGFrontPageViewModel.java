package dk.eazyit.halalguide.ui.front_page.models;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.Collections;
import java.util.List;

import dk.eazyit.halalguide.domain.HGLocation;
import dk.eazyit.halalguide.services.HGLocationService;
import dk.eazyit.halalguide.services.HGReporting;
import dk.eazyit.halalguide.ui.common.models.HGBaseViewModel;
import rx.subjects.BehaviorSubject;

/**
 * Created by Privat on 08/07/15.
 */
public class HGFrontPageViewModel extends HGBaseViewModel {

    public BehaviorSubject<List<HGLocation>> locationsSubject = BehaviorSubject.create();
    private List<HGLocation> locations = Collections.emptyList();

    public static HGFrontPageViewModel create() {
        return new HGFrontPageViewModel();
    }

    public void refreshLocation() {
        increaseCount();

        HGLocationService.getInstance().lastTenLocations(new FindCallback<HGLocation>() {
            @Override
            public void done(List<HGLocation> list, ParseException e) {
                decreaseCount();
                if ((HGFrontPageViewModel.this.exception = e) != null) {
                    HGReporting.reportError(e);
                    exceptionSubject.onError(e);
                } else {
                    locationsSubject.onNext(HGFrontPageViewModel.this.locations = list);
                }
            }
        });
    }
}


