/*
 * Interface for passing a callback function. 
 * Used for the DiskRover class to pass to lower window objects, which then
 * instruct the entire window to refresh when needed. 
 */
package diskrover;

/**
 *
 * @author Kristofer
 */
public interface RefreshCallback {
    void refresh();
}
