import java.math.BigDecimal;

public class Flight {
    private int id;
    private String flightNo;
    private String origin;
    private String destination;
    private int seatsAvailable;
    private double price;

    public Flight() {}

    public Flight(String flightNo, String origin, String destination, int seatsAvailable, double price) {
        this.flightNo = flightNo;
        this.origin = origin;
        this.destination = destination;
        this.seatsAvailable = seatsAvailable;
        this.price = price;
    }

    public Flight(int i, String flightNo, String origin, String destination, int seats, BigDecimal price) {
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFlightNo() { return flightNo; }
    public void setFlightNo(String flightNo) { this.flightNo = flightNo; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public int getSeatsAvailable() { return seatsAvailable; }
    public void setSeatsAvailable(int seatsAvailable) { this.seatsAvailable = seatsAvailable; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", flightNo='" + flightNo + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", seatsAvailable=" + seatsAvailable +
                ", price=" + price +
                '}';
    }
}
