package com.house.game.models;

public class GoldHotel extends Hotel {

    private UpdateHotel updateHotel;

    public GoldHotel(UpdateHotel updateHotel) {
        this.updateHotel = updateHotel;
    }

    @Override
    public void handleRequest(Hotel hotel) {
        Hotel platinumHotel = updateHotel.getPlatinumHotel();
        updateHotel.setState(platinumHotel);
        platinumHotel.setUser(hotel.getUser());
        platinumHotel.getUser().setMoney(hotel.getUser().getMoney() - platinumHotel.getValue());
        System.out.println("Hotel upgraded to PLATINUM");
    }

    @Override
    public String toString() {
        return "GoldHotel";
    }

    @Override
    public int getValue() {
        return 100;
    }

    @Override
    public int getPoints() {
        return 150;
    }
}
