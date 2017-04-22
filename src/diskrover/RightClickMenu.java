/*
 * Defines the right click menu for the LayeredPanelRectangles.
 */
package diskrover;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kristofer
 */
public class RightClickMenu extends JPopupMenu implements ActionListener {
    private static final String menuItemText_CopyPath_file = "Copy full file name";
    private static final String menuItemText_CopyPath_dir = "Copy full folder path";
    private static final String menuItemText_OpenPath = "Browse to this folder";
    private static final String menuItemText_DeletePerm = "Delete forever";
    private final FileRecord fileRecord;
    private final RefreshCallback refreshCallback;
    private final Desktop currentDesktop;
    
    public RightClickMenu(FileRecord fileRecord, boolean fileMarkedDeleted, RefreshCallback refreshCallback) {
        this.fileRecord = fileRecord;
        this.refreshCallback = refreshCallback;
        //RefreshCallback is to pass on to a control to command all of the LayeredPaneRectangles to refresh.
        
        //Check desktop support for opening explorer/finder/etc
        if (Desktop.isDesktopSupported()) currentDesktop = Desktop.getDesktop();
        else currentDesktop = null;
        boolean openPathSupported = 
                !fileMarkedDeleted && 
                (currentDesktop != null) && 
                currentDesktop.isSupported(Desktop.Action.BROWSE);
        
        //Add the menu items
        JMenuItem copyPathButton;
        if (fileRecord.isDirectory) copyPathButton = createMenuItem(menuItemText_CopyPath_dir, true);
        else copyPathButton = createMenuItem(menuItemText_CopyPath_file, true);
        this.add(copyPathButton);
        
        JMenuItem openPathButton = createMenuItem(menuItemText_OpenPath, openPathSupported);
        openPathButton.setEnabled(openPathSupported);
        this.add(openPathButton);
        
        this.addSeparator();
        
        JMenuItem deletePathButton = createMenuItem(menuItemText_DeletePerm, !fileMarkedDeleted);
        deletePathButton.setEnabled(!fileMarkedDeleted);
        this.add(deletePathButton);
    }
    
    private JMenuItem createMenuItem(String text, boolean addListener) {
        JMenuItem item = new JMenuItem(text);
        if (addListener) item.addActionListener(this);
        return item;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        //User clicked one of the menu items.
        
        String menuItemSelected = ae.getActionCommand();
        if ((menuItemSelected.compareTo(menuItemText_CopyPath_file) == 0) ||
            (menuItemSelected.compareTo(menuItemText_CopyPath_dir) == 0)){
            //Add path to clipboard
            
            StringSelection selection = new StringSelection(fileRecord.path);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        }
        else if (menuItemSelected.compareTo(menuItemText_OpenPath) == 0) {
            //Open path in folder browser
            
            try {
                File file = new File(fileRecord.path);
                if (!fileRecord.isDirectory) file = file.getParentFile();
                URI uri = file.toURI();
                currentDesktop.browse(uri);
            } catch (IOException ex) {
                System.out.println(RightClickMenu.class.getName() + 
                        " browse URI(" + fileRecord.path + ") failed: " + 
                        ex.getMessage());
            }
        }
        else if (menuItemSelected.compareTo(menuItemText_DeletePerm) == 0) {
            //Delete the clicked file/directory after asking for confirmation.
            
            String confirmationMessage = "Permanently delete the following ";
            if (fileRecord.isDirectory) confirmationMessage += "folder?\n";
            else confirmationMessage += "file?\n";
            confirmationMessage += fileRecord.path;
            String[] options = {"Yes", "No"};
            int userChoice = JOptionPane.showOptionDialog(
                    this,
                    confirmationMessage,
                    "Delete Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[1]);
            if (userChoice != 0) return;
            
            boolean deleted = fileRecord.delete();
            if (!deleted) {
                String errorMessage = "Could not delete ";
                if (fileRecord.isDirectory) errorMessage += "folder:\n";
                else errorMessage += "file:\n";
                errorMessage += fileRecord.path;
                JOptionPane.showMessageDialog(
                        this,
                        errorMessage,
                        "Deletion Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            refreshCallback.refresh();
        }
    }
}
