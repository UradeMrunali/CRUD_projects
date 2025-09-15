
import java.sql.Date;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // Ensure tables exist
        TableCreator.createTables();

        Scanner sc = new Scanner(System.in);
        BookDAO bookDAO = new BookDAO();
        MemberDAO memberDAO = new MemberDAO();
        LoanDAO loanDAO = new LoanDAO();

        while (true) {
            System.out.println("\n=== 📚 Library Menu ===");
            System.out.println("1. Add Book");
            System.out.println("2. Update Book");
            System.out.println("3. Search Book");
            System.out.println("4. Delete Book");
            System.out.println("5. Show All Books");
            System.out.println("6. Add Member");
            System.out.println("7. Show All Members");
            System.out.println("8. Delete Member");
            System.out.println("9. Borrow Book");
            System.out.println("10. Return Book");
            System.out.println("11. Extend Loan");
            System.out.println("12. Borrowed Books of Member");
            System.out.println("13. Overdue Books");
            System.out.println("14. Reports");
            System.out.println("0. Exit");
            System.out.print("👉 Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter Author: ");
                    String author = sc.nextLine();
                    bookDAO.addBook(new Book(title, author));
                    System.out.println("✅ Book added.");
                }
                case 2 -> {
                    System.out.print("Enter Book ID to Update: ");
                    int id = sc.nextInt(); sc.nextLine();
                    System.out.print("New Title: ");
                    String t = sc.nextLine();
                    System.out.print("New Author: ");
                    String a = sc.nextLine();
                    bookDAO.updateBook(id, t, a);
                    System.out.println("✅ Book updated.");
                }
                case 3 -> {
                    System.out.print("Enter keyword (Title/Author): ");
                    String kw = sc.nextLine();
                    List<Book> results = bookDAO.searchBook(kw);
                    if (results.isEmpty()) System.out.println("⚠️ No books found.");
                    else results.forEach(System.out::println);
                }
                case 4 -> {
                    System.out.print("Enter Book ID to Delete: ");
                    int id = sc.nextInt();
                    boolean deleted = bookDAO.deleteBook(id);
                    System.out.println(deleted ? "✅ Book deleted." : "❌ Cannot delete, book is borrowed.");
                }
                case 5 -> {
                    List<Book> books = bookDAO.getAllBooks();
                    if (books.isEmpty()) System.out.println("⚠️ No books available.");
                    else books.forEach(System.out::println);
                }
                case 6 -> {
                    System.out.print("Enter Member Name: ");
                    String n = sc.nextLine();
                    System.out.print("Enter Member Email: ");
                    String e = sc.nextLine();
                    memberDAO.addMember(new Member(n, e));
                    System.out.println("✅ Member added.");
                }
                case 7 -> {
                    List<Member> members = memberDAO.getAllMembers();
                    if (members.isEmpty()) System.out.println("⚠️ No members yet.");
                    else members.forEach(System.out::println);
                }
                case 8 -> {
                    System.out.print("Enter Member ID to Delete: ");
                    int id = sc.nextInt();
                    boolean deleted = memberDAO.deleteMember(id);
                    System.out.println(deleted ? "✅ Member deleted." : "❌ Cannot delete, return borrowed books first.");
                }
                case 9 -> {
                    System.out.print("Enter Book ID: ");
                    int b = sc.nextInt();
                    System.out.print("Enter Member ID: ");
                    int m = sc.nextInt();
                    boolean ok = loanDAO.borrowBook(b, m);
                    System.out.println(ok ? "✅ Book borrowed." : "❌ Book not available.");
                }
                case 10 -> {
                    System.out.print("Enter Loan ID to Return: ");
                    int loanId = sc.nextInt();
                    boolean ok = loanDAO.returnBook(loanId);
                    System.out.println(ok ? "✅ Book returned." : "❌ Failed to return book.");
                }
                case 11 -> {
                    System.out.print("Enter Loan ID to Extend: ");
                    int loanId = sc.nextInt();
                    System.out.print("Enter new return date (YYYY-MM-DD): ");
                    String dateStr = sc.next();
                    Date newDate = Date.valueOf(dateStr);
                    loanDAO.extendLoan(loanId, newDate);
                    System.out.println("✅ Loan extended.");
                }
                case 12 -> {
                    System.out.print("Enter Member ID: ");
                    int mid = sc.nextInt();
                    List<Loan> borrowed = loanDAO.borrowedBooks(mid);
                    if (borrowed.isEmpty()) System.out.println("⚠️ No borrowed books.");
                    else borrowed.forEach(System.out::println);
                }
                case 13 -> {
                    List<Loan> overdue = loanDAO.overdueBooks();
                    if (overdue.isEmpty()) System.out.println("✅ No overdue books.");
                    else overdue.forEach(System.out::println);
                }
                case 14 -> {
                    System.out.println("\n--- Reports ---");
                    String book = loanDAO.mostBorrowedBook();
                    String member = loanDAO.mostActiveMember();
                    System.out.println("Most Borrowed Book: " + (book == null ? "None" : book));
                    System.out.println("Most Active Member: " + (member == null ? "None" : member));
                    System.out.print("Enter Loan ID to check fine: ");
                    int loanId = sc.nextInt();
                    int fine = loanDAO.calculateFine(loanId);
                    System.out.println("Fine: ₹" + fine);
                }
                case 0 -> {
                    System.out.println("👋 Exiting... Goodbye!");
                    sc.close();
                    return;
                }
                default -> System.out.println("⚠️ Invalid choice.");
            }
        }
    }
}
