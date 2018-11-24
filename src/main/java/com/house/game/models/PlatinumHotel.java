package com.house.game.models;

public class PlatinumHotel extends Hotel {

    private UpdateHotel updateHotel;

    public PlatinumHotel(UpdateHotel updateHotel) {
        this.updateHotel = updateHotel;
    }


    @Override
    public void handleRequest(Hotel hotel){
        System.out.println("Hotel is already at PLATINUM");
       // updateHotel.setState(updateHotel.getPlatinumHotel());
    }

    @Override
    public String toString(){
        return "PlatinumHotel";
    }
    @Override
    public int getPoints() { // method for getting rent
        return 300;
    }

    @Override
    public int getValue() {
        return 200;
    }
}
