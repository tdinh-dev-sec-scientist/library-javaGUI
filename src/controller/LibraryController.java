package controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import model.*;
import util.ConfigLoader;
import view.*;

/**
 * The final Controller class, updated to handle events from the new UI design
 * and correctly save user settings.
 */
public class LibraryController {
    private final Library library;
    private MainView mainView;
    private final LoginView loginView;
    private final Map<String, String> users;

    public LibraryController(Library library, LoginView loginView) {
        this.library = library;
        this.loginView = loginView;
        this.users = new HashMap<>();
        
        // Load users from the config file
        users.put(ConfigLoader.getProperty("user.admin.username"), ConfigLoader.getProperty("user.admin.password"));
        users.put(ConfigLoader.getProperty("user.member.username"), ConfigLoader.getProperty("user.member.password"));

        this.loginView.getLoginButton().addActionListener(this::handleLogin);
    }

    private void handleLogin(ActionEvent e) {
        String username = loginView.getUsername();
        String password = loginView.getPassword();
        if (users.containsKey(username) && users.get(username).equals(password)) {
            loginView.closeView();
            SwingUtilities.invokeLater(() -> {
                mainView = new MainView(username);
                attachMainViewListeners();
                mainView.showView();
                refreshAllData();
            });
        } else {
            loginView.showErrorMessage("Invalid username or password.");
        }
    }

    private void attachMainViewListeners() {
        // ... (Listeners remain the same)
        mainView.getSettingsButton().addActionListener(this::handleSettings);
        mainView.getAddBookPanel().getAddButton().addActionListener(this::handleAddBook);
        mainView.getInventoryPanel().getEditMenuItem().addActionListener(this::handleEditBookFromInventory);
        mainView.getGalleryPanel().getEditMenuItem().addActionListener(this::handleEditBookFromGallery);
        mainView.getInventoryPanel().getDeleteButton().addActionListener(this::handleDeleteBook);
        mainView.getInventoryPanel().getExportButton().addActionListener(this::handleExport);
        mainView.getLendingPanel().getLendButton().addActionListener(this::handleLendBook);
        mainView.getLendingPanel().getReturnButton().addActionListener(this::handleReturnBook);
        addSearchListener(mainView.getInventoryPanel().getSearchField(), this::filterInventory);
        addSearchListener(mainView.getGalleryPanel().getSearchField(), this::filterGallery);
    }
    
    private void handleSettings(ActionEvent e) {
        String oldUsername = mainView.getCurrentUsername();
        SettingsDialog dialog = new SettingsDialog(mainView, oldUsername);
        dialog.setVisible(true);

        if (dialog.isLogoutTriggered()) {
            handleLogout();
            return;
        }

        if (dialog.isSaved()) {
            String newUsername = dialog.getNewUsername();
            String newAvatarPath = dialog.getNewAvatarPath();
            
            // --- Logic to save username permanently ---
            if (!newUsername.equals(oldUsername)) {
                // Determine which user key to update in the config file
                String userKeyToUpdate = null;
                if (oldUsername.equals(ConfigLoader.getProperty("user.admin.username"))) {
                    userKeyToUpdate = "user.admin.username";
                } else if (oldUsername.equals(ConfigLoader.getProperty("user.member.username"))) {
                    userKeyToUpdate = "user.member.username";
                }

                if (userKeyToUpdate != null) {
                    ConfigLoader.saveProperty(userKeyToUpdate, newUsername);
                    
                    // Also update the in-memory user map for the current session
                    String password = users.remove(oldUsername);
                    users.put(newUsername, password);

                    // Rename avatar file to match the new username
                    File oldAvatar = new File("profile_" + oldUsername + ".png");
                    if (oldAvatar.exists()) {
                        oldAvatar.renameTo(new File("profile_" + newUsername + ".png"));
                    }
                }
            }
            
            // Update avatar file if a new one was chosen
            if (newAvatarPath != null && !newAvatarPath.isEmpty()) {
                ImageUtils.saveProfilePicture(newUsername, newAvatarPath);
            }

            // Update the UI with the new information
            mainView.updateUserInfo(newUsername);
        }
    }
    
    private void handleLogout() {
        if (JOptionPane.showConfirmDialog(mainView, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            mainView.dispose();
            loginView.showView();
        }
    }

    private void refreshAllData() {
        java.util.List<Book> allBooks = library.getAllBooks();
        Map<String, Integer> bookStats = allBooks.stream()
            .collect(Collectors.groupingBy(Book::getType, Collectors.summingInt(b -> 1)));
        mainView.refreshAllViews(allBooks, bookStats);
    }
    
    // ... (All other handler methods remain the same)
    private void handleAddBook(ActionEvent e) {
        try {
            var panel = mainView.getAddBookPanel();
            String isbn = panel.getIsbn();
            if (isbn.isEmpty() || panel.getTitleText().isEmpty()) throw new IllegalArgumentException("ISBN and Title are required.");
            Book newBook = switch (panel.getBookType()) {
                case "Fiction" -> new FictionBook(isbn, panel.getTitleText(), panel.getAuthor(), panel.getYear(), panel.getDetailsText());
                case "Non-Fiction" -> new NonFictionBook(isbn, panel.getTitleText(), panel.getAuthor(), panel.getYear(), panel.getDetailsText());
                default -> new ReferenceBook(isbn, panel.getTitleText(), panel.getAuthor(), panel.getYear(), panel.getDetailsText());
            };
            library.addBook(newBook);
            String imagePath = panel.getSelectedImagePath();
            if (imagePath != null && !imagePath.isEmpty()) ImageUtils.saveBookImage(isbn, imagePath);
            JOptionPane.showMessageDialog(mainView, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            panel.clearFields();
            refreshAllData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainView, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleEditBookFromInventory(ActionEvent e) {
        String isbn = mainView.getInventoryPanel().getSelectedBookIsbn();
        openEditDialog(isbn);
    }

    private void handleEditBookFromGallery(ActionEvent e) {
        JMenuItem menuItem = (JMenuItem) e.getSource();
        JPopupMenu popupMenu = (JPopupMenu) menuItem.getParent();
        Component invoker = popupMenu.getInvoker();
        if (invoker instanceof JPanel) {
            String isbn = invoker.getName();
            openEditDialog(isbn);
        }
    }

    private void openEditDialog(String isbn) {
        if (isbn == null) {
            JOptionPane.showMessageDialog(mainView, "Please select a book to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Book bookToEdit = library.findBookByIsbn(isbn);
        if (bookToEdit != null) {
            EditBookDialog editDialog = new EditBookDialog(mainView, bookToEdit);
            editDialog.setVisible(true);
            if (editDialog.isSucceeded()) {
                library.updateBook(editDialog.getUpdatedBook());
                refreshAllData();
                JOptionPane.showMessageDialog(mainView, "Book updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void handleDeleteBook(ActionEvent e) {
        String isbn = mainView.getInventoryPanel().getSelectedBookIsbn();
        if (isbn == null) {
            JOptionPane.showMessageDialog(mainView, "Please select a book to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int choice = JOptionPane.showConfirmDialog(mainView, "Delete this book?", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            library.removeBook(isbn);
            refreshAllData();
        }
    }

    private void handleLendBook(ActionEvent e) {
        Book bookToLend = mainView.getLendingPanel().getSelectedBookToLend();
        String borrower = mainView.getLendingPanel().getBorrowerName();
        if (bookToLend == null || borrower.isEmpty()) {
            JOptionPane.showMessageDialog(mainView, "Please select a book and enter a borrower's name.", "Lending Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        bookToLend.setBorrower(borrower);
        bookToLend.setLoanDate(LocalDate.now());
        bookToLend.setDueDate(LocalDate.now().plusWeeks(2));
        bookToLend.setOnLoan(true);
        library.updateBook(bookToLend);
        mainView.getLendingPanel().clearLendForm();
        refreshAllData();
    }

    private void handleReturnBook(ActionEvent e) {
        String isbn = mainView.getLendingPanel().getSelectedLoanIsbn();
        if (isbn == null) {
            JOptionPane.showMessageDialog(mainView, "Please select a book to return.", "Return Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Book bookToReturn = library.findBookByIsbn(isbn);
        if (bookToReturn != null) {
            bookToReturn.setOnLoan(false);
            bookToReturn.setBorrower(null);
            bookToReturn.setLoanDate(null);
            bookToReturn.setDueDate(null);
            library.updateBook(bookToReturn);
            refreshAllData();
        }
    }
    
    private void handleExport(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        if (fileChooser.showSaveDialog(mainView) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }
            try (FileWriter writer = new FileWriter(fileToSave)) {
                TableModel model = mainView.getInventoryPanel().getBooksTable().getModel();
                for (int i = 0; i < model.getColumnCount(); i++) {
                    writer.write("\"" + model.getColumnName(i) + "\"" + (i < model.getColumnCount() - 1 ? "," : ""));
                }
                writer.write("\n");
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        writer.write("\"" + model.getValueAt(i, j).toString() + "\"" + (j < model.getColumnCount() - 1 ? "," : ""));
                    }
                    writer.write("\n");
                }
                JOptionPane.showMessageDialog(mainView, "Data exported successfully!", "Export Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainView, "Error exporting data: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addSearchListener(JTextField searchField, Runnable updateAction) {
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateAction.run(); }
            public void removeUpdate(DocumentEvent e) { updateAction.run(); }
            public void changedUpdate(DocumentEvent e) { updateAction.run(); }
        });
    }

    private void filterInventory() {
        String keyword = mainView.getInventoryPanel().getSearchField().getText();
        mainView.getInventoryPanel().refreshBookTable(library.searchBooks(keyword));
    }

    private void filterGallery() {
        String keyword = mainView.getGalleryPanel().getSearchField().getText();
        mainView.getGalleryPanel().updateGallery(library.searchBooks(keyword));
    }
}
