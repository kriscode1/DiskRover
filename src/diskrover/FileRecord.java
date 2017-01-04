/*
 * FileRecord manages the details of a file/directory. 
Each record keeps track of its children, forming a large tree. 
 */
package diskrover;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Kristofer
 */
public class FileRecord implements Comparable<FileRecord> {
    final String path;
    final String name;
    private long size;
    final boolean isDirectory;
    List<FileRecord> children;
    final int depth;
    public static volatile boolean stopWork = false;
    //stopWork can be toggled to abort the constructor from a separate thread
    
    public FileRecord(final String path, final int depth) {
        File fileInfo = new File(path);
        this.path = fileInfo.getAbsolutePath();
        name = fileInfo.getName();
        size = 0;
        this.depth = depth;
        children = null;
        
        //Get child files and folders
        if (fileInfo.isDirectory()) {
            isDirectory = true;
            File[] contents = fileInfo.listFiles();
            if (contents != null) {
                children = new ArrayList<>();
                for (File child : contents) {
                    if (stopWork) return;//To exit this thread quickly and safely
                    children.add(new FileRecord(child.getAbsolutePath(), this.depth + 1));
                }
            }
        } else {
            isDirectory = false;
        }
        
        //Calculate size and increment RecordCounter
        if (children != null) {
            for (FileRecord child : children) {
                size += child.size;
            }
        }
        if (isDirectory) {
            RecordCounter.folderCount++;
        } else {
            size = fileInfo.length();
            RecordCounter.fileCount++;
            RecordCounter.sizeCounted += size;
        }
        
        //Sort children now, while progress bar is displayed
        if (children != null) {
            Collections.sort(children);
            Collections.reverse(children);
        }
    }
    public FileRecord(final String name, final long size, final int depth) {
        //For initializing invalid file records without a path
        //Example: free space
        //These need to be told their size, without a File object lookup
        this.name = name;
        this.size = size;
        this.depth = depth;
        path = "";
        children = null;
        isDirectory = false;
    }
    public long getSize() {
        return size;
    }
    public void addFreeSpaceToSize(long freeSpace) {
        size += freeSpace;
    }
    public void removeFreeSpaceFromSize(long freeSpace) {
        size -= freeSpace;
    }
    
    @Override
    public int compareTo(FileRecord other) {
        int sizeDifference = (int)(this.size - other.size);
        if (sizeDifference == 0) {
            return this.name.compareToIgnoreCase(other.name);
        }
        return sizeDifference;
    }
}
