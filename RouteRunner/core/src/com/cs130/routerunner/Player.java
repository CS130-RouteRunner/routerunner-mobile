package com.cs130.routerunner;

import java.util.ArrayList;

/**
 * Created by Patrick on 11/15/2015.
 */
public class Player {
    private int money_;
    private ArrayList<Actor> truckList;

    public Player(int initialAmount){
        money_ = initialAmount;
        truckList = new ArrayList<Actor>();
    }

    public int getMoney(){
        return money_;
    }

    public void addMoney(int amount){
        money_ += amount;
    }

    public void addTruck(Actor truck){
        truckList.add(truck);
    }

    public ArrayList<Actor> getTruckList(){
        return truckList;
    }

    public void setTruckList(ArrayList<Actor> newTruckList){
        truckList = newTruckList;
    }
}
