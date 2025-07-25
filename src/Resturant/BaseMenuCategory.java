package Resturant;
import java.util.HashMap;
import java.util.Map;

public class BaseMenuCategory { // לא אבסטרקטית ולא ממשק
    protected String categoryName; // Protected כדי שהמחלקות היורשות יוכלו לגשת אליו
    protected Map<Integer, MenuItem> items; // Protected כדי שהמחלקות היורשות יוכלו לגשת אליו

    public BaseMenuCategory(String categoryName) {
        this.categoryName = categoryName;
        this.items = new HashMap<>();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Map<Integer, MenuItem> getMenuItems() {
        return items;
    }

    public MenuItem getItem(int itemNumber) {
        return items.get(itemNumber);
    }
}
