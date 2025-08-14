package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.Book;

/**
 * Updated InventoryPanel with right-click context menu and export button.
 */
public class InventoryPanel extends JPanel {

    private JTable booksTable;
    private DefaultTableModel booksTableModel;
    private JButton deleteButton;
    private JButton exportButton; // New button
    private JPopupMenu contextMenu; // New context menu
    private JMenuItem editMenuItem;
    private JTextField searchField;

    public InventoryPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setBackground(UIFactory.COLOR_BACKGROUND);

        JPanel headerPanel = new JPanel(new BorderLayout(20, 0));
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Book Inventory");
        titleLabel.setFont(UIFactory.FONT_TITLE);
        titleLabel.setForeground(UIFactory.COLOR_PRIMARY_TEXT);
        
        searchField = UIFactory.createTextField();
        searchField.setPreferredSize(new Dimension(300, 40));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(searchField, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- Table Panel ---
        JPanel tableCard = UIFactory.createCardPanel();
        tableCard.setLayout(new BorderLayout());
        
        String[] columnNames = {"ISBN", "Title", "Author", "Year", "Type", "Details", "Status"};
        booksTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        booksTable = UIFactory.createTable(booksTableModel);
        
        // --- Right-Click Menu ---
        contextMenu = new JPopupMenu();
        editMenuItem = new JMenuItem("Edit Selected Book");
        contextMenu.add(editMenuItem);
        
        booksTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = booksTable.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < booksTable.getRowCount()) {
                        booksTable.setRowSelectionInterval(row, row);
                        contextMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(booksTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableCard.add(scrollPane, BorderLayout.CENTER);
        
        add(tableCard, BorderLayout.CENTER);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        exportButton = UIFactory.createSecondaryButton("Export to CSV");
        deleteButton = UIFactory.createSecondaryButton("Delete Selected");
        deleteButton.setBackground(UIFactory.COLOR_DANGER);
        deleteButton.setForeground(Color.WHITE);

        buttonPanel.add(exportButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refreshBookTable(List<Book> books) {
        booksTableModel.setRowCount(0);
        for (Book book : books) {
            booksTableModel.addRow(new Object[]{
                    book.getIsbn(), book.getTitle(), book.getAuthor(),
                    book.getPublicationYear(), book.getType(),
                    book.getDetails(), book.isOnLoan() ? "On Loan" : "Available"
            });
        }
    }

    public String getSelectedBookIsbn() {
        int selectedRow = booksTable.getSelectedRow();
        return selectedRow >= 0 ? (String) booksTableModel.getValueAt(selectedRow, 0) : null;
    }
    
    // --- Getters for Controller ---
    public JMenuItem getEditMenuItem() { return editMenuItem; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getExportButton() { return exportButton; }
    public JTable getBooksTable() { return booksTable; }
    public JTextField getSearchField() { return searchField; }
}
