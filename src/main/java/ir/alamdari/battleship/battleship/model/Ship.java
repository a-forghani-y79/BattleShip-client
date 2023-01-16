package ir.alamdari.battleship.battleship.model;

import java.io.Serializable;
import java.util.Objects;

public class Ship implements Serializable {

    private int id;
    private int length;
    private String name;

    private String color;

    public Ship(int id, int length, String name, String color) {
        this.id = id;
        this.length = length;
        this.name = name;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "id=" + id +
                ", length=" + length +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ship ship = (Ship) o;
        return length == ship.length && name.equals(ship.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(length, name);
    }
}
