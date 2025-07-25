package Resturant;
import java.util.Arrays;

public class MainCoursesMenu extends BaseMenuCategory {

    public MainCoursesMenu() {
        super("Main Courses");
        items.put(1, new MenuItem("Steak", 75.0, Arrays.asList("Beef 250 grams", "Sauce of the house","Mashed potatoes" )));
        items.put(2, new MenuItem("Ribs", 80.0, Arrays.asList("Ribs 250 grams", "Grilled potatoes")));
        items.put(3, new MenuItem("Beef burger", 65.0, Arrays.asList("White bun", "Burger 250 grams", "Lettuce", "Tomato", "Golden onion")));
        items.put(4, new MenuItem("Chicken Salad",60.0, Arrays.asList("Lettuce","Cherry tomato","Cucumber","Red Onion","Grilled Chicken")));
    }
}

