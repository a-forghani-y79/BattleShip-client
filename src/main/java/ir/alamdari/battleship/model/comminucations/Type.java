package ir.alamdari.battleship.model.comminucations;

public enum Type {
    CONNECTION_CHECKING(1), GET_SHIPS(2), JOIN_REQUEST(3), OPPONENT_JOIN_CHECK(4),

    IS_MY_TURN(5),

    GET_WINNER(6),

    GET_LAST_STATE_BATTLE(7), SHOOT(8);

    Type(int code) {
        this.code = code;
    }

    private final int code;

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "Type{" + "code=" + code + "} " + super.toString();
    }
}
