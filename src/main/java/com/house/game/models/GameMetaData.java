package com.house.game.models;

import java.util.List;
import java.util.Map;

public class GameMetaData {
    private String boardInput;
    private int bankMoney;
    private int diceOutputs[];
    Map<Integer, Hotel> hotelMap;
    private UpdateHotel updateHotel;
    private List<User> players;
    private User currentPlayer;

    public String getBoardInput() {
        return boardInput;
    }

    public void setBoardInput(String boardInput) {
        this.boardInput = boardInput;
    }

    public int getBankMoney() {
        return bankMoney;
    }

    public void setBankMoney(int bankMoney) {
        this.bankMoney = bankMoney;
    }

    public int[] getDiceOutputs() {
        return diceOutputs;
    }

    public void setDiceOutputs(int[] diceOutputs) {
        this.diceOutputs = diceOutputs;
    }

    public Map<Integer, Hotel> getHotelMap() {
        return hotelMap;
    }

    public void setHotelMap(Map<Integer, Hotel> hotelMap) {
        this.hotelMap = hotelMap;
    }

    public UpdateHotel getUpdateHotel() {
        return updateHotel;
    }

    public void setUpdateHotel(UpdateHotel updateHotel) {
        this.updateHotel = updateHotel;
    }

    public List<User> getPlayers() {
        return players;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }

    public User getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(User currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
