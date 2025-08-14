package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.Book;
import model.FictionBook;
import model.NonFictionBook;
import model.ReferenceBook;

/**
 * A dedicated dialog for editing an existing book's information, including its cover image.
 */
public class EditBookDialog extends JDialog {

    private JTextField titleField, authorField, yearField, detailsField;
    private JButton saveButton, cancelButton, editImageButton;
    private JLabel imagePreviewLabel;
    private Book bookToEdit;
    private String newImagePath;
    private boolean succeeded = false;

    public EditBookDialog(Frame owner, Book book) {
        super(owner, "Edit Book", true);
        this.bookToEdit = book;

        setSize(650, 450);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(20, 20));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(20, 25, 20, 25));
        
        initUI();
        populateFields();
    }

    private void initUI() {
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        
        // --- Form Panel ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        // (GridBagLayout setup is the same as AddBookPanel)
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(UIFactory.createFormLabel("Title:"), gbc);
        gbc.gridx = 1; titleField = UIFactory.createTextField(); formPanel.add(titleField, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(UIFactory.createFormLabel("Author:"), gbc);
        gbc.gridx = 1; authorField = UIFactory.createTextField(); formPanel.add(authorField, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(UIFactory.createFormLabel("Year:"), gbc);
        gbc.gridx = 1; yearField = UIFactory.createTextField(); formPanel.add(yearField, gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        String detailLabel = switch (bookToEdit.getType()) {
            case "Fiction" -> "Genre:"; case "Non-Fiction" -> "Subject:"; case "Reference" -> "Category:"; default -> "Details:";
        };
        formPanel.add(UIFactory.createFormLabel(detailLabel), gbc);
        gbc.gridx = 1; detailsField = UIFactory.createTextField(); formPanel.add(detailsField, gbc);

        // --- Image Panel ---
        JPanel imagePanel = new JPanel(new BorderLayout(10, 10));
        imagePreviewLabel = new JLabel("No Image", SwingConstants.CENTER);
        imagePreviewLabel.setPreferredSize(new Dimension(190, 220));
        imagePreviewLabel.setBorder(BorderFactory.createLineBorder(UIFactory.COLOR_BORDER));
        editImageButton = UIFactory.createSecondaryButton("Edit Image");
        editImageButton.addActionListener(e -> browseImage());
        imagePanel.add(imagePreviewLabel, BorderLayout.CENTER);
        imagePanel.add(editImageButton, BorderLayout.SOUTH);

        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(imagePanel, BorderLayout.EAST);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        cancelButton = UIFactory.createSecondaryButton("Cancel");
        saveButton = UIFactory.createPrimaryButton("Save Changes");
        cancelButton.addActionListener(e -> dispose());
        saveButton.addActionListener(e -> saveChanges());
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void browseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            newImagePath = fileChooser.getSelectedFile().getAbsolutePath();
            ImageIcon icon = new ImageIcon(new ImageIcon(newImagePath).getImage().getScaledInstance(190, 220, Image.SCALE_SMOOTH));
            imagePreviewLabel.setText("");
            imagePreviewLabel.setIcon(icon);
        }
    }

    private void populateFields() {
        titleField.setText(bookToEdit.getTitle());
        authorField.setText(bookToEdit.getAuthor());
        yearField.setText(String.valueOf(bookToEdit.getPublicationYear()));
        
        if (bookToEdit instanceof FictionBook fb) detailsField.setText(fb.getGenre());
        else if (bookToEdit instanceof NonFictionBook nfb) detailsField.setText(nfb.getSubject());
        else if (bookToEdit instanceof ReferenceBook rb) detailsField.setText(rb.getCategory());
        
        ImageIcon currentIcon = ImageUtils.loadBookImage(bookToEdit.getIsbn());
        if (currentIcon != null) {
            imagePreviewLabel.setText("");
            imagePreviewLabel.setIcon(currentIcon);
        }
    }

    private void saveChanges() {
        try {
            bookToEdit.setTitle(titleField.getText());
            bookToEdit.setAuthor(authorField.getText());
            bookToEdit.setPublicationYear(Integer.parseInt(yearField.getText()));

            if (bookToEdit instanceof FictionBook fb) fb.setGenre(detailsField.getText());
            else if (bookToEdit instanceof NonFictionBook nfb) nfb.setSubject(detailsField.getText());
            else if (bookToEdit instanceof ReferenceBook rb) rb.setCategory(detailsField.getText());
            
            if (newImagePath != null) {
                ImageUtils.saveBookImage(bookToEdit.getIsbn(), newImagePath);
            }
            
            succeeded = true;
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Year must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSucceeded() { return succeeded; }
    public Book getUpdatedBook() { return bookToEdit; }
}
