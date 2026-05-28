package utils;

import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {
    private Color startColor;
    private Color endColor;
    private int orientation; // 0 for vertical, 1 for horizontal

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;

    public GradientPanel(Color startColor, Color endColor, int orientation) {
        this.startColor = startColor;
        this.endColor = endColor;
        this.orientation = orientation;
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int width = getWidth();
        int height = getHeight();

        GradientPaint gradient;
        if (orientation == VERTICAL) {
            gradient = new GradientPaint(0, 0, startColor, 0, height, endColor);
        } else {
            gradient = new GradientPaint(0, 0, startColor, width, 0, endColor);
        }

        g2.setPaint(gradient);
        g2.fillRect(0, 0, width, height);
    }
}
