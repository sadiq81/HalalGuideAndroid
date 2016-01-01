package dk.eazyit.halalguide.domain;

import dk.eazyit.halalguide.R;

/**
 * Created by Privat on 10/07/15.
 */
public enum HGShopCategory implements HGCategory {

    Groceries(0, R.string.groceries),
    Furniture(1, R.string.favorites),
    Butcher(2, R.string.butcher);

    private Number id;
    private int resource_id;

    HGShopCategory(int id, int resource_id) {
        this.id = id;
        this.resource_id = resource_id;
    }

    public Number getId() {
        return id;
    }

    public int getResource_id() {
        return resource_id;
    }

    public static HGShopCategory getCategory(Number id) {
        for (HGShopCategory cat : HGShopCategory.values()) {
            if (id.equals(cat.getId())) {
                return cat;
            }
        }
        return null;
    }
}
