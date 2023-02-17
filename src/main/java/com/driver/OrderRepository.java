package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

@Repository
public class OrderRepository {
    HashMap<String, Order> orderDb = new HashMap<>();
    HashMap<String, DeliveryPartner> partnerDb = new HashMap<>();
    HashMap<String, List<String>> pairDb = new HashMap<>();
    HashMap<String, String> assignedDb = new HashMap<>(); // <orderId, partnerId>

    public String addOrder(Order order) {
        orderDb.put(order.getId(), order);
        return "Added";
    }

    public String addPartner(String partnerId) {
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        partnerDb.put(partnerId, partner);
        return "Added";
    }

    public String addOrderPartnerPair(String orderId, String partnerId) {
        // This is basically assigning that order to that partnerId
        List<String> list = pairDb.getOrDefault(partnerId, new ArrayList<>());
        list.add(orderId);
        pairDb.put(partnerId, list);
        assignedDb.put(orderId, partnerId);
        DeliveryPartner partner = partnerDb.get(partnerId);
        partner.setNumberOfOrders(list.size());
        return "Added";

    }

    public Order getOrderById(String orderId) {
        // order should be returned with an orderId.
        for (String s : orderDb.keySet()) {
            if (s.equals(orderId)) {
                return orderDb.get(s);
            }
        }
        return null;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        // deliveryPartner should contain the value given by partnerId

        if (partnerDb.containsKey(partnerId)) {
            return partnerDb.get(partnerId);
        }
        return null;

    }

    public int getOrderCountByPartnerId(String partnerId) {
        // orderCount should denote the orders given by a partner-id
        int orders = pairDb.getOrDefault(partnerId, new ArrayList<>()).size();
        return orders;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        // orders should contain a list of orders by PartnerId

        List<String> orders = pairDb.getOrDefault(partnerId, new ArrayList<>());
        return orders;
    }

    public List<String> getAllOrders() {
        // Get all orders
        List<String> orders = new ArrayList<>();
        for (String s : orderDb.keySet()) {
            orders.add(s);
        }
        return orders;

    }

    public int getCountOfUnassignedOrders() {
        // Count of orders that have not been assigned to any DeliveryPartner
        int countOfOrders = orderDb.size() - assignedDb.size();
        return countOfOrders;
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        // countOfOrders that are left after a particular time of a DeliveryPartner
        int countOfOrders = 0;
        List<String> list = pairDb.get(partnerId);
        int deliveryTime = Integer.parseInt(time.substring(0, 2)) * 60 + Integer.parseInt(time.substring(3));
        for (String s : list) {
            Order order = orderDb.get(s);
            if (order.getDeliveryTime() > deliveryTime) {
                countOfOrders++;
            }
        }
        return countOfOrders;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        // Return the time when that partnerId will deliver his last delivery order.
        String time = "";
        List<String> list = pairDb.get(partnerId);
        int deliveryTime = 0;
        for (String s : list) {
            Order order = orderDb.get(s);
            deliveryTime = Math.max(deliveryTime, order.getDeliveryTime());
        }
        int hour = deliveryTime / 60;
        String sHour = "";
        if (hour < 10) {
            sHour = "0" + String.valueOf(hour);
        } else {
            sHour = String.valueOf(hour);
        }

        int min = deliveryTime % 60;
        String sMin = "";
        if (min < 10) {
            sMin = "0" + String.valueOf(min);
        } else {
            sMin = String.valueOf(min);
        }

        time = sHour + ":" + sMin;

        return time;

    }

    public String deletePartnerById(String partnerId) {
        // Delete the partnerId
        // And push all his assigned orders to unassigned orders.
        partnerDb.remove(partnerId);

        List<String> list = pairDb.getOrDefault(partnerId, new ArrayList<>());
        ListIterator<String> itr = list.listIterator();
        while (itr.hasNext()) {
            String s = itr.next();
            assignedDb.remove(s);
        }
        pairDb.remove(partnerId);
        return "Deleted";
    }

    public String deleteOrderById(String orderId) {

        // Delete an order and also
        // remove it from the assigned order of that partnerId
        orderDb.remove(orderId);
        String partnerId = assignedDb.get(orderId);
        assignedDb.remove(orderId);
        List<String> list = pairDb.get(partnerId);

        ListIterator<String> itr = list.listIterator();
        while (itr.hasNext()) {
            String s = itr.next();
            if (s.equals(orderId)) {
                itr.remove();
            }
        }
        pairDb.put(partnerId, list);

        return "Deleted";

    }
   /* HashMap<String,Order> orderMap;
    HashMap<String,DeliveryPartner> partnerMap;
    HashMap<String, List<String>> pairMap;

    public OrderRepository() {
        orderMap=new HashMap<>();
        partnerMap=new HashMap<>();
        pairMap=new HashMap<>();
    }
    public String addOrder(Order order){
        if(!orderMap.containsKey(order.getId())) {
            orderMap.put(order.getId(), order);
            return "order added";
        }
        return "order not added";
    }
    public String addPartner(String partnerId){
        if(!partnerMap.containsKey(partnerId)) {
            partnerMap.put(partnerId,new DeliveryPartner(partnerId));
            return "new Partner added";
        }
        return "partner not added";
    }
    public String addOrderPartnerPair(String orderId,String partnerId){
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)) {
            boolean isAssigned = orderMap.get(orderId).isAssigned();
            if (!pairMap.containsKey(partnerId))
                pairMap.put(partnerId,new ArrayList<String>());
            if (isAssigned == false){
                pairMap.get(partnerId).add(orderId);
                orderMap.get(orderId).setAssigned(true);
                return "orderpartner pair added succesfuly";
            }
        }
        return "cant be added..either partner or order not present";
    }
    public Order getOrderById(String orderId){
        if(orderMap.containsKey(orderId))
            return orderMap.get(orderId);
        return null;
    }
    public DeliveryPartner getPartnerById(String partnerId){
        if(partnerMap.containsKey(partnerId))
            return partnerMap.get(partnerId);
        return null;
    }
    public int getOrderCountByPartnerId(String partnerId){
        if(pairMap.containsKey(partnerId))
          return pairMap.get(partnerId).size();
        return 0;
    }
    public List<String> getOrdersByPartnerId(String partnerId){
        List<String> l=null;
        if(pairMap.containsKey(partnerId))
            l=pairMap.get(partnerId);
        return l;
    }
    public List<String> getAllOrders(){
       List<String> l=new ArrayList<>();
       for(String s:orderMap.keySet())
           l.add(s);
       return l;
    }
    public int getCountOfUnassignedOrders(){
        int count=0;
        for(Order obj:orderMap.values()){
            if(obj.isAssigned()==false)
                count++;
        }
        return count;
    }
    public int getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId){
        int hours=Integer.parseInt(time.substring(0,2));
        int mins=Integer.parseInt(time.substring(3));
        int totalTime=hours*60+mins;
        int count=0;
        if(pairMap.containsKey(partnerId)){
            for(String s:pairMap.get(partnerId)){
                if(orderMap.get(s).getDeliveryTime()>totalTime)
                    count++;
            }
        }
       return count;
    }
    public String getLastDeliveryTimeByPartnerId(String partnerId){
        int maxTime=0;
        for(String s: pairMap.get(partnerId)){
            if(maxTime<orderMap.get(s).getDeliveryTime())
                maxTime=orderMap.get(s).getDeliveryTime();
        }
        String hours=String.valueOf(maxTime/60);
        String mins=String.valueOf(maxTime%60);
        if(hours.length()==1)
            hours="0"+hours;
        if(mins.length()==1)
            mins="0"+mins;
        return hours+":"+mins;
    }
    public String deletePartnerById(String partnerId){
        if(partnerMap.containsKey(partnerId))
              partnerMap.remove(partnerId);
       if(pairMap.containsKey(partnerId)){
           for(String s:pairMap.get(partnerId))
               orderMap.get(s).setAssigned(false);
           pairMap.remove(partnerId);
           return " removed succesfully";
       }
       return " not found";
    }
    public void deleteOrderById(String orderId){
         if(orderMap.containsKey(orderId)) {
             orderMap.remove(orderId);
         }
         for(List<String> l:pairMap.values())
         {
             for(String s:l){
                 if(s.equals(orderId)) {
                     l.remove(orderId);
                 }
             }
         }
    }*/

}
