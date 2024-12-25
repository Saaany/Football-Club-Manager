package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Controllers.HomeController;
import sample.Controllers.LoginController;
import sample.Controllers.PlayerInfoController;
import sample.Controllers.searchWindowController;
import sample.Model.Player;
import util.LoginDTO;
import util.NetworkUtil;

import java.io.IOException;

public class Main extends Application {

    private Stage stage;
    private NetworkUtil networkUtil;
    private ReadThread readThread;

    public Stage getStage() {
        return stage;
    }

    public NetworkUtil getNetworkUtil() {
        return networkUtil;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        connectToServer();
        showLoginPage();
    }

    private void connectToServer() throws IOException {
        String serverAddress = "127.0.0.1";
        int serverPort = 33333;
        networkUtil = new NetworkUtil(serverAddress, serverPort);
        readThread = new ReadThread(this);
    }

    public void showLoginPage() throws Exception {
        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ViewFxml/login.fxml"));
        Parent root = loader.load();

        // Loading the controller
        LoginController controller = loader.getController();
        controller.setMain(this);

        // Set the primary stage
        stage.setTitle("Login");
        stage.setScene(new Scene(root, 620, 500));
        stage.show();
    }

    public void showHomePage(String userName, LoginDTO loginDTO) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ViewFxml/home.fxml"));
        Parent root = loader.load();

        // Loading the controller
        HomeController controller = loader.getController();
        controller.setLoginDTO(loginDTO);
        controller.init(userName);
        controller.setMain(this);
        readThread.setHome(controller);

        // Set the primary stage
        stage.setTitle("Home");
        stage.setScene(new Scene(root, 800, 650));
        stage.show();
    }

    public void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Incorrect Credentials");
        alert.setHeaderText("Incorrect Credentials");
        alert.setContentText("The username and password you provided is not correct.");
        alert.showAndWait();
    }


    public void showPlayerInfo(Player player, HomeController homeController,boolean buttonStatus) throws Exception {

        Stage stage2 = new Stage();
        stage2.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ViewFxml/PlayerInfo.fxml"));
        Parent root = loader.load();

        // Loading the controller
        PlayerInfoController controller = loader.getController();
        controller.setButtonStatus(buttonStatus);
        controller.init(player);
        controller.setMain(this);
        controller.setStage(stage2);
        controller.setPlayer(player);
        controller.setHomeController(homeController);

        // Set the primary stage
        stage2.setTitle("Player Information");
        stage2.setScene(new Scene(root, 400, 600));
        stage2.showAndWait();
    }

    public void showSearchWindow(HomeController homeController) throws IOException {

        Stage searchWindow = new Stage();
        searchWindow.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ViewFxml/searchWindow.fxml"));
        Parent root = loader.load();

        // Loading the controller
        searchWindowController controller = loader.getController();
        controller.setMain(this);
        controller.setStage(searchWindow);
        controller.setHomeController(homeController);


        // Set the primary stage
        searchWindow.setTitle("Player Information");
        searchWindow.setScene(new Scene(root, 820, 720));
        searchWindow.showAndWait();
    }

    //main method
    public static void main(String[] args) {
        // This will launch the JavaFX application
        launch(args);
    }

}
