package nl.zeesoft.games.illuminator;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import nl.zeesoft.games.illuminator.model.CharacterModel;

public class Opponent extends Character {
    public Opponent(CharacterModel characterModel,AssetManager assetManager) {
        super(characterModel,assetManager);
    }

    @Override
    public void initialize() {
        super.initialize();
        addRigidBody();
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