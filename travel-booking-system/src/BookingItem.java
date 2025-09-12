
public class BookingItem {
    private int id;
    private int bookingId;
    private String itemType; // FLIGHT or HOTEL
    private int itemId;
    private int quantity;
    private double price;

    public BookingItem() {}

    public BookingItem(String itemType, int itemId, int quantity, double price) {
        this.itemType = itemType;
        this.itemId = itemId;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return "BookingItem{" +
                "id=" + id +
                ", bookingId=" + bookingId +
                ", itemType='" + itemType + '\'' +
                ", itemId=" + itemId +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
