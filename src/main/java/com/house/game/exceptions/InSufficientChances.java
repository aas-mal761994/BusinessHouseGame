package com.house.game.exceptions;

public class InSufficientChances extends Exception {
    public InSufficientChances(String s){
        super(s);
    }

    public InSufficientChances(String s, Throwable t){
        super(s,t);
    }
}
