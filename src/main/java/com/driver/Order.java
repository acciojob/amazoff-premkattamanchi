package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        int hours=Integer.parseInt(deliveryTime.substring(0,2));
        int mins=Integer.parseInt(deliveryTime.substring(2));
        this.id=id;
        this.deliveryTime=hours*60+mins;
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
