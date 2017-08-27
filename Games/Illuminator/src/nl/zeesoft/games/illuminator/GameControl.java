package nl.zeesoft.games.illuminator;

import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Node;

/**
 * Basic game control interface.
 */
public interface GameControl {

    /*
     * Initialize the spatial
     */
    public void initialize();
    
    /*
     * Update the spatial.
     *
     * @param tpf Time per frame
     * @return True if the object can be detached from the rootNode
     */
    public boolean update(float tpf);
    
    /*
     * Attach the object to the root node.
     * 
     * @param rootNode The root node
     * @param bulletAppState The optional bullet app state
     */
    public void attachToRootNode(Node rootNode,BulletAppState bulletAppState);
    
    /*
     * Detach the object from the root node.
     * 
     * @param rootNode The root node
     * @param bulletAppState The optional bullet app state
     */
    public void detachFromRootNode(Node rootNode,BulletAppState bulletAppState);
}
