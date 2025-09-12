import java.math.BigDecimal;
import java.sql.*;

public class ReportsDAO {
    // 1. Top 5 selling products
    public void getTopSellingProducts() {
        String sql = """
                SELECT p.name, SUM(oi.quantity) as total_sold
                FROM order_items oi
                JOIN products p ON oi.product_id = p.id
                GROUP BY p.id, p.name
                ORDER BY total_sold DESC
                LIMIT 5;
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("Top 5 Selling Products:");
            while (rs.next()) {
                System.out.println(rs.getString("name") + " | Sold: " + rs.getInt("total_sold"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 2. Customer with highest spending
    public void getTopCustomer() {
        String sql = """
                SELECT c.id, c.name, SUM(p.price * oi.quantity) as total_spent
                FROM customers c
                JOIN orders o ON c.id = o.customer_id
                JOIN order_items oi ON o.id = oi.order_id
                JOIN products p ON oi.product_id = p.id
                GROUP BY c.id, c.name
                ORDER BY total_spent DESC
                LIMIT 1;
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                System.out.println("Top Customer: " + rs.getString("name") +
                        " | Total Spent: " + rs.getBigDecimal("total_spent"));
            } else {
                System.out.println("No orders found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 3. Monthly revenue (sum of all orders by month)
    public void getMonthlyRevenue() {
        String sql = """
                SELECT DATE_TRUNC('month', o.order_date) as month,
                       SUM(p.price * oi.quantity) as revenue
                FROM orders o
                JOIN order_items oi ON o.id = oi.order_id
                JOIN products p ON oi.product_id = p.id
                GROUP BY month
                ORDER BY month;
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("Monthly Revenue Report:");
            while (rs.next()) {
                Date month = rs.getDate("month");
                BigDecimal revenue = rs.getBigDecimal("revenue");
                System.out.println(month + " | Revenue: " + revenue);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
