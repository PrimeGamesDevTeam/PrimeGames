package net.primegames.components.store.payment;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Payment {

    public static class Builder{

        int id;
        String xuid;
        String transaction_id;
        String username;
        String email;
        String date;
        String time;
        String package_name;
        String package_price;
        String ip;
        String command;
        int inventory;
        String status;

        public static Builder create(){
            return new Builder();
        }

        public Builder id(int id){
            this.id = id;
            return this;
        }

        public Builder xuid(String xuid){
            this.xuid = xuid;
            return this;
        }

        public Builder transaction_id(String transaction_id){
            this.transaction_id = transaction_id;
            return this;
        }

        public Builder username(String username){
            this.username = username;
            return this;
        }

        public Builder email(String email){
            this.email = email;
            return this;
        }

        public Builder date(String date){
            this.date = date;
            return this;
        }

        public Builder time(String time){
            this.time = time;
            return this;
        }

        public Builder package_name(String package_name){
            this.package_name = package_name;
            return this;
        }

        public Builder package_price(String package_price){
            this.package_price = package_price;
            return this;
        }

        public Builder ip(String ip){
            this.ip = ip;
            return this;
        }

        public Builder command(String command){
            this.command = command;
            return this;
        }

        public Builder inventory(int inventory){
            this.inventory = inventory;
            return this;
        }

        public Builder status(String status){
            this.status = status;
            return this;
        }

        public Payment build(){
            if (this.id == 0 || this.xuid == null || this.transaction_id == null || this.username == null || this.email == null || this.date == null || this.time == null || this.package_name == null || this.package_price == null || this.ip == null || this.command == null || this.inventory == 0 || this.status == null){
                throw new IllegalArgumentException("Payment is not valid, Please provide all the required fields");
            }
            return new Payment(id, xuid, transaction_id, username, email, date, time, package_name, package_price, ip, command, inventory, status);
        }

    }

    private int id;
    private String xuid;
    private String transaction_id;
    private String username;
    private String email;
    private String date;
    private String time;
    private String package_name;
    private String package_price;
    private String ip;
    @Getter @Setter
    private String claimed_ip = null;
    @Getter @Setter
    private boolean claimed = false;
    private String command;
    private int inventory;
    String status;

    public Payment(int id, String xuid, String transaction_id, String username, String email, String date, String time, String package_name, String package_price, String ip, String command, int inventory, String status) {
        this.id = id;
        this.xuid = xuid;
        this.transaction_id = transaction_id;
        this.username = username;
        this.email = email;
        this.date = date;
        this.time = time;
        this.package_name = package_name;
        this.package_price = package_price;
        this.ip = ip;
        this.command = command;
        this.inventory = inventory;
        this.status = status;
    }

}
