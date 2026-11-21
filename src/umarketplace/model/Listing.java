package umarketplace.model;

public class Listing {
    private String id;
    private String category;
    private String title;
    private String status; // AVAILABLE, RESERVED, SOLD
    private double price;
    private String imagePath;
    private String sellerContact;
    private String condition;
    private String description;

    public Listing(String id, String category, String title, String status,
                   double price, String imagePath, String sellerContact,
                   String condition, String description) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.status = status;
        this.price = price;
        this.imagePath = imagePath;
        this.sellerContact = sellerContact;
        this.condition = condition;
        this.description = description;
    }

    public String getId() { return id; }
    public String getCategory() { return category; }
    public String getTitle() { return title; }
    public String getStatus() { return status; }
    public double getPrice() { return price; }
    public String getImagePath() { return imagePath; }
    public String getSellerContact() { return sellerContact; }
    public String getCondition() { return condition; }
    public String getDescription() { return description; }

    public String toFileLine() {
        return escape(id) + "|"
             + escape(category) + "|"
             + escape(title) + "|"
             + escape(status) + "|"
             + price + "|"
             + escape(imagePath) + "|"
             + escape(sellerContact) + "|"
             + escape(condition) + "|"
             + escape(description);
    }

    public static Listing fromFileLine(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 9) return null;
        try {
            double parsedPrice = Double.parseDouble(parts[4].trim());
            return new Listing(
                unescape(parts[0]),
                unescape(parts[1]),
                unescape(parts[2]),
                unescape(parts[3]),
                parsedPrice,
                unescape(parts[5]),
                unescape(parts[6]),
                unescape(parts[7]),
                unescape(parts[8])
            );
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("|", "\\|").replace("\n", " ");
    }

    private static String unescape(String value) {
        return value.replace("\\|", "|").replace("\\\\", "\\");
    }
}