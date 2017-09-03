package nl.zeesoft.games.illuminator.controls;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Quad;

public class CharacterStatusBar extends Node {
    private AssetManager    assetManager    = null;
    private GameCharacter       character       = null;
    
    private boolean         showMana        = false;
    
    private Geometry        healthBar       = null;
    private Geometry        manaBar         = null;

    public CharacterStatusBar(AssetManager assetManager,GameCharacter character) {
        this.assetManager = assetManager;
        this.character = character;
        if (character instanceof Player) {
            showMana = true;
        }
    }
    
    public void initialize() {
        float h = 0.1f;
        if (showMana) {
            h = 0.2f;
        }
        Quad q = new Quad(1.0f,h);
        Geometry g = new Geometry("Background", q);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Black);
        g.setMaterial(mat);

        Quad q2 = new Quad(0.96f,0.06f);
        healthBar = new Geometry("HealthBar", q2);
        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.setColor("Color", ColorRGBA.Green);
        healthBar.setMaterial(mat2);
        healthBar.setLocalTranslation(0.02f,0.02f,0.01f);

        Quad q3 = new Quad(0.96f,0.06f);
        manaBar = new Geometry("ManaBar", q3);
        Material mat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat3.setColor("Color", ColorRGBA.Blue);
        manaBar.setMaterial(mat3);
        manaBar.setLocalTranslation(0.02f,0.12f,0.01f);
        
        BillboardControl control = new BillboardControl();
        
        this.addControl(control);
        this.attachChild(g);
        this.attachChild(healthBar);
        if (showMana) {
            this.attachChild(manaBar);
        }
        
        character.attachChild(this);
    }

    public void setHealth(int health) {
        float factor = ((float)health / (float)character.getCharacterModel().maxHealth);
        healthBar.setLocalScale(factor,1.0f,1.0f);
    }

    public void setMana(int mana) {
        float factor = ((float)mana / (float)character.getCharacterModel().maxMana);
        manaBar.setLocalScale(factor,1.0f,1.0f);
    }
}
