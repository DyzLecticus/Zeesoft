package nl.zeesoft.games.illuminator;

import com.jme3.math.Vector3f;
import nl.zeesoft.games.illuminator.model.CharacterModel;

public class Opponent extends Character {
    public Opponent(CharacterModel characterModel) {
        super(characterModel);
    }

    @Override
    public Vector3f getDirection() {
        return new Vector3f();
    }

    @Override
    public Vector3f getLeft() {
        return new Vector3f();
    }
}