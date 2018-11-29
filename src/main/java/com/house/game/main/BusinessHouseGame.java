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
        try {
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
            int diceOutputs[]={2,2,1,4,4,2,4,4,2,2,2,1,4,4,2,4,4,2,2,2,1};
            bankMoney = startGame(players, boardInput, bankMoney, updateHotel, hotelMap, diceOutputs);
            showResults(bankMoney, players);
        } catch (InSufficientChances exception) {
            System.out.println("Exception due to: " + exception.getMessage());
        } catch (InSufficientFundsException exception) {
            System.out.println("Exception due to: " + exception.getMessage());
        } catch (InvalidDiceOutputException exception) {
            System.out.println("Exception due to: " + exception.getMessage());
        }

    }

    private static int startGame(List<User> players, String boardInput, int bankMoney, UpdateHotel updateHotel, Map<Integer, Hotel> hotelMap, int[] diceOutputs) throws InvalidDiceOutputException, InSufficientChances, InSufficientFundsException {
        for (int i = 0; i < diceOutputs.length; i++) {
            int playerTurn = getPlayerTurn(players.size(), i);
            User playerSelected = players.get(playerTurn - 1);
            if (diceOutputs[i] >= 1 && diceOutputs[i] <= 6 && playerSelected.getNoOfChances() > 0 && bankMoney > 0 && playerSelected.getMoney() > 0) {
                bankMoney = move(boardInput, bankMoney, updateHotel, hotelMap, diceOutputs[i], playerSelected);
            } else if (diceOutputs[i] > 6) {
                throw new InvalidDiceOutputException("Please Enter Valid dice output which can lie between 1-6");
            } else if (playerSelected.getNoOfChances() < 0) {
                throw new InSufficientChances("Number of Chances exhausted for player:" + playerSelected.getName());
            } else if (bankMoney < 0) {
                throw new InSufficientFundsException("Bank is out of Money :-( with balance:" + bankMoney);
            } else if (playerSelected.getMoney() < 0) {
                System.out.println("Player " + playerSelected.getName() + " is out of money, So OUT OF GAME");
                players.remove(playerSelected);
            }
            playerSelected.setNoOfChances(playerSelected.getNoOfChances() - 1);
        }
        return bankMoney;
    }

    private static int move(String boardInput, int bankMoney, UpdateHotel updateHotel, Map<Integer, Hotel> hotelMap, int diceOutput, User playerSelected) {
        int positionOnBoard = getPositionOnBoard(boardInput, diceOutput, playerSelected);
        char cellOnBoard = boardInput.charAt(positionOnBoard);
        if (cellsInBoardMap.containsKey(cellOnBoard)) {
            bankMoney = otherCells(bankMoney, playerSelected, cellOnBoard);
        } else {
            bankMoney = hotelCell(bankMoney, updateHotel, hotelMap, playerSelected, positionOnBoard);
        }
        playerSelected.setPosition(playerSelected.getPosition() + diceOutput);
        return bankMoney;
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

    private static int hotelCell(int bankMoney, UpdateHotel updateHotel, Map<Integer, Hotel> hotelMap, User playerSelected, int positionOnBoard) {
        if (hotelMap.containsKey(positionOnBoard)) {
            //Factory Design Pattern
            Hotel hotel = hotelMap.get(positionOnBoard);
            // If hotel belongs to different user
            if (!hotel.getUser().equals(playerSelected)) {
                payRentForHotel(playerSelected, hotel);
            } else {
                bankMoney = upgradeHotelType(bankMoney, updateHotel, hotelMap, playerSelected, positionOnBoard, hotel);
            }

        } else {
            // User is buying the Hotel for First Time
            bankMoney = buyHotel(bankMoney, updateHotel, hotelMap, playerSelected, positionOnBoard);
        }
        return bankMoney;
    }

    private static List<User> getPlayersDetails(Scanner sc) {
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

    private static void showResults(int bankMoney, List<User> players) {
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

    private static void payRentForHotel(User playerSelected, Hotel hotel) {
        User hotelOwner = hotel.getUser();
        hotelOwner.setMoney(hotelOwner.getMoney() + hotel.getPoints());
        playerSelected.setMoney(playerSelected.getMoney() - hotel.getPoints());
    }

    private static int upgradeHotelType(int bankMoney, UpdateHotel updateHotel, Map<Integer, Hotel> hotelMap, User playerSelected, int t, Hotel hotel) {
        //Upgrade the hotel to gold or platinum(State Design Pattern)
        updateHotel.updateHotel(hotel);
        updateHotel.getState().setUser(playerSelected);
        if (!(updateHotel.getState().getClass() == hotelMap.get(t).getClass())) {
            hotelMap.put(t, updateHotel.getState());
            //If hotel is updated only then it should be added
            bankMoney = bankMoney + updateHotel.getState().getValue();
        }
        return bankMoney;
    }

    private static int buyHotel(int bankMoney, UpdateHotel updateHotel, Map<Integer, Hotel> hotelMap, User playerSelected, int t) {
        Hotel silverHotel = new SilverHotel(updateHotel);
        silverHotel.setUser(playerSelected);
        playerSelected.setMoney(playerSelected.getMoney() - silverHotel.getValue());
        bankMoney = bankMoney + silverHotel.getValue();
        hotelMap.put(t, silverHotel);
        return bankMoney;
    }

    private static int getPlayerTurn(int numberOfPlayers, int i) {
        int playerTurn = (i + 1) % numberOfPlayers;
        if (i > 0 && playerTurn == 0) {
            playerTurn = numberOfPlayers;
        }
        return playerTurn;
    }

}
