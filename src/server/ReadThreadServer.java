package server;

import sample.Model.Player;
import sample.database.Database;
import sample.database.ServerClientInfo;
import util.LoginDTO;
import util.NetworkUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ReadThreadServer implements Runnable {
    private final Thread thr;
    private final NetworkUtil networkUtil;
    private  ServerDataStorage serverDataStorage;
    private HashMap<LoginDTO,ServerClientInfo> clientList = new HashMap<>();
    public HashMap<String, String> userMap;

    public ReadThreadServer(NetworkUtil networkUtil, ServerDataStorage serverDataStorage, HashMap<LoginDTO, ServerClientInfo> clientList, HashMap<String, String> userMap) {

        this.userMap = userMap;
        this.networkUtil = networkUtil;
        this.serverDataStorage = serverDataStorage;
        this.clientList = clientList;
        this.thr = new Thread(this);
        thr.start();
    }

    public void run() {
        try {
            while (true) {

                Object o = networkUtil.read();

                if (o != null) {
                    if (o instanceof LoginDTO) {

                        LoginDTO loginDTO = (LoginDTO) o;
                        String password = userMap.get(loginDTO.getUserName());
                        loginDTO.setStatus(loginDTO.getPassword().equals(password));

                        if(loginDTO.isStatus()){

                            int maxInstance = 1;
                            for(Map.Entry<LoginDTO,ServerClientInfo> entry: clientList.entrySet()){

                                String user = entry.getKey().getUserName();

                                if(user.equalsIgnoreCase(loginDTO.getUserName())){

                                    maxInstance +=1;
                                }
                            }
                            loginDTO.setClientInstance(maxInstance);
                            loginDTO.setServerDataStorage(serverDataStorage);
                            clientList.put(loginDTO,new ServerClientInfo(loginDTO.getUserName(), networkUtil));
                        }

                        networkUtil.write(loginDTO);
                    }

                    else if(o instanceof Player){

                        Player player = (Player) o;

                        if(player.getOnSell()){
                             updatingSellingProcess(player);
                        }
                        else if(!player.getOnSell()){
                            updatingBuyingProcess(player);
                        }
                    }

                    else if(o instanceof String){

                        String info = (String) o;

                        String[] clientInfo = info.split(",");

                        System.out.println(clientInfo[0]);
                        System.out.println(clientInfo[1]);

                        for(Map.Entry<LoginDTO,ServerClientInfo> entry: clientList.entrySet()){

                            LoginDTO user = entry.getKey();
                            String userName = user.getUserName();
                            String instance = String.valueOf(user.getClientInstance());

                            if(userName.equalsIgnoreCase(clientInfo[0]) && instance.equalsIgnoreCase(clientInfo[1])){
                                clientList.remove(user);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error occur...");
            System.out.println(e);
        } finally {
            try {
                System.out.println("Closing socket...");
                networkUtil.closeConnection();
            } catch (IOException e) {
                System.out.println("Error occur 2....");
                e.printStackTrace();
            }
        }
    }

    synchronized private void updatingBuyingProcess(Player player) throws IOException {

        boolean Found = false;
        String oldClubName = " ";
        String newClubName = player.getClubName();

        System.out.println("From Server :"+player.getClubName());

        //updating player on sell list

        for(int i =0;i<serverDataStorage.getPlayerOnSellList().size();i++){

            if(serverDataStorage.getPlayerOnSellList().get(i).getName().equalsIgnoreCase(player.getName())){
                Found = true;
                serverDataStorage.getPlayerOnSellList().remove(i);
                break;
            }
        }
        if(!Found){

            final String ALREADY_SOLD_MESSAGE ="already sold,buy";
            Object obj = ALREADY_SOLD_MESSAGE;

            networkUtil.write(obj);
        }

        else {

            //updating player list
            for (int i = 0;i<serverDataStorage.getPlayerList().size();i++){

                if(serverDataStorage.getPlayerList().get(i).getName().equalsIgnoreCase(player.getName())){

                    oldClubName = serverDataStorage.getPlayerList().get(i).getClubName();
                    serverDataStorage.getPlayerList().get(i).setOnSell(false);
                    serverDataStorage.getPlayerList().get(i).setClubName(player.getClubName());
                    break;
                }
            }

            //updating database
            String oldPlayerInfo = player.getName()+","+player.getCountry()+","+player.getAge()
                    +","+player.getHeight() +","+oldClubName+","+player.getPosition()
                    +","+player.getNumber()+","+player.getSalary()+",true";

            String newPlayerInfo = player.getName()+","+player.getCountry()+","+player.getAge()
                    +","+player.getHeight() +","+newClubName+","+player.getPosition()
                    +","+player.getNumber()+","+player.getSalary()+",false";

            Database.getInstance().writeToFile(oldPlayerInfo,newPlayerInfo);
            //finished updating database


            //sending online clients the updates
            player.setClubName(oldClubName+","+newClubName);
            Object obj = player;
            for(Map.Entry<LoginDTO, ServerClientInfo> entry :clientList.entrySet()) {

                LoginDTO user = entry.getKey();
                String userName = user.getUserName();

                System.out.println("User: "+userName+" Old + New: "+player.getClubName());
                clientList.get(user).getNetworkUtil().write(obj);
            }
        }
}

    synchronized private void updatingSellingProcess(Player player) throws IOException {

        boolean found = false;

        //updating player list
        for (int i = 0;i<serverDataStorage.getPlayerList().size();i++){

            if(serverDataStorage.getPlayerList().get(i).getName().equalsIgnoreCase(player.getName())){
                if(!serverDataStorage.getPlayerList().get(i).getOnSell()){
                    found = true;
                    serverDataStorage.getPlayerList().get(i).setOnSell(true);
                    break;
                }
            }
        }

        if(!found){

            final String ALREADY_SOLD_MESSAGE ="already sold,sell";
            Object obj = ALREADY_SOLD_MESSAGE;

            networkUtil.write(obj);
        }

        else {
            //updating player on sell list
            serverDataStorage.getPlayerOnSellList().add(player);

            //updating database
            String oldPlayerInfo = player.getName()+","+player.getCountry()+","+player.getAge()
                    +","+player.getHeight() +","+player.getClubName()+","+player.getPosition()
                    +","+player.getNumber()+","+player.getSalary()+",false";

            String newPlayerInfo = player.getName()+","+player.getCountry()+","+player.getAge()
                    +","+player.getHeight() +","+player.getClubName()+","+player.getPosition()
                    +","+player.getNumber()+","+player.getSalary()+",true";

            Database.getInstance().writeToFile(oldPlayerInfo,newPlayerInfo);
            //finished updating database

            //sending online clients the updates
            Object obj = player;
            for(Map.Entry<LoginDTO, ServerClientInfo> entry :clientList.entrySet()) {

                LoginDTO user = entry.getKey();
                String userName = user.getUserName();
                System.out.println("User: "+userName+" Old + New: "+player.getClubName());
                clientList.get(user).getNetworkUtil().write(obj);
            }
        }
    }
}



