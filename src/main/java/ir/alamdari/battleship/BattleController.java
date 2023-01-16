package ir.alamdari.battleship;

import ir.alamdari.battleship.dataHolder.DataHolder;
import ir.alamdari.battleship.model.Battle;
import ir.alamdari.battleship.model.Player;
import ir.alamdari.battleship.model.Ship;
import ir.alamdari.battleship.model.comminucations.Request;
import ir.alamdari.battleship.model.comminucations.Response;
import ir.alamdari.battleship.model.comminucations.Type;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BattleController {

    @FXML
    public Text myNameText;
    @FXML
    public Text opponentNameText;
    @FXML
    public GridPane myGrid;
    @FXML
    public GridPane opponentGrid;
    @FXML
    public Text winnerText;

    private Timeline timeline;
    private String ip;
    private int port;

    private String playerName;

    private List<Ship> ship;

    private Player winner;


    @FXML
    public void initialize() {
        ip = DataHolder.getInstance().getData().get("ip");
        port = Integer.parseInt(DataHolder.getInstance().getData().get("port"));
        ship = requestAndGetShips();
        playerName = DataHolder.getInstance().getData().get("name");
        winnerText.setText("");


        Battle battle = getLastStateOfBattle();


        if (battle.getPlayerOne().getName().equals(playerName)) {
            myNameText.setText(battle.getPlayerOne().getName());
            opponentNameText.setText(battle.getPlayerTwo().getName());
        } else {
            opponentNameText.setText(battle.getPlayerOne().getName());
            myNameText.setText(battle.getPlayerTwo().getName());
        }

        myGrid.getColumnConstraints().forEach(columnConstraints -> columnConstraints.setPrefWidth(40));
        myGrid.getRowConstraints().forEach(rowConstraints -> rowConstraints.setPrefHeight(40));
        myGrid.setPrefHeight(400);
        myGrid.setPrefWidth(400);

        opponentGrid.getColumnConstraints().forEach(columnConstraints -> columnConstraints.setPrefWidth(40));
        opponentGrid.getRowConstraints().forEach(rowConstraints -> rowConstraints.setPrefHeight(40));
        opponentGrid.setPrefHeight(400);
        opponentGrid.setPrefWidth(400);

//        System.out.println("playerName = " + playerName);
//        System.out.println("battle = " + battle.toString());
//
//        System.out.println("Player One");
//        printArray(battle.getPlayerOneArea());
//
//        System.out.println("Player two");
//        printArray(battle.getPlayerTwoArea());


        initAreas(battle);

        timeline = new Timeline(new KeyFrame(Duration.seconds(1),
                event1 -> {
                    updateAreas(getLastStateOfBattle());
                    if (isMyTurn()) {
                        opponentGrid.setDisable(false);
                    } else {
                        opponentGrid.setDisable(true);
                    }
                    winner = getWinner();

                    if (winner != null) {
                        endGame(winner);
                    }
                    timeline.setCycleCount(1000);
                }));
        int cycleCount = 10000;
        timeline.setCycleCount(cycleCount);
        timeline.play();

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

    private void endGame(Player winner) {
        if (winner.getName().equals(playerName))
            winnerText.setText("You Win !!!");
        else winnerText.setText("You Lose :(");
    }

    private void updateAreas(Battle battle) {
        opponentGrid.getChildren().clear();
        myGrid.getChildren().clear();

        initAreas(battle);

    }

    private boolean isMyTurn() {
        boolean turn = false;
        SocketAddress socketAddress = new InetSocketAddress(ip, port);
        Socket socket = new Socket();
        // Timeout required - it's in milliseconds
        int timeout = 500;
        try {
            socket.connect(socketAddress, timeout);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            Request request = new Request();
            request.setRequestType(Type.IS_MY_TURN);
            request.setFrom(new Player(playerName));

            oos.writeObject(request);

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            Response response = (Response) objectInputStream.readObject();

            turn = (boolean) response.getData();

            socket.close();
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        return turn;
    }

    private void onClickButton(Button button) {
        int x = button.getId().charAt(3) - '0';
        int y = button.getId().charAt(4) - '0';
        SocketAddress socketAddress = new InetSocketAddress(ip, port);
        Socket socket = new Socket();
        // Timeout required - it's in milliseconds
        int timeout = 500;
        try {
            socket.connect(socketAddress, timeout);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            Request request = new Request();
            request.setRequestType(Type.SHOOT);
            request.setData(new int[]{x, y});
            request.setFrom(new Player(playerName));

            oos.writeObject(request);

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            Response response = (Response) objectInputStream.readObject();

            socket.close();
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private Player getWinner() {

        Player winner = null;
        SocketAddress socketAddress = new InetSocketAddress(ip, port);
        Socket socket = new Socket();
        // Timeout required - it's in milliseconds
        int timeout = 500;
        try {
            socket.connect(socketAddress, timeout);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            Request request = new Request();
            request.setRequestType(Type.GET_WINNER);
            request.setData(null);
            request.setFrom(new Player(playerName));

            oos.writeObject(request);

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            Response response = (Response) objectInputStream.readObject();

            winner = (Player) response.getData();

            socket.close();
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        return winner;
    }


    private void initAreas(Battle battle) {
        if (battle != null) {
            Button button;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {

                    if (playerName.equals(battle.getPlayerOne().getName())) {
                        button = initButton(i, j, battle.getPlayerOneArea()[i][j], false);
                        myGrid.add(button, j, i);
                        button = initButton(i, j, battle.getPlayerTwoArea()[i][j], true);
                        button.setOnMouseClicked(event -> onClickButton((Button) event.getSource()));
                        opponentGrid.add(button, j, i);
                    } else {
                        button = initButton(i, j, battle.getPlayerTwoArea()[i][j], false);
                        myGrid.add(button, j, i);
                        button = initButton(i, j, battle.getPlayerOneArea()[i][j], true);
                        button.setOnMouseClicked(event -> onClickButton((Button) event.getSource()));
                        opponentGrid.add(button, j, i);

                    }
                }
            }
        }
    }


    Button initButton(int i, int j, int type, boolean isOpponent) {
        //type 0 for water | -1 for destroyed ship cell
        Button button = new Button("");
        button.setId("btn" + i + j);
        button.setFocusTraversable(false);
        button.setPrefHeight(40);
        button.setPrefWidth(40);
        if (isOpponent) {
            if (type == 0) {
                //  button.setStyle("-fx-background-color: blue");
            } else if (type == -1) {
                button.setStyle("-fx-background-color: #a80202");
            } else if (type== -2) {
                button.setStyle("-fx-background-color: #595959");
            }
        } else {
            if (type == 0) {
                button.setStyle("-fx-background-color: rgba(0,149,255,0.34)");
            } else if (type == -1) {
                button.setStyle("-fx-background-color: rgba(180,70,70,0.38)");
            }else if (type== -2) {
                button.setStyle("-fx-background-color: #595959");
            }
            else {
                button.setStyle("-fx-background-color: " + getShipColorForId(type));
            }
            button.setDisable(true);
        }


        return button;
    }

    private String getShipColorForId(int i) {
        String color = "#FAFAFA";
        for (Ship s : ship) {
            if (s.getId() == i) color = s.getColor();
        }
        return color;
    }

    private List<Ship> requestAndGetShips() {
        List<Ship> ships = new ArrayList<>();
        SocketAddress socketAddress = new InetSocketAddress(ip, port);
        Socket socket = new Socket();
        // Timeout required - it's in milliseconds
        int timeout = 500;
        try {
            socket.connect(socketAddress, timeout);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            Request request = new Request();
            request.setRequestType(Type.GET_SHIPS);
            request.setData(null);
            request.setFrom(new Player(playerName));

            oos.writeObject(request);

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            Response response = (Response) objectInputStream.readObject();

            ships = (List<Ship>) response.getData();

            socket.close();
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        return ships;
    }


    private Battle getLastStateOfBattle() {
        Battle battle = null;
        SocketAddress socketAddress = new InetSocketAddress(ip, port);
        Socket socket = new Socket();
        // Timeout required - it's in milliseconds
        int timeout = 500;
        try {
            socket.connect(socketAddress, timeout);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            Request request = new Request();
            request.setRequestType(Type.GET_LAST_STATE_BATTLE);
            request.setData(null);
            request.setFrom(new Player(playerName));

            oos.writeObject(request);

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            Response response = (Response) objectInputStream.readObject();

            battle = (Battle) response.getData();

            socket.close();
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        return battle;
    }


}
