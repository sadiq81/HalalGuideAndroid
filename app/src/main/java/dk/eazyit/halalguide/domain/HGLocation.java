package dk.eazyit.halalguide.domain;

import android.content.Context;
import android.location.Location;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;

import java.util.List;

@ParseClassName("Location")
public class HGLocation extends HGBaseEntity /*implements Parcelable */ {

    public static String INTENT = "dk.eazyit.halalguide.domain.hglocation";

    public HGLocation() {
    }

    public String getAddressCity() {
        return getString("addressCity");
    }

    public void setAddressCity(String addressCity) {
        if (addressCity != null) {
            put("addressCity", addressCity);
        }
    }

    public String getAddressPostalCode() {
        return getString("addressPostalCode");
    }

    public void setAddressPostalCode(String addressPostalCode) {
        if (addressPostalCode != null) {
            put("addressPostalCode", addressPostalCode);
        }
    }

    public String getAddressRoad() {
        return getString("addressRoad");
    }

    public void setAddressRoad(String addressRoad) {
        if (addressRoad != null) {
            put("addressRoad", addressRoad);
        }
    }

    public String getAddressRoadNumber() {
        return getString("addressRoadNumber");
    }

    public void setAddressRoadNumber(String addressRoadNumber) {
        if (addressRoadNumber != null) {
            put("addressRoadNumber", addressRoadNumber);
        }
    }

    public boolean getAlcohol() {
        return getBoolean("alcohol");
    }

    public void setAlcohol(boolean alcohol) {
        put("alcohol", alcohol);
    }


    public Number getCreationStatus() {
        return getNumber("creationStatus");
    }

    public void setCreationStatus(Number creationStatus) {
        if (creationStatus != null) {
            put("creationStatus", creationStatus);
        }
    }

    public Number getDistance() {
        return 0;
    }

    public String getHomePage() {
        return getString("homePage");
    }

    public void setHomePage(String homePage) {
        if (homePage != null) {
            put("homePage", homePage);
        }
    }

    public ParseGeoPoint getPoint() {
        return getParseGeoPoint("point");
    }

    public void setPoint(ParseGeoPoint point) {
        if (point != null) {
            put("point", point);
        }
    }

    public Number getLocationType() {
        return getNumber("locationType");
    }

    public void setLocationType(Number locationType) {
        if (locationType != null) {
            put("locationType", locationType);
        }
    }

    public Number getLanguage() {
        return getNumber("language");
    }

    public void setLanguage(Number language) {
        if (language != null) {
            put("language", language);
        }
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        if (name != null) {
            put("name", name);
        }
    }

    public boolean getNonHalal() {
        return getBoolean("nonHalal");
    }

    public void setNonHalal(boolean nonHalal) {
        put("nonHalal", nonHalal);
    }

    public boolean getPork() {
        return getBoolean("pork");
    }

    public void setPork(boolean pork) {
        put("pork", pork);
    }

    public String getSubmitterId() {
        return getString("submitterId");
    }

    public void setSubmitterId(String submitterId) {
        if (submitterId != null) {
            put("submitterId", submitterId);
        }
    }

    public String getTelephone() {
        return getString("telephone");
    }

    public void setTelephone(String telephone) {
        if (telephone != null) {
            put("telephone", telephone);
        }
    }

    public List<Number> getCategories() {
        return (List<Number>) get("categories");
    }

    public void setCategories(List<Number> categories) {
        if (categories != null) {
            put("categories", categories);
        }
    }

    public List<String> getNavneloebenummer() {
        return (List<String>) get("navneloebenummer");
    }

    public void setNavneloebenummer(List<String> navneloebenummer) {
        if (navneloebenummer != null) {
            put("navneloebenummer", navneloebenummer);
        }
    }

    public String getFacebookId() {
        return getString("facebookId");
    }

    public void setFacebookId(String facebookId) {
        if (facebookId != null) {
            put("facebookId", facebookId);
        }
    }

    public Location getLocation() {
        Location loc = new Location("default");
        loc.setLatitude(getPoint().getLatitude());
        loc.setLongitude(getPoint().getLongitude());
        return loc;
    }

    public String getCategoryString(Context context) {

        if (getCategories() == null || getCategories().size() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Number number : getCategories()) {
            switch (getLocationType().intValue()) {
                case 0: {
                    String value = context.getResources().getString(HGDiningCategory.getCategory(number).getResource_id());
                    sb.append(value + ", ");
                    break;
                }
                case 1: {
                    String value = context.getResources().getString(HGShopCategory.getCategory(number).getResource_id());
                    sb.append(value + ", ");
                    break;
                }
                case 2: {
                    String value = context.getResources().getString(HGLanguage.getLanguage(number.intValue()).getResource_id());
                    sb.append(value + ", ");
                    break;
                }
            }

        }
        sb.delete(sb.length() - 2, sb.length() - 1);
        return sb.toString();
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(getObjectId());
//        dest.writeInt(getCreationStatus().intValue());
//        dest.writeSerializable((Serializable) getNavneloebenummer());
//        dest.writeString(getSubmitterId());
//        dest.writeString(getName());
//        dest.writeString(getAddressRoad());
//        dest.writeString(getAddressRoadNumber());
//        dest.writeDouble(getPoint().getLatitude());
//        dest.writeDouble(getPoint().getLongitude());
//        dest.writeString(getAddressCity());
//        dest.writeString(getAddressPostalCode());
//        dest.writeByte((byte) (getPork() ? 1 : 0));
//        dest.writeByte((byte) (getAlcohol() ? 1 : 0));
//        dest.writeByte((byte) (getNonHalal() ? 1 : 0));
//        dest.writeSerializable((Serializable) getCategories());
//        dest.writeString(getHomePage());
//        dest.writeInt(getLanguage().intValue());
//        dest.writeInt(getLocationType().intValue());
//        dest.writeString(getTelephone());
//        dest.writeLong(getCreatedAt().getTime());
//        dest.writeLong(getUpdatedAt().getTime());
//
//    }
//
//    public static final Parcelable.Creator<HGLocation> CREATOR = new Parcelable.Creator<HGLocation>() {
//        public HGLocation createFromParcel(Parcel in) {
//            HGLocation loc = HGLocation.createWithoutData(HGLocation.class, in.readString());
//            loc.setCreationStatus(in.readInt());
//            loc.setNavneloebenummer((List<String>) in.readSerializable());
//            loc.setSubmitterId(in.readString());
//            loc.setName(in.readString());
//            loc.setAddressRoad(in.readString());
//            loc.setAddressRoadNumber(in.readString());
//            loc.setPoint(new ParseGeoPoint(in.readDouble(), in.readDouble()));
//            loc.setAddressCity(in.readString());
//            loc.setAddressPostalCode(in.readString());
//            loc.setPork(in.readByte() == 1);
//            loc.setAlcohol(in.readByte() == 1);
//            loc.setNonHalal(in.readByte() == 1);
//            loc.setCategories((List<Number>) in.readSerializable());
//            loc.setHomePage(in.readString());
//            loc.setLanguage(in.readInt());
//            loc.setLocationType(in.readInt());
//            loc.setTelephone(in.readString());
//            loc.setCreatedAt(new Date(in.readLong()));
//            loc.setUpdatedAt(new Date(in.readLong()));
//            return loc;
//        }
//
//        public HGLocation[] newArray(int size) {
//            return new HGLocation[size];
//        }
//    };

}
