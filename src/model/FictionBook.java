package model;

/**
 * Represents a Fiction Book, which is a subclass of Book.
 * It includes a specific genre.
 */
public class FictionBook extends Book {
    private static final long serialVersionUID = 1L;
    private String genre;

    /**
     * Constructor for FictionBook.
     * @param isbn The ISBN of the book.
     * @param title The title of the book.
     * @param author The author of the book.
     * @param publicationYear The publication year.
     * @param genre The genre of the fiction book.
     */
    public FictionBook(String isbn, String title, String author, int publicationYear, String genre) {
        super(isbn, title, author, publicationYear);
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String getDetails() {
        return "Genre: " + genre;
    }

    @Override
    public String getType() {
        return "Fiction";
    }
}
