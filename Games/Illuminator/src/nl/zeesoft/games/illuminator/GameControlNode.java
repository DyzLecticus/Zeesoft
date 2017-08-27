package nl.zeesoft.games.illuminator;

import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Node;

/**
 * Abstract game node control class.
 */
public abstract class GameControlNode extends Node implements GameControl {
    @Override
    public void attachToRootNode(Node rootNode,BulletAppState bulletAppState) {
        rootNode.attachChild(this);
        if (bulletAppState!=null) {
            bulletAppState.getPhysicsSpace().add(this);
        }
    }
    
    @Override
    public void detachFromRootNode(Node rootNode,BulletAppState bulletAppState) {
        rootNode.detachChild(this);
        if (bulletAppState!=null) {
            bulletAppState.getPhysicsSpace().remove(this);
        }
    }
}
