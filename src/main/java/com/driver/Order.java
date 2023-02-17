package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        this.id = id;
        int time = Integer.parseInt(deliveryTime.substring(0, 2)) * 60 + Integer.parseInt(deliveryTime.substring(3));
        this.deliveryTime = time;
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }
}
//package com.driver;
//
//public class Order {
//
//    private String id;
//    private int deliveryTime;
//    private boolean isAssigned=false;
//
//    public Order(String id, String deliveryTime) {
//
//        // The deliveryTime has to converted from string to int and then stored in the attribute
//        //deliveryTime  = HH*60 + MM
//        int hours=Integer.parseInt(deliveryTime.substring(0,2));
//        int mins=Integer.parseInt(deliveryTime.substring(3));
//        this.id=id;
//        this.deliveryTime=hours*60+mins;
//    }
//
//    public Order() {
//    }
//
//    public boolean isAssigned() {
//        return isAssigned;
//    }
//
//    public void setAssigned(boolean assigned) {
//        isAssigned = assigned;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public int getDeliveryTime() {return deliveryTime;}
//}
