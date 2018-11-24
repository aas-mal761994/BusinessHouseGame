package com.house.game.models;

public class SilverHotel extends Hotel {
    private UpdateHotel updateHotel;

    public SilverHotel(UpdateHotel updateHotel){
        this.updateHotel=updateHotel;
    }

    @Override
    public int getValue() {
        return 200;
    }

    @Override
    public int getPoints() {
        return 50;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void handleRequest(Hotel hotel){
        Hotel goldHotel=updateHotel.getGoldHotel();
        updateHotel.setState(goldHotel);
        goldHotel.setUser(hotel.getUser());
        goldHotel.getUser().setMoney(hotel.getUser().getMoney()-goldHotel.getValue());
        System.out.println("Hotel upgraded to GOLD");
    }

    @Override
    public String toString(){
        return "SilverHotel";
    }
}
