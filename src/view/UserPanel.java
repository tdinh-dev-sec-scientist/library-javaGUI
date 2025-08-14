package view;

import model.Book;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * The final redesigned UserPanel, acting as a modern activity dashboard.
 */
public class UserPanel extends JPanel {

    private DefaultTableModel activeLoansModel;
    private JTable activeLoansTable;
    private DefaultTableModel overdueLoansModel;
    private JTable overdueLoansTable;
    private JLabel totalLoansValueLabel;
    private JLabel overdueBooksValueLabel;

    public UserPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setBackground(UIFactory.COLOR_BACKGROUND);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel dashboardTitle = new JLabel("Activity Dashboard");
        dashboardTitle.setFont(UIFactory.FONT_TITLE);
        dashboardTitle.setForeground(UIFactory.COLOR_PRIMARY_TEXT);
        headerPanel.add(dashboardTitle, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setOpaque(false);
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        statsPanel.setOpaque(false);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        totalLoansValueLabel = new JLabel("0");
        JPanel totalLoansCard = createStatCard("Total Books on Loan", totalLoansValueLabel);
        
        overdueBooksValueLabel = new JLabel("0");
        JPanel overdueBooksCard = createStatCard("Overdue Books", overdueBooksValueLabel);
        
        statsPanel.add(totalLoansCard);
        statsPanel.add(overdueBooksCard);

        JPanel tablesPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        tablesPanel.setOpaque(false);

        JPanel activeLoansCard = UIFactory.createCardPanel();
        activeLoansCard.setLayout(new BorderLayout(0, 10));
        activeLoansCard.add(UIFactory.createFormLabel("Active Loans"), BorderLayout.NORTH);
        String[] activeColumns = {"Title", "Borrower", "Due Date", "Days Left"};
        activeLoansModel = new DefaultTableModel(activeColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        activeLoansTable = UIFactory.createTable(activeLoansModel);
        JScrollPane activeScrollPane = new JScrollPane(activeLoansTable);
        activeScrollPane.setBorder(BorderFactory.createEmptyBorder());
        activeLoansCard.add(activeScrollPane, BorderLayout.CENTER);

        JPanel overdueLoansCard = UIFactory.createCardPanel();
        overdueLoansCard.setLayout(new BorderLayout(0, 10));
        overdueLoansCard.add(UIFactory.createFormLabel("Overdue Loans"), BorderLayout.NORTH);
        String[] overdueColumns = {"Title", "Borrower", "Due Date", "Days Overdue"};
        overdueLoansModel = new DefaultTableModel(overdueColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        overdueLoansTable = UIFactory.createTable(overdueLoansModel);
        overdueLoansTable.getColumnModel().getColumn(3).setCellRenderer(new OverdueCellRenderer());
        JScrollPane overdueScrollPane = new JScrollPane(overdueLoansTable);
        overdueScrollPane.setBorder(BorderFactory.createEmptyBorder());
        overdueLoansCard.add(overdueScrollPane, BorderLayout.CENTER);

        tablesPanel.add(activeLoansCard);
        tablesPanel.add(overdueLoansCard);

        mainContentPanel.add(statsPanel);
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContentPanel.add(tablesPanel);

        add(mainContentPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, JLabel valueLabel) {
        JPanel card = UIFactory.createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(UIFactory.COLOR_PRIMARY_TEXT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIFactory.FONT_BODY_PLAIN);
        titleLabel.setForeground(UIFactory.COLOR_SECONDARY_TEXT);
        
        card.add(valueLabel);
        card.add(titleLabel);
        return card;
    }

    public void refreshUserTables(List<Book> allBooks) {
        activeLoansModel.setRowCount(0);
        overdueLoansModel.setRowCount(0);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int totalLoans = 0;
        int overdueCount = 0;

        for (Book book : allBooks) {
            if (book.isOnLoan() && book.getDueDate() != null) {
                totalLoans++;
                long daysBetween = ChronoUnit.DAYS.between(java.time.LocalDate.now(), book.getDueDate());
                
                if (daysBetween >= 0) {
                    activeLoansModel.addRow(new Object[]{book.getTitle(), book.getBorrower(), book.getDueDate().format(formatter), daysBetween});
                } else {
                    overdueCount++;
                    overdueLoansModel.addRow(new Object[]{book.getTitle(), book.getBorrower(), book.getDueDate().format(formatter), Math.abs(daysBetween)});
                }
            }
        }
        
        totalLoansValueLabel.setText(String.valueOf(totalLoans));
        overdueBooksValueLabel.setText(String.valueOf(overdueCount));
    }
    
    private static class OverdueCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setForeground(isSelected ? Color.WHITE : UIFactory.COLOR_DANGER);
            c.setFont(c.getFont().deriveFont(Font.BOLD));
            ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
            return c;
        }
    }
}
