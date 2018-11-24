package com.house.game.exception;

public class InvalidDiceOutputException extends Exception {

    public InvalidDiceOutputException(String s) {
        super(s);
    }

    public InvalidDiceOutputException(String s, Throwable t) {
        super(s, t);
    }
}
