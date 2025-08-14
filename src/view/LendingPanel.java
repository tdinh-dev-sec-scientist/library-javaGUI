package view;

import java.awt.*;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.Book;

/**
 * Redesigned panel for handling book lending and returns.
 */
public class LendingPanel extends JPanel {

    private JComboBox<Book> bookComboBox;
    private JTextField borrowerField;
    private JButton lendButton;
    private JTable loansTable;
    private DefaultTableModel loansTableModel;
    private JButton returnButton;

    public LendingPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setBackground(UIFactory.COLOR_BACKGROUND);

        JLabel titleLabel = new JLabel("Lending Management");
        titleLabel.setFont(UIFactory.FONT_TITLE);
        titleLabel.setForeground(UIFactory.COLOR_PRIMARY_TEXT);
        add(titleLabel, BorderLayout.NORTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.4);
        splitPane.setDividerSize(10);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setOpaque(false);

        JPanel lendingFormCard = UIFactory.createCardPanel();
        lendingFormCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lendingFormCard.add(UIFactory.createFormLabel("Available Book:"), gbc);
        bookComboBox = UIFactory.createComboBox(new Book[]{});
        bookComboBox.setRenderer(new BookCellRenderer());
        gbc.gridx = 1;
        lendingFormCard.add(bookComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        lendingFormCard.add(UIFactory.createFormLabel("Borrower Name:"), gbc);
        borrowerField = UIFactory.createTextField();
        gbc.gridx = 1;
        lendingFormCard.add(borrowerField, gbc);

        lendButton = UIFactory.createPrimaryButton("Lend Selected Book");
        gbc.gridy = 2; gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        lendingFormCard.add(lendButton, gbc);
        
        splitPane.setTopComponent(lendingFormCard);

        JPanel loansCard = UIFactory.createCardPanel();
        loansCard.setLayout(new BorderLayout(10,10));
        
        JLabel loansTitle = UIFactory.createFormLabel("Currently Loaned Books");
        loansTitle.setFont(UIFactory.FONT_SUBTITLE);
        loansCard.add(loansTitle, BorderLayout.NORTH);
        
        String[] columnNames = {"ISBN", "Title", "Borrower", "Loan Date", "Due Date"};
        loansTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        loansTable = UIFactory.createTable(loansTableModel);
        JScrollPane scrollPane = new JScrollPane(loansTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        loansCard.add(scrollPane, BorderLayout.CENTER);

        returnButton = UIFactory.createSecondaryButton("Return Selected Book");
        loansCard.add(returnButton, BorderLayout.SOUTH);
        
        splitPane.setBottomComponent(loansCard);
        
        add(splitPane, BorderLayout.CENTER);
    }

    public Book getSelectedBookToLend() { return (Book) bookComboBox.getSelectedItem(); }
    public String getBorrowerName() { return borrowerField.getText().trim(); }
    public JButton getLendButton() { return lendButton; }
    public JButton getReturnButton() { return returnButton; }

    public String getSelectedLoanIsbn() {
        int row = loansTable.getSelectedRow();
        return row >= 0 ? (String) loansTable.getValueAt(row, 0) : null;
    }

    public void refreshBookComboBox(List<Book> allBooks) {
        Vector<Book> availableBooks = new Vector<>();
        for (Book book : allBooks) {
            if (!book.isOnLoan() && !(book instanceof model.ReferenceBook)) {
                availableBooks.add(book);
            }
        }
        bookComboBox.setModel(new DefaultComboBoxModel<>(availableBooks));
    }

    public void refreshLoansTable(List<Book> allBooks) {
        loansTableModel.setRowCount(0);
        for (Book book : allBooks) {
            if (book.isOnLoan()) {
                loansTableModel.addRow(new Object[]{
                    book.getIsbn(), book.getTitle(), book.getBorrower(),
                    book.getLoanDate(), book.getDueDate()
                });
            }
        }
    }
    
    public void clearLendForm() {
        borrowerField.setText("");
    }
    
    private static class BookCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Book) {
                setText(((Book) value).getTitle() + " (" + ((Book) value).getIsbn() + ")");
            }
            return this;
        }
    }
}
