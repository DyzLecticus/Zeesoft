package nl.zeesoft.games.illuminator.controls;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class DeathExplosion extends Node {
    private AssetManager    assetManager    = null;
    private ParticleEmitter e1             = null;
    private ParticleEmitter e2             = null;
    private float           time            = -1.0f;
    
    public DeathExplosion(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
    
    public void initialize() {
        e1 = getNewFlash(1,1.0f,true);
        e2 = getNewFlame(1,1.0f,true);
        this.attachChild(e1);
        this.attachChild(e1);
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
        flash.setEndSize(3.0f);
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
        flame.setStartColor(new ColorRGBA(0.0f,0.0f,0.1f, (float) (1f / countFactorF)));
        flame.setEndColor(new ColorRGBA(0.0f,0.0f,0.1f,0f));
        flame.setStartSize(1.0f);
        flame.setEndSize(2f);
        flame.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
        flame.setParticlesPerSec(0);
        flame.setGravity(0,-5,0);
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

    public void start() {
        e1.emitAllParticles();
        e2.emitAllParticles();
        time = 0.0f;
    }

    public void stop() {
        e1.killAllParticles();
        e2.killAllParticles();
        time = 0.0f;
    }
    
    public boolean update(float tpf) {
        boolean done = false;
        time = time + tpf;
        if (time>=1.0f) {
            stop();
            done = true;
        }
        return done;
    }
}
