package ir.alamdari.battleship.battleship;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ServerConnectorController {
    @FXML
    public TextField textIp;
    @FXML
    public TextField textPort;
    @FXML
    public Button btnCheck;
    @FXML
    public Text textStatus;
    @FXML
    public Button btnGo;

    @FXML
    public void initialize() {
        btnGo.setDisable(true);
        textStatus.setVisible(false);
        textIp.setText("127.0.0.1");
        textPort.setText("9876");

        btnCheck.setOnMouseClicked(btnCheckOnClick());
        textPort.setFocusTraversable(false
        );
        Platform.runLater(() -> btnCheck.requestFocus());


    }

    private EventHandler<MouseEvent> btnCheckOnClick() {
        return event -> {
            textStatus.setText(null);
            textStatus.setVisible(true);
            String ip = textIp.getText();
            if (ip == null || ip.length() == 0 || ip.isBlank()) {
                textStatus.setText("Enter IP address PLZ");
                return;
            }
            String port = textPort.getText();
            if (port == null || port.length() == 0 || port.isBlank()) {
                textStatus.setText("Enter Port number PLZ");
                System.out.println("port validation problem");
                return;
            }
            int portInt;
            try {
                portInt = Integer.parseInt(port);
            } catch (NumberFormatException e) {
                textStatus.setText(e.getMessage());
                e.printStackTrace();
                return;
            }
            if (isSocketAlive(ip, portInt)) {
                textStatus.setVisible(true);
                textStatus.setText("Server is Ready !");
                btnGo.setDisable(false);
            } else {
                textStatus.setText("Server is not Reachable !");
            }
        };
    }

    private boolean isSocketAlive(String hostName, int port) {
        boolean isAlive = false;
        // Creates a socket address from a hostname and a port number
        SocketAddress socketAddress = new InetSocketAddress(hostName, port);
        Socket socket = new Socket();
        // Timeout required - it's in milliseconds
        int timeout = 500;
        try {
            socket.connect(socketAddress, timeout);
            socket.close();
            isAlive = true;
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        return isAlive;
    }
}
