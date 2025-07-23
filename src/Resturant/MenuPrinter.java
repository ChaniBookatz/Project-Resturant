package Resturant;
import java.util.*;
import java.util.stream.Collectors;

public class MenuPrinter {

    private List<BaseMenuCategory> categories;
    private Map<Integer, BaseMenuCategory> categoryMapping;
    private List<String> currentOrder; // יכיל גם מידע על מרכיבים מותאמים אישית
    private String tableNumber; // מספר שולחן נשאר כאן כי הוא חלק מההזמנה הפעילה

    public MenuPrinter() {
        categories = new ArrayList<>();
        categoryMapping = new HashMap<>();
        currentOrder = new ArrayList<>();
        initializeMenuCategories();
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    private void initializeMenuCategories() {
        categories.add(new StartersMenu());
        categories.add(new MainCoursesMenu());
        categories.add(new DessertsMenu());
        categories.add(new DrinksMenu());

        for (int i = 0; i < categories.size(); i++) {
            categoryMapping.put(i + 1, categories.get(i));
        }
    }

    public void printCategories() {
        System.out.println("\n--- Menu Categories ---");
        for (Map.Entry<Integer, BaseMenuCategory> entry : categoryMapping.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue().getCategoryName());
        }
    }

    public String getCategoryName(int choice) {
        BaseMenuCategory category = categoryMapping.get(choice);
        return (category != null) ? category.getCategoryName() : null;
    }

    private BaseMenuCategory getMenuCategoryByName(String categoryName) {
        for (BaseMenuCategory category : categories) {
            if (category.getCategoryName().equals(categoryName)) {
                return category;
            }
        }
        return null;
    }

    public void printMenuForCategory(String categoryName) {
        BaseMenuCategory category = getMenuCategoryByName(categoryName);
        if (category == null) {
            System.out.println("Category doesn't exist.");
            return;
        }

        System.out.println("\n--- Menu " + category.getCategoryName() + " ---");
        Map<Integer, MenuItem> items = category.getMenuItems();

        if (items != null && !items.isEmpty()) {
            for (Map.Entry<Integer, MenuItem> entry : items.entrySet()) {
                MenuItem item = entry.getValue();
                System.out.println(entry.getKey() + ". " + item.getName() + " (" + item.getPrice() + " ש\"ח)");
                if (item.getIngredients() != null && !item.getIngredients().isEmpty()) {
                    System.out.println("   ingredients: " + String.join(", ", item.getIngredients()));
                } else {
                    System.out.println("No specified ingredients.");
                }
            }
        } else {
            System.out.println("Category is currently empty. try again another time.");
        }
    }

    public boolean isValidChoice(String categoryName, int itemChoice) {
        BaseMenuCategory category = getMenuCategoryByName(categoryName);
        return category != null && category.getMenuItems().containsKey(itemChoice);
    }

    public String getItemName(String categoryName, int itemChoice) {
        BaseMenuCategory category = getMenuCategoryByName(categoryName);
        return (category != null && category.getItem(itemChoice) != null) ? category.getItem(itemChoice).getName() : null;
    }

    public double getItemPrice(String categoryName, int itemChoice) {
        BaseMenuCategory category = getMenuCategoryByName(categoryName);
        return (category != null && category.getItem(itemChoice) != null) ? category.getItem(itemChoice).getPrice() : 0.0;
    }

    public MenuItem getMenuItem(String categoryName, int itemChoice) {
        BaseMenuCategory category = getMenuCategoryByName(categoryName);
        return (category != null) ? category.getItem(itemChoice) : null;
    }

        public List<String> customizeIngredients(Scanner scanner, MenuItem item) {

        if (item.getIngredients().isEmpty()) {
            System.out.println("Unable to personalise this dish.");
            return new ArrayList<>(item.getIngredients());
        }

        List<String> currentIngredients = new ArrayList<>(item.getIngredients());
        boolean customizing = true;

        while (customizing) {
            System.out.println("\n--- Personalisation of Dish: " + item.getName() + " ---");
            System.out.println(" Current ingredients: " + String.join(",", currentIngredients));
            System.out.println("Choose which ingredient you would like to remove, or '0' to go finalise.");

            for (int i = 0; i < currentIngredients.size(); i++) {
                System.out.println((i + 1) + ". " + currentIngredients.get(i));
            }

            System.out.print("Enter your choice: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice == 0) {
                    customizing = false;
                } else if (choice > 0 && choice <= currentIngredients.size()) {
                    String removedIngredient = currentIngredients.remove(choice - 1);
                    System.out.println(removedIngredient + "  was removed from your dish.");
                    System.out.println("------------------------");
                } else {
                    System.out.println("Unable to process your choice, please try again.");
                }
            } catch (NumberFormatException e) {
                    System.out.println("Unable to process your choice, please try again.");
            }
        }
        return currentIngredients;
    }

    public void addItemToOrder(String categoryName, int itemChoice, List<String> customIngredients) {
        if (isValidChoice(categoryName, itemChoice)) {
            String ingredientsString = customIngredients.stream()
                    .map(s -> s.replace(":", "\\:"))
                    .collect(Collectors.joining(";"));
            if (ingredientsString.isEmpty() && !getMenuItem(categoryName, itemChoice).getIngredients().isEmpty()) {
                ingredientsString = "Without ingredients";
            } else if (ingredientsString.isEmpty()) {
                ingredientsString = " that is not an accessible ingredient.";
            }
            currentOrder.add(categoryName + ":" + itemChoice + ":" + ingredientsString);
        }
    }

    public List<String> getCurrentOrder() {
        return new ArrayList<>(currentOrder); // החזרת עותק למניעת שינויים חיצוניים
    }

    // מתודות חדשות: Getter-ים למידע על תפריטים לצורך העברה ל-ReceiptGenerator
    public Map<String, Map<Integer, String>> getAllMenuItemsNamesByCategory() {
        Map<String, Map<Integer, String>> allMenuItems = new HashMap<>();
        for (BaseMenuCategory category : categories) {
            Map<Integer, String> itemsInCat = new HashMap<>();
            for (Map.Entry<Integer, MenuItem> entry : category.getMenuItems().entrySet()) {
                itemsInCat.put(entry.getKey(), entry.getValue().getName());
            }
            allMenuItems.put(category.getCategoryName(), itemsInCat);
        }
        return allMenuItems;
    }

    public Map<String, Map<Integer, Double>> getAllMenuItemsPricesByCategory() {
        Map<String, Map<Integer, Double>> allItemPrices = new HashMap<>();
        for (BaseMenuCategory category : categories) {
            Map<Integer, Double> pricesInCat = new HashMap<>();
            for (Map.Entry<Integer, MenuItem> entry : category.getMenuItems().entrySet()) {
                pricesInCat.put(entry.getKey(), entry.getValue().getPrice());
            }
            allItemPrices.put(category.getCategoryName(), pricesInCat);
        }
        return allItemPrices;
    }

    // הצגת סיכום ההזמנה (לצורך הצגה בלבד, לא לחישובים או תשלום)
    public void displayOrderSummary() {
        if (currentOrder.isEmpty()) {
            System.out.println("\nOrder is currently empty.");
            return;
        }

        System.out.println("\n--- Order Summary ---");
        System.out.println(" Table number: " + tableNumber);
        System.out.println("-------------------------");
        double subTotal = 0.0;

        int itemCounter = 1;
        for (String orderItem : currentOrder) {
            String[] parts = orderItem.split(":");
            String categoryName = parts[0];
            int itemNumber = Integer.parseInt(parts[1]);
            String customIngredientsString = (parts.length > 2) ? parts[2] : "";

            BaseMenuCategory category = getMenuCategoryByName(categoryName);
            if (category != null) {
                MenuItem item = category.getItem(itemNumber);
                if (item != null) {
                    System.out.printf("%d. %s (%s) - %.2f ש\"ח%n", itemCounter, item.getName(), category.getCategoryName(), item.getPrice());
                    if (!customIngredientsString.isEmpty() && !customIngredientsString.equals("No specified ingredients")) {
                        System.out.println("personalised ingredients: " + customIngredientsString.replace(";", ", ").replace("\\:", ":"));
                    } else if (customIngredientsString.equals("No specified ingredients") && !item.getIngredients().isEmpty()) {
                        System.out.println("All ingredients were removed");
                    }
                    subTotal += item.getPrice();
                    itemCounter++;
                }
            }
        }

        System.out.println("-------------------------");
        System.out.printf("Total  (without tip): %.2f ש\"ח%n", subTotal);
        System.out.println("-------------------------");
    }

    // מתודה לאיפוס ההזמנה לאחר תשלום מוצלח
    public void clearCurrentOrder() {
        currentOrder.clear();
    }
}
