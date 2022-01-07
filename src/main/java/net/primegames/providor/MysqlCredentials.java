package net.primegames.providor;

import lombok.Data;

@Data
public class MysqlCredentials {

    private String host;
    private int port;
    private String database;
    private String username;
    private String password;


    public MysqlCredentials(String host, String user, String password, String database) {
        this.host = host;
        this.username = user;
        this.password = password;
        this.database = database;
        port = 3306;
    }

    public MysqlCredentials(String host, String user, String password, String database, int port) {
        this.host = host;
        this.username = user;
        this.password = password;
        this.database = database;
        this.port = port;
    }

}
