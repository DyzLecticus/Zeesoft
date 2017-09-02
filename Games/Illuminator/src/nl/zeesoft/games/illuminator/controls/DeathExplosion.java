package nl.zeesoft.games.illuminator.controls;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import nl.zeesoft.games.illuminator.GameControlNode;

public class DeathExplosion extends GameControlNode {
    private AssetManager        assetManager    = null;
    private ParticleEmitter[]   emitters        = new ParticleEmitter[3];
    private float               lifeTime        = 0.0f;
    private float               lifeTimeMax     = 1.0f;
    
    public DeathExplosion(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
    
    @Override
    public void initialize() {
        emitters[0] = getNewFlash(1,1.0f,true);
        emitters[1] = getNewFlame(1,1.0f,true);
        emitters[2] = getNewShockwave(1,1.0f);
        for (int i = 0; i < emitters.length; i++) {
            this.attachChild(emitters[i]);
        }
    }
    
    @Override
    public boolean update(float tpf) {
        boolean done = false;
        lifeTime += tpf;
        if (lifeTime>=lifeTimeMax) {
            stop();
            done = true;
        }
        return done;
    }

    private ParticleEmitter getNewFlash(int countFactor, float countFactorF,boolean pointSprite){
        Type type = Type.Point;
        if (!pointSprite) {
            type = Type.Triangle;
        }
        ParticleEmitter flash = new ParticleEmitter("Flash", type, 24 * countFactor);
        flash.setSelectRandomImage(true);
        flash.setStartColor(new ColorRGBA(0.0f,0.0f,1.0f, (float) (1f / countFactorF)));
        flash.setEndColor(new ColorRGBA(0.0f,0.0f,1.0f,0f));
        flash.setStartSize(0.3f);
        flash.setEndSize(5.0f);
        flash.setShape(new EmitterSphereShape(Vector3f.ZERO,0.05f));
        flash.setParticlesPerSec(0);
        flash.setGravity(0, 0, 0);
        flash.setLowLife(0.2f);
        flash.setHighLife(0.2f);
        flash.setImagesX(2);
        flash.setImagesY(2);
        Material mat = new Material(assetManager,"Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture",assetManager.loadTexture("Effects/Explosion/flash.png"));
        mat.setBoolean("PointSprite",pointSprite);
        flash.setMaterial(mat);
        return flash;
    }
 
    private ParticleEmitter getNewFlame(int countFactor, float countFactorF,boolean pointSprite){
        Type type = Type.Point;
        if (!pointSprite) {
            type = Type.Triangle;
        }
        ParticleEmitter flame = new ParticleEmitter("Flame", type, 32 * countFactor);
        flame.setSelectRandomImage(true);
        flame.setStartColor(new ColorRGBA(0.0f,0.0f,0.5f, (float) (1f / countFactorF)));
        flame.setEndColor(new ColorRGBA(0.0f,0.0f,0.1f,0f));
        flame.setStartSize(3.0f);
        flame.setEndSize(1.0f);
        flame.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
        flame.setParticlesPerSec(0);
        flame.setGravity(0,0,0);
        flame.setLowLife(0.5f);
        flame.setHighLife(0.5f);
        flame.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 7, 0));
        flame.getParticleInfluencer().setVelocityVariation(1f);
        flame.setImagesX(2);
        flame.setImagesY(2);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
        mat.setBoolean("PointSprite",pointSprite);
        flame.setMaterial(mat);
        return flame;
    }

    private ParticleEmitter getNewShockwave(int countFactor, float countFactorF){
        ParticleEmitter shockwave = new ParticleEmitter("Shockwave", ParticleMesh.Type.Triangle, 1 * countFactor);
        shockwave.setFaceNormal(Vector3f.UNIT_Y);
        shockwave.setStartColor(new ColorRGBA(0.0f,0.0f,1.0f,(float) (1.0f / countFactorF)));
        shockwave.setEndColor(new ColorRGBA(0.0f,0.0f,0.5f,0f));
        shockwave.setStartSize(0.1f);
        shockwave.setEndSize(6.0f);
        shockwave.setParticlesPerSec(0);
        shockwave.setGravity(0, 0, 0);
        shockwave.setLowLife(0.7f);
        shockwave.setHighLife(0.7f);
        shockwave.setImagesX(1);
        shockwave.setImagesY(1);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture",assetManager.loadTexture("Effects/Explosion/shockwave.png"));
        shockwave.setMaterial(mat);
        return shockwave;
    }
    
    public void start() {
        for (int i = 0; i < emitters.length; i++) {
            emitters[i].emitAllParticles();
        }
        lifeTime = 0.0f;
    }

    public void stop() {
        for (int i = 0; i < emitters.length; i++) {
            emitters[i].killAllParticles();
        }
        lifeTime = 0.0f;
    }
}
