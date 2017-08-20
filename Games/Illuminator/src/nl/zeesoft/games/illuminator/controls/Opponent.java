package nl.zeesoft.games.illuminator.controls;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import nl.zeesoft.games.illuminator.model.CharacterModel;
import nl.zeesoft.games.illuminator.model.OpponentModel;

public class Opponent extends Character {
    private ParticleEmitter     shockWave       = null;

    private boolean             upDo            = false;
    private float               upDelayed       = 0.0f;
    
    public Opponent(CharacterModel characterModel,AssetManager assetManager) {
        super(characterModel,assetManager);
    }

    @Override
    public void initialize() {
        super.initialize();
        addRigidBody();
        addShockwave();
    }
    
    @Override
    protected void startShockWave(int impacting) {
        if (shockWave!=null) {
            shockWave.killAllParticles();
            shockWave.emitAllParticles();
        }
    }

    @Override
    protected void stopShockWave() {
        if (shockWave!=null) {
            shockWave.killAllParticles();
        }
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
    protected float getAttackDelay() {
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

    private void addShockwave() {
        shockWave = this.getNewShockwave(1,1.0f);
        getCharacterModel().attachParticleEmitterToHead(shockWave);
    }

    private ParticleEmitter getNewShockwave(int countFactor, float countFactorF){
        ParticleEmitter shockwave = new ParticleEmitter("Shockwave", ParticleMesh.Type.Triangle, 1 * countFactor);
        shockwave.setFaceNormal(Vector3f.UNIT_Y);
        shockwave.setStartColor(new ColorRGBA(0.0f,0.0f,1.0f,(float) (1.0f / countFactorF)));
        shockwave.setEndColor(new ColorRGBA(0.0f,0.0f,1.0f,0f));
        shockwave.setStartSize(0.1f);
        shockwave.setEndSize(3.0f);
        shockwave.setParticlesPerSec(0);
        shockwave.setGravity(0, 0, 0);
        shockwave.setLowLife(0.5f);
        shockwave.setHighLife(0.5f);
        shockwave.setImagesX(1);
        shockwave.setImagesY(1);
        Material mat = new Material(getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture",getAssetManager().loadTexture("Effects/Explosion/shockwave.png"));
        shockwave.setMaterial(mat);
        return shockwave;
    }
}