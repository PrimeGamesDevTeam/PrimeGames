package net.primegames.providor;

import lombok.Data;
import net.primegames.providor.connection.MySqlConnectionBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;

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

    public static MysqlCredentials getCredentials(Plugin plugin, String prefix) {
        FileConfiguration config = plugin.getConfig();
        if (config.getString(prefix + ".mysql.host") == null || config.getString(prefix + ".mysql.port") == null || config.getString(prefix + ".mysql.database") == null || config.getString(prefix + ".mysql.username") == null || config.getString(prefix + ".mysql.password") == null) {
            Bukkit.getLogger().warning(MySqlProvider.PREFIX + "MySQL Credentials are missing in config.yml" + " of prefix " + prefix + ". Setting defaults...");
            setDefaults(config, prefix);
            plugin.saveConfig();
            Bukkit.getLogger().warning(MySqlProvider.PREFIX + "MySQL Credentials are set to defaults, Make sure to set them correctly in config.yml of " + plugin.getName() + " plugin.");
        }
        String host = config.getString(prefix + ".mysql.host");
        int port = config.getInt(prefix + ".mysql.port");
        String database = config.getString(prefix+ ".mysql.database");
        String username = config.getString(prefix + ".mysql.username");
        String password = config.getString(prefix + ".mysql.password");
        return new MysqlCredentials(host, username, password, database, port);
    }

    private static void setDefaults(FileConfiguration config, String prefix) {
        config.set(prefix + ".mysql.host", "127.0.0.1");
        config.set(prefix + ".mysql.port", 3306);
        config.set(prefix + ".mysql.database", "core");
        config.set(prefix + ".mysql.username", "root");
        config.set(prefix + ".mysql.password", "password");
    }

    public Connection createConnection() {
        return new MySqlConnectionBuilder(this).getConnection();
    }
}
