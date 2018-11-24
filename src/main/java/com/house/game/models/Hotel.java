package com.house.game.models;

public abstract class Hotel {

    public User user;

    public abstract int getValue();

    public abstract int getPoints();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void handleRequest(Hotel hotel){
        System.out.println("Nothing can be done here");
    }
}
