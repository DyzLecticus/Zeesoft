package nl.zeesoft.games.illuminator;

import com.jme3.math.Vector3f;
import java.util.List;

public interface SpellObjectProvider {

    public List<GameControlNode> initializeSpellObjects(String spellName, Vector3f location);
    
    public void releaseSpellObjects(List<GameControlNode> objects);
}
