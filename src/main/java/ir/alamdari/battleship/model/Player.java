package ir.alamdari.battleship.model;

import java.io.Serializable;

public class Player implements Serializable {
    public static final long serialVersionUID = 98765678L;
    private String name;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
