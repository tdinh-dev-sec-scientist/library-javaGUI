package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * A utility class for creating styled UI components.
 * This ensures a consistent look and feel across the entire application.
 * It's the foundation of our new design system.
 */
public class UIFactory {

    // --- Color Palette ---
    public static final Color COLOR_BACKGROUND = new Color(244, 247, 252);
    public static final Color COLOR_CARD = Color.WHITE;
    public static final Color COLOR_PRIMARY_TEXT = new Color(33, 37, 41);
    public static final Color COLOR_SECONDARY_TEXT = new Color(108, 117, 125);
    public static final Color COLOR_ACCENT = new Color(88, 86, 214); // A modern purple
    public static final Color COLOR_ACCENT_LIGHT = new Color(237, 237, 255);
    public static final Color COLOR_DANGER = new Color(220, 53, 69);
    public static final Color COLOR_SUCCESS = new Color(25, 135, 84);
    public static final Color COLOR_BORDER = new Color(222, 226, 230);

    // --- Font Scheme ---
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY_PLAIN = new Font("Segoe UI", Font.PLAIN, 14);

    /**
     * Creates a standard styled primary button.
     * @param text The text for the button.
     * @return A styled JButton.
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BODY_BOLD);
        button.setBackground(COLOR_ACCENT);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    /**
     * Creates a secondary styled button (e.g., for "Cancel" or "Clear").
     * @param text The text for the button.
     * @return A styled JButton.
     */
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BODY_BOLD);
        button.setBackground(new Color(230, 232, 235));
        button.setForeground(COLOR_PRIMARY_TEXT);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Creates a standard styled text field.
     * @return A styled JTextField.
     */
    public static JTextField createTextField() {
        JTextField textField = new JTextField(20);
        textField.setFont(FONT_BODY_PLAIN);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER),
            new EmptyBorder(8, 12, 8, 12)
        ));
        return textField;
    }
    
    /**
     * Creates a standard styled JComboBox.
     * @param items The items to add to the combo box.
     * @return A styled JComboBox.
     */
    public static <E> JComboBox<E> createComboBox(E[] items) {
        JComboBox<E> comboBox = new JComboBox<>(items);
        comboBox.setFont(FONT_BODY_PLAIN);
        comboBox.setBackground(Color.WHITE);
        return comboBox;
    }

    /**
     * Creates a standard styled label for forms.
     * @param text The text for the label.
     * @return A styled JLabel.
     */
    public static JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_BODY_BOLD);
        label.setForeground(COLOR_PRIMARY_TEXT);
        return label;
    }

    /**
     * Creates a panel with a card-like appearance.
     * @return A JPanel styled as a card.
     */
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_CARD);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        return panel;
    }
    
    /**
     * Creates a consistently styled JTable.
     * @param model The table model to use.
     * @return A styled JTable.
     */
    public static JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(FONT_BODY_PLAIN);
        table.setRowHeight(32);
        table.setGridColor(COLOR_BORDER);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(COLOR_ACCENT_LIGHT);
        table.setSelectionForeground(COLOR_PRIMARY_TEXT);
        table.setShowGrid(true);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BODY_BOLD);
        header.setBackground(new Color(248, 249, 250));
        header.setForeground(COLOR_SECONDARY_TEXT);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));
        header.setReorderingAllowed(false);
        
        return table;
    }
}
