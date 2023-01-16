package ir.alamdari.battleship.model.comminucations;

import ir.alamdari.battleship.model.Player;

import java.io.Serializable;

public class Request implements Serializable {
    public static final long serialVersionUID = 4212134L;
    private Player from;
    private Type requestType;
    private Object data;

    public Request(Player from, Type requestType, Object data) {
        this.from = from;
        this.requestType = requestType;
        this.data = data;
    }

    public Request() {
    }

    public Type getRequestType() {
        return requestType;
    }

    public void setRequestType(Type requestType) {
        this.requestType = requestType;
    }

    public Player getFrom() {
        return from;
    }

    public void setFrom(Player from) {
        this.from = from;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
