package ir.alamdari.battleship;

import ir.alamdari.battleship.dataHolder.DataHolder;
import ir.alamdari.battleship.model.Player;
import ir.alamdari.battleship.model.Ship;
import ir.alamdari.battleship.model.comminucations.Request;
import ir.alamdari.battleship.model.comminucations.Response;
import ir.alamdari.battleship.model.comminucations.Type;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class PlanningController {

    @FXML
    public Text welcomeText ;
    private String ip;
    private int port;
    private Timeline timeline;

    private String playerName;

    @FXML
    public Button btnNext;
    private Ship selectedShip;

    private int[][] myArea;

    @FXML
    public ListView<Ship> listView;

    @FXML
    public GridPane gridPane;

    @FXML
    public void initialize() {

        ip = DataHolder.getInstance().getData().get("ip");
        port = Integer.parseInt(DataHolder.getInstance().getData().get("port"));
        playerName = DataHolder.getInstance().getData().get("name");
        welcomeText.setText("Welcome "+playerName);
        myArea = new int[10][10];
//        printArray(myArea);

        gridPane.getColumnConstraints().forEach(columnConstraints -> columnConstraints.setPrefWidth(40));
        gridPane.getRowConstraints().forEach(rowConstraints -> rowConstraints.setPrefHeight(40));
        gridPane.setPrefHeight(400);
        gridPane.setPrefWidth(400);
//        Button[][] area = new Button[10][10];
        Button button;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                button = initButton(i, j);
                button.setOnMouseClicked(event -> onClickButton((Button) event.getSource()));
                gridPane.add(button, j, i);
                //     area[i][j] = button;
            }
        }

        List<Ship> ships = requestAndGetShips();


        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            public void updateSelected(boolean selected) {
                super.updateSelected(selected);
                if (selected) {
                    setFocused(true);
                }
            }

            @Override
            protected void updateItem(Ship item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else if (item != null) {
                    setText(item.getName() + " Len:" + item.getLength());
                    //setTextFill(Color.web(item.getColor()));
                    setTextAlignment(TextAlignment.CENTER);
                    setGraphic(new Circle(10, Paint.valueOf(item.getColor())));
                } else {
                    setText(null);
                    setGraphic(null);
                }

            }
        });
        listView.getItems().addAll(ships);


        listView.setOnMouseClicked(event -> selectedShip = listView.getSelectionModel().getSelectedItem());


        btnNext.setOnMouseClicked(event -> {

            if (join()){
                btnNext.setDisable(true);
                btnNext.setText("Waiting for Opponent ...");

                timeline = new Timeline(new KeyFrame(Duration.seconds(1),
                        event1 -> {
                    if (isOpponentReady()){
                        letsFight();
                    }
                }));
                timeline.setCycleCount(200);
                timeline.play();
            }

        });

    }

    void onClickButton(Button button) {
        if (selectedShip != null) {
            int x = button.getId().charAt(3) - '0';
            int y = button.getId().charAt(4) - '0';

            for (int i = 0; i < selectedShip.getLength(); i++) {
                myArea[x+i][y ] = selectedShip.getId();
                for (Node child : gridPane.getChildren()) {
                    if (child.getId().equals("btn" + (x+i) + "" + (y))) {
                        child.setStyle("-fx-background-color: " + selectedShip.getColor());
                        child.setDisable(true);
                    }
                }
            }
//            printArray(myArea);

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

    private void printArray(int[][] array) {
        System.out.println("-------------------");
        for (int[] ints : array) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
        System.out.println("-------------------");
    }

    private List<Ship> requestAndGetShips() {
        List<Ship> ships = new ArrayList<>();
        SocketAddress socketAddress = new InetSocketAddress(ip, port);
        Socket socket = new Socket();
        // Timeout required - it's in milliseconds
        int timeout = 500;
        try {
            socket.connect(socketAddress, timeout);
            ObjectOutputStream oos =
                    new ObjectOutputStream(socket.getOutputStream());

            Request request = new Request();
            request.setRequestType(Type.GET_SHIPS);
            request.setData(null);
            request.setFrom(new Player(playerName));

            oos.writeObject(request);

            ObjectInputStream objectInputStream =
                    new ObjectInputStream(socket.getInputStream());

            Response response = (Response) objectInputStream.readObject();

            ships = (List<Ship>) response.getData();

            socket.close();
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        return ships;
    }

    private boolean join(){
        boolean join = false;
        SocketAddress socketAddress = new InetSocketAddress(ip, port);
        Socket socket = new Socket();
        // Timeout required - it's in milliseconds
        int timeout = 500;
        try {
            socket.connect(socketAddress, timeout);
            ObjectOutputStream oos =
                    new ObjectOutputStream(socket.getOutputStream());

            Request request = new Request();
            request.setRequestType(Type.JOIN_REQUEST);
            request.setData(myArea);
            request.setFrom(new Player(playerName));

            oos.writeObject(request);

            ObjectInputStream objectInputStream =
                    new ObjectInputStream(socket.getInputStream());

            Response response = (Response) objectInputStream.readObject();

            join = (boolean) response.getData();

            socket.close();
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        return join;
    }

    private boolean isOpponentReady(){
        boolean ready = false;
        SocketAddress socketAddress = new InetSocketAddress(ip, port);
        Socket socket = new Socket();
        // Timeout required - it's in milliseconds
        int timeout = 500;
        try {
            socket.connect(socketAddress, timeout);
            ObjectOutputStream oos =
                    new ObjectOutputStream(socket.getOutputStream());

            Request request = new Request();
            request.setRequestType(Type.OPPONENT_JOIN_CHECK);
            request.setData(null);
            request.setFrom(new Player(playerName));

            oos.writeObject(request);

            ObjectInputStream objectInputStream =
                    new ObjectInputStream(socket.getInputStream());

            Response response = (Response) objectInputStream.readObject();

            ready = (boolean) response.getData();

            socket.close();
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        return ready;
    }

    private void letsFight(){


        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(
                    "battle.fxml"));
            root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Battle");
            stage.setScene(new Scene(root));
            timeline.stop();

            stage.show();
            // Hide this current window (if this is what you want)
            gridPane.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}