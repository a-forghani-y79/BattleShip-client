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
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class BattleController {

    @FXML
    public Text playerOneText;
    @FXML
    public Text playerTwoText;
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


        Battle battle
                = getLastStateOfBattle();

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

                    timeline.setCycleCount(timeline.getCycleCount() + 1);
                }));
        int cycleCount = 10;
        timeline.setCycleCount(cycleCount);
        timeline.play();

    }

    private void endGame(Player winner) {
        if (winner.getName().equals(playerName))
            winnerText.setText("You Win !!!");
        else
            winnerText.setText("You Lose :(");
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
            ObjectOutputStream oos =
                    new ObjectOutputStream(socket.getOutputStream());

            Request request = new Request();
            request.setRequestType(Type.IS_MY_TURN);
            request.setFrom(new Player(playerName));

            oos.writeObject(request);

            ObjectInputStream objectInputStream =
                    new ObjectInputStream(socket.getInputStream());

            Response response = (Response) objectInputStream.readObject();

            System.out.println("response.getMessage() = " + response.getMessage());
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
            ObjectOutputStream oos =
                    new ObjectOutputStream(socket.getOutputStream());

            Request request = new Request();
            request.setRequestType(Type.SHOOT);
            request.setData(new int[]{x, y});
            request.setFrom(new Player(playerName));

            oos.writeObject(request);

            ObjectInputStream objectInputStream =
                    new ObjectInputStream(socket.getInputStream());

            Response response = (Response) objectInputStream.readObject();

            System.out.println("response.getMessage() = " + response.getMessage());

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
            ObjectOutputStream oos =
                    new ObjectOutputStream(socket.getOutputStream());

            Request request = new Request();
            request.setRequestType(Type.GET_WINNER);
            request.setData(null);
            request.setFrom(new Player(playerName));

            oos.writeObject(request);

            ObjectInputStream objectInputStream =
                    new ObjectInputStream(socket.getInputStream());

            Response response = (Response) objectInputStream.readObject();

            System.out.println("response.getMessage() = " + response.getMessage());
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
                        button = initButton(i, j,
                                battle.getPlayerTwoArea()[i][j], true);
                        button.setOnMouseClicked(event -> onClickButton((Button) event.getSource()));
                        opponentGrid.add(button, i, j);

                        button = initButton(i, j,
                                battle.getPlayerOneArea()[i][j], false);
                        myGrid.add(button, i, j);
                        //     area[i][j] = button;
                    }else{
                        button = initButton(i, j,
                                battle.getPlayerOneArea()[i][j], true);
                        button.setOnMouseClicked(event -> onClickButton((Button) event.getSource()));
                        opponentGrid.add(button, i, j);

                        button = initButton(i, j,
                                battle.getPlayerTwoArea()[i][j], false);
                        myGrid.add(button, i, j);
                    }
                }
            }
        }
    }


    Button initButton(int i, int j, int type, boolean isOpponent) {
        //type 0 for water | -1 for destroyed ship cell
        Button button = new Button("");
        button.setId("btn" + i + j);
        button.setPrefHeight(50);
        button.setPrefWidth(50);
        if (isOpponent) {
            if (type == 0) {
                button.setStyle("-fx-background-color: blue");
            } else if (type == -1) {
                button.setStyle("-fx-background-color: #ff4800");
            }
        } else {
            if (type == 0) {
                button.setStyle("-fx-background-color: blue");
            } else if (type == -1) {
                button.setStyle("-fx-background-color: #ff4800");
            } else {
                button.setStyle("-fx-background-color: " + getShipColorForId(type));
            }
            button.setDisable(true);
        }


        return button;
    }

    private String getShipColorForId(int i) {
        for (Ship s : ship) {
            if (s.getId() == i)
                return s.getColor();
        }
        return "#FAFAFA";
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

            System.out.println("response.getMessage() = " + response.getMessage());
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
            ObjectOutputStream oos =
                    new ObjectOutputStream(socket.getOutputStream());

            Request request = new Request();
            request.setRequestType(Type.GET_LAST_STATE_BATTLE);
            request.setData(null);
            request.setFrom(new Player(playerName));

            oos.writeObject(request);

            ObjectInputStream objectInputStream =
                    new ObjectInputStream(socket.getInputStream());

            Response response = (Response) objectInputStream.readObject();

            System.out.println("response.getMessage() = " + response.getMessage());
            battle = (Battle) response.getData();

            socket.close();
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        return battle;
    }


}
