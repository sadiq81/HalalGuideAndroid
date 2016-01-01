package dk.eazyit.halalguide.domain;

import dk.eazyit.halalguide.HGApplication;
import dk.eazyit.halalguide.R;

/**
 * Created by Privat on 09/07/15.
 */
public enum HGLanguage implements HGCategory {

    None(0, R.string.none, android.R.id.empty),
    Danish(1, R.string.danish, R.drawable.danish),
    Arabic(2, R.string.arabic, R.drawable.arabic),
    Urdu(3, R.string.urdu, R.drawable.urdu),
    English(4, R.string.english, R.drawable.english);

    private Number id;
    private int resource_id;
    private int image_resourceId;

    HGLanguage(Number id, int resource_id, int image_resourceId) {
        this.id = id;
        this.resource_id = resource_id;
        this.image_resourceId = image_resourceId;
    }

    //Determined from iOS app
    public static HGLanguage getLanguage(int number) {
        switch (number) {
            case 0:
                return HGLanguage.None;
            case 1:
                return HGLanguage.Danish;
            case 2:
                return HGLanguage.Arabic;
            case 3:
                return HGLanguage.Urdu;
            case 4:
                return HGLanguage.English;
            default:
                return HGLanguage.None;

        }
    }

    public Number getId() {
        return id;
    }

    @Override
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
