package server;

import sample.Model.Player;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class ServerDataStorage implements Serializable {

    //private HashMap<String,String> userMap;
    private List<Player> playerList;
    private List<Player> playerOnSellList;

    public ServerDataStorage(List<Player> playerList, List<Player> playerOnSellList) {
        //this.userMap = userMap;
        this.playerList = playerList;
        this.playerOnSellList = playerOnSellList;
    }


//    public HashMap<String, String> getUserMap() {
//        return userMap;
//    }
//
//    public void setUserMap(HashMap<String, String> userMap) {
//        this.userMap = userMap;
//    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public List<Player> getPlayerOnSellList() {
        return playerOnSellList;
    }

    public void setPlayerOnSellList(List<Player> playerOnSellList) {
        this.playerOnSellList = playerOnSellList;
    }
}
