package ir.alamdari.battleship.battleship.model;

import java.io.Serializable;
import java.util.List;

public class Battle implements Serializable {
    private String playerOneName;
    private String playerTwoName;
    private List<Ship> ships;
    private int[][] area;

    public Battle(String playerOneName, String playerTwoName, List<Ship> ships, int[][] area) {
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;
        this.ships = ships;
        this.area = area;
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public void setPlayerOneName(String playerOneName) {
        this.playerOneName = playerOneName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }

    public void setPlayerTwoName(String playerTwoName) {
        this.playerTwoName = playerTwoName;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    public int[][] getArea() {
        return area;
    }

    public void setArea(int[][] area) {
        this.area = area;
    }
}
