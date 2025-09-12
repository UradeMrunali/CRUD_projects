import java.time.LocalDateTime;

public class Orders {
    private int id;
    private int customerId;
    private LocalDateTime orderDate;

    // Constructors
    public Orders() {
    }

    public Orders(int id, int customerId, LocalDateTime orderDate) {
        this.id = id;
        this.customerId = customerId;
        this.orderDate = orderDate;
    }

    public Orders(int customerId, LocalDateTime orderDate) {
        this.customerId = customerId;
        this.orderDate = orderDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    // toString method
    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", orderDate=" + orderDate +
                '}';
    }
}


