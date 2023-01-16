package ir.alamdari.battleship.model.comminucations;


import ir.alamdari.battleship.model.Player;

import java.io.Serializable;

public class Response implements Serializable {
    public static final long serialVersionUID = 45637289L;
    private Player to;
    private Type responseType;
    private Object data;
    private String message;

    public Response(Player to, Type responseType, Object data, String message) {
        this.to = to;
        this.responseType = responseType;
        this.data = data;
        this.message = message;
    }


    public Response() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Player getTo() {
        return to;
    }

    public void setTo(Player to) {
        this.to = to;
    }

    public Type getResponseType() {
        return responseType;
    }

    public void setResponseType(Type responseType) {
        this.responseType = responseType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "to=" + to.toString() +
                ", responseType=" + responseType +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
