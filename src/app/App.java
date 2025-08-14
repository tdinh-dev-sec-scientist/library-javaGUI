package app;

import controller.LibraryController;
import javax.swing.*;
import model.Library;
import view.LoginView;

/**
 * The main entry point for the Library Management System application.
 */
public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Set a modern look and feel for the entire application.
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                System.err.println("Nimbus L&F not found, falling back to default.");
            }
            
            // 1. Initialize the Model
            // The model loads its own data from the specified file.
            Library libraryModel = new Library("books.dat");

            // 2. Initialize the first View (Login Screen)
            LoginView loginView = new LoginView();

            // 3. Initialize the Controller
            // The controller links the model and the view together.
            new LibraryController(libraryModel, loginView);

            // 4. Make the initial view visible
            loginView.showView();
        });
    }
}
