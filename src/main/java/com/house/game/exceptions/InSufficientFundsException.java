package com.house.game.exceptions;

public class InSufficientFundsException extends Exception {
    public InSufficientFundsException(String s) {
        super(s);
    }
    public  InSufficientFundsException(String s,Throwable t){
        super(s,t);
    }

}
