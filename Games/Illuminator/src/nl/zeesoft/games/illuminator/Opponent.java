package nl.zeesoft.games.illuminator;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import nl.zeesoft.games.illuminator.model.CharacterModel;
import nl.zeesoft.games.illuminator.model.OpponentModel;

public class Opponent extends Character {
    private boolean     upDo            = false;
    private float       upDelayed       = 0.0f;
    private boolean     attackDo        = false;
    private float       attackDelayed   = -1.0f;
    
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
        Vector3f dir = getCharacterControl().getViewDirection().negate();
        dir.y = 0;
        return dir;
    }

    @Override
    public Vector3f getLeft() {
        Vector3f left = getCharacterControl().getViewDirection();
        left.y = 0;
        return left;
    }

    @Override
    public void setUp(boolean v) {
        if (v) {
            upDo = true;
        } else {
            super.setUp(v);
            upDo = false;
            upDelayed = 0.0f;
        }
    }

    @Override
    public float getAttackDelay() {
        return getOpponentModel().attackDelay;
    }

    @Override
    public void update(float tpf) {
        if (upDo) {
            //System.out.print(upDelayed);
            upDelayed = upDelayed + tpf;
        }
        if (upDelayed>=getOpponentModel().upDelay) {
            super.setUp(true);
        }
        super.update(tpf);
    }
    
    public OpponentModel getOpponentModel() {
        return (OpponentModel) getCharacterModel();
    }
}