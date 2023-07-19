/**
 * Stores information on all players playing the game
 * Contains all information on card uses/avaliability
 *
 * @author Brandon Moyer
 * @version 1.1.0
 */

import java.util.Scanner;
import java.util.ArrayList;

public class Player{
    ArrayList<Card> hand = new ArrayList<Card>();
    String playerName;
    Scanner in = new Scanner(System.in);
    public Player(String playerName, Deck gameDeck){//#Constructs a regular player
        this.playerName=playerName;
        for (int i = 0; i < 7; i++){//Draws starting cards
            drawCard(gameDeck);
        }
    }
    
    public Player(){//#Constructs a empty placeholder player
        playerName = "";
    }
    
    public String takeTurn(String lastSpecial, Deck gameDeck){//#Controls the functionality of turns
        String toReturn = "";
        System.out.println();
        System.out.println("====================================================================================================");//Line break in between turns, 100 = signs
        System.out.println();
        System.out.println(playerName + "'s Turn");
        
        boolean startedTurn = false;
        if (Game.TESTINGMODE){//Skips if TESTINGMODE is active
            startedTurn = true;
        }
        while (!startedTurn){//Loops until the turn is started by player
            System.out.println("Type start to start turn");
            String inputed = in.nextLine().toLowerCase();
            if (inputed.equals("start")){
                startedTurn = true;
            }
        }
        System.out.println();
        System.out.println(Game.getPlayerCards());//Gets some info on other players
        String unoOut = Game.getPlayerUno();
        if (unoOut != ""){
            System.out.println(unoOut);
        }
        
        System.out.print(gameDeck.topCard());//Gets the top card
        if (lastSpecial.length() >= 8 && lastSpecial.charAt(7) == '4'){//#Checks for plus 4 as last played
            int hundreds = lastSpecial.charAt(0) - 48;//Gets parts of an int from the string
            int tens = lastSpecial.charAt(1) - 48;
            int ones = lastSpecial.charAt(2) - 48;
            System.out.println(", +" + (hundreds * 100 + tens * 10 + ones));//Information about the stacked number
            if (hasPlus4()){//#Checks for stackable plus 4
                System.out.println("Play a plus 4 or select 0 to draw cards instead");
                int choiceIndex = indexSelect(lastSpecial, Deck.topCard(), false);//Gets input
                if (choiceIndex != -1){
                    playCard(choiceIndex, gameDeck);//Plays chosen card as a regular plus 4
                    ones += 4;//Updates the stacked number
                    if (ones >= 10){//Reformats the stacked number
                        ones += -10;
                        tens += 1;
                    }
                    if (tens >= 10){
                        tens += -10;
                        hundreds += 1;
                    }
                    if (hundreds >= 10){//Prevents errors
                        hundreds += -10;
                    }
                    toReturn = hundreds + "" + tens + "" + ones + "plus4";//Prepares info to be returned
                    gameDeck.useCard(new Card(colorSelect(), "plus4"));//Puts a temp card using the chosen color in the pile
                }
                else if (choiceIndex == -1){
                    int drawNum = hundreds * 100 + tens * 10 + ones;
                    plusDraw(drawNum, gameDeck);//Draws all the required cards
                }
            }
            else{
                int drawNum = hundreds * 100 + tens * 10 + ones;
                System.out.println("You dont have a plus 4");
                plusDraw(drawNum, gameDeck);//Draws all the required cards
            }
        }
        else if (lastSpecial.length() >= 8 && lastSpecial.charAt(7) == '2'){//#Checks for plus 2 as last played
            int hundreds = lastSpecial.charAt(0) - 48;//Gets parts of an int from the string
            int tens = lastSpecial.charAt(1) - 48;
            int ones = lastSpecial.charAt(2) - 48;
            System.out.println(", +" + (hundreds * 100 + tens * 10 + ones));//Information about the stacked number
            if (hasPlus2()){//#Checks for a stackable plus 2
                System.out.println("Play a plus 2 or select 0 to draw cards instead");
                int choiceIndex = indexSelect(lastSpecial, Deck.topCard(), false);//Gets input
                if (choiceIndex != -1){
                    playCard(choiceIndex, gameDeck);//Plays chosen card
                    ones += 2;//Updates the stacked number
                    if (ones >= 10){//Reformats the stacked number
                        ones += -10;
                        tens += 1;
                    }
                    if (tens >= 10){
                        tens += -10;
                        hundreds += 1;
                    }
                    if (hundreds >= 10){//Prevents errors
                        hundreds += -10;
                    }
                    toReturn = hundreds + "" + tens + "" + ones + "plus2";//Prepared info to be returned
                }
                else if (choiceIndex == -1){
                    int drawNum = hundreds * 100 + tens * 10 + ones;
                    plusDraw(drawNum, gameDeck);//Draws all the required cards
                }
            }
            else{
                int drawNum = hundreds * 100 + tens * 10 + ones;
                System.out.println("You dont have a plus 2");
                plusDraw(drawNum, gameDeck);//Draws all the requried cards
            }
        }
        else if (lastSpecial.contains("skip")){//#Checks for skip as last played
            System.out.println();
            System.out.println("Skipped");//Skips players turn
        }
        else{//#Only runs if no spacial card was last played
            boolean drewCards = handCheck(gameDeck);//Checks to see if you have a playable card
            int choiceIndex = -2;
            if (!drewCards){//Runs if you didnt draw cards
                System.out.println("\nSelect a card to play or 0 to draw cards");
                choiceIndex = indexSelect(lastSpecial, gameDeck.topCard(), false);//Gets input
            }
            else//Runs if you drew cards
            {
                System.out.println("\nSelect a card to play");
                choiceIndex = indexSelect(lastSpecial, gameDeck.topCard(), true);//Gets input
            }
            if (choiceIndex > -1){
                if (hand.get(choiceIndex).getSymbol() == "plus4"){//Plays a plus 4
                    toReturn = "004plus4";
                    playCard(choiceIndex, gameDeck);
                    gameDeck.useCard(new Card(colorSelect(), "plus4"));
                }
                else if (hand.get(choiceIndex).getSymbol() == "plus2"){//Plays a plus 2
                    toReturn = "002plus2";
                    playCard(choiceIndex, gameDeck);
                }
                else if (hand.get(choiceIndex).getSymbol() == "reverse"){//Plays a reverse
                    toReturn = "reverse";
                    playCard(choiceIndex, gameDeck);
                }
                else if (hand.get(choiceIndex).getSymbol() == "skip"){//Plays a skip
                    toReturn = "skip";
                    playCard(choiceIndex, gameDeck);
                }
                else if (hand.get(choiceIndex).getSymbol() == "wild"){//Plays a wild
                    playCard(choiceIndex, gameDeck);
                    gameDeck.useCard(new Card(colorSelect(), "wild"));
                }
                else{//Plays a number card
                    playCard(choiceIndex, gameDeck);
                }
            }
            else if (choiceIndex <= -1){
                forcedDraw(gameDeck);//Draws cards
                choiceIndex = indexSelect(lastSpecial, gameDeck.topCard(), true);//Gets input
                if (hand.get(choiceIndex).getSymbol() == "plus4"){//Plays plus 4
                    toReturn = "004plus4";
                    playCard(choiceIndex, gameDeck);
                    gameDeck.useCard(new Card(colorSelect(), "plus4"));
                }
                else if (hand.get(choiceIndex).getSymbol() == "plus2"){//Plays plus 2
                    toReturn = "002plus2";
                    playCard(choiceIndex, gameDeck);
                }
                else if (hand.get(choiceIndex).getSymbol() == "reverse"){//Plays reverse
                    toReturn = "reverse";
                    playCard(choiceIndex, gameDeck);
                }
                else if (hand.get(choiceIndex).getSymbol() == "skip"){//Plays skip
                    toReturn = "skip";
                    playCard(choiceIndex, gameDeck);
                }
                else if (hand.get(choiceIndex).getSymbol() == "wild"){//Plays wild
                    playCard(choiceIndex, gameDeck);
                    gameDeck.useCard(new Card(colorSelect(), "wild"));
                }
                else{//Plays number card
                    playCard(choiceIndex, gameDeck);
                }
            }
        }
        return toReturn;
    }
    
    private String colorSelect(){//#Gets a valid color input
        String colorChoice = "";
        boolean validInput = false;
        while (!validInput){//Loops until valid input
            System.out.println("What color would you like?");
            colorChoice = in.nextLine().toLowerCase();//Scanner input
            if (colorChoice.equals("blue") || colorChoice.equals("green") || colorChoice.equals("yellow") || colorChoice.equals("red")){//Checks for valid color inputs
                validInput = true;
            }
            else{
                System.out.println("Not a valid color");
            }
        }
        return colorChoice;
    }
    
    private int indexSelect(String input, Card topCard, boolean alreadyDrew){//#Gets a playable input
        int choiceIndex = -2;
        boolean playableCard = false;
        System.out.println(getHand());
        while (!playableCard){//Loops until valid input
            choiceIndex = Game.stringInt(0, hand.size()) - 1;//Gets input
            if ((input == "") && (choiceIndex > -1)){//No special card checks
                if ((choiceIndex < hand.size()) && (hand.get(choiceIndex).getColor() == "wild")){
                    playableCard = true;
                }
                else if ((choiceIndex < hand.size()) && ((hand.get(choiceIndex).getColor().equals(topCard.getColor())) || (hand.get(choiceIndex).getSymbol().equals(topCard.getSymbol())))){
                    playableCard = true;
                }
                else{
                    System.out.println("Not valid input or card can't be played");
                    System.out.println(getHand());
                }
            }
            else if ((input.contains("plus2")) && (choiceIndex > -1)){//Plus 2 checks
                if ((choiceIndex < hand.size()) && (hand.get(choiceIndex).getSymbol() == "plus2")){
                    playableCard = true;
                }
                else{
                    System.out.println("Not valid input or card can't be played");
                    System.out.println(getHand());
                }
            }
            else if ((input.contains("plus4")) && (choiceIndex > -1)){//Plus 4 checks
                if ((choiceIndex < hand.size()) && (hand.get(choiceIndex).getSymbol() == "plus4")){
                    playableCard = true;
                }
                else{
                    System.out.println("Not valid input or card can't be played");
                    System.out.println(getHand());
                }
            }
            else if (choiceIndex == -1 && !alreadyDrew){//Draw check
                playableCard = true;
            }
            else{//Invalid input
                System.out.println("Not valid input or card can't be played");
                System.out.println(getHand());
            }
        }
        return choiceIndex;
    }
    
    public String getName(){//#Gets the players name
        return playerName;
    }
    
    public boolean hasPlus2(){//#Checks to see if you have a plus 2
        boolean toReturn = false;
        for (int i = 0; i < hand.size(); i++){//Loops through all cards in hand
            if (hand.get(i).getSymbol() == "plus2"){
                toReturn = true;
            }
        }
        return toReturn;
    }
    
    public boolean hasPlus4(){//#Checks to see if you have a plus 4
        boolean toReturn = false;
        for (int i = 0; i < hand.size(); i++){//Loops through all cards in hand
            if (hand.get(i).getSymbol() == "plus4"){
                toReturn = true;
            }
        }
        return toReturn;
    }
    
    private boolean handCheck(Deck gameDeck){//#^Checks to see if you have a card you can play, and if not, draws cards
        boolean toReturn = false;
        boolean playableCard = false;
        boolean alreadyRan = false;
        while (!playableCard){//Loops untl playable card is in hand
            for (int i = 0; i < hand.size(); i++){//Checks hand for playable cards

                if (hand.get(i).playable(gameDeck.topCard())){
                    playableCard = true;
                }
            }
            if (!playableCard){//Runs if no playable cards
                if (!alreadyRan){//Only runs once per method call
                    System.out.println("\nYou dont have a card you can play");
                    alreadyRan = true;
                }
                System.out.println("Drawn Card: " + drawCard(gameDeck));//Draws cards
                toReturn = true;
            }
        }
        return toReturn;
    }
    
    private void forcedDraw(Deck gameDeck){//#^Draws cards until you get a new playable card
        boolean playableNewCard = false;
        Card newCard = new Card("", "");
        while (!playableNewCard){//Loops until playable card is drawn
            newCard = drawCard(gameDeck);
            if (newCard.playable(gameDeck.topCard())){
                playableNewCard = true;
            }
            if (!playableNewCard){
                System.out.println("Drawn Card: " + newCard);
            }
        }
    }
    
    private void plusDraw(int drawNum, Deck gameDeck){//#Draws cards until they are all drawn
        for (int i = 0; i < drawNum; i++){//Loops until enough cards have been drawn
            System.out.println("Drawn Card: " + drawCard(gameDeck) + ", " + (drawNum - (i + 1)) + " left");//Prints the info on the last drawn card and how many are left
        }
        System.out.println(getHand());
    }
    
    private Card drawCard(Deck gameDeck){//#Gets a card from the draw pile and adds it to the hand and returns it
        Card drawnCard = gameDeck.nextCard();//Draws the card
        hand.add(hand.size(), drawnCard);
        return drawnCard;
    }
    
    public String getHand(){//#Gets all cards in the players hand
        String toReturn = "";
        for (int i = 0; i < hand.size(); i++){//Loops through all cards in hand
            toReturn = toReturn + (i + 1) + ": ";//Cards Index
            toReturn = toReturn + hand.get(i).toString();
            if (i < hand.size() - 1){
                toReturn = toReturn + ", ";
            }
        }
        return toReturn.substring(0, toReturn.length() /*- 2*/);//Removes the ", " at the end
    }
    
    public void playCard(int played, Deck gameDeck){//#Plays the selected card
        gameDeck.useCard(hand.get(played));
        hand.remove(played);
    }
    
    public boolean checkWin(){//#Checks to see if the player has won
        boolean toReturn = false;
        if (hand.size() <= 0){
            toReturn = true;
        }
        return toReturn;
    }
    
    public boolean checkUno(){//#Checks to see if the player has uno
        boolean toReturn = false;
        if (hand.size() == 1){
            toReturn = true;
        }
        return toReturn;
    }
    
    public int cardNum(){//#Gets the number of cards in the hand
        return hand.size();
    }
    
    public String toString(){//#Gets just the players name
        return playerName;
    }
}