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
                    System.out.println("Thank you and goodbye! ");
                    running = false; // יציאה מהלולאה הראשית
                    break;
                default:
                    System.out.println("Unable to process your choice. Please try again: ");
                    break;
            }
        }
        scanner.close(); // סגירת הסקאנר בסיום
    }
}