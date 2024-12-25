package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import sample.Main;
import sample.Model.Player;
import util.LoginDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HomeController {

    private Main main;
    public String clubName;

    @FXML
    private AnchorPane initialWindow;
    public AnchorPane getInitialWindow(){
        return initialWindow;
    }

    @FXML
    private Label message;

    @FXML
    private ImageView image;

    @FXML
    private ListView<Player> listView;

    @FXML
    private ListView<Player> listViewPOS;

    @FXML
    private ListView<Player> listViewBP;


    public void setListViewBP(List<Player> players){
        listViewBP.getItems().setAll(players);
    }
    public void setListViewPOS(List<Player> players){
        listViewPOS.getItems().setAll(players);
    }
    public void setPlayerListView(List<Player> players){
        listView.getItems().setAll(players);
    }

    private LoginDTO loginDTO;

    private List<Player> playerList;
    private List<Player> playerOnSellList;


    public void init(String msg) {

        this.clubName = msg;

        message.setText(msg);
        String imageUrl = "Images/"+clubName.toLowerCase()+".png";
        Image img = new Image(Main.class.getResourceAsStream(imageUrl));
        image.setImage(img);

        listView.setVisible(false);
        listViewPOS.setVisible(false);
        listViewBP.setVisible(false);

        initialWindow.setVisible(true);

        //initializing club players
        for (Player player : playerList) {
            if (player.getClubName().equalsIgnoreCase(clubName) && !player.getOnSell()) {
                clubPlayers.add(player);
            }
        }
    }

    //Button Methods
    //todo:finish all the logical shits
    @FXML
    void logoutAction(ActionEvent event) {
        try {

            String logoutInfo = loginDTO.getUserName()+","+ loginDTO.getClientInstance();
            System.out.println(logoutInfo);

            main.getNetworkUtil().write(logoutInfo);

            Thread.sleep(500);
            main.getNetworkUtil().closeConnection();
            main.start(main.getStage());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //home data storage
    public  List<Player> clubPlayers = new ArrayList<>();
    public  List<Player> onSellPlayerList = new ArrayList<>();
    public  List<Player> buyPlayerList = new ArrayList<>();

    private boolean buyPlayerListIsSet = false;
    private boolean posListIsSet = false;


    @FXML
    void btnPlayerListClicked(ActionEvent event) {

        initialWindow.setVisible(false);
        listView.setVisible(true);
        listViewPOS.setVisible(false);
        listViewBP.setVisible(false);

        listView.getItems().clear();

        listView.getItems().setAll(clubPlayers);

        try {
            listView.setOnMouseClicked(evnt -> {
                Player player = listView.getSelectionModel().getSelectedItem();
                System.out.println(player);

                try {
                    System.out.println("showing Player info");

                    main.showPlayerInfo(player,this,true);
                } catch (Exception e) {
                    System.out.println(e);
                    //e.printStackTrace();
                }
            });
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @FXML
    void btnBuyPlayerClicked(ActionEvent event) {

        initialWindow.setVisible(false);
        listView.setVisible(false);
        listViewPOS.setVisible(false);
        listViewBP.setVisible(true);

        listViewBP.getItems().clear();

        String clubName = this.clubName;

        if(!buyPlayerListIsSet){
            for (Player player : playerOnSellList) {
                System.out.println("Club Name: "+this.clubName);
                if (!player.getClubName().equalsIgnoreCase(clubName)) {

                        buyPlayerList.add(player);
                }
             }
            buyPlayerListIsSet = true;
        }

        listViewBP.getItems().setAll(buyPlayerList);


        try {
            listViewBP.setOnMouseClicked(evnt -> {
                Player player = listViewBP.getSelectionModel().getSelectedItem();
                System.out.println(player);

                try {
                    System.out.println("showing Player info");
                    main.showPlayerInfo(player,this,true);
                    System.out.println("Player name: "+player.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @FXML
    void btnPlayerOnSellClicked(ActionEvent event) {

        initialWindow.setVisible(false);
        listView.setVisible(false);
        listViewPOS.setVisible(true);
        listViewBP.setVisible(false);

        listViewPOS.getItems().clear();

        if(!posListIsSet){
            for (Player player : playerOnSellList) {
                if (player.getClubName().equals(clubName)) {
                    onSellPlayerList.add(player);
                }
            }
            posListIsSet = true;
        }

        listViewPOS.getItems().setAll(onSellPlayerList);

        try {
            listViewPOS.setOnMouseClicked(evnt -> {
                Player player = listViewPOS.getSelectionModel().getSelectedItem();
                System.out.println(player);

                try {
                    System.out.println("showing Player info");
                    main.showPlayerInfo(player,this,false);
                    System.out.println("Player name: "+player.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @FXML
    void btnSearchOptionsClicked(ActionEvent event) {

        btnPlayerListClicked(event);
        initialWindow.setVisible(true);
        listView.setVisible(false);
        listViewPOS.setVisible(false);
        listViewBP.setVisible(false);

        try {
            main.showSearchWindow(this);
        } catch (IOException e) {
            System.out.println("search window show failed...");
            e.printStackTrace();
        }
    }
    //Button Methods finished


    public void setMain(Main main) {
        this.main = main;
    }

    public void setLoginDTO(LoginDTO loginDTO) {
        this.loginDTO = loginDTO;
        this.playerList = loginDTO.getServerDataStorage().getPlayerList();
        this.playerOnSellList = loginDTO.getServerDataStorage().getPlayerOnSellList();
        System.out.println("player on sell: "+ loginDTO.getServerDataStorage().getPlayerOnSellList());
    }

    public void showPlayerAlreadySold(String status){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Already Sold");

        if(status.equalsIgnoreCase("buy")){
            alert.setHeaderText("This player has already been sold out!");
            alert.setContentText("SORRY! THIS PLAYER IS NO MORE AVAILABLE TO BUY.");
        }else if(status.equalsIgnoreCase("sell")){
            alert.setHeaderText("This player is already in selling process");
            alert.setContentText("SORRY! THIS PLAYER IS NO MORE AVAILABLE TO SELL.");
        }

        alert.showAndWait();

    }

}
