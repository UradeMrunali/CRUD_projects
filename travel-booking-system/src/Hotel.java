import java.math.BigDecimal;

public class Hotel {
    private int id;
    private String name;
    private String location;
    private int roomsAvailable;
    private double pricePerNight;

    public Hotel() {}

    public Hotel(String name, String location, int roomsAvailable, double pricePerNight) {
        this.name = name;
        this.location = location;
        this.roomsAvailable = roomsAvailable;
        this.pricePerNight = pricePerNight;
    }

    public Hotel(int i, String name, String loc, int rooms, BigDecimal price) {
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getRoomsAvailable() { return roomsAvailable; }
    public void setRoomsAvailable(int roomsAvailable) { this.roomsAvailable = roomsAvailable; }

    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", roomsAvailable=" + roomsAvailable +
                ", pricePerNight=" + pricePerNight +
                '}';
    }
}
