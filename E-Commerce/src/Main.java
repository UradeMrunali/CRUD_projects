import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    // instantiate DAOs (make sure these classes are in your project)
    private static final CustomersDAO customerDAO = new CustomersDAO();
    private static final ProductsDAO productDAO = new ProductsDAO();
    private static final OrdersDAO ordersDAO = new OrdersDAO();
    //  private static final OrderItemsDAO orderItemDAO = new OrderItemDAO();
    private static final ReportsDAO reportsDAO = new ReportsDAO();

    public static void main(String[] args) {
        // 1. Create tables if not exist
        CreateTables.createTablesIfNotExist();

        // 2. Main menu loop
        while (true) {
            System.out.println("\n=== E-Commerce System ===");
            System.out.println("1. Customers");
            System.out.println("2. Products");
            System.out.println("3. Orders");
            System.out.println("4. Reports");
            System.out.println("0. Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> customersMenu();
                case "2" -> productsMenu();
                case "3" -> ordersMenu();
                case "4" -> reportsMenu();
                case "0" -> {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    // -------------------- Customers --------------------
    private static void customersMenu() {
        while (true) {
            System.out.println("\n-- Customers --");
            System.out.println("1. Register new customer");
            System.out.println("2. Update customer");
            System.out.println("3. Delete customer");
            System.out.println("4. Get customer by ID");
            System.out.println("5. Show all customers");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            String opt = scanner.nextLine().trim();

            switch (opt) {
                case "1" -> registerCustomer();
                case "2" -> updateCustomer();
                case "3" -> deleteCustomer();
                case "4" -> getCustomerById();
                case "5" -> showAllCustomers();
                case "0" -> { return; }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private static void registerCustomer() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        Customers c = new Customers();
        c.setName(name);
        c.setEmail(email);
        c.setPhone(phone);

        customerDAO.addCustomer(c);
    }

    private static void updateCustomer() {
        try {
            System.out.print("Customer ID to update: ");
            int id = Integer.parseInt(scanner.nextLine());

            Customers existing = customerDAO.getCustomerById(id);
            if (existing == null) {
                System.out.println("Customer not found.");
                return;
            }

            System.out.print("New name (enter to keep [" + existing.getName() + "]): ");
            String name = scanner.nextLine();
            if (!name.isBlank()) existing.setName(name);

            System.out.print("New email (enter to keep [" + existing.getEmail() + "]): ");
            String email = scanner.nextLine();
            if (!email.isBlank()) existing.setEmail(email);

            System.out.print("New phone (enter to keep [" + existing.getPhone() + "]): ");
            String phone = scanner.nextLine();
            if (!phone.isBlank()) existing.setPhone(phone);

            customerDAO.updateCustomer(existing);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid input.");
        }
    }

    private static void deleteCustomer() {
        try {
            System.out.print("Customer ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine());
            customerDAO.deleteCustomer(id);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid input.");
        }
    }

    private static void getCustomerById() {
        try {
            System.out.print("Customer ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            Customers c = customerDAO.getCustomerById(id);
            if (c != null) System.out.println(c);
            else System.out.println("Not found.");
        } catch (NumberFormatException ex) {
            System.out.println("Invalid input.");
        }
    }

    private static void showAllCustomers() {
        List<Customers> customers = customerDAO.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            System.out.println("\n--- Customer List ---");
            for (Customers c : customers) {
                System.out.println(c);
            }
        }
    }

    // -------------------- Products --------------------
    private static void productsMenu() {
        while (true) {
            System.out.println("\n-- Products --");
            System.out.println("1. Add new product");
            System.out.println("2. Update price & stock");
            System.out.println("3. Delete product");
            System.out.println("4. Search product by name");
            System.out.println("5. Show all products");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            String opt = scanner.nextLine().trim();

            switch (opt) {
                case "1" -> addProduct();
                case "2" -> updateProduct();
                case "3" -> deleteProduct();
                case "4" -> searchProduct();
                case "5" -> showAllProducts();
                case "0" -> { return; }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private static void addProduct() {
        try {
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Price (e.g. 12.50): ");
            BigDecimal price = new BigDecimal(scanner.nextLine());
            System.out.print("Stock (int): ");
            int stock = Integer.parseInt(scanner.nextLine());

            Products p = new Products();
            p.setName(name);
            p.setPrice(price);
            p.setStock(stock);

            productDAO.addProduct(p);
        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    private static void updateProduct() {
        try {
            System.out.print("Product ID to update: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("New price: ");
            BigDecimal price = new BigDecimal(scanner.nextLine());
            System.out.print("New stock: ");
            int stock = Integer.parseInt(scanner.nextLine());

            productDAO.updateProduct(id, price, stock);
        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    private static void deleteProduct() {
        try {
            System.out.print("Product ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine());
            productDAO.deleteProduct(id);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void searchProduct() {
        System.out.print("Search name: ");
        String name = scanner.nextLine();
        List<Products> results = productDAO.searchProductByName(name);
        if (results.isEmpty()) System.out.println("No products found.");
        else results.forEach(System.out::println);
    }

    private static void showAllProducts() {
        List<Products> products = productDAO.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            System.out.println("\n--- Product List ---");
            for (Products p : products) {
                System.out.println(p);
            }
        }
    }

    // -------------------- Orders --------------------
    private static void ordersMenu() {
        while (true) {
            System.out.println("\n-- Orders --");
            System.out.println("1. Place order");
            System.out.println("2. Cancel order");
            System.out.println("3. Get order details");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            String opt = scanner.nextLine().trim();

            switch (opt) {
                case "1" -> placeOrderInteractive();
                case "2" -> cancelOrderInteractive();
                case "3" -> getOrderDetailsInteractive();
                case "0" -> { return; }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private static void placeOrderInteractive() {
        try {
            System.out.print("Customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine());
            List<OrderItems> items = new ArrayList<>();

            while (true) {
                System.out.print("Product ID (or 'done'): ");
                String pid = scanner.nextLine().trim();
                if (pid.equalsIgnoreCase("done")) break;
                int productId = Integer.parseInt(pid);

                System.out.print("Quantity: ");
                int qty = Integer.parseInt(scanner.nextLine());

                OrderItems oi = new OrderItems();
                oi.setProductId(productId);
                oi.setQuantity(qty);
                items.add(oi);
            }

            if (items.isEmpty()) {
                System.out.println("No items added. Aborting order.");
                return;
            }

            ordersDAO.placeOrder(customerId, items);

        } catch (NumberFormatException ex) {
            System.out.println("Invalid number input.");
        }
    }

    private static void cancelOrderInteractive() {
        try {
            System.out.print("Order ID to cancel: ");
            int orderId = Integer.parseInt(scanner.nextLine());
            ordersDAO.cancelOrder(orderId);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid input.");
        }
    }

    private static void getOrderDetailsInteractive() {
        try {
            System.out.print("Order ID: ");
            int orderId = Integer.parseInt(scanner.nextLine());
            ordersDAO.getOrderDetails(orderId);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid input.");
        }
    }

    // -------------------- Reports --------------------
    private static void reportsMenu() {
        while (true) {
            System.out.println("\n-- Reports --");
            System.out.println("1. Top 5 selling products");
            System.out.println("2. Customer with highest spending");
            System.out.println("3. Monthly revenue");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            String opt = scanner.nextLine().trim();

            switch (opt) {
                case "1" -> reportsDAO.getTopSellingProducts();
                case "2" -> reportsDAO.getTopCustomer();
                case "3" -> reportsDAO.getMonthlyRevenue();
                case "0" -> { return; }
                default -> System.out.println("Invalid option");
            }
        }
    }
}