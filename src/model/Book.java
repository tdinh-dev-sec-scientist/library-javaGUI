package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents an abstract Book.
 * This class is the base for all types of books in the library.
 * It implements Serializable to allow book objects to be saved to a file.
 */
public abstract class Book implements Serializable {
    // A version number for serialization. Change this if the class structure changes.
    private static final long serialVersionUID = 1L;

    private String isbn;
    private String title;
    private String author;
    private int publicationYear;
    private boolean onLoan;
    private String borrower;
    private LocalDate loanDate;
    private LocalDate dueDate;

    /**
     * Constructor for the Book class.
     * @param isbn The International Standard Book Number.
     * @param title The title of the book.
     * @param author The author of the book.
     * @param publicationYear The year the book was published.
     */
    public Book(String isbn, String title, String author, int publicationYear) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.onLoan = false; // A new book is not on loan by default.
    }

    // --- Getters and Setters ---

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public boolean isOnLoan() {
        return onLoan;
    }

    public void setOnLoan(boolean onLoan) {
        this.onLoan = onLoan;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Abstract method to get the specific details of the book type.
     * Must be implemented by subclasses.
     * @return A string representing the specific detail (e.g., Genre, Subject).
     */
    public abstract String getDetails();

    /**
     * Abstract method to get the type of the book.
     * @return A string representing the book type (e.g., "Fiction").
     */
    public abstract String getType();
}
