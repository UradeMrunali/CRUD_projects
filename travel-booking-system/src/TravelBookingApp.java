
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class TravelBookingApp {
    public static void main(String[] args) {
        // Initialize tables first
        TableInitializer.init();

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Travel Booking System =====");
            System.out.println("1. Add Customer");
            System.out.println("2. Add Flight");
            System.out.println("3. Add Hotel");
            System.out.println("4. Search Flights by Origin/Destination");
            System.out.println("5. Search Hotels by Location");
            System.out.println("6. Create Booking");
            System.out.println("7. Make Payment");
            System.out.println("8. Cancel Booking");
            System.out.println("9. Reports");
            System.out.println("10. Updates");
            System.out.println("11. Delete");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter Phone: ");
                    String phone = sc.nextLine();
                    Customers cust = new Customers(0, name, email, phone);
                    CustomersDAO.registerCustomer(cust);
                    System.out.println("Customer added.");
                }

                case 2 -> {
                    System.out.print("Enter Flight No: ");
                    String flightNo = sc.nextLine();
                    System.out.print("Enter Origin: ");
                    String origin = sc.nextLine();
                    System.out.print("Enter Destination: ");
                    String destination = sc.nextLine();
                    System.out.print("Enter Seats Available: ");
                    int seats = sc.nextInt();
                    System.out.print("Enter Price: ");
                    BigDecimal price = sc.nextBigDecimal();
                    Flight flight = new Flight(0, flightNo, origin, destination, seats, price);
                    FlightDAO.addFlight(flight);
                    System.out.println("Flight added.");
                }

                case 3 -> {
                    System.out.print("Enter Hotel Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Location: ");
                    String loc = sc.nextLine();
                    System.out.print("Enter Rooms Available: ");
                    int rooms = sc.nextInt();
                    System.out.print("Enter Price per Night: ");
                    BigDecimal price = sc.nextBigDecimal();
                    Hotel hotel = new Hotel(0, name, loc, rooms, price);
                    HotelDAO.addHotel(hotel);
                    System.out.println("Hotel added.");
                }

                case 4 -> {
                    System.out.print("Enter Origin: ");
                    String origin = sc.nextLine();
                    System.out.print("Enter Destination: ");
                    String dest = sc.nextLine();
                    List<Flight> flights = FlightDAO.searchFlights(origin, dest);
                    flights.forEach(System.out::println);
                }

                case 5 -> {
                    System.out.print("Enter Location: ");
                    String loc = sc.nextLine();
                    List<Hotel> hotels = HotelDAO.searchHotels(loc);
                    hotels.forEach(System.out::println);
                }

                case 6 -> {
                    System.out.print("Enter Customer ID: ");
                    int custId = sc.nextInt();
                    sc.nextLine();
                    Booking booking = new Booking();
                    booking.setCustomerId(custId);
                    System.out.print("Enter number of items: ");
                    int items = sc.nextInt();
                    for (int i = 0; i < items; i++) {
                        System.out.print("Enter Item Type (FLIGHT/HOTEL): ");
                        String type = sc.next();
                        System.out.print("Enter Item ID: ");
                        int itemId = sc.nextInt();
                        System.out.print("Enter Quantity: ");
                        int qty = sc.nextInt();
                        System.out.print("Enter Price: ");
                        double price = sc.nextDouble();
                        BookingItem item = new BookingItem();
                        item.setItemType(type);
                        item.setItemId(itemId);
                        item.setQuantity(qty);
                        item.setPrice(price);
                        booking.addItem(item);
                    }
                    BookingDAO.createBooking(booking);
                    System.out.println("Booking created.");
                }

                case 7 -> {
                    System.out.print("Enter Booking ID: ");
                    int bookingId = sc.nextInt();
                    System.out.print("Enter Amount: ");
                    double amt = sc.nextDouble();
                    sc.nextLine();
                    System.out.print("Enter Payment Status (PENDING/SUCCESS/FAILED): ");
                    String status = sc.nextLine();
                    Payment payment = new Payment();
                    payment.setBookingId(bookingId);
                    payment.setAmount(amt);
                    payment.setStatus(status);
                    PaymentDAO.makePayment(payment);
                    System.out.println("Payment recorded.");
                }

                case 8 -> {
                    System.out.print("Enter Booking ID to Cancel: ");
                    int id = sc.nextInt();
                    BookingDAO.cancelBooking(id);
                    System.out.println("Booking canceled.");
                }

                case 9 -> {
                    System.out.println("\n--- Reports ---");
                    System.out.println("1. Get all bookings of a customer");
                    System.out.println("2. Top 5 customers by spending");
                    System.out.println("3. Most popular flight route");
                    System.out.println("4. Hotel with maximum occupancy");
                    System.out.println("5. Monthly revenue report");
                    System.out.print("Choose: ");
                    int r = sc.nextInt();
                    switch (r) {
                        case 1 -> {
                            System.out.print("Enter Customer ID: ");
                            int custId = sc.nextInt();
                            BookingDAO.getBookingsByCustomer(custId);
                        }
                        case 2 -> BookingDAO.getTopCustomers();
                        case 3 -> BookingDAO.getPopularFlightRoute();
                        case 4 -> HotelDAO.getMaxOccupancyHotel();
                        case 5 -> BookingDAO.getMonthlyRevenue();
                        default -> System.out.println("Invalid report option.");
                    }
                }

                case 10 -> {
                    System.out.println("\n--- Update Menu ---");
                    System.out.println("1. Update Customer Info");
                    System.out.println("2. Update Flight Seats/Price");
                    System.out.println("3. Update Hotel Rooms/Price");
                    System.out.print("Choose: ");
                    int u = sc.nextInt();
                    sc.nextLine();
                    switch (u) {
                        case 1 -> {
                            System.out.print("Enter Customer ID: ");
                            int id = sc.nextInt();
                            sc.nextLine();
                            System.out.print("Enter New Name: ");
                            String newName = sc.nextLine();
                            System.out.print("Enter New Email: ");
                            String newEmail = sc.nextLine();
                            System.out.print("Enter New Phone: ");
                            String newPhone = sc.nextLine();
                            CustomersDAO.updateCustomer(new Customers(id, newName, newEmail, newPhone));
                            System.out.println("Customer updated.");
                        }
                        case 2 -> {
                            System.out.print("Enter Flight ID: ");
                            int id = sc.nextInt();
                            System.out.print("Enter New Seats: ");
                            int newSeats = sc.nextInt();
                            System.out.print("Enter New Price: ");
                            double newPrice = sc.nextDouble();
                            FlightDAO.updateFlight(id, newSeats, newPrice);
                            System.out.println("Flight updated.");
                        }
                        case 3 -> {
                            System.out.print("Enter Hotel ID: ");
                            int id = sc.nextInt();
                            System.out.print("Enter New Rooms: ");
                            int newRooms = sc.nextInt();
                            System.out.print("Enter New Price per Night: ");
                            double newPrice = sc.nextDouble();
                            HotelDAO.updateHotel(id, newRooms, newPrice);
                            System.out.println("Hotel updated.");
                        }
                        default -> System.out.println("Invalid update option.");
                    }
                }

                case 11 -> {
                    System.out.println("\n--- Delete Menu ---");
                    System.out.println("1. Delete Customer");
                    System.out.println("2. Delete Flight");
                    System.out.println("3. Delete Hotel");
                    System.out.print("Choose: ");
                    int d = sc.nextInt();
                    sc.nextLine();
                    switch (d) {
                        case 1 -> {
                            System.out.print("Enter Customer ID: ");
                            int id = sc.nextInt();
                            CustomersDAO.deleteCustomer(id);
                            System.out.println("Customer deleted (cascade deletes bookings/payments).");
                        }
                        case 2 -> {
                            System.out.print("Enter Flight ID: ");
                            int id = sc.nextInt();
                            FlightDAO.deleteFlight(id);
                            System.out.println("Flight deleted.");
                        }
                        case 3 -> {
                            System.out.print("Enter Hotel ID: ");
                            int id = sc.nextInt();
                            HotelDAO.deleteHotel(id);
                            System.out.println("Hotel deleted.");
                        }
                        default -> System.out.println("Invalid delete option.");
                    }
                }

                case 0 -> {
                    System.out.println("Exiting...");
                    return;
                }

                default -> System.out.println("Invalid option.");
            }
        }
    }
}
