package ir.alamdari.battleship;

import ir.alamdari.battleship.model.Ship;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ListViewCellFactory implements Callback<ListView<Ship>,
        ListCell<Ship>> {
    @Override
    public ListCell<Ship> call(ListView<Ship> param) {
        return new ListCell<>(){
            @Override
            protected void updateItem(Ship item, boolean empty) {
                super.updateItem(item, empty);
            }
        };
    }
}
