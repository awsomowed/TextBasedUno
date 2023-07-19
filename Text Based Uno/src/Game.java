/**
 * The program that controls the game of Uno
 *
 * @author Brandon Mover
 * @version 1.2.0
 **/
import java.util.Scanner;
import java.util.ArrayList;

public class Game{
    final static boolean TESTINGMODE = false;
    
    static ArrayList<Player> players = new ArrayList<Player>();
    
    static String hasUno = "";
    
    static int iOut = 0;
    
    static Deck playDeck;
    
    public static void main(String[] args){//#The main method controls the creation of the deck and players and the turns
    	System.out.println("If you can see this line that means that the console will not properly clear itself.\nWhenever you see the following symbol, that means that due to the output terminal the program has failed to clear the output.");
        System.out.print('\u000C');
        Scanner in = new Scanner(System.in);
        System.out.println();
        
        //#Start of the initilization
        
        System.out.println("How many decks do you want to play with?");
        int numDecks = 1;
        if (!TESTINGMODE){
            numDecks = stringInt( 1, 100);//Gets a valid int
        }
        else if (TESTINGMODE){
            System.out.println("1");
        }
        System.out.println();
        
        System.out.println("Do you want to set custom card multipliers?");
        String cardMultIn = "No";
        boolean cardMult = false;
        while (!cardMultIn.equals("no") && !cardMultIn.equals("yes")){//Loops until a valid input
            if (cardMultIn != "No"){
                System.out.println("Invalid Input, please type yes or no");
            }
            if (!TESTINGMODE){
                cardMultIn = in.nextLine().toLowerCase();
            }
            else if (TESTINGMODE){
                cardMultIn = "no";
                System.out.println("no");
            }
        }
        if (cardMultIn.equals("no")){
            cardMult = false;
        }
        else if(cardMultIn.equals("yes")){
            cardMult = true;
        }
        System.out.println();
        playDeck = new Deck(numDecks, cardMult);//Creates the deck
        
        System.out.println("How many players are playing?");
        int numPlayers = 5;
        if (!TESTINGMODE){
            numPlayers = stringInt(2, 100);//Gets valid input
        }
        else if (TESTINGMODE){
            System.out.println("5");
        }
        System.out.println();
        
        if (!TESTINGMODE){
            for (int i = 0; i < numPlayers; i++){
                boolean uniqeName = false;
                String playerName = "";
                while (!uniqeName){//Loops until a valid name
                    boolean didRun = false;
                    if (!didRun){
                        System.out.println("What is player " + (i + 1) + "'s name?");
                        didRun = true;
                    }
                    else{
                        System.out.println("That name is already taken, please choose another");
                    }
                    playerName = in.nextLine();
                    uniqeName = true;
                    for (int n = 0; n < players.size(); n++){
                        if (players.get(n).getName().equals(playerName)){
                            uniqeName = false;
                        }
                    }
                }
                Player playerTemp = new Player(playerName, playDeck);
                players.add(playerTemp);
                System.out.println();
            }
        }
        else if (TESTINGMODE){
            for (int i = 0; i < numPlayers; i++){
                System.out.println("What is player " + (i + 1) + "'s name?");
                String playerName = "player " + (i + 1);
                System.out.println("player " + (i + 1));
                Player playerTemp = new Player(playerName, playDeck);
                players.add(playerTemp);
                System.out.println();
            }
        }
        
        //#Start of the turn system
        
        String winner = "";
        boolean hasWinner = false;
        String lastOutput = "";
        while (!hasWinner){//Loops until there is a winner
            for (int i = 0; i < numPlayers; i++){//Goes through all the players
                iOut = i;
                hasUno = "";
                for (int p = 0; p < numPlayers; p++){//Loops to check for players that have uno
                    if (players.get(p).checkUno()){
                        hasUno = hasUno + players.get(p).getName() + " has uno, ";
                    }
                }
                if (hasUno.length() > 1){//Removes last ", " if there is someone with uno
                    hasUno = hasUno.substring(0, hasUno.length() - 2);
                }
                if (hasWinner == false){//Only runs if the game is still going
                    if (!TESTINGMODE){//Clears the output if TESTINGMODE is off
                        System.out.print('\u000C');
                        System.out.println();
                    }
                    lastOutput = players.get(i).takeTurn(lastOutput, playDeck);//Has a player take thier turn and stores information to be passed to the next turn
                    System.out.println("Enter to continue");
                    String confirm = in.nextLine();
                }
                if (players.get(i).checkWin()){
                    winner = players.get(i).getName();
                    hasWinner = true;
                }
                if (lastOutput == "reverse"){//Calls the reverse method if a reverse card was played
                    reverse(i);
                    lastOutput = "";
                }
            }
        }
        System.out.println(winner + " has won the game!");//Declares the winner
    }
    
    private static void reverse(int playerIndex){//#Reverses the ArrayList that stores the players in order to reverse the turn order
        Player placeholder = new Player();//Creates a blank placeholder player
        ArrayList<Player> reversedPlayers = new ArrayList<Player>();
        for (int i = 0; i < players.size(); i ++){//Fills the reversed array with placeholder players
            reversedPlayers.add(placeholder);
        }
        for (int i = 0; i < players.size(); i++){//Loops through all players
            int minusIndex = playerIndex - i;
            if (minusIndex < 0){//Gets one index that will be switched
                int diff = 0 - minusIndex;
                minusIndex = (players.size()) - diff;
            }
            int plusIndex = playerIndex + i;
            if (plusIndex > players.size()){//Gets the other index that will be switched
                int diff = plusIndex - players.size();
                plusIndex = 0 + diff;
            }
            if (plusIndex >= players.size()){//Prevents out of bounds
                plusIndex -= players.size();
            }
            if (minusIndex >= players.size()){//Prevents out of bounds
                minusIndex -= players.size();
            }
            if (plusIndex < 0){//Prevents out of bounds
                plusIndex += players.size();
            }
            if (minusIndex < 0){//Prevents out of bounds
                minusIndex += players.size();
            }
            reversedPlayers.set(plusIndex, players.get(minusIndex));//Sets the first player to the correct index
            reversedPlayers.set(minusIndex, players.get(plusIndex));//Sets the other to the correct index
        }
        players = reversedPlayers;//Copies the reversed array to the players array
    }
    
    public static void removeCards(){//#Makes all players put thier first card in thier hand down no matter what
        for (int i = 0; i < players.size(); i++){
            players.get(i).playCard(0, playDeck);
        }
    }
    
    public static int stringInt(int min, int max){//#Gets an input as a string and turns it into an int
        Scanner in = new Scanner(System.in);
        int choiceVal = -2;
        boolean validIn = false;
        while (!validIn){//Loops until valid input
            String valString = in.nextLine();
            choiceVal = 0;
            int carryVal = 0;
            for (int i = 0; i < valString.length(); i++){//Converts each char to int in expanded form, then adds them up to get an int
                int nextVal = (valString.charAt(valString.length() - (i + 1)) + carryVal) - 48;
                carryVal = 0;
                while (nextVal >= 10){//Makes sure the numbers add correctly
                    nextVal += -10;
                    carryVal += 1;
                }
                choiceVal += nextVal * Math.pow(10, i);
            }
            choiceVal += (carryVal * Math.pow(10, valString.length()));
            boolean isNumerical = true;
            boolean isValid = false;
            for (int i = 0; i < valString.length(); i++){//Makes sure there are no chars other than numbers in the input
                int nextVal = (valString.charAt(valString.length() - (i + 1)) + carryVal) - 48;
                if (!(nextVal < 10 && nextVal >= 0)){
                    isNumerical = false;
                }
            }
            if ((choiceVal >= min) && (choiceVal <= max)){//Checks to see if it is valid or not
                isValid = true;
            }
            if (isNumerical && isValid){
                validIn = true;
            }
            else{
                System.out.println("Invalid Input, range is " + min + " to " + max);
            }
        }
        return choiceVal;
    }
    
    public static int getPlayerNum(){//#Gets the number of players
        return players.size();
    }
    
    public static String getPlayerCards(){//#Gets all the players number of cards
        String toReturn = "";
        for (int i = 0; i < players.size(); i++){//Loops through all players
            if (i != iOut){//Excludes the player that is next for thier turn
                toReturn += players.get(i).getName() + " has " + players.get(i).cardNum() + " cards, ";
            }
        }
        return toReturn.substring(0, toReturn.length() - 2);//Removes the extra ", " at the end
    }
    
    public static String getPlayerUno(){//#Gets the players that have uno
        return hasUno;
    }
}