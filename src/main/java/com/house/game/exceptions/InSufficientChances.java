package com.house.game.exceptions;

public class InSufficientChances extends RuntimeException {
    public InSufficientChances(String s){
        super(s);
    }

    public InSufficientChances(String s, Throwable t){
        super(s,t);
    }
}
