/*
 * Manages implementing the JLabel for each rectangle and handles the label
 * border coloring, background coloring, etc. 
 */
package diskrover;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 *
 * @author Kristofer
 */
public class LayeredPaneRectangle {
    JLabel label;
    FileRecord file;
    private final Border blackBorder = BorderFactory.createLineBorder(Color.black, 1);
    private final Border whiteBorder = BorderFactory.createLineBorder(Color.white, 1);
    private static final int BRIGHT_MODIFIER = 70;
    
    public LayeredPaneRectangle(final FileRecord file, String text, int layer, int x, int y, int width, int height) {
        this.file = file;
        label = new JLabel(text);
        label.setVerticalAlignment(JLabel.TOP);
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setOpaque(true);
        setBackgroundToDarkColor(layer);
        label.setForeground(Color.black);
        setBorderBlack();
        label.setBounds(x, y, width, height);
    }
    public void setBorderBlack() {
        label.setBorder(blackBorder);
    }
    public void setBorderWhite() {
        label.setBorder(whiteBorder);
    }
    public void setBackgroundToDarkColor(int layer) {
        label.setBackground(getDarkColorForLayer(layer));
    }
    public void setBackgroundToLightColor(int layer) {
        label.setBackground(getLightColorForLayer(layer));
    }
    public void setBackgroundToDarkGray() {
        label.setBackground(new Color(convertRGBToInt(128, 128, 128)));
    }
    public void setBackgroundToLightGray() {
        label.setBackground(new Color(convertRGBToInt(128+BRIGHT_MODIFIER, 
                                                      128+BRIGHT_MODIFIER, 
                                                      128+BRIGHT_MODIFIER)));
    }
    public void setTextAlignmentToCenter() {
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
    }
    
    private static int convertRGBToInt(int red, int green, int blue) {
        int rgb = red;
        rgb = (rgb << 8) + green;
        rgb = (rgb << 8) + blue;
        return rgb;
    }
    private Color getDarkColorForLayer(int layer) { 
        layer = layer % 6;
        switch (layer) {
            case 0: return new Color(convertRGBToInt(255, 0, 0));//Color.RED;
            case 1: return new Color(convertRGBToInt(255, 128, 0));//Color.ORANGE;
            case 2: return new Color(convertRGBToInt(255, 255, 0));//Color.YELLOW;
            case 3: return new Color(convertRGBToInt(0, 255, 0));//Color.GREEN;
            case 4: return new Color(convertRGBToInt(0, 0, 255));//Color.BLUE;
            case 5: return new Color(convertRGBToInt(128, 0, 255));//Color.MAGENTA;
            default: return Color.BLACK;
        }
    }
    private Color getLightColorForLayer(int layer) { 
        //This code can be made more efficient by not creating a new Color object
        //each time this function is called, but I'm leaving it for now for
        //readability on how I'm uniformly brightening the colors.
        layer = layer % 6;
        switch (layer) {
            case 0: return new Color(convertRGBToInt(255, BRIGHT_MODIFIER, BRIGHT_MODIFIER));//Color.RED;
            case 1: return new Color(convertRGBToInt(255, 128+BRIGHT_MODIFIER, BRIGHT_MODIFIER));//Color.ORANGE;
            case 2: return new Color(convertRGBToInt(255, 255, BRIGHT_MODIFIER));//Color.YELLOW;
            case 3: return new Color(convertRGBToInt(BRIGHT_MODIFIER, 255, BRIGHT_MODIFIER));//Color.GREEN;
            case 4: return new Color(convertRGBToInt(BRIGHT_MODIFIER, BRIGHT_MODIFIER, 255));//Color.BLUE;
            case 5: return new Color(convertRGBToInt(128+BRIGHT_MODIFIER, 0+BRIGHT_MODIFIER, 255));//Color.MAGENTA;
            default: return Color.BLACK;
        }
    }
}
