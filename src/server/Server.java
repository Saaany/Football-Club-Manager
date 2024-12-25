package server;

import sample.Model.Player;
import sample.database.Database;
import sample.database.ServerClientInfo;
import util.LoginDTO;
import util.NetworkUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Server{

    private static Server instance;
    public ServerSocket serverSocket;
    public HashMap<String, String> userMap;
    private List<Player> playerList;
    private List<Player> playerOnSellList = new ArrayList<>();
    private ServerDataStorage serverDataStorage;
    private HashMap<LoginDTO, ServerClientInfo> clientList = new HashMap<>();

    private Server() {
        userMap = new HashMap<>();
        userMap.put("Arsenal", "a");
        userMap.put("Manchester City", "m");
        userMap.put("Chelsea", "c");
        userMap.put("Barcelona", "b");
        userMap.put("Real Madrid", "r");
        userMap.put("Liverpool","l");
        userMap.put("Manchester United","u");

        playerList = Database.getInstance().getObservableList();

        for(Player player:playerList){
            if(player.getOnSell()){
                playerOnSellList.add(player);
            }
        }

        this.serverDataStorage = new ServerDataStorage(playerList,playerOnSellList);

        try {
            serverSocket = new ServerSocket(33333);
            System.out.println("Server Started...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                serve(clientSocket);
            }

        } catch (Exception e) {
            System.out.println("Server starts:" + e);
        }
    }

    public static Server getInstance(){

        if(instance==null){
            instance = new Server();
        }
        return instance;
    }

    public void serve(Socket clientSocket) throws IOException {

        NetworkUtil networkUtil = new NetworkUtil(clientSocket);
        new ReadThreadServer(networkUtil,serverDataStorage,clientList,userMap);
    }

    public static void main(String[] args) {
      Server server = new Server();
    }
}
