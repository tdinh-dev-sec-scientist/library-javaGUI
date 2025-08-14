package view;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Redesigned panel for adding a new book, with image selection functionality.
 */
public class AddBookPanel extends JPanel {

    private JTextField isbnField, titleField, authorField, yearField, detailsField;
    private JComboBox<String> typeComboBox;
    private JButton addButton, clearButton, browseButton;
    private JLabel imagePreviewLabel;
    private String selectedImagePath;

    public AddBookPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setBackground(UIFactory.COLOR_BACKGROUND);

        JLabel titleLabel = new JLabel("Add a New Book");
        titleLabel.setFont(UIFactory.FONT_TITLE);
        titleLabel.setForeground(UIFactory.COLOR_PRIMARY_TEXT);
        add(titleLabel, BorderLayout.NORTH);

        JPanel mainContentPanel = new JPanel(new BorderLayout(20, 20));
        mainContentPanel.setOpaque(false);

        // --- Form Panel ---
        JPanel formCard = UIFactory.createCardPanel();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formCard.add(UIFactory.createFormLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        isbnField = UIFactory.createTextField();
        formCard.add(isbnField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formCard.add(UIFactory.createFormLabel("Title:"), gbc);
        gbc.gridx = 1;
        titleField = UIFactory.createTextField();
        formCard.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formCard.add(UIFactory.createFormLabel("Author:"), gbc);
        gbc.gridx = 1; authorField = UIFactory.createTextField(); formCard.add(authorField, gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        formCard.add(UIFactory.createFormLabel("Year:"), gbc);
        gbc.gridx = 1; yearField = UIFactory.createTextField(); formCard.add(yearField, gbc);
        gbc.gridx = 0; gbc.gridy = 4;
        formCard.add(UIFactory.createFormLabel("Type:"), gbc);
        gbc.gridx = 1; typeComboBox = UIFactory.createComboBox(new String[]{"Fiction", "Non-Fiction", "Reference"}); formCard.add(typeComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 5;
        formCard.add(UIFactory.createFormLabel("Details:"), gbc);
        gbc.gridx = 1; detailsField = UIFactory.createTextField(); formCard.add(detailsField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        clearButton = UIFactory.createSecondaryButton("Clear");
        addButton = UIFactory.createPrimaryButton("Add Book");
        buttonPanel.add(clearButton);
        buttonPanel.add(addButton);
        
        gbc.gridx = 1; gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(20, 10, 0, 10);
        formCard.add(buttonPanel, gbc);

        // --- Image Panel ---
        JPanel imagePanel = UIFactory.createCardPanel();
        imagePanel.setLayout(new BorderLayout(10, 10));
        imagePanel.setPreferredSize(new Dimension(250, 350));
        
        imagePreviewLabel = new JLabel("No Image Selected", SwingConstants.CENTER);
        imagePreviewLabel.setOpaque(true);
        imagePreviewLabel.setBackground(new Color(233, 236, 239));
        imagePreviewLabel.setBorder(BorderFactory.createLineBorder(UIFactory.COLOR_BORDER));
        
        browseButton = UIFactory.createSecondaryButton("Browse Image");
        browseButton.addActionListener(e -> browseImage());

        imagePanel.add(UIFactory.createFormLabel("Book Cover"), BorderLayout.NORTH);
        imagePanel.add(imagePreviewLabel, BorderLayout.CENTER);
        imagePanel.add(browseButton, BorderLayout.SOUTH);

        mainContentPanel.add(formCard, BorderLayout.CENTER);
        mainContentPanel.add(imagePanel, BorderLayout.EAST);
        add(mainContentPanel, BorderLayout.CENTER);
    }
    
    private void browseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath();
            ImageIcon icon = new ImageIcon(new ImageIcon(selectedImagePath).getImage().getScaledInstance(190, 220, Image.SCALE_SMOOTH));
            imagePreviewLabel.setText("");
            imagePreviewLabel.setIcon(icon);
        }
    }

    public String getIsbn() { return isbnField.getText().trim(); }
    public String getTitleText() { return titleField.getText().trim(); }
    public String getAuthor() { return authorField.getText().trim(); }
    public int getYear() { return Integer.parseInt(yearField.getText().trim()); }
    public String getBookType() { return (String) typeComboBox.getSelectedItem(); }
    public String getDetailsText() { return detailsField.getText().trim(); }
    public String getSelectedImagePath() { return selectedImagePath; }
    public JButton getAddButton() { return addButton; }
    public JButton getClearButton() { return clearButton; }

    public void clearFields() {
        isbnField.setText("");
        titleField.setText("");
        authorField.setText("");
        yearField.setText("");
        detailsField.setText("");
        typeComboBox.setSelectedIndex(0);
        imagePreviewLabel.setIcon(null);
        imagePreviewLabel.setText("No Image Selected");
        selectedImagePath = null;
    }
}
