package view;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * A dialog for changing user settings like username and profile picture.
 * This version fixes the non-editable fields and adds placeholder text.
 */
public class SettingsDialog extends JDialog {

    private JTextField usernameField, emailField, dobField;
    private JLabel avatarPreviewLabel;
    private JButton saveButton, cancelButton, browseButton, logoutButton;
    
    private String currentUsername;
    private String newAvatarPath;
    private boolean saved = false;
    private boolean logout = false;

    public SettingsDialog(Frame owner, String username) {
        super(owner, "Settings", true);
        this.currentUsername = username;

        setSize(500, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(UIFactory.COLOR_BACKGROUND);
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(20, 25, 20, 25));

        // --- Title ---
        JLabel titleLabel = new JLabel("User Settings");
        titleLabel.setFont(UIFactory.FONT_TITLE.deriveFont(22f));
        titleLabel.setBorder(new EmptyBorder(0,0,10,0));
        add(titleLabel, BorderLayout.NORTH);

        // --- Form ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Avatar
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 3; gbc.weightx = 0.0;
        gbc.insets = new Insets(8, 5, 8, 20);
        avatarPreviewLabel = new JLabel();
        avatarPreviewLabel.setPreferredSize(new Dimension(80, 80));
        ImageIcon currentAvatar = ImageUtils.loadProfilePicture(currentUsername, 80);
        avatarPreviewLabel.setIcon(currentAvatar);
        formPanel.add(avatarPreviewLabel, gbc);

        // Username
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridheight = 1; gbc.weightx = 0.0;
        gbc.insets = new Insets(8, 5, 8, 5);
        formPanel.add(UIFactory.createFormLabel("Name:"), gbc);
        gbc.gridx = 2; gbc.weightx = 1.0;
        usernameField = createPlaceholderTextField(currentUsername);
        formPanel.add(usernameField, gbc);

        // Email
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(UIFactory.createFormLabel("Email:"), gbc);
        gbc.gridx = 2;
        emailField = createPlaceholderTextField(currentUsername.toLowerCase() + "@example.com");
        formPanel.add(emailField, gbc);

        // Date of Birth
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(UIFactory.createFormLabel("Date of Birth:"), gbc);
        gbc.gridx = 2;
        dobField = createPlaceholderTextField("DD/MM/YYYY");
        dobField.setText("01/01/1990"); // Default value
        dobField.setForeground(Color.BLACK);
        formPanel.add(dobField, gbc);

        // Browse Button
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3; gbc.anchor = GridBagConstraints.CENTER;
        browseButton = UIFactory.createSecondaryButton("Change Avatar");
        browseButton.addActionListener(e -> browseAvatar());
        formPanel.add(browseButton, gbc);

        // --- Buttons ---
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setOpaque(false);
        JPanel actionButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionButtons.setOpaque(false);
        
        logoutButton = UIFactory.createSecondaryButton("Logout");
        logoutButton.setBackground(UIFactory.COLOR_DANGER);
        logoutButton.setForeground(Color.WHITE);
        
        cancelButton = UIFactory.createSecondaryButton("Cancel");
        saveButton = UIFactory.createPrimaryButton("Save");

        actionButtons.add(cancelButton);
        actionButtons.add(saveButton);
        bottomPanel.add(logoutButton, BorderLayout.WEST);
        bottomPanel.add(actionButtons, BorderLayout.EAST);

        // --- Add to Dialog ---
        add(formPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- Listeners ---
        saveButton.addActionListener(e -> save());
        cancelButton.addActionListener(e -> dispose());
        logoutButton.addActionListener(e -> logout());
    }
    
    // Helper method to create a JTextField with placeholder functionality
    private JTextField createPlaceholderTextField(String placeholder) {
        JTextField textField = UIFactory.createTextField();
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);
        textField.setEditable(true); // Explicitly set to editable

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });
        return textField;
    }

    private void browseAvatar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            newAvatarPath = selectedFile.getAbsolutePath();
            ImageIcon icon = new ImageIcon(new ImageIcon(newAvatarPath).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
            avatarPreviewLabel.setIcon(icon);
        }
    }

    private void save() {
        this.saved = true;
        dispose();
    }
    
    private void logout() {
        this.logout = true;
        dispose();
    }

    // --- Getters for the Controller ---
    public boolean isSaved() { return saved; }
    public boolean isLogoutTriggered() { return logout; }
    public String getNewUsername() { return usernameField.getText(); }
    public String getNewAvatarPath() { return newAvatarPath; }
}
