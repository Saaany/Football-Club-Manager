package sample;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.ListView;
import sample.Controllers.HomeController;
import sample.Model.Player;
import sample.database.ServerClientInfo;
import server.ServerDataStorage;
import util.LoginDTO;

import java.io.IOException;

public class ReadThread implements Runnable {
    private final Thread thr;
    private final Main main;
    private String userName;
    private HomeController home;

    public ReadThread(Main main) {
        this.main = main;
        this.thr = new Thread(this);
        thr.start();
    }

    void setHome(HomeController home){
        this.home = home;
    }

    public void run() {

        try {
            while (true) {
                Object o = main.getNetworkUtil().read();

                LoginDTO loginDTO;

                if (o != null) {
                    if (o instanceof LoginDTO) {
                        loginDTO = (LoginDTO) o;
                        System.out.println(loginDTO.getUserName());
                        System.out.println(loginDTO.isStatus());
                        // the following is necessary to update JavaFX UI components from user created threads
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (loginDTO.isStatus()) {
                                    try {
                                        userName = loginDTO.getUserName();
                                        main.showHomePage(loginDTO.getUserName(),loginDTO);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    main.showAlert();
                                }

                            }
                        });
                    }

                    else if(o instanceof Player){

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                Player player = (Player)o;

                                if(player.getOnSell()){
                                    if(!home.clubName.equalsIgnoreCase(player.getClubName())){
                                        //player.setClubName(oldClubName);
                                        home.buyPlayerList.add(player);
                                        home.setListViewBP(home.buyPlayerList);
                                    }
                                    else if(home.clubName.equalsIgnoreCase(player.getClubName())){

                                        for(int i=0;i<home.clubPlayers.size();i++){

                                            if(player.getName().equalsIgnoreCase(home.clubPlayers.get(i).getName())){
                                                home.clubPlayers.remove(i);
                                                home.onSellPlayerList.add(player);
                                                home.setPlayerListView(home.clubPlayers);
                                                home.setListViewPOS(home.onSellPlayerList);
                                                break;
                                            }
                                        }
                                    }
                                }

                                else if(!player.getOnSell()){

                                    String[] clubNames = player.getClubName().split(",");
                                    System.out.println(clubNames[0]+","+clubNames[1]);
                                    String oldClubName = clubNames[0];
                                    String newClubName = clubNames[1];

                                    player.setClubName(newClubName);

                                    if(!home.clubName.equalsIgnoreCase(oldClubName)){

                                        //int index = -1;
                                        for(int i=0;i<home.buyPlayerList.size();i++){

                                            if(home.buyPlayerList.get(i).getName().equalsIgnoreCase(player.getName())){

                                               // index = i;
                                                home.buyPlayerList.remove(i);
                                                break;
                                            }
                                        }
                                        System.out.println("Home: "+home.clubName+"\n"+"Old: "+oldClubName+"\n"+"New: "+newClubName+"\n");
                                        home.setListViewBP(home.buyPlayerList);
                                       }

                                    else if(home.clubName.equalsIgnoreCase(oldClubName)) {

                                        for (int i = 0; i < home.onSellPlayerList.size(); i++) {

                                            if (home.onSellPlayerList.get(i).getName().equalsIgnoreCase(player.getName())) {

                                                home.onSellPlayerList.remove(i);
                                                break;
                                            }
                                        }
                                        System.out.println("Home: " + home.clubName + "\n" + "Old: " + oldClubName + "\n" + "New: " + newClubName + "\n");
                                        home.setListViewPOS(home.onSellPlayerList);
                                    }

                                    if(home.clubName.equalsIgnoreCase(newClubName)){

                                        boolean alreadyAdded = false;
                                        for(int i=0;i<home.clubPlayers.size();i++){

                                            if(home.clubPlayers.get(i).getName().equalsIgnoreCase(player.getName())){
                                                alreadyAdded = true;
                                                break;
                                            }
                                        }

                                        if(!alreadyAdded){
                                            home.clubPlayers.add(player);
                                        }
                                        home.setPlayerListView(home.clubPlayers);
                                    }
                                }
                            }
                        });
                    }

                    else if(o instanceof String){

                        String message = (String) o;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if(message.equalsIgnoreCase("already sold,buy")){
                                    home.showPlayerAlreadySold("buy");
                                }
                                else if(message.equalsIgnoreCase("already sold,sell")){
                                    home.showPlayerAlreadySold("sell");
                                }
                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                main.getNetworkUtil().closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



