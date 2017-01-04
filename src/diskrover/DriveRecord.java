/*
 * DriveRecord manages the FileRecords of a storage drive.
 */
package diskrover;

/**
 *
 * @author Kristofer
 */
public class DriveRecord {
    public final String path;
    public final long totalSpace;
    public final long freeSpace;
    public final long impliedUsedSpace;
    public FileRecord root;
    public FileRecord freeSpaceRecord;
    
    public DriveRecord(final String path) {
        java.io.File rootFile = new java.io.File(path);
        this.path = rootFile.getAbsolutePath();
        totalSpace = rootFile.getTotalSpace();
        freeSpace = rootFile.getFreeSpace();
        impliedUsedSpace = totalSpace - freeSpace;
        root = new FileRecord(this.path, 0);
        freeSpaceRecord = new FileRecord("Free Space", freeSpace, 0);
    }
    public long getCalculatedUsedSpace() {
        return root.getSize();
    }
    public void insertFreeSpaceRecord() {
        root.children.add(freeSpaceRecord);
        root.addFreeSpaceToSize(freeSpaceRecord.getSize());
    }
    public void removeFreeSpaceRecord() {
        root.children.remove(freeSpaceRecord);
        root.removeFreeSpaceFromSize(freeSpaceRecord.getSize());
    }
}
