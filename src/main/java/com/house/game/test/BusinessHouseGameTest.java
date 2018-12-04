package com.house.game.test;

import com.house.game.exceptions.InSufficientChances;
import com.house.game.exceptions.InSufficientFundsException;
import com.house.game.exceptions.InvalidDiceOutputException;
import com.house.game.main.BusinessHouseGame;
import com.house.game.models.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BusinessHouseGameTest {
    BusinessHouseGame businessHouseGame = new BusinessHouseGame();
    static GameMetaData gameMetaData = new GameMetaData();

    @Before
    public void createDummyDataForGame() {

        int diceOutputs[] = {2, 2, 1, 4, 2, 3, 4, 1, 3, 2, 2, 7, 4, 7, 2, 4, 4, 2, 2, 2, 2};
        List<User> players = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User.Builder().name("TestPlayer_" + i).noOfChances(10).sequenceNumber(i).build();
            user.setNoOfChances(10);
            players.add(user);
        }
        gameMetaData.setPlayers(players);
        gameMetaData.setBankMoney(5000);
        gameMetaData.setDiceOutputs(diceOutputs);
        gameMetaData.setBoardInput("JHLHELHLHJ");
        gameMetaData.setHotelMap(new HashMap<>());
        gameMetaData.setUpdateHotel(new UpdateHotel());
    }

    @Test(expected = InvalidDiceOutputException.class)
    public void invalidDiceOutputExceptionTest() {
        businessHouseGame.startGame(gameMetaData);
    }

    @Test(expected = InSufficientChances.class)
    public void inSufficientChancesExceptionTest() {
        gameMetaData.getPlayers().stream().forEach(p -> {
            p.setNoOfChances(2);
        });
        businessHouseGame.startGame(gameMetaData);
    }

    @Test(expected = InSufficientFundsException.class)
    public void inSufficientFundsException() {
        gameMetaData.setBankMoney(0);
        businessHouseGame.startGame(gameMetaData);
    }

    @Test(expected = InSufficientFundsException.class)
    public void inSufficientFundsExceptionTest2() {
        gameMetaData.setBankMoney(100);
        businessHouseGame.startGame(gameMetaData);
    }

    @Test
    public void getPlayerTurnTest() {
        assertEquals(1, businessHouseGame.getPlayerTurn(gameMetaData.getPlayers().size(), 0));
        assertEquals(1, businessHouseGame.getPlayerTurn(gameMetaData.getPlayers().size(), gameMetaData.getPlayers().size()));
        assertEquals(gameMetaData.getPlayers().size(), businessHouseGame.getPlayerTurn(gameMetaData.getPlayers().size(), gameMetaData.getPlayers().size() - 1));
    }

    @Test
    public void buyHotelFirstTimeTest() {
        int bankMoney = gameMetaData.getBankMoney() + 200;
        int playerMoney = gameMetaData.getCurrentPlayer().getMoney() - 200;
        assertEquals(bankMoney, businessHouseGame.buyHotel(gameMetaData, 1));
        assertEquals(playerMoney, gameMetaData.getCurrentPlayer().getMoney());
    }

    @Test
    public void upgradeHotelSilverToGoldTest() {
        int bankMoney = gameMetaData.getBankMoney() + 100;
        gameMetaData.setCurrentPlayer(gameMetaData.getPlayers().get(0));
        int playerMoney = gameMetaData.getCurrentPlayer().getMoney() - 100;
        Map<Integer, Hotel> hotelMap = gameMetaData.getHotelMap();
        Hotel hotel = new SilverHotel(gameMetaData.getUpdateHotel());
        hotel.setUser(gameMetaData.getCurrentPlayer());
        hotelMap.put(1, hotel);
        assertEquals(bankMoney, businessHouseGame.upgradeHotelType(gameMetaData, 1, hotel));
        assertEquals(playerMoney, gameMetaData.getCurrentPlayer().getMoney());

    }

    @Test
    public void upgradeHotelGoldToPlatinumTest() {
        int bankMoney = gameMetaData.getBankMoney() + 200;
        gameMetaData.setCurrentPlayer(gameMetaData.getPlayers().get(0));
        int playerMoney = gameMetaData.getCurrentPlayer().getMoney() - 200;
        Map<Integer, Hotel> hotelMap = gameMetaData.getHotelMap();
        UpdateHotel updateHotel = gameMetaData.getUpdateHotel();
        updateHotel.setState(new GoldHotel(updateHotel));
        Hotel hotel = new GoldHotel(updateHotel);
        hotel.setUser(gameMetaData.getCurrentPlayer());
        hotelMap.put(1, hotel);
        assertEquals(bankMoney, businessHouseGame.upgradeHotelType(gameMetaData, 1, hotel));
        assertEquals(playerMoney, gameMetaData.getCurrentPlayer().getMoney());
    }
}
