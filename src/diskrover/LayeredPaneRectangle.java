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
    private final Border blackBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
    private final Border whiteBorder = BorderFactory.createLineBorder(Color.WHITE, 1);
    private final Border orangeBorder = BorderFactory.createLineBorder(Color.ORANGE, 1);
    private static final int BRIGHT_MODIFIER = 70;
    private boolean fileMarkedDeleted = false;
    
    public LayeredPaneRectangle(final FileRecord file, String text, int layer, int x, int y, int width, int height, RefreshCallback refreshCallback) {
        this.file = file;
        checkIfFileIsDeleted();
        
        label = new JLabel(text);
        label.setVerticalAlignment(JLabel.TOP);
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setOpaque(true);
        label.setBounds(x, y, width, height);
        label.setComponentPopupMenu(new RightClickMenu(this.file, fileMarkedDeleted, refreshCallback));
        
        //Coloring, changes if file/folder is deleted
        if (fileMarkedDeleted) {
            setFileDeletedColoring();
        } else {
            setBackgroundToDarkColor(layer);
            label.setForeground(Color.BLACK);
            setBorderBlack();
        }
    }
    
    public void setTextAlignmentToCenter() {
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
    }
    
    public void setFileColoring(boolean mouseOver, int layer) {
        //Sets label coloring for file/directory records.
        
        if (fileMarkedDeleted) {
            setFileDeletedColoring();
        } else {
            label.setForeground(Color.BLACK);
            if (mouseOver) {
                setBackgroundToLightColor(layer);
                setBorderWhite();
            } else {
                setBackgroundToDarkColor(layer);
                setBorderBlack();
            }
        }
    }
    
    public void setSpaceColoring(boolean mouseOver) {
        //Sets label coloring for free space records.
        
        label.setForeground(Color.BLACK);
        if (mouseOver) {
            setBackgroundToLightGray();
            setBorderWhite();
        } else {
            setBackgroundToDarkGray();
            setBorderBlack();
        }
    }
    
    //////////// Private helper functions below ////////////
    private void setFileDeletedColoring() {
        label.setBackground(Color.BLACK);
        label.setForeground(Color.RED);
        label.setBorder(orangeBorder);
    }
    private void setBorderBlack() {
        label.setBorder(blackBorder);
    }
    private void setBorderWhite() {
        label.setBorder(whiteBorder);
    }
    private void setBackgroundToDarkColor(int layer) {
        label.setBackground(getDarkColorForLayer(layer));
    }
    private void setBackgroundToLightColor(int layer) {
        label.setBackground(getLightColorForLayer(layer));
    }
    private void setBackgroundToDarkGray() {
        label.setBackground(new Color(convertRGBToInt(128, 128, 128)));
    }
    private void setBackgroundToLightGray() {
        label.setBackground(new Color(convertRGBToInt(128+BRIGHT_MODIFIER, 
                                                      128+BRIGHT_MODIFIER, 
                                                      128+BRIGHT_MODIFIER)));
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
    private void checkIfFileIsDeleted() {
        if (!fileMarkedDeleted) fileMarkedDeleted = !file.exists();
    }
}
