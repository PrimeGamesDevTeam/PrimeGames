package net.primegames.utils;

import net.primegames.PrimeGames;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class LoggerUtils {

    private static String PREFIX = "&l&aPrimes&2&lCore&r";
    private static final String PREFIX_INFO = "&7[" + PREFIX + " &rInfo] ";
    private static final String PREFIX_ERROR = "&7[" + PREFIX + " &4ERROR&r&7] &4";
    private static final String PREFIX_WARN = "&7[" + PREFIX + " &eWarning&r&7] &c";
    private static final String PREFIX_SUCCESS = "&7[" + PREFIX + " &7 &aSUCCESS] ";

    static public void info(String msg) {
        Bukkit.getConsoleSender().sendMessage(ColorUtils.getColString(PREFIX_INFO + msg));
    }

    static public void success(String msg) {
        Bukkit.getConsoleSender().sendMessage(ColorUtils.getColString(PREFIX_SUCCESS + msg));
    }

    static public void log(String prefix, String msg) {
        Bukkit.getConsoleSender().sendMessage(ColorUtils.getColString(prefix + msg));
    }

    static public void error(String msg) {
        Bukkit.getConsoleSender().sendMessage(ColorUtils.getColString(PREFIX_ERROR + msg));
    }

    static public void error(String msg, Throwable e) {
        Bukkit.getConsoleSender().sendMessage(ColorUtils.getColString(PREFIX_ERROR + msg));
        e.printStackTrace();
    }


    static public void warn(String msg) {
        Bukkit.getConsoleSender().sendMessage(ColorUtils.getColString(PREFIX_WARN + msg));
    }

    public static void warn(String msg, Throwable e) {
        Bukkit.getConsoleSender().sendMessage(ColorUtils.getColString(PREFIX_WARN + msg));
        e.printStackTrace();
    }

    public static void warn(String msg, Object... args) {
        warn(String.format(msg, args));
    }

    static public void debug(String msg) {
        PrimeGames.getInstance().getLogger().log(Level.CONFIG, msg);
    }

    public static void setPrefix(String s) {
        PREFIX = s;
    }

}
