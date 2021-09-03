package budget;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PurchaseSorter {

    public void sortAll(List<Purchase> purchases) {
        if (purchases.isEmpty()) {
            System.out.println("\nThe purchase list is empty!");
            return;
        }

        System.out.println("\nAll:");

        purchases.stream()
                .sorted(Comparator.comparingDouble(Purchase::getPrice).reversed())
                .forEach(System.out::println);

        double totalSum = purchases.stream()
                            .mapToDouble(Purchase::getPrice)
                            .sum();

        System.out.printf("Total: $%.2f\n", totalSum);
    }

    public void sortByType(List<Purchase> purchases) {
        double foodSum = getCategorySum(purchases, Category.FOOD);
        double entertainmentSum = getCategorySum(purchases, Category.ENTERTAINMENT);
        double clothesSum = getCategorySum(purchases, Category.CLOTHES);
        double otherSum = getCategorySum(purchases, Category.OTHER);
        double totalSum = foodSum + entertainmentSum + clothesSum + otherSum;

        class CategoryHolder {
            private final Category category;
            private final double price;

            public CategoryHolder(Category category, double price) {
                this.category = category;
                this.price = price;
            }

            public double getPrice() {
                return price;
            }
        }

        List<CategoryHolder> categoryHolders = new ArrayList<>();
        categoryHolders.add(new CategoryHolder(Category.FOOD, foodSum));
        categoryHolders.add(new CategoryHolder(Category.ENTERTAINMENT, entertainmentSum));
        categoryHolders.add(new CategoryHolder(Category.CLOTHES, clothesSum));
        categoryHolders.add(new CategoryHolder(Category.OTHER, otherSum));

        System.out.println("\nTypes:");

        categoryHolders.stream()
                        .sorted(Comparator.comparingDouble(CategoryHolder::getPrice).reversed())
                        .forEach(holder -> System.out.printf("%s - $%.2f\n", holder.category.name, holder.price));

        System.out.printf("Total sum: $%.2f\n", totalSum);
    }

    public void sortByCertainType(List<Purchase> purchases, Category category) {
        if (purchases.isEmpty()) {
            System.out.println("\nThe purchase list is empty!");
            return;
        }

        System.out.println("\n" + category.name + ":");

        purchases.stream()
                .filter(purchase -> purchase.getCategory().equals(category))
                .sorted(Comparator.comparingDouble(Purchase::getPrice).reversed())
                .forEach(System.out::println);
    }

    private double getCategorySum(List<Purchase> purchases, Category category) {
        return  purchases.stream()
                            .filter(purchase -> purchase.getCategory().equals(category))
                            .mapToDouble(Purchase::getPrice)
                            .sum();
    }


}
