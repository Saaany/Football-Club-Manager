package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.Main;
import sample.Model.Player;
import util.LoginDTO;

import java.util.ArrayList;
import java.util.List;

public class searchWindowController {

    private Main main;
    private LoginDTO loginDTO;
    private Stage stage;
    private HomeController home;
    private List<Player> clubPlayers;

    public void setMain(Main main){this.main = main;}
    public void setLogInDTO(LoginDTO loginDTO) {
        this.loginDTO = loginDTO;
    }
    public void setStage(Stage searchWindow) {
        this.stage = searchWindow;
    }

    public void setHomeController(HomeController homeController) {
        this.home = homeController;
        labelClubName.setText(home.clubName);
        clubPlayers = homeController.clubPlayers;

        String imageUrl = "Images/"+home.clubName.toLowerCase()+".png";
        Image img = new Image(Main.class.getResourceAsStream(imageUrl));
        initWindowImg.setImage(img);
    }


    @FXML
    private ListView<String> cwListView;

    @FXML
    private Label labelClubName;

    @FXML
    private TextField textFieldPosition;

    @FXML
    private TextField textFieldCountry;

    @FXML
    private TextField textFieldLower;

    @FXML
    private TextField textFieldHigher;


    @FXML
    private TextField textFieldName;

    @FXML
    private AnchorPane initialWindow;

    @FXML
    private AnchorPane tysWindow;

    @FXML
    private ImageView clubImage;

    @FXML
    private  ImageView initWindowImg;

    @FXML
    private Label labelTys;


    @FXML
    private ListView<Player> searchListView;
    private void updateSearchList(List<Player> list) {
        cwListView.setVisible(false);
        initialWindow.setVisible(false);
        searchListView.setVisible(true);
        searchListView.getItems().setAll(list);

        try {
            searchListView.setOnMouseClicked(event -> {
                Player player = searchListView.getSelectionModel().getSelectedItem();
                System.out.println(player);

                try {
                    System.out.println("showing Player info");

                    main.showPlayerInfo(player,home,false);
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
    void btnNameShowClicked(ActionEvent event){
        tysWindow.setVisible(false);
        List<Player> nameWiseSearchList = new ArrayList<>();
        String countryInput = textFieldName.getText();

        boolean found = false;
        for (Player clubPlayer : clubPlayers) {
            if (clubPlayer.getName().toLowerCase().contains(countryInput.toLowerCase())) {
                nameWiseSearchList.add(clubPlayer);
                found = true;
            }
        }
        updateSearchList(nameWiseSearchList);
    }

    @FXML
    void btnCountryShowClicked(ActionEvent event) {
        tysWindow.setVisible(false);
        List<Player> countryWiseSearchList = new ArrayList<>();
        String countryInput = textFieldCountry.getText();

        if(countryInput.equalsIgnoreCase("all")){

            searchListView.setVisible(false);
            cwListView.setVisible(true);
            int len = clubPlayers.size();
            boolean[] found = new boolean[len];
            List<String> cwList = new ArrayList<>();

            for(int i=0;i<len;i++){
                if(found[i]) continue;

                found[i]= true;
                int count=0;

                for (int j=i;j<len;j++){

                    if(clubPlayers.get(i).getCountry().equalsIgnoreCase(clubPlayers.get(j).getCountry())){
                        count +=1;
                        found[j] = true;
                    }
                }
                String str = String.format("%-40s  :   %40s",clubPlayers.get(i).getCountry(),count);
                cwList.add(str);
            }
            cwListView.getItems().setAll(cwList);

            try {
                cwListView.setOnMouseClicked(evnt -> {
                    String str = cwListView.getSelectionModel().getSelectedItem();
                    System.out.println(str);

                    try {
                        System.out.println("showing Player info");

                        for (Player clubPlayer : clubPlayers) {
                            if (str.contains(clubPlayer.getCountry())) {
                                main.showPlayerInfo(clubPlayer, home, false);
                            }
                        }
                    } catch (Exception e) {
                       // e.printStackTrace();
                        System.out.println(e);
                    }
                });
            }catch (Exception e){
                System.out.println(e);
            }
        }

        else {
            for (Player clubPlayer : clubPlayers) {
                if (countryInput.equalsIgnoreCase(clubPlayer.getCountry())) {
                    countryWiseSearchList.add(clubPlayer);
                }
            }
            updateSearchList(countryWiseSearchList);
        }
    }


    @FXML
    void btnCloseClicked(ActionEvent event) {
        tysWindow.setVisible(false);
        home.getInitialWindow().setVisible(true);
        stage.close();
    }

    @FXML
    void btnMaxAgeClicked(ActionEvent event) {
            tysWindow.setVisible(false);
            searchListView.getItems().clear();

            List<Player> maxAgeList = new ArrayList<>();
            int maxAge = -1;

            for(Player clubPlayer : clubPlayers) {

                if (clubPlayer.getAge() > maxAge) {
                    maxAge = clubPlayer.getAge();
                }
            }

        for (Player clubPlayer : clubPlayers) {

            if (clubPlayer.getAge() == maxAge) {
                maxAgeList.add(clubPlayer);
            }
        }
        updateSearchList(maxAgeList);
    }

    @FXML
    void btnMaxHeightClicked(ActionEvent event) {
        tysWindow.setVisible(false);
        searchListView.getItems().clear();
        double maxHeight = -1;
        List<Player> maxHeightList = new ArrayList<>();

        for (Player clubPlayer : clubPlayers) {

            if (clubPlayer.getHeight() > maxHeight) {
                maxHeight = clubPlayer.getHeight();
            }
        }

        for (Player clubPlayer : clubPlayers) {

            if (clubPlayer.getHeight() == maxHeight) {
                maxHeightList.add(clubPlayer);
            }
        }

        updateSearchList(maxHeightList);

    }

    @FXML
    void btnMaxSalaryClicked(ActionEvent event) {
        tysWindow.setVisible(false);
        searchListView.getItems().clear();
        double maxSalary = -1;
        List<Player> maxSalaryList = new ArrayList<>();

        for (Player clubPlayer : clubPlayers) {

            if (clubPlayer.getSalary() > maxSalary) {
                maxSalary = clubPlayer.getSalary();
            }
        }

        for (Player clubPlayer : clubPlayers) {

            if (clubPlayer.getSalary() == maxSalary) {
                maxSalaryList.add(clubPlayer);
            }
        }

        updateSearchList(maxSalaryList);
    }

    @FXML
    void btnPositionShowClicked(ActionEvent event) {
        tysWindow.setVisible(false);
        List<Player> positionWiseSearchList = new ArrayList<>();
        String positionInput = textFieldPosition.getText();

        boolean found = false;
        for (Player clubPlayer : clubPlayers) {
            if (positionInput.equalsIgnoreCase(clubPlayer.getPosition())) {
                positionWiseSearchList.add(clubPlayer);
                found = true;
            }
        }
        updateSearchList(positionWiseSearchList);
    }

    @FXML
    void btnSalaryRangeClicked(ActionEvent event) {
        tysWindow.setVisible(false);
        List<Player> salaryRangeWiseSearchList = new ArrayList<>();
        String lowRangeInput = textFieldLower.getText();
        double lower = Double.parseDouble(lowRangeInput);
        String highRangeInput = textFieldHigher.getText();
        double higher = Double.parseDouble(highRangeInput);

        boolean found = false;
        for (Player clubPlayer : clubPlayers) {
            if (clubPlayer.getSalary() >= lower && clubPlayer.getSalary() <= higher) {

                salaryRangeWiseSearchList.add(clubPlayer);
                found = true;
            }
        }
        updateSearchList(salaryRangeWiseSearchList);
    }

    @FXML
    void btnTysClicked(ActionEvent event) {
        initialWindow.setVisible(false);
        cwListView.setVisible(false);
        searchListView.setVisible(false);
        double totalYearlySalary = 0;

        for (Player clubPlayer : clubPlayers) {
            totalYearlySalary += clubPlayer.getSalary();
        }

         totalYearlySalary *= 52;// approx 52 weeks in a year
         labelTys.setText(String.valueOf(totalYearlySalary));
         String imageUrl = "Images/"+home.clubName.toLowerCase()+".png";
         Image img = new Image(Main.class.getResourceAsStream(imageUrl));
         clubImage.setImage(img);

         tysWindow.setVisible(true);

    }

}
