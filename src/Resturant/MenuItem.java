package Resturant;
import java.util.List;
import java.util.Collections;

public class MenuItem {
    private String name;
    private double price;
    private List<String> ingredients; // הוספת רשימת מרכיבים

    public MenuItem(String name, double price, List<String> ingredients) {
        this.name = name;
        this.price = price;
        // יצירת רשימה בלתי ניתנת לשינוי כדי למנוע שינויים חיצוניים
        this.ingredients = Collections.unmodifiableList(ingredients);
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getIngredients() {
        return ingredients;
    }
}

