/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author beepxD
 */
public class History {
    
    private int id;
    private String username;
    private String name;
    private Double price;
    private int stock;
    private Timestamp timestamp;

    private SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
    
    public History(String username, String name, int stock, double price){
        this.username = username;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.timestamp = new Timestamp(new Date().getTime());
    }
    
    public History(int id, String username, String name, int stock, double price, String timestamp){
        this.id = id;
        this.username = username;
        this.name = name;
        this.price = price;
        this.stock = stock;
        try {
            this.timestamp = new Timestamp(dateformat.parse(timestamp).getTime());
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice(){
        return price;
    }
    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
