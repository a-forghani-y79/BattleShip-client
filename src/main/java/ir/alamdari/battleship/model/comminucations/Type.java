package ir.alamdari.battleship.model.comminucations;

public enum Type {
    CONNECTION_CHECKING(1)

    ;

    Type(int code) {
        this.code = code;
    }

    private final int code;

    public int getCode() {
        return code;
    }
}
