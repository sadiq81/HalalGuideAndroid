package dk.eazyit.halalguide.domain;

import com.parse.ParseClassName;
import com.parse.ParseFile;

/**
 * Created by Privat on 05/08/15.
 */
@ParseClassName("LocationPicture")
public class HGLocationPicture extends HGBaseEntity /*implements Parcelable */ {

    public HGLocationPicture() {
    }

    public Number getCreationStatus() {
        return getNumber("creationStatus");
    }

    public void setCreationStatus(Number creationStatus) {
        put("creationStatus", creationStatus);
    }

    public String getLocationId() {
        return getString("locationId");
    }

    public void setLocationId(String locationId) {
        put("locationId", locationId);
    }

    public String getReviewId() {
        return getString("reviewId");
    }

    public void setReviewId(String reviewId) {
        put("reviewId", reviewId);
    }

    public ParseFile getPicture() {
        return getParseFile("picture");
    }

    public void setPicture(ParseFile picture) {
        put("picture", picture);
    }

    public String getSubmitterId() {
        return getString("submitterId");
    }

    public void setSubmitterId(String submitterId) {
        put("submitterId", submitterId);
    }

    public ParseFile getThumbnail() {
        return getParseFile("thumbnail");
    }

    public void setThumbnail(ParseFile thumbnail) {
        put("thumbnail", thumbnail);
    }

    public ParseFile getMediumPicture() {
        return getParseFile("mediumPicture");
    }

    public void setMediumPicture(ParseFile mediumPicture) {
        put("mediumPicture", mediumPicture);
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        try {
//            dest.writeString(getObjectId());
//            dest.writeInt(getCreationStatus().intValue());
//            dest.writeString(getLocationId());
//            dest.writeString(getReviewId());
//
//            byte[] data = getPicture().getData();
//            dest.writeInt(data.length);
//            dest.writeByteArray(data);
//
//            dest.writeString(getPicture().getName());
//            dest.writeString(getSubmitterId());
//            dest.writeLong(getCreatedAt().getTime());
//            dest.writeLong(getUpdatedAt().getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static final Parcelable.Creator<HGLocationPicture> CREATOR = new Parcelable.Creator<HGLocationPicture>() {
//        public HGLocationPicture createFromParcel(Parcel in) {
//            HGLocationPicture loc = HGLocationPicture.createWithoutData(HGLocationPicture.class, in.readString());
//            loc.setCreationStatus(in.readInt());
//            loc.setLocationId(in.readString());
//            loc.setReviewId(in.readString());
//
//            int size = in.readInt();
//            byte[] data = new byte[size];
//            in.readByteArray(data);
//            in.createByteArray();
//            loc.setPicture(new ParseFile(in.readString(), data));
//            loc.setSubmitterId(in.readString());
//
//            loc.setCreatedAt(new Date(in.readLong()));
//            loc.setUpdatedAt(new Date(in.readLong()));
//            return loc;
//        }
//
//        public HGLocationPicture[] newArray(int size) {
//            return new HGLocationPicture[size];
//        }
//    };
}

