package com.house.game.models;

public class Jail implements Cell {
    @Override
    public int getPoints() {
        return -150;
    }
}
