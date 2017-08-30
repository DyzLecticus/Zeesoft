package nl.zeesoft.games.illuminator.controls;

import com.jme3.math.Vector3f;
import java.util.List;
import nl.zeesoft.games.illuminator.GameControlNode;

public interface PlayerSpellProvider {

    public List<GameControlNode> initializeSpellObjects(String spellName, Vector3f location);
    
    public void releaseSpellObjects(List<GameControlNode> objects);
}
