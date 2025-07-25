package Resturant;
import java.util.Arrays;

public class DessertsMenu extends BaseMenuCategory {

    public DessertsMenu() {
        super("Dessert");
        items.put(1, new MenuItem("Chocolate Brownies", 35.0, Arrays.asList("Vanilla Cream","ice cream")));
        items.put(2, new MenuItem("Ice cream", 20.0, Arrays.asList("Vanilla", "Chocolate")));
        items.put(3, new MenuItem("Fruit Sorbet",20.0,Arrays.asList("Mango","Strawberry","passion fruit")));
    }
}

