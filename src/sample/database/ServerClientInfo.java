package sample.database;

import util.NetworkUtil;

import java.io.Serializable;

public class ServerClientInfo implements Serializable {

    private NetworkUtil networkUtil;
    private String userName;

    public ServerClientInfo(String userName,NetworkUtil networkUtil){
        this.networkUtil = networkUtil;
        this.userName = userName;
    }
    public NetworkUtil getNetworkUtil() {
        return networkUtil;
    }
}
