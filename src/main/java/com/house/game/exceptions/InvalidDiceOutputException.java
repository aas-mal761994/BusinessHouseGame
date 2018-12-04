package com.house.game.exceptions;

public class InvalidDiceOutputException extends RuntimeException {

    public InvalidDiceOutputException(String s) {
        super(s);
    }

    public InvalidDiceOutputException(String s, Throwable t) {
        super(s, t);
    }
}
