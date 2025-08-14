package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.Book;

/**
 * The main frame of the application, redesigned with a modern sidebar navigation,
 * user profile section, and welcome message.
 */
public class MainView extends JFrame {

    private InventoryPanel inventoryPanel;
    private AddBookPanel addBookPanel;
    private LendingPanel lendingPanel;
    private GalleryPanel galleryPanel;
    private UserPanel userPanel;
    private ReportingPanel reportingPanel;
    
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    private JLabel welcomeLabel;
    private JLabel userProfilePicLabel;
    private JLabel usernameLabel;
    private String currentUsername;
    
    private JButton dashboardButton, inventoryButton, galleryButton, lendingButton, addBookButton, reportingButton, settingsButton;
    private JButton selectedButton; // To track the currently selected button

    public MainView(String username) {
        this.currentUsername = username;
        setTitle("Library Management System - Pro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 900);
        setLocationRelativeTo(null);
        
        initModernUI();
    }

    private void initModernUI() {
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");

        Container contentPane = getContentPane();
        contentPane.setBackground(UIFactory.COLOR_BACKGROUND);
        contentPane.setLayout(new BorderLayout());

        JPanel sidebar = createSidebar();
        contentPane.add(sidebar, BorderLayout.WEST);

        JPanel mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setOpaque(false);
        mainContent.setBorder(new EmptyBorder(20, 25, 20, 25));

        welcomeLabel = new JLabel("Welcome Back, " + currentUsername + "!" + " 👋");
        welcomeLabel.setFont(UIFactory.FONT_TITLE);
        welcomeLabel.setForeground(UIFactory.COLOR_PRIMARY_TEXT);
        mainContent.add(welcomeLabel, BorderLayout.NORTH);
        
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setOpaque(false);

        userPanel = new UserPanel();
        inventoryPanel = new InventoryPanel();
        galleryPanel = new GalleryPanel();
        lendingPanel = new LendingPanel();
        addBookPanel = new AddBookPanel();
        reportingPanel = new ReportingPanel();

        contentPanel.add(userPanel, "Dashboard");
        contentPanel.add(inventoryPanel, "Inventory");
        contentPanel.add(galleryPanel, "Gallery");
        contentPanel.add(lendingPanel, "Lending");
        contentPanel.add(addBookPanel, "Add Book");
        contentPanel.add(reportingPanel, "Reporting");
        
        mainContent.add(contentPanel, BorderLayout.CENTER);
        contentPane.add(mainContent, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new SidebarPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(260, getHeight()));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UIFactory.COLOR_BORDER));

        JLabel logoLabel = new JLabel("TD");
        logoLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 48));
        logoLabel.setForeground(UIFactory.COLOR_ACCENT);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(new EmptyBorder(20, 0, 30, 0));
        sidebar.add(logoLabel);

        dashboardButton = createNavButton("Dashboard", new SettingsIcon(18, false));
        inventoryButton = createNavButton("Inventory", new SettingsIcon(18, false));
        galleryButton = createNavButton("Gallery", new SettingsIcon(18, false));
        lendingButton = createNavButton("Lending", new SettingsIcon(18, false));
        addBookButton = createNavButton("Add Book", new SettingsIcon(18, false));
        reportingButton = createNavButton("Reporting", new SettingsIcon(18, false));
        
        sidebar.add(dashboardButton);
        sidebar.add(inventoryButton);
        sidebar.add(galleryButton);
        sidebar.add(lendingButton);
        sidebar.add(addBookButton);
        sidebar.add(reportingButton);

        sidebar.add(Box.createVerticalGlue());

        JPanel profilePanel = new JPanel();
        profilePanel.setOpaque(false);
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        userProfilePicLabel = new JLabel();
        userProfilePicLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userProfilePicLabel.setPreferredSize(new Dimension(80, 80));
        userProfilePicLabel.setMaximumSize(new Dimension(80, 80));
        loadProfilePicture();
        
        usernameLabel = new JLabel(currentUsername);
        usernameLabel.setFont(UIFactory.FONT_BODY_BOLD.deriveFont(16f));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel userRoleLabel = new JLabel("Librarian");
        userRoleLabel.setFont(UIFactory.FONT_BODY_PLAIN);
        userRoleLabel.setForeground(UIFactory.COLOR_SECONDARY_TEXT);
        userRoleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        profilePanel.add(userProfilePicLabel);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        profilePanel.add(usernameLabel);
        profilePanel.add(userRoleLabel);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebar.add(profilePanel);

        settingsButton = createNavButton("Settings", new SettingsIcon(18, true));
        sidebar.add(settingsButton);
        
        // Set initial selected button
        setSelectedButton(dashboardButton);

        return sidebar;
    }

    private JButton createNavButton(String text, Icon icon) {
        JButton button = new JButton(text);
        button.setActionCommand(text);
        button.setFont(UIFactory.FONT_BODY_BOLD);
        button.setForeground(UIFactory.COLOR_SECONDARY_TEXT);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(15, 25, 15, 25));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setIcon(icon);
        button.setIconTextGap(15);
        
        button.addActionListener(e -> {
            if (!text.equals("Settings")) {
                cardLayout.show(contentPanel, e.getActionCommand());
                setSelectedButton((JButton) e.getSource());
            }
        });

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != selectedButton) {
                    button.setForeground(UIFactory.COLOR_ACCENT);
                    button.setFont(UIFactory.FONT_BODY_BOLD.deriveFont(15f)); // Zoom in
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (button != selectedButton) {
                    button.setForeground(UIFactory.COLOR_SECONDARY_TEXT);
                    button.setFont(UIFactory.FONT_BODY_BOLD); // Zoom out
                }
            }
        });
        
        return button;
    }

    private void setSelectedButton(JButton button) {
        if (selectedButton != null) {
            // Reset old selected button
            selectedButton.setForeground(UIFactory.COLOR_SECONDARY_TEXT);
            selectedButton.setFont(UIFactory.FONT_BODY_BOLD);
        }
        selectedButton = button;
        // Style new selected button
        selectedButton.setForeground(UIFactory.COLOR_ACCENT);
        selectedButton.setFont(UIFactory.FONT_BODY_BOLD.deriveFont(15f));
    }

    public void loadProfilePicture() {
        ImageIcon icon = ImageUtils.loadProfilePicture(currentUsername, 80);
        userProfilePicLabel.setIcon(icon);
    }
    
    public void updateUserInfo(String newUsername) {
        this.currentUsername = newUsername;
        usernameLabel.setText(newUsername);
        welcomeLabel.setText("Good Morning, " + newUsername + "!");
        loadProfilePicture();
    }
    
    public void showView() {
        setVisible(true);
    }
    
    public void refreshAllViews(List<Book> allBooks, Map<String, Integer> bookStats) {
        inventoryPanel.refreshBookTable(allBooks);
        galleryPanel.updateGallery(allBooks);
        lendingPanel.refreshBookComboBox(allBooks);
        lendingPanel.refreshLoansTable(allBooks);
        userPanel.refreshUserTables(allBooks);
        reportingPanel.updateChart(bookStats);
    }
    
    public String getCurrentUsername() { return currentUsername; }
    public JButton getSettingsButton() { return settingsButton; }
    public JButton getDashboardButton() { return dashboardButton; }
    public JButton getInventoryButton() { return inventoryButton; }
    public JButton getGalleryButton() { return galleryButton; }
    public JButton getLendingButton() { return lendingButton; }
    public JButton getAddBookButton() { return addBookButton; }
    public JButton getReportingButton() { return reportingButton; }
    
    public InventoryPanel getInventoryPanel() { return inventoryPanel; }
    public GalleryPanel getGalleryPanel() { return galleryPanel; }
    public AddBookPanel getAddBookPanel() { return addBookPanel; }
    public LendingPanel getLendingPanel() { return lendingPanel; }

    /**
     * Custom JPanel for the sidebar with a decorative background.
     */
    private static class SidebarPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            g2.setColor(UIFactory.COLOR_CARD);
            g2.fillRect(0, 0, width, height);

            Path2D.Double wave = new Path2D.Double();
            wave.moveTo(0, height * 0.75);
            wave.curveTo(width * 0.3, height * 0.65, width * 0.7, height * 0.95, width, height * 0.85);
            wave.lineTo(width, height);
            wave.lineTo(0, height);
            wave.closePath();

            g2.setColor(new Color(252, 228, 236));
            g2.fill(wave);

            g2.translate(0, 20);
            g2.setColor(new Color(255, 244, 228));
            g2.fill(wave);

            g2.dispose();
        }
    }
    
    /**
     * Custom Icon class to draw a gear for the settings button.
     */
    private static class SettingsIcon implements Icon {
        private int size;
        private boolean isGear;

        public SettingsIcon(int size, boolean isGear) {
            this.size = size;
            this.isGear = isGear;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            if (!isGear) return;

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(c.getForeground());

            int centerX = x + size / 2;
            int centerY = y + size / 2;
            int outerRadius = size / 2;
            int innerRadius = size / 4;
            int teeth = 8;
            double toothAngle = Math.PI / teeth;

            Path2D gear = new Path2D.Double();
            for (int i = 0; i < teeth; i++) {
                double angle = 2 * Math.PI * i / teeth;
                gear.moveTo(centerX + Math.cos(angle - toothAngle) * innerRadius, centerY + Math.sin(angle - toothAngle) * innerRadius);
                gear.lineTo(centerX + Math.cos(angle - toothAngle * 0.5) * outerRadius, centerY + Math.sin(angle - toothAngle * 0.5) * outerRadius);
                gear.lineTo(centerX + Math.cos(angle + toothAngle * 0.5) * outerRadius, centerY + Math.sin(angle + toothAngle * 0.5) * outerRadius);
                gear.lineTo(centerX + Math.cos(angle + toothAngle) * innerRadius, centerY + Math.sin(angle + toothAngle) * innerRadius);
            }
            g2.fill(gear);
            
            g2.setColor(UIFactory.COLOR_CARD);
            g2.fillOval(centerX - innerRadius, centerY - innerRadius, innerRadius * 2, innerRadius * 2);
            g2.dispose();
        }

        @Override
        public int getIconWidth() { return size; }
        @Override
        public int getIconHeight() { return size; }
    }
}
