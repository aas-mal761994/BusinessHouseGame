package com.house.game.exceptions;

public class InSufficientFundsException extends RuntimeException {
    public InSufficientFundsException(String s) {
        super(s);
    }
    public  InSufficientFundsException(String s,Throwable t){
        super(s,t);
    }

}
