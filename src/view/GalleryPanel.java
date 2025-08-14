package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.Book;

/**
 * Updated GalleryPanel with right-click context menu for editing.
 */
public class GalleryPanel extends JPanel {

    private JPanel galleryGridPanel;
    private JTextField searchField;
    private JPopupMenu contextMenu;
    private JMenuItem editMenuItem;

    public GalleryPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setBackground(UIFactory.COLOR_BACKGROUND);

        // ... (Header remains the same) ...
        JPanel headerPanel = new JPanel(new BorderLayout(20, 0));
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Book Gallery");
        titleLabel.setFont(UIFactory.FONT_TITLE);
        titleLabel.setForeground(UIFactory.COLOR_PRIMARY_TEXT);
        searchField = UIFactory.createTextField();
        searchField.setPreferredSize(new Dimension(300, 40));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(searchField, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        galleryGridPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 20, 20));
        galleryGridPanel.setOpaque(false);

        // --- Right-Click Menu ---
        contextMenu = new JPopupMenu();
        editMenuItem = new JMenuItem("Edit This Book");
        contextMenu.add(editMenuItem);

        JScrollPane scrollPane = new JScrollPane(galleryGridPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(UIFactory.COLOR_BACKGROUND);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void updateGallery(List<Book> books) {
        galleryGridPanel.removeAll();
        for (Book book : books) {
            galleryGridPanel.add(createBookCard(book));
        }
        galleryGridPanel.revalidate();
        galleryGridPanel.repaint();
    }

    private JPanel createBookCard(Book book) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setPreferredSize(new Dimension(220, 350));
        card.setBackground(UIFactory.COLOR_CARD);
        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        card.setComponentPopupMenu(contextMenu); // Attach the menu to the card

        // Store the book's ISBN in the card itself to retrieve it later
        card.setName(book.getIsbn());

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Select the card on right-click to show the menu for the correct item
                if (SwingUtilities.isRightMouseButton(e)) {
                    // This is a simple way to "select" the card for the controller
                    // A more robust solution might use a custom event or selection model
                    getRootPane().putClientProperty("selectedIsbn", card.getName());
                }
            }
        });

        // ... (The rest of the card creation is the same) ...
        JLabel coverLabel = new JLabel();
        coverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        coverLabel.setPreferredSize(new Dimension(190, 220));
        ImageIcon icon = ImageUtils.loadBookImage(book.getIsbn());
        if (icon != null) {
            coverLabel.setIcon(icon);
        } else {
            coverLabel.setText("No Image");
            coverLabel.setOpaque(true);
            coverLabel.setBackground(new Color(233, 236, 239));
            coverLabel.setForeground(UIFactory.COLOR_SECONDARY_TEXT);
            coverLabel.setFont(UIFactory.FONT_BODY_BOLD);
        }
        JPanel detailsPanel = new JPanel();
        detailsPanel.setOpaque(false);
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("<html><p style='width:160px'>" + book.getTitle() + "</p></html>");
        titleLabel.setFont(UIFactory.FONT_BODY_BOLD);
        titleLabel.setForeground(UIFactory.COLOR_PRIMARY_TEXT);
        JLabel authorLabel = new JLabel("by " + book.getAuthor());
        authorLabel.setFont(UIFactory.FONT_BODY_PLAIN);
        authorLabel.setForeground(UIFactory.COLOR_SECONDARY_TEXT);
        JLabel statusLabel = new JLabel(book.isOnLoan() ? "On Loan" : "Available");
        statusLabel.setFont(UIFactory.FONT_BODY_BOLD);
        statusLabel.setForeground(book.isOnLoan() ? UIFactory.COLOR_DANGER : UIFactory.COLOR_SUCCESS);
        detailsPanel.add(titleLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailsPanel.add(authorLabel);
        detailsPanel.add(Box.createVerticalGlue());
        detailsPanel.add(statusLabel);
        card.add(coverLabel, BorderLayout.NORTH);
        card.add(detailsPanel, BorderLayout.CENTER);

        return card;
    }
    
    // --- Getters for Controller ---
    public JMenuItem getEditMenuItem() { return editMenuItem; }
    public JTextField getSearchField() { return searchField; }
    
    private static class WrapLayout extends FlowLayout {
        public WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }
    }
}
