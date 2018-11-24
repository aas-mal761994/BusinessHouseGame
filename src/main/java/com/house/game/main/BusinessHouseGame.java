package com.house.game.main;

import com.house.game.exception.InvalidDiceOutputException;
import com.house.game.models.*;

import java.util.*;

public class BusinessHouseGame {
    public static Map<Character, Cell> map = new HashMap<>();
    static {
        map.put('J', new Jail());
        map.put('L', new Lottery());
        map.put('E', new EmptyCell());
    }

    public static void main(String[] args) throws InvalidDiceOutputException {
        //Take inputs from user
        System.out.println("*****************************************WELCOME IN BUSINESS HOUSE GAME*****************************************");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of Players:");
        int numberOfPlayers = Integer.valueOf(sc.next());
        System.out.println("\n");
        System.out.print("Enter the maximum number of chances(Each Player):");
        int maximumNumberOfChances = Integer.valueOf(sc.next());
        System.out.println("\n");
        int bankMoney = 5000;
        UpdateHotel updateHotel = new UpdateHotel();
        List<User> players = new LinkedList<>();
        //Builder Design Pattern:Builder Pattern is used when we want to make an object immutable(by not making any setter and getter for the object)
        for (int p = 1; p <= numberOfPlayers; p++) {
            User player = new User.Builder().name("Player" + p).noOfChances(maximumNumberOfChances).sequenceNumber(p).build();
            players.add(player);
        }
        System.out.print("Enter the Board Input:");
        String boardInput = sc.next();
        System.out.println("\n");
        Map<Integer, Hotel> hotelMap = new HashMap<>();
        //TODO:think a way to get it from user
        int diceOutputs[] = {2, 2, 1, 4, 2, 3, 4, 1, 3, 2, 2, 7, 4, 7, 2, 4, 4, 2, 2, 2, 2};
        //int diceOutputs[]={2,2,1,4,4,2,4,4,2,2,2,1,4,4,2,4,4,2,2,2,1};
        for (int i = 0; i < diceOutputs.length; i++) {
            int playerTurn = getPlayerTurn(numberOfPlayers, i);
            User playerSelected = players.get(playerTurn - 1);
            if (diceOutputs[i] >= 1 && diceOutputs[i] <= 6 && playerSelected.getNoOfChances() <= maximumNumberOfChances) {
                int t = diceOutputs[i] - 1 + playerSelected.getPosition();
                if (((diceOutputs[i] - 1) + playerSelected.getPosition()) >= boardInput.length()) {
                    t = t % boardInput.length();
                }
                char pos = boardInput.charAt(t);
                if (map.containsKey(pos)) {
                    //Factory Design Pattern
                    Cell cell = map.get(pos);
                    playerSelected.setMoney(playerSelected.getMoney() + cell.getPoints());
                    bankMoney = bankMoney - cell.getPoints();
                } else {
                    if (hotelMap.containsKey(t)) {
                        //Factory Design Pattern
                        Hotel hotel = hotelMap.get(t);
                        // If hotel belongs to different user
                        if (!hotel.getUser().equals(playerSelected)) {
                            payRentForHotel(playerSelected, hotel);
                        } else {
                            bankMoney = upgradeHotelType(bankMoney, updateHotel, hotelMap, playerSelected, t, hotel);
                        }

                    } else {
                        // User is buying the Hotel for First Time
                        bankMoney = buyHotel(bankMoney, updateHotel, hotelMap, playerSelected, t);
                    }
                }
                playerSelected.setPosition(playerSelected.getPosition() + diceOutputs[i]);
            } else {
                throw new InvalidDiceOutputException("Please Enter Valid dice output which can lie between 1-6");
            }
        }
        showResults(bankMoney, players);

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
