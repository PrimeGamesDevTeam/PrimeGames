package net.primegames.core.utils;

import net.primegames.core.PrimesCore;

import java.util.logging.Level;

public class LoggerUtils {


    static public void info(String $msg){
        PrimesCore.getInstance().getLogger().info($msg);
    }

    static public void error(String $msg){
        PrimesCore.getInstance().getLogger().log(Level.SEVERE, $msg);
    }

    static public void warn(String $msg){
        PrimesCore.getInstance().getLogger().warning($msg);
    }

    static public void debug(String $msg){
        PrimesCore.getInstance().getLogger().log(Level.INFO, $msg);
    }

}
