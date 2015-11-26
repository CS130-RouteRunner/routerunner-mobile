package com.cs130.routerunner;

import com.cs130.routerunner.Actors.Box.Box;
import com.cs130.routerunner.Actors.Box.BoxFactory;
import com.cs130.routerunner.Actors.Box.BoxType;
import com.cs130.routerunner.Actors.Box.ConcreteBoxFactory;
import com.cs130.routerunner.Actors.Box.DeliveryPoint;
import com.cs130.routerunner.Actors.Truck;

import java.util.ArrayList;

/**
 * Created by Patrick on 11/15/2015.
 */
public class Player {
    private int money_;
    private ArrayList<Truck> truckList_;
    private Box deliveryPoint_;
    private Box spawnPoint_;
    private int playerNum_;
    public PlayerButtonInfo playerButtonInfo_;

    private int truckID_;

    public Player(int initialAmount, int playerNum, BoxFactory boxFactory){
        truckID_ = 0;
        money_ = initialAmount;
        truckList_ = new ArrayList<Truck>();
        playerNum_ = playerNum;
        deliveryPoint_ = boxFactory.createBox(BoxType.DeliveryPoint,
                playerNum_);
        spawnPoint_ = boxFactory.createBox(BoxType.SpawnPoint, playerNum_);
    }

    public Box getSpawnPoint() { return spawnPoint_;}
    public Box getDeliveryPoint(){ return deliveryPoint_;}
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

    public PlayerButtonInfo getPlayerButtonInfo() { return playerButtonInfo_; }

    public int getTruckID() { return truckID_; }

    public void incTruckID_() { this.truckID_++; }
}
