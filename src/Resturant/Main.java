package Resturant;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RestaurantManager manager = new RestaurantManager(scanner);
        boolean running = true;

        System.out.println("Welcome to our restaurant!");

        // הגדרת מספר שולחן
        manager.initializeTableNumber();

        // לולאת התפריט הראשי
        while (running) {
            manager.printMainMenu(); // הדפסת התפריט
            String mainChoice = scanner.nextLine(); // קליטת בחירה

            switch (mainChoice) {
                case "1":
                    // טיפול בהזמנה: אם המתודה מחזירה 1, זה אומר יציאה מהתוכנית כולה
                    if (manager.handleMenuAndOrderInteraction() == 1) {
                        running = false;
                    }
                    break;
                case "2":
                    manager.displayCurrentOrderSummary(); // הצגת סיכום הזמנה
                    break;
                case "3":
                    System.out.println("Your receipt will be automatically saved after payment.");
                    System.out.println("Please select '4' inorder to pay.");
                    break;
                case "4":
                    // טיפול בתשלום: אם המתודה מחזירה true, זה אומר שהתשלום בוצע בהצלחה וצריך לצאת
                    if (manager.handlePayment()) {
                        running = false;
                    }
                    break;
                case "5":
                    if (!manager.isOrderEmpty()) { // נניח שנוסיף מתודה isOrderEmpty ל-RestaurantManager
                        System.out.println("\n!!! אזהרה: קיימת הזמנה פתוחה ולא משולמת בשולחן " + manager.getTableNumber() + " !!!");
                        System.out.print("האם אתה בטוח שברצונך לצאת מבלי לשלם? (כן/לא): ");
                        String confirmExit = scanner.nextLine();
                        if (confirmExit.equalsIgnoreCase("כן")) {
                            System.out.println("תודה ולהתראות!");
                            running = false;
                        } else {
                            System.out.println("ביטול יציאה. חוזרים לתפריט הראשי.");
                        }
                    } else {
                        System.out.println("תודה ולהתראות!");
                        running = false;
                    }
                    break;
                default:
                    System.out.println("Unable to process your choice. Please try again: ");
                    break;
            }
        }
        scanner.close(); // סגירת הסקאנר בסיום
    }
}
