package nl.zeesoft.games.illuminator;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Quad;

public class CharacterStatusBar extends Node {
    private AssetManager    assetManager    = null;
    private Character       character       = null;
    
    private Geometry        healthBar       = null;

    public CharacterStatusBar(AssetManager assetManager,Character character) {
        this.assetManager = assetManager;
        this.character = character;
    }
    
    public void initialize() {
        Quad q = new Quad(1.0f,0.1f);
        Geometry g = new Geometry("Background", q);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Black);
        g.setMaterial(mat);

        Quad q2 = new Quad(0.9f,0.08f);
        healthBar = new Geometry("HealthBar", q2);
        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.setColor("Color", ColorRGBA.Green);
        healthBar.setMaterial(mat2);
        healthBar.setLocalTranslation(0.05f,0.01f,0.01f);
        
        BillboardControl control = new BillboardControl();
        
        this.addControl(control);
        this.attachChild(g);
        this.attachChild(healthBar);
        
        character.attachChild(this);
    }

    public void setHealth(int health) {
        float factor = ((float)health / (float)character.getCharacterModel().maxHealth);
        healthBar.setLocalScale(factor,1.0f,1.0f);
    }
}
