package com.cs130.routerunner;

import com.cs130.routerunner.Actors.Truck;

import java.util.ArrayList;

/**
 * Created by Patrick on 11/15/2015.
 */
public class Player {
    private int money_;
    private ArrayList<Truck> truckList_;
    public PlayerButtonInfo playerButtonInfo_;

    public Player(int initialAmount){
        money_ = initialAmount;
        truckList_ = new ArrayList<Truck>();
    }

    public int getMoney(){
        return money_;
    }

    public void setPlayerButtonInfo(PlayerButtonInfo pbi){
        playerButtonInfo_ = pbi;
    }

    public void addMoney(int amount){
        money_ += amount;
    }

    public void subtractMoney(int amount){
        money_ -= amount;
    }

    public void addTruck(Truck truck){
        truckList_.add(truck);
    }

    public ArrayList<Truck> getTruckList(){
        return truckList_;
    }

    public void setTruckList(ArrayList<Truck> newTruckList){
        truckList_ = newTruckList;
    }

    public PlayerButtonInfo getPlayerButtonInfo(){ return playerButtonInfo_;}

}
