package ir.alamdari.battleship.battleship;

import ir.alamdari.battleship.battleship.model.Ship;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class HelloController {

    private Ship selectedShip;

    private int [][]myArea;

    @FXML
    public ListView<Ship> listView;

    @FXML
    public GridPane gridPane;

    @FXML
    public void initialize() {

        myArea = new int[10][10];
        printArray(myArea);

        gridPane.getColumnConstraints().forEach(columnConstraints -> columnConstraints.setPrefWidth(50));
        gridPane.getRowConstraints().forEach(rowConstraints -> rowConstraints.setPrefHeight(50));
        gridPane.setPrefHeight(500);
        gridPane.setPrefWidth(500);
//        Button[][] area = new Button[10][10];
        Button button;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                button = initButton(i,j);
                button.setOnMouseClicked(event -> onClickButton((Button) event.getSource()));
                gridPane.add(button, j, i);
           //     area[i][j] = button;
            }
        }

        List<Ship> ships = new ArrayList<>();
        ships.add(new Ship(1,3, "Nav jangi","#FF00FF"));
        ships.add(new Ship(2,2, "kashti 2","#FF00FF"));
        ships.add(new Ship(3,2, "kashti 1","#FF00FF"));
        ships.add(new Ship(4,1, "ghayegh 1","#FF00FF"));
        ships.add(new Ship(5, 1, "ghayegh 2","#FF00FF"));


        listView.setCellFactory(param -> new ListCell<>(){
            @Override
            public void updateSelected(boolean selected) {
                super.updateSelected(selected);
                if (selected){
                    setFocused(true);
                }
            }

            @Override
            protected void updateItem(Ship item, boolean empty) {
                super.updateItem(item,empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                }else if (item != null){
                    setText(item.getName() + " Len:" +item.getLength());
                    //setTextFill(Color.web(item.getColor()));
                    setTextAlignment(TextAlignment.CENTER);
                    setGraphic(new Circle(10, Paint.valueOf(item.getColor())));
                }
                else {
                    setText(null);
                    setGraphic(null);
                }

            }
        });
        listView.getItems().addAll(ships);


        listView.setOnMouseClicked(event -> selectedShip = listView.getSelectionModel().getSelectedItem());
    }

    void onClickButton(Button button){
        if (selectedShip != null){
            int x = button.getId().charAt(3)-'0';
            int y = button.getId().charAt(4)-'0';

            for (int i = 0; i < selectedShip.getLength(); i++) {
                myArea[x][y+i]= selectedShip.getId();
                for (Node child : gridPane.getChildren()) {
                    if (child.getId().equals("btn"+x+""+(y+i))) {
                        child.setStyle("-fx-background-color: "+selectedShip.getColor());
                        child.setDisable(true);
                    }
                }
            }
            printArray(myArea);

            listView.getItems().remove(selectedShip);
            selectedShip = null;
        }
    }


    Button initButton(int i, int j) {
        Button button = new Button("");
        button.setId("btn" + i + j);
        button.setPrefHeight(50);
        button.setPrefWidth(50);
        return button;
    }
    
    private void printArray(int[][] array){
        System.out.println("-------------------");
        for (int[] ints : array) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
        System.out.println("-------------------");
    }

}