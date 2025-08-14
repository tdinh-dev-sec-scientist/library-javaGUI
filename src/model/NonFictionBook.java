package model;

/**
 * Represents a Non-Fiction Book, a subclass of Book.
 * It includes a specific subject.
 */
public class NonFictionBook extends Book {
    private static final long serialVersionUID = 1L;
    private String subject;

    /**
     * Constructor for NonFictionBook.
     * @param isbn The ISBN of the book.
     * @param title The title of the book.
     * @param author The author of the book.
     * @param publicationYear The publication year.
     * @param subject The subject of the non-fiction book.
     */
    public NonFictionBook(String isbn, String title, String author, int publicationYear, String subject) {
        super(isbn, title, author, publicationYear);
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String getDetails() {
        return "Subject: " + subject;
    }

    @Override
    public String getType() {
        return "Non-Fiction";
    }
}
