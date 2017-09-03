package nl.zeesoft.games.illuminator.model;

import com.jme3.math.Vector3f;

/**
 * Default opponent model.
 */
public class OpponentModelTough extends OpponentModel {
    public OpponentModelTough() {
        scale = 0.21f;
        radius = 0.55f;
        height = 1.9f;
        translation = new Vector3f(0.0f,-1.5f,0.0f);
        
        attackDamages.clear();
        attackDamages.add(8);
        attackDamages.add(14);
        attackDamages.add(20);
        
        spellDamages.clear();
        spellDamages.add(34);
    }
}
