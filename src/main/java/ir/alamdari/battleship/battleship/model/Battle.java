package ir.alamdari.battleship.battleship.model;

import java.io.Serializable;
import java.util.List;

public class Battle implements Serializable {
    private String playerOneName;
    private String playerTwoName;
    private List<Ship> ships;
    private int[][] area = new int[10][20];
}
