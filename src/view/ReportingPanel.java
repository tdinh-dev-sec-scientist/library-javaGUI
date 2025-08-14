package view;

import java.awt.*;
import java.util.Map;
import javax.swing.*;

/**
 * A functional panel for displaying graphs and reports about the library's data.
 */
public class ReportingPanel extends JPanel {

    private BarChartPanel barChartPanel;

    public ReportingPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        setBackground(UIFactory.COLOR_BACKGROUND);

        // --- Header ---
        JLabel titleLabel = new JLabel("Library Analytics & Reporting");
        titleLabel.setFont(UIFactory.FONT_TITLE);
        titleLabel.setForeground(UIFactory.COLOR_PRIMARY_TEXT);
        add(titleLabel, BorderLayout.NORTH);
        
        // --- Main Content ---
        JPanel chartCard = UIFactory.createCardPanel();
        chartCard.setLayout(new BorderLayout());
        
        JLabel chartTitle = new JLabel("Number of Books by Type", SwingConstants.CENTER);
        chartTitle.setFont(UIFactory.FONT_SUBTITLE);
        chartTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        barChartPanel = new BarChartPanel();
        
        chartCard.add(chartTitle, BorderLayout.NORTH);
        chartCard.add(barChartPanel, BorderLayout.CENTER);
        
        add(chartCard, BorderLayout.CENTER);
    }
    
    /**
     * Updates the bar chart with new data.
     * @param data A map containing book counts by type.
     */
    public void updateChart(Map<String, Integer> data) {
        barChartPanel.setData(data);
    }
}
