package dk.eazyit.halalguide;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import dk.eazyit.halalguide.domain.HGDiningCategory;
import dk.eazyit.halalguide.domain.HGLanguage;
import dk.eazyit.halalguide.domain.HGShopCategory;

/**
 * Created by Privat on 09/07/15.
 */
public class HGSettings {

    private static HGSettings instance = null;
    SharedPreferences preferences;
    Number maximumDistanceShop;
    Number maximumDistanceDining;
    Number maximumDistanceMosque;
    Boolean halalFilter;
    Boolean alcoholFilter;
    Boolean porkFilter;
    List<HGDiningCategory> categoriesFilter;
    List<HGShopCategory> shopCategoriesFilter;
    HGLanguage language;
    List<String> favorites;
    Gson gson;

    protected HGSettings() {
        Context mContext = HGApplication.getAppContext();
        preferences = mContext.getSharedPreferences("dk.eazyit.halalguide", 0);
        gson = new Gson();
        maximumDistanceShop = getMaximumDistanceShop();
        maximumDistanceDining = getMaximumDistanceDining();
        maximumDistanceMosque = getMaximumDistanceMosque();

        halalFilter = isHalalFilter();
        alcoholFilter = isAlcoholFilter();
        porkFilter = isPorkFilter();

        categoriesFilter = getCategoriesFilter();
        shopCategoriesFilter = getShopCategoriesFilter();
        favorites = getFavorites();

        language = getLanguage();

    }

    public static HGSettings getInstance() {
        if (instance == null) {
            instance = new HGSettings();
        }
        return instance;
    }

    public Number getMaximumDistanceShop() {
        if (maximumDistanceShop == null) {
            maximumDistanceShop = preferences.getInt("maximumDistanceShop", 5);
        }
        return maximumDistanceShop;
    }

    public void setMaximumDistanceShop(Number maximumDistanceShop) {
        this.maximumDistanceShop = maximumDistanceShop;
    }

    public Number getMaximumDistanceDining() {
        if (maximumDistanceDining == null) {
            maximumDistanceDining = preferences.getInt("maximumDistanceDining", 5);
        }
        return maximumDistanceDining;
    }

    public void setMaximumDistanceDining(Number maximumDistanceDining) {
        this.maximumDistanceDining = maximumDistanceDining;
    }

    public Number getMaximumDistanceMosque() {
        if (maximumDistanceMosque == null) {
            maximumDistanceMosque = preferences.getInt("maximumDistanceMosque", 5);
        }
        return maximumDistanceMosque;
    }

    public void setMaximumDistanceMosque(Number maximumDistanceMosque) {
        this.maximumDistanceMosque = maximumDistanceMosque;
    }

    public Boolean isHalalFilter() {
        if (halalFilter == null) {
            halalFilter = preferences.getBoolean("halalFilter", false);
        }
        return halalFilter;
    }

    public void setHalalFilter(boolean halalFilter) {
        this.halalFilter = halalFilter;
    }

    public Boolean isAlcoholFilter() {
        if (alcoholFilter == null) {
            alcoholFilter = preferences.getBoolean("alcoholFilter", false);
        }
        return alcoholFilter;
    }

    public void setAlcoholFilter(boolean alcoholFilter) {
        this.alcoholFilter = alcoholFilter;
    }

    public Boolean isPorkFilter() {
        if (porkFilter == null) {
            porkFilter = preferences.getBoolean("porkFilter", false);
        }
        return porkFilter;
    }

    public void setPorkFilter(boolean porkFilter) {
        this.porkFilter = porkFilter;
    }

    public List<HGDiningCategory> getCategoriesFilter() {
        if (categoriesFilter == null) {
            String json = preferences.getString("categoriesFilter", "[]");
            Type listType = new TypeToken<ArrayList<HGDiningCategory>>() {
            }.getType();
            categoriesFilter = new Gson().fromJson(json, listType);
        }
        return categoriesFilter;
    }

    public void setCategoriesFilter(List<HGDiningCategory> categoriesFilter) {
        this.categoriesFilter = categoriesFilter;
    }

    public List<HGShopCategory> getShopCategoriesFilter() {
        if (shopCategoriesFilter == null) {
            String json = preferences.getString("shopCategoriesFilter", "[]");
            Type listType = new TypeToken<ArrayList<HGShopCategory>>() {
            }.getType();
            shopCategoriesFilter = new Gson().fromJson(json, listType);
        }
        return shopCategoriesFilter;
    }

    public void setShopCategoriesFilter(List<HGShopCategory> shopCategoriesFilter) {
        this.shopCategoriesFilter = shopCategoriesFilter;
    }

    public HGLanguage getLanguage() {
        if (language == null) {
            String json = preferences.getString("language", gson.toJson(HGLanguage.None));
            language = new Gson().fromJson(json, HGLanguage.class);
        }
        return language;
    }

    public void setLanguage(HGLanguage language) {
        this.language = language;
    }

    public List<String> getFavorites() {
        if (favorites == null) {
            String json = preferences.getString("favorites", "[]");
            Type listType = new TypeToken<ArrayList<String>>() {
            }.getType();
            favorites = new Gson().fromJson(json, listType);
        }
        return favorites;
    }

    public void setFavorites(List<String> favorites) {
        this.favorites = favorites;
    }

    public void persistFilters() {
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();

        editor.putInt("maximumDistanceShop", maximumDistanceShop.intValue());
        editor.putInt("maximumDistanceDining", maximumDistanceDining.intValue());
        editor.putInt("maximumDistanceMosque", maximumDistanceMosque.intValue());

        editor.putBoolean("halalFilter", halalFilter);
        editor.putBoolean("alcoholFilter", alcoholFilter);
        editor.putBoolean("porkFilter", porkFilter);

        editor.putString("categoriesFilter", gson.toJson(categoriesFilter));
        editor.putString("shopCategoriesFilter", gson.toJson(shopCategoriesFilter));

        editor.putString("language", gson.toJson(language));

        editor.putString("favorites", gson.toJson(favorites));

        editor.commit();

    }

    public void discardFilterChanges() {
        favorites = null;
        language = null;
        shopCategoriesFilter = null;
        categoriesFilter = null;
        porkFilter = null;
        alcoholFilter = null;
        halalFilter = null;
        maximumDistanceMosque = null;
        maximumDistanceDining = null;
        maximumDistanceShop = null;
    }
}
