package budget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class BudgetController {

    private double income;
    private final List<Purchase> purchases;
    private final Scanner scanner;
    private final PurchaseSorter sorter;

    public BudgetController(Scanner scanner) {
        this.scanner = scanner;
        this.purchases = new ArrayList<>();
        this.sorter = new PurchaseSorter();
    }

    public void start() {
        boolean isOn = true;

        while (isOn) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addIncome();
                    break;

                case "2":
                    addPurchase();
                    break;

                case "3":
                    listPurchases();
                    break;

                case "4":
                    printBalance();
                    break;

                case "5":
                    save();
                    break;

                case "6":
                    load();
                    break;

                case "7":
                    sort();
                    break;

                case "0":
                    isOn = false;
                    System.out.println("\nBye!");
                    break;

                default:
                    System.out.println("\nInvalid action!\n");
            }
        }
    }

    private void printMenu() {
        System.out.println("Choose your action:\n" +
                "1) Add income\n" +
                "2) Add purchase\n" +
                "3) Show list of purchases\n" +
                "4) Balance\n" +
                "5) Save\n" +
                "6) Load\n" +
                "7) Analyze (Sort)\n" +
                "0) Exit");
    }

    private void addIncome() {
        System.out.println("\nEnter income:");

        try {
            double toAdd = Double.parseDouble(scanner.nextLine());
            income += toAdd;
            System.out.println("Income was added!\n");
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid income entered!\n");
        }

    }

    private void addPurchase() {
        boolean isAdding = true;
        Category category;
        String purchaseMenu = "\nChoose the type of purchase \n" +
                "1) Food\n" +
                "2) Clothes\n" +
                "3) Entertainment\n" +
                "4) Other\n" +
                "5) Back";

        while (isAdding) {
            System.out.println(purchaseMenu);
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    category = Category.FOOD;
                    break;

                case "2":
                    category = Category.CLOTHES;
                    break;

                case "3":
                    category = Category.ENTERTAINMENT;
                    break;

                case "4":
                    category = Category.OTHER;
                    break;

                case "5":
                    isAdding = false;
                    System.out.println();
                    continue;

                default:
                    System.out.println("\nInvalid category!");
                    continue;
            }

            System.out.println("\nEnter purchase name:");
            String name = scanner.nextLine().trim();
            System.out.println("Enter its price:");

            try {
                double price = Double.parseDouble(scanner.nextLine().trim());
                purchases.add(new Purchase(name, price, category));

                //adjust income
                income -= price;

                System.out.println("Purchase was added!");
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid price given! Purchase not added!");
            }
        }
    }

    private void listPurchases() {
        if (purchases.isEmpty()) {
            System.out.println("\nThe purchase list is empty\n");
            return;
        }

        boolean isListing = true;
        Category category;
        String listPurchaseMenu = "\nChoose the type of purchases\n" +
                "1) Food\n" +
                "2) Clothes\n" +
                "3) Entertainment\n" +
                "4) Other\n" +
                "5) All\n" +
                "6) Back";

        while (isListing) {
            System.out.println(listPurchaseMenu);
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    category = Category.FOOD;
                    break;

                case "2":
                    category = Category.CLOTHES;
                    break;

                case "3":
                    category = Category.ENTERTAINMENT;
                    break;

                case "4":
                    category = Category.OTHER;
                    break;

                case "5":
                    category = Category.ALL;
                    break;

                case "6":
                    System.out.println();
                    isListing = false;
                    continue;

                default:
                    System.out.println("\nInvalid category!");
                    continue;
            }

            listPurchasesByCategory(category);
        }
    }

    private void listPurchasesByCategory(Category category) {
        List<Purchase> categoryList = new ArrayList<>();
        AtomicReference<Double> total = new AtomicReference<>((double) 0);

        purchases.forEach(purchase -> {
            if (Category.ALL.equals(category)) {
                categoryList.add(purchase);
                total.updateAndGet(v -> v + purchase.getPrice());
            } else if (purchase.getCategory().equals(category)) {
                categoryList.add(purchase);
                total.updateAndGet(v -> v + purchase.getPrice());
            }
        });

        System.out.println("\n" + category + ":");

        if (categoryList.isEmpty()) {
            System.out.println("The purchase list is empty!");
        } else {
            categoryList.forEach(System.out::println);
            System.out.printf("Total sum: $%.2f\n", total.get());
        }
    }

    private void printBalance() {
        System.out.printf("\nBalance: $%.2f\n\nc", income);
    }

    private void save() {
        File file = new File("purchases.txt");
        try (PrintWriter printWriter = new PrintWriter(file)) {
            //first line will be total income
            printWriter.println(income);

            for (Purchase purchase : purchases) {
                printWriter.println(purchase.getName() + ":" + purchase.getPrice() + ":" + purchase.getCategory());
            }

            System.out.println("\nPurchases were saved!\n");
        } catch (IOException e) {
            System.out.printf("An exception occurs when saving file: %s", e.getMessage());
        }
    }

    private void load() {
        File file = new File("purchases.txt");
        boolean isFirstLine = true;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {

                if (isFirstLine) {
                    income = Double.parseDouble(scanner.nextLine());
                    isFirstLine = false;
                    continue;
                }

                String line = scanner.nextLine();

                if (line.isEmpty()) continue;

                String[] parts = line.split(":".trim());

                try {
                    String name = parts[0];
                    String priceStr = parts[1];
                    double price;

                    if (priceStr.startsWith("$")) {
                        priceStr = priceStr.substring(1);
                    }

                    price = Double.parseDouble(priceStr);

                    Purchase purchase = new Purchase(name, price);

                    if (parts.length == 3) {
                        purchase.setCategory(Category.setByString(parts[2]));
                    }

                    purchases.add(purchase);
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing line: " + line);
                    System.out.println("Line will be skipped");
                }
            }

            System.out.println("\nPurchases were loaded!\n");

        } catch (FileNotFoundException e) {
            System.out.println("No file found: purchases.txt");
        }

    }

    private void sort() {
        boolean isSorting = true;
        String menu = "\nHow do you want to sort?\n" +
                "1) Sort all purchases\n" +
                "2) Sort by type\n" +
                "3) Sort certain type\n" +
                "4) Back";


        while (isSorting) {
            System.out.println(menu);
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    sorter.sortAll(purchases);
                    break;

                case "2":
                    sorter.sortByType(purchases);
                    break;

                case "3":
                    sortByCertainType();
                    break;

                case "4":
                    System.out.println();
                    isSorting = false;
                    continue;

                default:
                    System.out.println("\nInvalid action!");
            }
        }
    }

    private void sortByCertainType() {
        String menu = "\nChoose the type of purchase\n" +
                "1) Food\n" +
                "2) Clothes\n" +
                "3) Entertainment\n" +
                "4) Other";

        System.out.println(menu);
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                sorter.sortByCertainType(purchases, Category.FOOD);
                break;

            case "2":
                sorter.sortByCertainType(purchases, Category.CLOTHES);
                break;

            case "3":
                sorter.sortByCertainType(purchases, Category.ENTERTAINMENT);
                break;

            case "4":
                sorter.sortByCertainType(purchases, Category.OTHER);
                break;

            default:
                System.out.println("\nInvalid action!");
        }

    }

}
