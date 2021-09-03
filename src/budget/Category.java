package budget;

public enum Category {

    FOOD("Food"),
    ENTERTAINMENT("Entertainment"),
    CLOTHES("Clothes"),
    OTHER("Other"),
    ALL("All");

    String name;

    Category(String name) {
        this.name = name;
    }

    public static Category setByString(String toSet) {
        toSet = toSet.toLowerCase();

        switch (toSet) {
            case "food":
                return Category.FOOD;
            case "entertainment":
                return Category.ENTERTAINMENT;
            case "clothes":
                return Category.CLOTHES;
            default:
                return Category.OTHER;
        }

    }
}
