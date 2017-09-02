package nl.zeesoft.games.illuminator.controls;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import nl.zeesoft.games.illuminator.GameControlNode;

public class SceneFlame extends GameControlNode {
    private static final float  SIZE            = 1.0f;
    
    private AssetManager        assetManager    = null;
    private ParticleEmitter     flare           = null;
    
    private PointLight          glow            = null;
    
    private float               burst           = 0.0f;

    
    public SceneFlame(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
    
    @Override
    public void initialize() {
        flare = getNewFlame(1,1.0f,true);
        this.attachChild(flare);
                
        glow = new PointLight();
        glow.setColor(new ColorRGBA(1.0f,0.9f,0.6f,1.0f));
        glow.setRadius(SIZE * 100f);
    }

    @Override
    public boolean update(float tpf) {
        burst += tpf;
        if (burst>=0.1f) {
            burst = 0.0f;
            flare.emitAllParticles();
        }
        return false;
    }
    
    @Override
    public void attachToRootNode(Node rootNode,BulletAppState bulletAppState) {
        super.attachToRootNode(rootNode, bulletAppState);
        glow.setPosition(getLocalTranslation());
        rootNode.addLight(glow);
    }
    
    @Override
    public void detachFromRootNode(Node rootNode,BulletAppState bulletAppState) {
        super.detachFromRootNode(rootNode, bulletAppState);
        rootNode.removeLight(glow);
    }
    
    private ParticleEmitter getNewFlame(int countFactor, float countFactorF,boolean pointSprite){
        ParticleMesh.Type type = ParticleMesh.Type.Point;
        if (!pointSprite) {
            type = ParticleMesh.Type.Triangle;
        }
        ParticleEmitter flame = new ParticleEmitter("Flame", type, 32 * countFactor);
        flame.setSelectRandomImage(true);
        flame.setRandomAngle(true);
        flame.setStartColor(new ColorRGBA(0.5f,0.4f,0.2f, (float) (1f / countFactorF)));
        flame.setEndColor(new ColorRGBA(0.02f,0.01f,0.01f,0f));
        flame.setStartSize(SIZE);
        flame.setEndSize(0.01f);
        flame.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
        flame.setParticlesPerSec(0);
        flame.setGravity(0,0,0);
        flame.setLowLife(0.5f);
        flame.setHighLife(1.5f);
        flame.getParticleInfluencer().setInitialVelocity(new Vector3f(0,5f,0));
        flame.getParticleInfluencer().setVelocityVariation(0.2f);
        flame.setImagesX(2);
        flame.setImagesY(2);
        Material mat = new Material(assetManager,"Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture",assetManager.loadTexture("Effects/Explosion/flame.png"));
        mat.setBoolean("PointSprite",pointSprite);
        flame.setMaterial(mat);
        return flame;
    }
}
