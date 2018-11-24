package com.house.game.models;

public class UpdateHotel {
    Hotel silverHotel;
    Hotel goldHotel;
    Hotel platinumHotel;

    Hotel state;

    public UpdateHotel() {
        silverHotel = new SilverHotel(this);
        goldHotel= new GoldHotel(this);
        platinumHotel= new PlatinumHotel(this);
        state=silverHotel;
    }

    public void updateHotel(Hotel hotel) {
        state.handleRequest(hotel);
    }

    @Override
    public String toString() {
        return state.toString();
    }

    public void setState(Hotel state) {
        this.state = state;
    }

    public Hotel getSilverHotel() {
        return silverHotel;
    }


    public Hotel getGoldHotel() {
        return goldHotel;
    }


    public Hotel getPlatinumHotel() {
        return platinumHotel;
    }

    public Hotel getState() {
        return state;
    }
}
