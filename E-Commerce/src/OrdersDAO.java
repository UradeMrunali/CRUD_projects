import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrdersDAO {
    // Place new order (customer buys multiple products)
    public void placeOrder(int customerId, List<OrderItems> items) {
        String insertOrderSQL = "INSERT INTO orders (customer_id) VALUES (?) RETURNING id";
        String insertItemSQL = "INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?)";
        String updateStockSQL = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            // 1. Insert into orders
            int orderId;
            try (PreparedStatement pstmt = conn.prepareStatement(insertOrderSQL)) {
                pstmt.setInt(1, customerId);
                ResultSet rs = pstmt.executeQuery();
                rs.next();
                orderId = rs.getInt(1);
            }

            // 2. Insert order items + decrease stock
            for (OrderItems item : items) {
                // check and update stock
                try (PreparedStatement stockStmt = conn.prepareStatement(updateStockSQL)) {
                    stockStmt.setInt(1, item.getQuantity());
                    stockStmt.setInt(2, item.getProductId());
                    stockStmt.setInt(3, item.getQuantity());

                    int rows = stockStmt.executeUpdate();
                    if (rows == 0) {
                        conn.rollback();
                        System.out.println("Not enough stock for product ID: " + item.getProductId());
                        return;
                    }
                }

                // insert into order_items
                try (PreparedStatement itemStmt = conn.prepareStatement(insertItemSQL)) {
                    itemStmt.setInt(1, orderId);
                    itemStmt.setInt(2, item.getProductId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.executeUpdate();
                }
            }

            conn.commit(); // Commit transaction
            System.out.println("Order placed successfully with ID: " + orderId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cancel order (restore stock & delete order)
    public void cancelOrder(int orderId) {
        String getItemsSQL = "SELECT product_id, quantity FROM order_items WHERE order_id = ?";
        String restoreStockSQL = "UPDATE products SET stock = stock + ? WHERE id = ?";
        String deleteItemsSQL = "DELETE FROM order_items WHERE order_id = ?";
        String deleteOrderSQL = "DELETE FROM orders WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // 1. Get items for this order
            List<OrderItems> items = new ArrayList<>();
            try (PreparedStatement pstmt = conn.prepareStatement(getItemsSQL)) {
                pstmt.setInt(1, orderId);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    OrderItems item = new OrderItems();
                    item.setProductId(rs.getInt("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    items.add(item);
                }
            }

            // 2. Restore stock
            for (OrderItems item : items) {
                try (PreparedStatement pstmt = conn.prepareStatement(restoreStockSQL)) {
                    pstmt.setInt(1, item.getQuantity());
                    pstmt.setInt(2, item.getProductId());
                    pstmt.executeUpdate();
                }
            }

            // 3. Delete order items
            try (PreparedStatement pstmt = conn.prepareStatement(deleteItemsSQL)) {
                pstmt.setInt(1, orderId);
                pstmt.executeUpdate();
            }

            // 4. Delete order
            try (PreparedStatement pstmt = conn.prepareStatement(deleteOrderSQL)) {
                pstmt.setInt(1, orderId);
                pstmt.executeUpdate();
            }

            conn.commit();
            System.out.println("Order " + orderId + " canceled successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get order details with product list + total price
    public void getOrderDetails(int orderId) {
        String sql = """
                SELECT o.id as order_id, o.order_date, c.name as customer_name,
                       p.name as product_name, p.price, oi.quantity,
                       (p.price * oi.quantity) as total_item_price
                FROM orders o
                JOIN customers c ON o.customer_id = c.id
                JOIN order_items oi ON o.id = oi.order_id
                JOIN products p ON oi.product_id = p.id
                WHERE o.id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            BigDecimal totalPrice = BigDecimal.ZERO;
            System.out.println("Order Details:");
            while (rs.next()) {
                System.out.println("Order ID: " + rs.getInt("order_id"));
                System.out.println("Customer: " + rs.getString("customer_name"));
                System.out.println("Order Date: " + rs.getTimestamp("order_date"));

                String product = rs.getString("product_name");
                BigDecimal price = rs.getBigDecimal("price");
                int qty = rs.getInt("quantity");
                BigDecimal itemTotal = rs.getBigDecimal("total_item_price");

                System.out.println(" - " + product + " | Price: " + price + " | Qty: " + qty + " | Subtotal: " + itemTotal);

                totalPrice = totalPrice.add(itemTotal);
            }

            System.out.println("Total Order Price: " + totalPrice);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
