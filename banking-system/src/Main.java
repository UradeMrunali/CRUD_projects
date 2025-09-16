import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            // Step 1: Initialize tables
            TableInitializer.createTables(conn);

            // Step 2: DAO objects
            CustomerDAO customersDAO = new CustomerDAO();
            AccountDAO accountsDAO = new AccountDAO();
            TransactionDAO transactionsDAO = new TransactionDAO();
            ReportDAO reportsDAO = new ReportDAO();

            Scanner sc = new Scanner(System.in);
            int choice;

            do {
                System.out.println("\n===== Banking System Menu =====");
                System.out.println("1. Add Customer");
                System.out.println("2. Open Account");
                System.out.println("3. Deposit");
                System.out.println("4. Withdraw");
                System.out.println("5. Transfer");
                System.out.println("6. Delete Account");
                System.out.println("7. Last N Transactions");
                System.out.println("8. Transactions by Date Range");
                System.out.println("9. Reports - Top 5 Richest Customers");
                System.out.println("10. Reports - Accounts with Negative Balance");
                System.out.println("11. Reports - Bank Total Deposits");
                System.out.println("0. Exit");
                System.out.print("Enter choice: ");
                choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter customer name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter customer email: ");
                        String email = sc.nextLine();
                        customersDAO.addCustomer(new Customer(0, name, email));
                        System.out.println("Customer added successfully!");
                        break;

                    case 2:
                        System.out.print("Enter customer ID: ");
                        int custId = sc.nextInt();
                        System.out.print("Enter initial balance: ");
                        double initBalance = sc.nextDouble();
                        accountsDAO.openAccount(new Account(0, custId, initBalance));
                        System.out.println("Account opened successfully!");
                        break;

                    case 3:
                        System.out.print("Enter account ID: ");
                        int accId = sc.nextInt();
                        System.out.print("Enter amount to deposit: ");
                        double depAmt = sc.nextDouble();
                        accountsDAO.deposit(accId, depAmt);
                        System.out.println("Deposit successful!");
                        break;

                    case 4:
                        System.out.print("Enter account ID: ");
                        int wAccId = sc.nextInt();
                        System.out.print("Enter amount to withdraw: ");
                        double wAmt = sc.nextDouble();
                        accountsDAO.withdraw(wAccId, wAmt);
                        break;

                    case 5:
                        System.out.print("Enter source account ID: ");
                        int fromAcc = sc.nextInt();
                        System.out.print("Enter destination account ID: ");
                        int toAcc = sc.nextInt();
                        System.out.print("Enter amount to transfer: ");
                        double tAmt = sc.nextDouble();
                        accountsDAO.transfer(fromAcc, toAcc, tAmt);
                        System.out.println("Transfer successful!");
                        break;

                    case 6:
                        System.out.print("Enter account ID to delete: ");
                        int delAccId = sc.nextInt();
                        accountsDAO.deleteAccount(delAccId);
                        break;

                    case 7:
                        System.out.print("Enter account ID: ");
                        int tAccId = sc.nextInt();
                        System.out.print("Enter N (number of transactions): ");
                        int n = sc.nextInt();
                        List<Transaction> lastTxns = transactionsDAO.getLastNTransactions(tAccId, n);
                        lastTxns.forEach(System.out::println);
                        break;

                    case 8:
                        System.out.print("Enter account ID: ");
                        int dAccId = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter start date (YYYY-MM-DD): ");
                        String start = sc.nextLine();
                        System.out.print("Enter end date (YYYY-MM-DD): ");
                        String end = sc.nextLine();
                        List<Transaction> txns = transactionsDAO.getTransactionsByDateRange(dAccId, start, end);
                        txns.forEach(System.out::println);
                        break;

                    case 9:
                        System.out.println("Top 5 richest customers:");
                        reportsDAO.top5RichestCustomers().forEach(System.out::println);
                        break;

                    case 10:
                        System.out.println("Accounts with negative balance:");
                        reportsDAO.accountsWithNegativeBalance().forEach(System.out::println);
                        break;

                    case 11:
                        System.out.println("Total bank deposits: " + reportsDAO.totalBankDeposits());
                        break;

                    case 0:
                        System.out.println("Exiting Banking System. Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } while (choice != 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
