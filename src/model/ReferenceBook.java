package model;

/**
 * Represents a Reference Book, a subclass of Book.
 * It includes a specific category. Reference books cannot be loaned.
 */
public class ReferenceBook extends Book {
    private static final long serialVersionUID = 1L;
    private String category;

    /**
     * Constructor for ReferenceBook.
     * @param isbn The ISBN of the book.
     * @param title The title of the book.
     * @param author The author of the book.
     * @param publicationYear The publication year.
     * @param category The category of the reference book.
     */
    public ReferenceBook(String isbn, String title, String author, int publicationYear, String category) {
        super(isbn, title, author, publicationYear);
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getDetails() {
        return "Category: " + category;
    }

    @Override
    public String getType() {
        return "Reference";
    }

    /**
     * Overrides the setOnLoan method because reference books cannot be loaned.
     * It will always be set to false.
     */
    @Override
    public void setOnLoan(boolean onLoan) {
        super.setOnLoan(false); // Reference books can never be on loan.
    }
}
