module ir.alamdari.battleship.battleship {
    requires javafx.controls;
    requires javafx.fxml;


    opens ir.alamdari.battleship.battleship to javafx.fxml;
    exports ir.alamdari.battleship.battleship;
    exports ir.alamdari.battleship.battleship.model;

}