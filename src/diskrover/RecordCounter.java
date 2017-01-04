/*
 * RecordCounter provides the framework for scanning a storage drive in a 
 * multithreaded way so the user can watch a progress bar update. It also 
 * stores some global static variables. 
 */
package diskrover;

/**
 *
 * @author Kristofer
 */
public class RecordCounter extends javax.swing.SwingWorker<DriveRecord,String> {
    private class DriveRecordWorkThread extends Thread {
        private final String drivePath;
        public volatile DriveRecord drive;
        
        public DriveRecordWorkThread(final String drivePath) {
            this.drivePath = drivePath;
        }
        
        @Override
        public void run() {
            drive = new DriveRecord(drivePath);
        }
    }
    
    public static String drivePath;
    public static volatile long sizeCounted;    // |Summed as records are read
    public static volatile long fileCount;      // |
    public static volatile long folderCount;    // |
    public static DriveRecord drive;
    
    @Override
    protected DriveRecord doInBackground() {
        //Start mapping the drive in a separate thread
        //This is necessary because all of the mapping code is in constructors
        DriveRecordWorkThread mapDrive = new DriveRecordWorkThread(drivePath);
        mapDrive.start();
        
        //Calculate an estimate of how much space there is to map
        java.io.File rootFile = new java.io.File(drivePath);
        long impliedUsedSpace = rootFile.getTotalSpace() - rootFile.getFreeSpace();
        
        //Update progress bar using the above estimation
        while (mapDrive.isAlive()) {
            if (impliedUsedSpace != 0) {
                double progress = (double) RecordCounter.sizeCounted / impliedUsedSpace;
                progress *= 100.0;
                if (progress <= 100) {
                    setProgress((int)progress);
                } else {
                    setProgress(100);
                }
            }
        }
        drive = mapDrive.drive;
        return drive;
    }
    
    @Override
    protected void done() {
        GlobalGUI.cancelButton.doClick();
    }
}
