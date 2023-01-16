package ir.alamdari.battleship.battleship.model.comminucations;

import ir.alamdari.battleship.battleship.model.Player;

import java.io.Serializable;

public class Response implements Serializable {
    private Player to;
    private int responseType;
    private Object data;
}
