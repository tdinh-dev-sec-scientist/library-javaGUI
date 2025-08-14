package view;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;

/**
 * A custom panel that draws a simple bar chart.
 */
public class BarChartPanel extends JPanel {

    private Map<String, Integer> data;
    private Map<String, Color> colors;

    public BarChartPanel() {
        this.data = new LinkedHashMap<>();
        this.colors = new LinkedHashMap<>();
        setBackground(Color.WHITE);
    }

    /**
     * Updates the chart with new data.
     * @param data A map where keys are categories and values are the numbers.
     */
    public void setData(Map<String, Integer> data) {
        this.data = data;
        // Assign some default colors
        colors.put("Fiction", new Color(136, 84, 199));
        colors.put("Non-Fiction", new Color(255, 149, 0));
        colors.put("Reference", new Color(88, 86, 214));
        repaint(); // Redraw the component with new data
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data == null || data.isEmpty()) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int maxVal = data.values().stream().max(Integer::compareTo).orElse(0);
        if (maxVal == 0) maxVal = 10; // Avoid division by zero

        int barWidth = 60;
        int spacing = 40;
        int x = 50;
        int chartHeight = getHeight() - 100;
        int chartY = 50;

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            int value = entry.getValue();
            int barHeight = (int) (((double) value / maxVal) * chartHeight);

            // Draw bar
            g2.setColor(colors.getOrDefault(entry.getKey(), Color.GRAY));
            g2.fillRect(x, chartY + chartHeight - barHeight, barWidth, barHeight);

            // Draw value on top
            g2.setColor(UIFactory.COLOR_PRIMARY_TEXT);
            g2.setFont(UIFactory.FONT_BODY_BOLD);
            String valStr = String.valueOf(value);
            int strWidth = g2.getFontMetrics().stringWidth(valStr);
            g2.drawString(valStr, x + (barWidth - strWidth) / 2, chartY + chartHeight - barHeight - 5);

            // Draw label at bottom
            g2.setFont(UIFactory.FONT_BODY_PLAIN);
            strWidth = g2.getFontMetrics().stringWidth(entry.getKey());
            g2.drawString(entry.getKey(), x + (barWidth - strWidth) / 2, chartY + chartHeight + 20);

            x += barWidth + spacing;
        }
    }
}
