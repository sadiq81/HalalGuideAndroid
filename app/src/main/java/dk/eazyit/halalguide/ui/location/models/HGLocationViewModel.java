package dk.eazyit.halalguide.ui.location.models;

import android.location.Location;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

import java.util.ArrayList;
import java.util.List;

import dk.eazyit.halalguide.HGSettings;
import dk.eazyit.halalguide.domain.HGCreationStatus;
import dk.eazyit.halalguide.domain.HGDiningCategory;
import dk.eazyit.halalguide.domain.HGLanguage;
import dk.eazyit.halalguide.domain.HGLocation;
import dk.eazyit.halalguide.domain.HGLocationType;
import dk.eazyit.halalguide.domain.HGShopCategory;
import dk.eazyit.halalguide.extensions.HGQuery;
import dk.eazyit.halalguide.services.HGGPSService;
import dk.eazyit.halalguide.services.HGLocationService;
import dk.eazyit.halalguide.services.HGReporting;
import dk.eazyit.halalguide.ui.common.models.HGBaseViewModel;
import rx.subjects.BehaviorSubject;


/**
 * Created by Privat on 09/07/15.
 */
public class HGLocationViewModel extends HGBaseViewModel {

    public BehaviorSubject<List<HGLocation>> locationsSubject = BehaviorSubject.create();
    public List<HGLocation> locations = new ArrayList<>(20);
    private HGLocationType locationType;
    private int page = 0;

    public static HGLocationViewModel create() {
        return new HGLocationViewModel();
    }

    public void refreshLocation() {
        increaseCount();

        HGLocationService.getInstance().locationsByQuery(getQuery(), new FindCallback<HGLocation>() {
            @Override
            public void done(java.util.List<HGLocation> list, ParseException e) {

                decreaseCount();

                if ((HGLocationViewModel.this.exception = e) != null) {
                    HGReporting.reportError(e);
                    exceptionSubject.onNext(e);
                } else {
                    HGLocationViewModel.this.locations.addAll(list);
                    locationsSubject.onNext(HGLocationViewModel.this.locations);
                }
            }
        });
    }

    private HGQuery<HGLocation> getQuery() {

        HGQuery query = new HGQuery(HGLocation.class);
        query.whereEqualTo("locationType", this.locationType.getId());
        query.whereEqualTo("creationStatus", HGCreationStatus.Approved.getId());

        if (locationType.equals(HGLocationType.Dining)) {

            if (!HGSettings.getInstance().isPorkFilter().booleanValue()) {
                query.whereEqualTo("pork", false);
            }

            if (!HGSettings.getInstance().isAlcoholFilter().booleanValue()) {
                query.whereEqualTo("alcohol", false);
            }

            if (!HGSettings.getInstance().isHalalFilter().booleanValue()) {
                query.whereEqualTo("nonHalal", false);
            }

            if (HGSettings.getInstance().getCategoriesFilter().size() > 0) {
                List<Number> numbers = new ArrayList<>();
                for (HGDiningCategory category : HGSettings.getInstance().getCategoriesFilter()) {
                    numbers.add(category.getId());
                }
                query.whereContainedIn("categories", numbers);
            }

        } else if (locationType.equals(HGLocationType.Shop)) {

            if (HGSettings.getInstance().getShopCategoriesFilter().size() > 0) {
                List<Number> numbers = new ArrayList<>();
                for (HGShopCategory hgShopCategory : HGSettings.getInstance().getShopCategoriesFilter()) {
                    numbers.add(hgShopCategory.getId());
                }
                query.whereContainedIn("categories", numbers);
            }

        } else if (locationType.equals(HGLocationType.Mosque)) {

            if (!HGLanguage.None.equals(HGSettings.getInstance().getLanguage())) {
                query.whereEqualTo("language", HGSettings.getInstance().getLanguage().getId());
            }
        }

        /*
        if (self.searchText && [self.searchText length] > 0) {

            HGQuery *name = [HGQuery orQueryWithSubqueries:@[query]];
            [name whereKey:@"name" matchesRegex:self.searchText modifiers:@"i"];

            HGQuery *addressCity = [HGQuery orQueryWithSubqueries:@[query]];
            [addressCity whereKey:@"addressCity" matchesRegex:self.searchText modifiers:@"i"];

            HGQuery *addressPostalCode = [HGQuery orQueryWithSubqueries:@[query]];
            [addressPostalCode whereKey:@"addressPostalCode" matchesRegex:self.searchText modifiers:@"i"];

            HGQuery *addressRoad = [HGQuery orQueryWithSubqueries:@[query]];
            [addressRoad whereKey:@"addressRoad" matchesRegex:self.searchText modifiers:@"i"];

            HGQuery *addressRoadNumber = [HGQuery orQueryWithSubqueries:@[query]];
            [addressRoadNumber whereKey:@"addressRoadNumber" matchesRegex:self.searchText modifiers:@"i"];

            HGQuery *homePage = [HGQuery orQueryWithSubqueries:@[query]];
            [homePage whereKey:@"homePage" matchesRegex:self.searchText modifiers:@"i"];

            HGQuery *telephone = [HGQuery orQueryWithSubqueries:@[query]];
            [telephone whereKey:@"telephone" matchesRegex:self.searchText modifiers:@"i"];

            HGQuery *or = [HGQuery orQueryWithSubqueries:@[name, addressCity, addressPostalCode, addressRoad, addressRoadNumber, homePage, telephone]];
            //Or queries do not support geo location and limit/skip
            listLocations = [NSArray new];
            return or;
        }
         */
        query.setLimit(20);
        query.setSkip(page * 20);

        Location location = HGGPSService.getInstance().getLocation();
        if (location != null) {
            query.whereWithinKilometers("point", new ParseGeoPoint(location.getLatitude(), location.getLongitude()), getMaximumDistance() < 20 ? getMaximumDistance() : 20000);
        }

        return query;
    }

    public int getMaximumDistance() {
        switch (getLocationType()) {
            case Dining: {
                return HGSettings.getInstance().getMaximumDistanceDining().intValue();
            }
            case Mosque: {
                return HGSettings.getInstance().getMaximumDistanceMosque().intValue();
            }
            case Shop: {
                return HGSettings.getInstance().getMaximumDistanceShop().intValue();
            }
            default:
                return 0;
        }
    }

    public void nextPage() {
        page += 1;
    }

    public void resetPage() {
        page = 0;
        locations.clear();
        refreshLocation();
    }

    public HGLocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(HGLocationType locationType) {
        this.locationType = locationType;
    }
}
