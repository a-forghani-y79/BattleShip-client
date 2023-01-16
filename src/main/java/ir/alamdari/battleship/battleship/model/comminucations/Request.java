package ir.alamdari.battleship.battleship.model.comminucations;

import ir.alamdari.battleship.battleship.model.Player;

import java.io.Serializable;

public class Request implements Serializable {
    private Player from;
    private int requestType;
    private Object data;

    public Request(Player from, int requestType, Object data) {
        this.from = from;
        this.requestType = requestType;
        this.data = data;
    }

    public Player getFrom() {
        return from;
    }

    public void setFrom(Player from) {
        this.from = from;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
