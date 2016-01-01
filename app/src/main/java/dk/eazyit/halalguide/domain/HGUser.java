package dk.eazyit.halalguide.domain;

import android.net.Uri;

import com.crashlytics.android.Crashlytics;
import com.parse.ParseClassName;
import com.parse.ParseUser;

import java.io.File;
import java.net.URI;
import java.util.Map;

import dk.eazyit.halalguide.BuildConfig;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Privat on 15/08/15.
 */
@ParseClassName("_User")
public class HGUser extends ParseUser {

    public Map<String, String> getFacebookUserData() {
        return (Map<String, String>) get("userData");
    }

    public String getFacebookId() {
        return getFacebookUserData().get("id");
    }

    public String getFacebookName() {
        return getFacebookUserData().get("name");
    }

    public String getFacebookLocation() {
        return getFacebookUserData().get("location.name");
    }

    public String getFacebookGender() {
        return getFacebookUserData().get("gender");
    }

    public String getFacebookBirthday() {
        return getFacebookUserData().get("birthday");
    }

    public String getFacebookRelationship() {
        return getFacebookUserData().get("relationship");
    }

    public URI getFacebookProfileUrlSmall() {
        try {
            return new URI("https://graph.facebook.com/" + getFacebookId() + "/picture?type=small&return_ssl_resources=1");
        } catch (Exception e) {
            return null;
        }
    }

    public URI getFacebookProfileUrl() {
        try {
            return new URI("https://graph.facebook.com/" + getFacebookId() + "/picture?type=normal&return_ssl_resources=1");
        } catch (Exception e) {
            return null;
        }
    }

}
