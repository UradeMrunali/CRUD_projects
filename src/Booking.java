
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Booking {
    private int id;
    private int customerId;
    private LocalDateTime bookingDate;
    private double totalAmount;
    private List<BookingItem> items = new ArrayList<>();

    public Booking() {}

    public Booking(int customerId) {
        this.customerId = customerId;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public List<BookingItem> getItems() { return items; }
    public void setItems(List<BookingItem> items) { this.items = items; }
    public void addItem(BookingItem item) { this.items.add(item); }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", bookingDate=" + bookingDate +
                ", totalAmount=" + totalAmount +
                ", items=" + items +
                '}';
    }
}

