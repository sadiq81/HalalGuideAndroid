package dk.eazyit.halalguide.domain;

import dk.eazyit.halalguide.R;

/**
 * Created by Privat on 07/07/15.
 */
public enum HGDiningCategory implements HGCategory {

    Afghan(0, R.string.afghan), African(1, R.string.african), American(2, R.string.american), Argentine(3, R.string.argentine),
    Asian(4, R.string.asian), Belgian(5, R.string.belgian), Brazilian(6, R.string.brazilian), British(7, R.string.british),
    Buffet(8, R.string.buffet), Burger(9, R.string.burger), Bakery(10, R.string.bakery), Bagel(11, R.string.bagel),
    BubbleThea(12, R.string.bubbleThea), Arabic(13, R.string.arabic), Cafe(14, R.string.cafe), Caribbean(14, R.string.caribbean),
    Cupcake(15, R.string.cupcake), Candy(16, R.string.candy), Chinese(17, R.string.chinese), Danish(18, R.string.danish),
    Dessert(19, R.string.dessert), Fish(20, R.string.fish), Fruit(21, R.string.fruit), FastFood(22, R.string.fastFood),
    French(23, R.string.french), German(24, R.string.german), Grill(25, R.string.grill), Greek(26, R.string.greek),
    IceCream(27, R.string.iceCream), Juice(28, R.string.juice), Kiosk(29, R.string.kiosk), Indian(30, R.string.indian),
    Indonesian(31, R.string.indonesian), Irish(32, R.string.irish), Italian(33, R.string.italian), Iranian(34, R.string.iranian),
    Japanese(35, R.string.japanese), Kebab(36, R.string.kebab), Korean(37, R.string.korean), Kosher(38, R.string.kosher),
    Lebanese(39, R.string.lebanese), Mediterranean(40, R.string.mediterranean), Malaysian(41, R.string.malaysian), Moroccan(42, R.string.moroccan),
    Mexican(43, R.string.mexican), Nordic(44, R.string.nordic), Nepalese(45, R.string.nepalese), Pastry(46, R.string.pastry),
    Pakistani(47, R.string.pakistani), Persian(48, R.string.persian), Pizza(49, R.string.pizza), Portuguese(50, R.string.portuguese),
    Russian(51, R.string.russian), Seafood(52, R.string.seafood), Salad(53, R.string.salad), Sandwich(54, R.string.sandwich),
    Spanish(55, R.string.spanish), Steak(56, R.string.steak), Soup(57, R.string.soup), Sushi(58, R.string.sushi),
    Tapas(59, R.string.tapas), Thai(60, R.string.thai), Tibetan(61, R.string.tibetan), Turkish(62, R.string.turkish),
    Vegan(63, R.string.vegan), Vietnamese(64, R.string.vietnamese), Wok(65, R.string.wok);

    private Number id;
    private int resource_id;

    HGDiningCategory(int id, int resource_id) {
        this.id = id;
        this.resource_id = resource_id;
    }

    public Number getId() {
        return id;
    }

    public int getResource_id() {
        return resource_id;
    }

    public static HGDiningCategory getCategory(Number id) {
        for (HGDiningCategory cat : HGDiningCategory.values()) {
            if (id.equals(cat.getId())) {
                return cat;
            }
        }
        return null;
    }

}
