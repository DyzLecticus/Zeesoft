package nl.zeesoft.games.illuminator;

import java.util.List;
import nl.zeesoft.games.illuminator.controls.GameCharacter;

public interface AttackHandler {
    
    public List<GameCharacter> getAttackCollisions(GameCharacter character,int attacking);
}
