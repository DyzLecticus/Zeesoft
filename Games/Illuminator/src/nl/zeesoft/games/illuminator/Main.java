package nl.zeesoft.games.illuminator;

import nl.zeesoft.games.illuminator.model.GameModel;

/**
 * This class is used to test the game.
 */
public class Main {
    public static void main(String[] args) {
        GameModel gameModel = new GameModel();
        gameModel.initialize();
        gameModel.setGodMode(true);
        gameModel.setDebug(true);
        Game game = new Game(gameModel);
        game.start();
    }
}
