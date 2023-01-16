module ir.alamdari.battleship.battleship {
    requires javafx.controls;
    requires javafx.fxml;


    opens ir.alamdari.battleship to javafx.fxml;
    exports ir.alamdari.battleship;
    exports ir.alamdari.battleship.model;
    exports ir.alamdari.battleship.model.comminucations;

}