package Resturant;
import java.util.Arrays;

public class DrinksMenu extends BaseMenuCategory {

    public DrinksMenu() {
        super("Drinks");
        items.put(1, new MenuItem("Coffee", 12.0, Arrays.asList("Ground Coffee beans", "Hot water", "Milk", "Sugar","Sweetner")));
        items.put(2, new MenuItem("Mint Tea", 10.0, Arrays.asList("Boiling Water", "Lemon", "Mint leaves","Honey")));
        items.put(3, new MenuItem("Fresh Orange Juice", 15.0, Arrays.asList("Fresh Oranges","Ice")));
        items.put(4, new MenuItem("Coca Cola", 13.0, Arrays.asList()));
        items.put(5, new MenuItem("Cola Zero",13.0,Arrays.asList()));
    }
}
