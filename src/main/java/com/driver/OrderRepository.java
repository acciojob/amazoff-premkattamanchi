package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {
    HashMap<String,Order> orderMap;
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
        int mins=Integer.parseInt(time.substring(2));
        int totalTime=hours*60+mins;
        int count=0;
       for(String s:pairMap.get(partnerId)){
           if(orderMap.get(s).getDeliveryTime()>totalTime)
               count++;
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
    }

}
