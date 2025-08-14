package view;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * A modern and clean LoginView.
 */
public class LoginView extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginView() {
        setTitle("Library System Login");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(UIFactory.COLOR_CARD);
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // --- Header ---
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("Welcome Back!");
        titleLabel.setFont(UIFactory.FONT_TITLE);
        titleLabel.setForeground(UIFactory.COLOR_PRIMARY_TEXT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Please login to access the system.");
        subtitleLabel.setFont(UIFactory.FONT_BODY_PLAIN);
        subtitleLabel.setForeground(UIFactory.COLOR_SECONDARY_TEXT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subtitleLabel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // --- Form Panel ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 5, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;

        gbc.gridy = 0;
        formPanel.add(UIFactory.createFormLabel("Username"), gbc);
        
        gbc.gridy = 1;
        usernameField = UIFactory.createTextField();
        formPanel.add(usernameField, gbc);

        gbc.gridy = 2;
        formPanel.add(UIFactory.createFormLabel("Password"), gbc);
        
        gbc.gridy = 3;
        passwordField = new JPasswordField();
        passwordField.setFont(UIFactory.FONT_BODY_PLAIN);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIFactory.COLOR_BORDER),
            new EmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(passwordField, gbc);
        
        gbc.gridy = 4;
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)), gbc);

        gbc.gridy = 5;
        loginButton = UIFactory.createPrimaryButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 45));
        formPanel.add(loginButton, gbc);

        // Add Key Listener for Enter key
        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        };
        usernameField.addKeyListener(enterListener);
        passwordField.addKeyListener(enterListener);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    public String getUsername() { return usernameField.getText(); }
    public String getPassword() { return new String(passwordField.getPassword()); }
    public JButton getLoginButton() { return loginButton; }
    public void showView() { setVisible(true); }
    public void closeView() { dispose(); }
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Failed", JOptionPane.ERROR_MESSAGE);
    }
}
