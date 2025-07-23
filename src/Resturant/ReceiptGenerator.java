package Resturant;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ReceiptGenerator {
    private List<String> orderItemsData; // יכיל את כל הנתונים של ההזמנה (קטגוריה:מספר:מרכיבים מותאמים)
    private Map<String, Map<Integer, String>> menuItemsNamesByCategory; // מפה של שמות פריטים לפי קטגוריה ומספר
    private Map<String, Map<Integer, Double>> menuItemsPricesByCategory; // מפה של מחירים לפי קטגוריה ומספר
    private String tableNumber;
    private double tipAmount;
    private double subTotal; // נוסיף שדה לשמירת הסכום לתשלום לפני טיפ

    // בנאי מעודכן שיקבל את כל הנתונים הדרושים
    public ReceiptGenerator(List<String> orderItemsData,
                            Map<String, Map<Integer, String>> menuItemsNamesByCategory,
                            Map<String, Map<Integer, Double>> menuItemsPricesByCategory,
                            String tableNumber) {
        this.orderItemsData = orderItemsData;
        this.menuItemsNamesByCategory = menuItemsNamesByCategory;
        this.menuItemsPricesByCategory = menuItemsPricesByCategory;
        this.tableNumber = tableNumber;
        this.tipAmount = 0.0; // אתחול טיפ
        this.subTotal = calculateSubTotal(); // חישוב subTotal בעת יצירת האובייקט
    }

    public void setTipAmount(double tipAmount) {
        this.tipAmount = tipAmount;
    }

    public double getTipAmount() {
        return tipAmount;
    }

    private void saveReceipt() {
        String directoryPath = "receipts/";
        File directory = new File(directoryPath);
        // Create the 'receipts' directory if it doesn't exist
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                System.err.println("שגיאה: לא ניתן ליצור תיקיית קבלות בנתיב: " + directoryPath);
                return; // Exit if directory creation fails
            }
        }

        String filename = directoryPath + "receipt_table_" + tableNumber + "_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")) + ".txt";

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("--- Receipt ---\n");
            writer.write("Date and time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "\n");
            writer.write("Table number: " + tableNumber + "\n");
            writer.write("--------------------------------\n");
            writer.write("Order details:\n");

            for (String orderItem : orderItemsData) {
                String[] parts = orderItem.split(":");
                String categoryName = parts[0];
                int itemNumber = Integer.parseInt(parts[1]);
                String customIngredientsString = (parts.length > 2) ? parts[2] : "";

                String itemName = menuItemsNamesByCategory.get(categoryName).get(itemNumber);
                double itemPrice = menuItemsPricesByCategory.get(categoryName).get(itemNumber);

                writer.write(String.format("  - %s (%s): %.2f ש\"ח%n", itemName, categoryName, itemPrice));
                if (!customIngredientsString.isEmpty() && !customIngredientsString.equals("no customised ingredients")) {
                    writer.write(String.format("ingredients: %s%n", customIngredientsString.replace(";", ", ").replace("\\:", ":")));
                }
            }
            writer.write("--------------------------------\n");
            writer.write(String.format("Total (without tip): %.2f ש\"ח%n", subTotal));
            writer.write(String.format("tip: %.2f ש\"ח%n", tipAmount));
            writer.write(String.format(" Total payment(with tip): %.2f ש\"ח%n", subTotal + tipAmount));
            writer.write("--------------------------------\n");
            writer.write("Thank you for eating at us!\n");

            System.out.println("Receipt has been saved as: " + filename);
        } catch (IOException e) {
            System.err.println("Unable to save receipt: " + e.getMessage());
        }
    }

    // חישוב הסכום לתשלום לפני טיפ
    private double calculateSubTotal() {
        double total = 0.0;
        for (String orderItem : orderItemsData) {
            String[] parts = orderItem.split(":");
            String categoryName = parts[0];
            int itemNumber = Integer.parseInt(parts[1]);

            Map<Integer, Double> prices = menuItemsPricesByCategory.get(categoryName);
            if (prices != null && prices.containsKey(itemNumber)) {
                total += prices.get(itemNumber);
            }
        }
        return total;
    }

    public double getSubTotal() {
        return subTotal;
    }

    // מתודת התשלום עוברת לכאן
    public boolean processPayment(Scanner scanner) {

        if (orderItemsData.isEmpty()) {
            System.out.println("\nThere are no dishes ordered inorder to save a receipt.");
            return false;
        }

        displayOrderSummaryForPayment();

        double totalAmount = subTotal + tipAmount;
        System.out.printf("\nTotal payment (including tip of %.2f ש\"ח) is: %.2f ש\"ח%n", tipAmount, totalAmount);

        boolean paymentCompletedSuccessfully = false;
        boolean paymentChoiceMade = false;

        while (!paymentChoiceMade) {
            System.out.println("\n--- Choose desired payment method ---");
            System.out.println("1. cash");
            System.out.println("2. card");
            System.out.println("3. cancel payment");
            System.out.print("Enter your choice: ");
            String paymentChoice = scanner.nextLine();

            switch (paymentChoice) {
                case "1":
                    System.out.print("Please enter how much cash was given:");
                    try {
                        double amountReceived = Double.parseDouble(scanner.nextLine());
                        if (amountReceived >= totalAmount) {
                            double change = amountReceived - totalAmount;
                            System.out.printf("Payment was received, change due is: %.2f ש\"ח%n", change);
                            System.out.println("Thank you, order has been placed! Enjoy your meal");
                            saveReceipt(); // שמירת קבלה
                            paymentCompletedSuccessfully = true;
                            paymentChoiceMade = true;
                        } else {
                            System.out.printf("Payment received (%.2f ש\"ח) is too low. Please change payment method or add more money.%n", amountReceived);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Unable to process your choice. please try again: ");
                    }
                    break;
                case "2":
                    System.out.println("Preprocessing your card payment...");
                    System.out.printf("Card payment was successful. Payment made: %.2f ש\"ח.%n", totalAmount);
                    System.out.println("Thank you, your order has been placed! Enjoy your meal :) ");
                    System.out.println("DEBUG: Calling saveReceipt() now...");
                    saveReceipt(); // שמירת קבלה
                    paymentCompletedSuccessfully = true;
                    paymentChoiceMade = true;
                    break;
                case "3":
                    System.out.println("Payment has been cancelled. you can add more to your order or exit");
                    paymentCompletedSuccessfully = false;
                    paymentChoiceMade = true;
                    break;
                default:
                    System.out.println("Unable to process your choice. please try again: ");
                    break;
            }
        }
        return paymentCompletedSuccessfully;
    }

    // מתודה פנימית להצגת סיכום הזמנה עבור תהליך התשלום
    private void displayOrderSummaryForPayment() {
        if (orderItemsData.isEmpty()) {
            System.out.println("\nOrder is currently empty.");
            return;
        }

        System.out.println("\n--- Final order for payment: ---");
        System.out.println("Table number: " + tableNumber);
        System.out.println("-------------------------");

        int itemCounter = 1;
        for (String orderItem : orderItemsData) {
            String[] parts = orderItem.split(":");
            String categoryName = parts[0];
            int itemNumber = Integer.parseInt(parts[1]);
            String customIngredientsString = (parts.length > 2) ? parts[2] : "";

            String itemName = menuItemsNamesByCategory.get(categoryName).get(itemNumber);
            double itemPrice = menuItemsPricesByCategory.get(categoryName).get(itemNumber);

            System.out.printf("%d. %s (%s) - %.2f ש\"ח%n", itemCounter, itemName, categoryName, itemPrice);
            if (!customIngredientsString.isEmpty()) {
                System.out.println("with personalised ingredients: " + customIngredientsString.replace(";", ", ").replace("\\:", ":"));
            }
            itemCounter++;
        }

        System.out.println("-------------------------");
        System.out.printf("Total payment (without tip): %.2f ש\"ח%n", subTotal);
        // טיפ מוצג רק אם כבר נקבע (בשלב הטיפ לפני התשלום)
        if (tipAmount > 0) {
            System.out.printf("tip: %.2f ש\"ח%n", tipAmount);
            System.out.printf("Total payment, including tip: %.2f ש\"ח%n", subTotal + tipAmount);
        }
        System.out.println("-------------------------");
    }
}
