package nl.zeesoft.games.illuminator.model;

/**
 * Default player model.
 */
public class PlayerModel extends CharacterModel {
    public PlayerModel() {
        attackDamages.clear();
        attackDamages.add(5);
        attackDamages.add(10);
        attackDamages.add(20);
    }
}
