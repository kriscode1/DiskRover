/*
 * GlobalGUI shares a cancel button so another thread can close the popup
 * for the user when the calculations are complete. 
 */
package diskrover;

/**
 *
 * @author Kristofer
 */
public class GlobalGUI {
    public static volatile javax.swing.JButton cancelButton;
}
