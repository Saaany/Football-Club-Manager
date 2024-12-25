package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.Main;
import sample.Model.Player;
import java.util.Optional;

public class PlayerInfoController {

        private Main main;
        private Stage stage;
        private boolean buttonStatus;

        @FXML
        private VBox playerAlertBox;

        @FXML
        private ImageView imgPlayer;

        @FXML
        private Label labelName;

        @FXML
        private Label labelAge;

        @FXML
        private Label labelHeight;

        @FXML
        private Label labelCountry;

        @FXML
        private Label labelPosition;

        @FXML
        private Label labelNumber;

        @FXML
        private Label labelSalary;

        @FXML
        private Button btnClose;

        @FXML
        private Button btnClose2;

        @FXML
        private Button btnSell;

        @FXML
        private Button btnBuy;

        private Player player;

        private HomeController home;

        public void setHomeController(HomeController homeController){this.home = homeController;}

        @FXML
        void closeAlertBox(ActionEvent event) throws Exception {

        }

        @FXML
        void btnCloseClicked(ActionEvent event) throws Exception {
            stage.close();
        }

        @FXML
        void btnClose2Clicked(ActionEvent event) throws Exception {
            stage.close();
        }

        @FXML
        void btnSellClicked(ActionEvent event) throws Exception {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Sell");
            alert.setHeaderText("Confirm Selling Process");
            alert.setContentText("Are you sure to sell this player?");

            Optional<ButtonType> result = alert.showAndWait();

            if(result.get().equals(ButtonType.OK)){

                player.setOnSell(true);

                //updating home gui
                //home.clubPlayers.remove(player);
                //home.onSellPlayerList.add(player);//updating from reader thread

                home.btnPlayerOnSellClicked(event);
                home.btnPlayerListClicked(event);


                try {
                    main.getNetworkUtil().write(player);
                }catch (Exception e){
                    System.out.println(e);
                    e.printStackTrace();
                }

                stage.close();
            }
            else if(result.get().equals(ButtonType.CANCEL)){
                stage.close();
            }
        }

    @FXML
    void btnBuyClicked(ActionEvent event) throws Exception {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Buy");
        alert.setHeaderText("Confirm Buying Process");
        alert.setContentText("Are you sure to Buy this player?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get().equals(ButtonType.OK)) {

            player.setOnSell(false);
            System.out.println("old--> "+player.getClubName());
            player.setClubName(home.clubName);
            System.out.println("new--> "+player.getClubName());

            //updating home gui
            home.buyPlayerList.remove(player);

            home.btnPlayerListClicked(event);
            home.btnBuyPlayerClicked(event);

            try {
                main.getNetworkUtil().write(player);
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
            }

            stage.close();
        } else if (result.get().equals(ButtonType.CANCEL)) {
            stage.close();
        }
    }

        public void init(Player player){

            labelName.setText(player.getName());
            labelAge.setText(String.valueOf(player.getAge()));
            labelHeight.setText(String.valueOf(player.getHeight()));
            labelPosition.setText(player.getPosition());
            labelCountry.setText(player.getCountry());
            labelSalary.setText(String.valueOf(player.getSalary()));
            labelNumber.setText(String.valueOf(player.getNumber()));

            if(buttonStatus){
                if(player.getOnSell()){
                    btnBuy.setVisible(true);
                    btnClose.setVisible(true);
                    btnSell.setVisible(false);
                }
                else {
                    btnBuy.setVisible(false);
                    btnClose.setVisible(true);
                    btnSell.setVisible(true);
                }
            }else {
                btnBuy.setVisible(false);
                btnSell.setVisible(false);
                btnClose.setVisible(false);
                btnClose2.setVisible(true);

            }
        }


        public void setStage(Stage stage){
            this.stage = stage;
        }

        public void setMain(Main main){
            this.main = main;
        }

        public void setPlayer(Player player){this.player = player;}

        public void setButtonStatus(boolean buttonStatus) {
                this.buttonStatus = buttonStatus;
        }

}
