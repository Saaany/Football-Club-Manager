package util;

import server.ServerDataStorage;
import java.io.Serializable;

public class LoginDTO implements Serializable {

    private String userName;
    private String password;
    private boolean status;
    private int clientInstanceNo = 1;
    private ServerDataStorage serverDataStorage;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setServerDataStorage(ServerDataStorage serverDataStorage) {
        this.serverDataStorage = serverDataStorage;
    }

    public int getClientInstance() {
        return clientInstanceNo;
    }

    public void setClientInstance(int clientInstance) {
        this.clientInstanceNo = clientInstance;
    }

    public ServerDataStorage getServerDataStorage() {
        return serverDataStorage;
    }

}
