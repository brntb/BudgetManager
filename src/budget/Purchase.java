package budget;

public class Purchase {

    private final String name;
    private final double price;
    private Category category;

    public Purchase(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Purchase(String name, double price, Category category) {
        this(name, price);
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return String.format("%s $%.2f", name, price);
    }
}
