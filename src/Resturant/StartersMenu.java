package Resturant;
import java.util.Arrays;

public class StartersMenu extends BaseMenuCategory {

    public StartersMenu() {
        super("Starters");
        items.put(1, new MenuItem("Home fries", 28.0, Arrays.asList("Potato", " Sweet Potato")));
        items.put(2, new MenuItem("Side Salad", 25.0, Arrays.asList("Cucumber", "Cherry tomatoes", "Purple onion", " Vinaigrette")));
        items.put(3, new MenuItem("Focaccia sticks", 20.0, Arrays.asList(" White bread")));
    }
}
