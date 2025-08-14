package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * The main model class for the Library.
 * It manages the collection of books and handles data persistence.
 */
public class Library {

    private List<Book> books;
    private final String dataFilePath;

    /**
     * Constructor for the Library.
     * @param dataFilePath The path to the file where book data is stored.
     */
    public Library(String dataFilePath) {
        this.dataFilePath = dataFilePath;
        this.books = new CopyOnWriteArrayList<>(); // Thread-safe list for concurrent modifications
        loadBooksFromFile();
    }

    /**
     * Adds a new book to the library.
     * @param book The book to add.
     * @throws IllegalArgumentException if a book with the same ISBN already exists.
     */
    public void addBook(Book book) {
        if (findBookByIsbn(book.getIsbn()) != null) {
            throw new IllegalArgumentException("Book with ISBN " + book.getIsbn() + " already exists.");
        }
        books.add(book);
        saveBooksToFile();
    }

    /**
     * Removes a book from the library using its ISBN.
     * @param isbn The ISBN of the book to remove.
     * @throws IllegalArgumentException if no book with the given ISBN is found.
     */
    public void removeBook(String isbn) {
        Book bookToRemove = findBookByIsbn(isbn);
        if (bookToRemove == null) {
            throw new IllegalArgumentException("Book with ISBN " + isbn + " not found.");
        }
        books.remove(bookToRemove);
        saveBooksToFile();
    }

    /**
     * Updates an existing book's information.
     * @param updatedBook The book object with updated details.
     */
    public void updateBook(Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getIsbn().equals(updatedBook.getIsbn())) {
                books.set(i, updatedBook);
                saveBooksToFile();
                return;
            }
        }
    }

    /**
     * Finds a book by its ISBN.
     * @param isbn The ISBN to search for.
     * @return The Book object if found, otherwise null.
     */
    public Book findBookByIsbn(String isbn) {
        return books.stream()
                .filter(book -> book.getIsbn().equals(isbn))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns a list of all books in the library.
     * @return A new ArrayList containing all books.
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }
    
    /**
     * Searches for books based on a keyword.
     * The search is case-insensitive and checks title, author, and ISBN.
     * @param keyword The term to search for.
     * @return A list of books that match the keyword.
     */
    public List<Book> searchBooks(String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(lowerCaseKeyword) ||
                               book.getAuthor().toLowerCase().contains(lowerCaseKeyword) ||
                               book.getIsbn().toLowerCase().contains(lowerCaseKeyword))
                .collect(Collectors.toList());
    }

    /**
     * Saves the current list of books to the data file using serialization.
     */
    @SuppressWarnings("unchecked")
    public void saveBooksToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFilePath))) {
            oos.writeObject(new ArrayList<>(books)); // Write a standard ArrayList
        } catch (IOException e) {
            System.err.println("Error saving book data: " + e.getMessage());
            // In a real app, you might show an error dialog to the user.
        }
    }

    /**
     * Loads the list of books from the data file.
     * If the file doesn't exist, it starts with an empty library.
     */
    private void loadBooksFromFile() {
        File dataFile = new File(dataFilePath);
        if (dataFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
                List<Book> loadedBooks = (List<Book>) ois.readObject();
                this.books = new CopyOnWriteArrayList<>(loadedBooks);
            } catch (FileNotFoundException e) {
                // This case is handled by dataFile.exists(), but it's good practice to have it.
                System.out.println("Data file not found. Starting with an empty library.");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading book data: " + e.getMessage());
                // If the file is corrupt, start with a fresh list.
                this.books = new CopyOnWriteArrayList<>();
            }
        } else {
            System.out.println("No existing data file found. A new one will be created on save.");
        }
    }
}
