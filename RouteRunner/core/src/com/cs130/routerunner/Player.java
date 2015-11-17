package com.cs130.routerunner;

import com.cs130.routerunner.Actors.Base;
import com.cs130.routerunner.Actors.Truck;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Patrick on 11/15/2015.
 */
public class Player {
    private int money_;
    private ArrayList<Truck> truckList_;
    private Base base_;
    private HashMap<Integer, Truck> opponentActorList_;
    public PlayerButtonInfo playerButtonInfo_;

    private int truckID_;

    public Player(int initialAmount){
        truckID_ = 0;
        money_ = initialAmount;
        truckList_ = new ArrayList<Truck>();
        //TODO: change to real player base position
        base_ = new Base(500, 500);
	opponentActorList_ = new HashMap<Integer, Actor>();
    }

    public Base getBase(){ return base_;};
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
    //public Actor getOpponentActor(int id) { return opponentActorList_.get(id); }

    //public void addOpponentActor(Integer i, Actor a) { opponentActorList_.put(i, a); }
}
