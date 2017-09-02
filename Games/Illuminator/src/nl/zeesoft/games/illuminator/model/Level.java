package nl.zeesoft.games.illuminator.model;

public class Level {
    public int      opponents   = 3;
    public int      toughGuys   = 0;
    public int      goal        = 10;
    
    public Level(int opponents,int toughGuys, int goal) {
        this.opponents = opponents;
        this.toughGuys = toughGuys;
        this.goal = 10;
    }
}
