package dk.eazyit.halalguide.domain;

import dk.eazyit.halalguide.HGApplication;
import dk.eazyit.halalguide.R;

/**
 * Created by Privat on 09/07/15.
 */
public enum HGLocationType {

    Dining(0, R.string.dining, R.drawable.dining),
    Mosque(1, R.string.mosque, R.drawable.mosque),
    Shop(2, R.string.shop, R.drawable.shop);

    private Number id;
    private int resource_id;
    private int image_resourceId;

    public static String intent = "HGLocationType";

    HGLocationType(int id, int resource_id, int image_resourceId) {
        this.id = id;
        this.resource_id = resource_id;
        this.image_resourceId = image_resourceId;
    }

    //Determined from iOS app
    public static HGLocationType getLocation(int number) {
        switch (number) {
            case 0:
                return HGLocationType.Dining;
            case 1:
                return HGLocationType.Mosque;
            case 2:
                return HGLocationType.Shop;
            default:
                throw new IllegalArgumentException("Unknown location type");
        }
    }

    public Number getId() {
        return id;
    }

    public int getResource_id() {
        return resource_id;
    }

    public int getImageResourceId() {
        return image_resourceId;
    }

    @Override
    public String toString() {
        return HGApplication.getAppContext().getString(resource_id);
    }
}
