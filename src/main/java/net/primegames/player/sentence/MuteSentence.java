package net.primegames.player.sentence;

import java.sql.Date;

public class MuteSentence {

    private final Date date;

    public MuteSentence(Date date){
        this.date = date;
    }

    //check if time has passed
    public boolean isTimePassed(){
        return new Date(System.currentTimeMillis()).after(date);
    }

    //get time left until mute ends
    public String getTimeLeft() {
        long timeLeft = date.getTime() - System.currentTimeMillis();
        long days = timeLeft / (1000 * 60 * 60 * 24);
        long hours = (timeLeft % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (timeLeft % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (timeLeft % (1000 * 60)) / 1000;
        return days + "d " + hours + "h " + minutes + "m " + seconds + "s";
    }
}
