package ir.alamdari.battleship.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Battle implements Serializable {
    public static final long serialVersionUID = 45634327289L;

    private Player playerOne;
    private Player playerTwo;
    private List<Ship> ships;
    private int[][] playerOneArea;
    private int[][] playerTwoArea;

    public Battle(Player playerOne, Player playerTwo, List<Ship> ships, int[][] playerOneArea, int[][] playerTwoArea) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.ships = ships;
        this.playerOneArea = playerOneArea;
        this.playerTwoArea = playerTwoArea;
    }

    public Battle() {
    }
    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    public int[][] getPlayerOneArea() {
        return playerOneArea;
    }

    public void setPlayerOneArea(int[][] playerOneArea) {
        this.playerOneArea = playerOneArea;
    }

    public int[][] getPlayerTwoArea() {
        return playerTwoArea;
    }

    public void setPlayerTwoArea(int[][] playerTwoArea) {
        this.playerTwoArea = playerTwoArea;
    }

    @Override
    public String toString() {
        return "Battle{" +
                "playerOne=" + playerOne.toString() +
                ", playerTwo=" + playerTwo.toString() +
                ", ships=" + ships +
                ", playerOneArea=" + Arrays.toString(playerOneArea) +
                ", playerTwoArea=" + Arrays.toString(playerTwoArea) +
                '}';
    }
}
