package com.house.game.main;

import com.house.game.exceptions.InSufficientChances;
import com.house.game.exceptions.InSufficientFundsException;
import com.house.game.exceptions.InvalidDiceOutputException;
import com.house.game.models.*;

import java.util.*;

public class BusinessHouseGame {
    public static Map<Character, Cell> cellsInBoardMap = new HashMap<>();

    static {
        cellsInBoardMap.put('J', new Jail());
        cellsInBoardMap.put('L', new Lottery());
        cellsInBoardMap.put('E', new EmptyCell());
    }

    public static void main(String[] args) {
        System.out.println("*****************************************WELCOME IN BUSINESS HOUSE GAME*****************************************");
        businessHouseGame();
    }

    private static void businessHouseGame() {
        Scanner sc = new Scanner(System.in);
        //Builder Design Pattern:Builder Pattern is used when we want to make an object immutable(by not making any setter and getter for the object)
        List<User> players = getPlayersDetails(sc);
        System.out.print("Enter the Board Input:");
        String boardInput = sc.next();
        System.out.println("\n");
        int bankMoney = 5000;
        UpdateHotel updateHotel = new UpdateHotel();
        Map<Integer, Hotel> hotelMap = new HashMap<>();
        //TODO:think a way to get it from user
        //int diceOutputs[] = {2, 2, 1, 4, 2, 3, 4, 1, 3, 2, 2, 7, 4, 7, 2, 4, 4, 2, 2, 2, 2};
        int diceOutputs[] = {2, 2, 1, 4, 4, 2, 4, 4, 2, 2, 2, 1, 4, 4, 2, 4, 4, 2, 2, 2, 1};
        GameMetaData gameMetaData = new GameMetaData();
        gameMetaData.setBankMoney(bankMoney);
        gameMetaData.setBoardInput(boardInput);
        gameMetaData.setDiceOutputs(diceOutputs);
        gameMetaData.setHotelMap(hotelMap);
        gameMetaData.setPlayers(players);
        gameMetaData.setUpdateHotel(updateHotel);
        bankMoney = startGame(gameMetaData);
        showResults(bankMoney, players);
    }


    public static int startGame(GameMetaData gameMetaData) {
            for (int i = 0; i < gameMetaData.getDiceOutputs().length; i++) {
                int playerTurn = getPlayerTurn(gameMetaData.getPlayers().size(), i);
                int diceOutputs[] = gameMetaData.getDiceOutputs();
                User playerSelected = gameMetaData.getPlayers().get(playerTurn - 1);
                gameMetaData.setCurrentPlayer(playerSelected);
                if (diceOutputs[i] >= 1 && diceOutputs[i] <= 6 && playerSelected.getNoOfChances() > 0 && gameMetaData.getBankMoney() > 0 && playerSelected.getMoney() > 0) {
                    gameMetaData.setBankMoney(moveOnBoard(gameMetaData, diceOutputs[i]));
                } else if (diceOutputs[i] > 6) {
                    throw new InvalidDiceOutputException("Please Enter Valid dice output which can lie between 1-6");
                } else if (playerSelected.getNoOfChances() <= 0) {
                    throw new InSufficientChances("Number of Chances exhausted for player:" + playerSelected.getName());
                } else if (gameMetaData.getBankMoney() <= 0) {
                    throw new InSufficientFundsException("Bank is out of Money :-( with balance:" + gameMetaData.getBankMoney());
                } else if (playerSelected.getMoney() < 0) {
                    System.out.println("Player " + playerSelected.getName() + " is out of money, So OUT OF GAME");
                    gameMetaData.getPlayers().remove(playerSelected);
                }
                playerSelected.setNoOfChances(playerSelected.getNoOfChances() - 1);
            }

        return gameMetaData.getBankMoney();
    }

    private static int moveOnBoard(GameMetaData gameMetaData, int diceOutput) {
        int positionOnBoard = getPositionOnBoard(gameMetaData.getBoardInput(), diceOutput, gameMetaData.getCurrentPlayer());
        char cellOnBoard = gameMetaData.getBoardInput().charAt(positionOnBoard);
        if (cellsInBoardMap.containsKey(cellOnBoard)) {
            gameMetaData.setBankMoney(otherCells(gameMetaData.getBankMoney(), gameMetaData.getCurrentPlayer(), cellOnBoard));
        } else {
            gameMetaData.setBankMoney(hotelCell(gameMetaData, positionOnBoard));
        }
        gameMetaData.getCurrentPlayer().setPosition(gameMetaData.getCurrentPlayer().getPosition() + diceOutput);
        return gameMetaData.getBankMoney();
    }

    private static int getPositionOnBoard(String boardInput, int diceOutput, User playerSelected) {
        int positionOnBoard = diceOutput - 1 + playerSelected.getPosition();
        if (((diceOutput - 1) + playerSelected.getPosition()) >= boardInput.length()) {
            positionOnBoard = positionOnBoard % boardInput.length();
        }
        return positionOnBoard;
    }

    private static int otherCells(int bankMoney, User playerSelected, char cellOnBoard) {
        Cell cell = cellsInBoardMap.get(cellOnBoard);
        playerSelected.setMoney(playerSelected.getMoney() + cell.getPoints());
        bankMoney = bankMoney - cell.getPoints();
        return bankMoney;
    }

    private static int hotelCell(GameMetaData gameMetaData, int positionOnBoard) {
        if (gameMetaData.getHotelMap().containsKey(positionOnBoard)) {
            //Factory Design Pattern
            Hotel hotel = gameMetaData.getHotelMap().get(positionOnBoard);
            if (!hotel.getUser().equals(gameMetaData.getCurrentPlayer())) {
                payRentForHotel(gameMetaData.getCurrentPlayer(), hotel);
            } else {
                gameMetaData.setBankMoney(upgradeHotelType(gameMetaData, positionOnBoard, hotel));
            }

        } else {
            // User is buying the Hotel for First Time
            gameMetaData.setBankMoney(buyHotel(gameMetaData, positionOnBoard));
        }
        return gameMetaData.getBankMoney();
    }

    public static List<User> getPlayersDetails(Scanner sc) {
        System.out.print("Enter the number of Players:");
        int numberOfPlayers = Integer.valueOf(sc.next());
        List<User> players = new LinkedList<>();
        System.out.println("\n");
        System.out.print("Enter the maximum number of chances(Each Player):");
        int maximumNumberOfChances = Integer.valueOf(sc.next());
        for (int p = 1; p <= numberOfPlayers; p++) {
            User player = new User.Builder().name("Player" + p).noOfChances(maximumNumberOfChances).sequenceNumber(p).build();
            players.add(player);
        }
        System.out.println("\n");
        return players;
    }

    public static void showResults(int bankMoney, List<User> players) {
        players.stream().forEach(p -> {
            System.out.println(p.getName() + " has " + p.getMoney());
        });
        System.out.println("Money Left at bank:" + bankMoney);
        Optional<User> maximum = players.stream().max((p1, p2) -> {
            if (p1.getMoney() > p2.getMoney())
                return 1;
            else if (p1.getMoney() < p2.getMoney())
                return -1;
            else
                return 0;
        });
        System.out.println("Winner is " + maximum.get().getName());
    }

    public static void payRentForHotel(User playerSelected, Hotel hotel) {
        User hotelOwner = hotel.getUser();
        hotelOwner.setMoney(hotelOwner.getMoney() + hotel.getPoints());
        playerSelected.setMoney(playerSelected.getMoney() - hotel.getPoints());
    }

    public static int upgradeHotelType(GameMetaData gameMetaData, int t, Hotel hotel) {
        //Upgrade the hotel to gold or platinum(State Design Pattern)
        UpdateHotel updateHotel = gameMetaData.getUpdateHotel();
        Map<Integer, Hotel> hotelMap = gameMetaData.getHotelMap();
        updateHotel.updateHotel(hotel);
        updateHotel.getState().setUser(gameMetaData.getCurrentPlayer());
        if (!(updateHotel.getState().getClass() == hotelMap.get(t).getClass())) {
            hotelMap.put(t, gameMetaData.getUpdateHotel().getState());
            //If hotel is updated only then it should be added
            gameMetaData.setBankMoney(gameMetaData.getBankMoney() + updateHotel.getState().getValue());
        }
        return gameMetaData.getBankMoney();
    }

    public static int buyHotel(GameMetaData gameMetaData, int t) {
        Hotel silverHotel = new SilverHotel(gameMetaData.getUpdateHotel());
        silverHotel.setUser(gameMetaData.getCurrentPlayer());
        gameMetaData.getCurrentPlayer().setMoney(gameMetaData.getCurrentPlayer().getMoney() - silverHotel.getValue());
        gameMetaData.setBankMoney(gameMetaData.getBankMoney() + silverHotel.getValue());
        gameMetaData.getHotelMap().put(t, silverHotel);
        return gameMetaData.getBankMoney();
    }

    public static int getPlayerTurn(int numberOfPlayers, int i) {
        int playerTurn = (i + 1) % numberOfPlayers;
        if (i > 0 && playerTurn == 0) {
            playerTurn = numberOfPlayers;
        }
        return playerTurn;
    }

}
