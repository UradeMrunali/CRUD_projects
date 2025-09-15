
public class Book {
    private int id;
    private String title;
    private String author;
    private boolean available;

    public Book() {}
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.available = true;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return id + " | " + title + " by " + author + " | Available: " + available;
    }
}

