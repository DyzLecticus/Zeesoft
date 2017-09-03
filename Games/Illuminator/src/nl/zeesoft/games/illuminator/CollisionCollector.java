package nl.zeesoft.games.illuminator;

import com.jme3.collision.CollisionResults;
import nl.zeesoft.games.illuminator.controls.GameCharacter;

public interface CollisionCollector {
    
    public CollisionResults getAttackCollisions(GameCharacter character,int attacking);
}
