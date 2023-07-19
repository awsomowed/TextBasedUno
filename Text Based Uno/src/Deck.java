/**
 * Stores all cards in the draw/discard pile and the full deck
 *
 * @author Brandon Moyer
 * @version 1.1.0
 */

import java.util.ArrayList;
import java.lang.Math;
import java.util.Scanner;

public class Deck{
    
    private ArrayList<Card> fullDeck = new ArrayList<Card>();

    
    private static ArrayList<Card> discardPile;

    
    private ArrayList<Card> drawPile = new ArrayList<Card>();

    public Deck(int deckSize, boolean customCardMults){//#The constructor controlls how many of each card is in the deck
        int standardMult = 1;
        int numMult = 1;
        int skipMult = 1;
        int revMult = 1;
        int plus2Mult = 1;
        int wildMult = 1;
        int plus4Mult = 1;
        boolean enoughCards = false;
        while (!enoughCards){//Loops until there are enough cards
            if (customCardMults){//Gets custom multipliers
                Scanner in = new Scanner(System.in);
                System.out.println("Please set the Number Card Multiplier.");
                numMult = Game.stringInt(0 , 100);
                System.out.println();
                System.out.println("Please set the Skip Multiplier.");
                skipMult = Game.stringInt(0 , 100);
                System.out.println();
                System.out.println("Please set the Reverse Multiplier.");
                revMult = Game.stringInt(0 , 100);
                System.out.println();
                System.out.println("Please set the Plus 2 Multiplier.");
                plus2Mult = Game.stringInt(0 , 100);
                System.out.println();
                System.out.println("Please set the Wild Multiplier.");
                wildMult = Game.stringInt(0 , 100);
                System.out.println();
                System.out.println("Please set the Plus 4 Multiplier.");
                plus4Mult = Game.stringInt(0 , 100);
                System.out.println();
            }
            else if (!customCardMults){//Ups standard mult if there are not enough cards
                numMult = standardMult;
                skipMult = standardMult;
                revMult = standardMult;
                plus2Mult = standardMult;
                wildMult = standardMult;
                plus4Mult = standardMult;
            }
            Card addCard = new Card("", "0");//Creates blank card
            for (int n = 0; n < 2 * numMult; n++){
                for (int ns = 0; ns < 10; ns++){//Adds number cards
                    if (ns != 0){
                        addCard = new Card("red", "" + ns);
                        fullDeck.add(addCard);
                        addCard = new Card("blue", "" + ns);
                        fullDeck.add(addCard);
                        addCard = new Card("green", "" + ns);
                        fullDeck.add(addCard);
                        addCard = new Card("yellow", "" + ns);
                        fullDeck.add(addCard);
                    }
                    if (ns == 0 && n % 2 == 0){
                        addCard = new Card("red", "0");
                        fullDeck.add(addCard);
                        addCard = new Card("blue", "0");
                        fullDeck.add(addCard);
                        addCard = new Card("green", "0");
                        fullDeck.add(addCard);
                        addCard = new Card("yellow", "0");
                        fullDeck.add(addCard);
                    }
                }
            }
            for (int s = 0; s < 2 * skipMult; s++){//Adds skip cards
                addCard = new Card("red", "skip");
                fullDeck.add(addCard);
                addCard = new Card("blue", "skip");
                fullDeck.add(addCard);
                addCard = new Card("green", "skip");
                fullDeck.add(addCard);
                addCard = new Card("yellow", "skip");
                fullDeck.add(addCard);
            }
            for (int r = 0; r < 2 * revMult; r++){//Adds reverse cards
                addCard = new Card("red", "reverse");
                fullDeck.add(addCard);
                addCard = new Card("blue", "reverse");
                fullDeck.add(addCard);
                addCard = new Card("green", "reverse");
                fullDeck.add(addCard);
                addCard = new Card("yellow", "reverse");
                fullDeck.add(addCard);
            }
            for (int p2 = 0; p2 < 2 * plus2Mult; p2++){//Adds plus 2s
                addCard = new Card("red", "plus2");
                fullDeck.add(addCard);
                addCard = new Card("blue", "plus2");
                fullDeck.add(addCard);
                addCard = new Card("green", "plus2");
                fullDeck.add(addCard);
                addCard = new Card("yellow", "plus2");
                fullDeck.add(addCard);
            } 
            for (int w = 0; w < 4 * wildMult; w++){//Adds wilds
                addCard = new Card("wild", "wild");
                fullDeck.add(addCard);
            }
            for (int p4 = 0; p4 < 4 * plus4Mult; p4++){//Adds plus 4s
                addCard = new Card("wild", "plus4");
                fullDeck.add(addCard);
            }
            if (fullDeck.size() >= (Game.getPlayerNum() * 2 * 7)){//Checks the size of the deck
                enoughCards = true;
            }
            else{
                if (customCardMults){//Runs if custom multipliers were used 
                    System.out.println("Not Enough Cards, please increase the card multipliers");
                }
                else if (!customCardMults){//Runs if custom multipliers were not used
                    System.out.println("Not Enough Cards, Increasing multiplier by 1");
                    standardMult ++; 
                }
            }
        }
        fullDeck = randomizeDeck(fullDeck);//Randomizes the deck
        for (int i = 0; i < deckSize; i++){//Applies the deck multiplier
            for (int q = 0; q < fullDeck.size(); q++)//Adds all cards from the full deck to the draw pile
            {
                drawPile.add(fullDeck.get(q));
            }
        }
        discardPile = new ArrayList<Card>();
        discardPile.add(0, drawPile.get(0));//Takes the first card from the draw pile and add it to the discard pile    
        drawPile.remove(0);
        boolean notWild = false;
        while (!notWild){//Loops until the first top card is not wild
            if (topCard().getColor() == "wild"){
                discardPile.add(discardPile.size(), drawPile.get(0));//Gets new top card
                drawPile.remove(0);
            }
            else{
                notWild = true;
            }
        }
    }
    
    public ArrayList<Card> getDiscardPile(){//#For debugging, gets the discard pile
        return discardPile;
    }
    
    public ArrayList<Card> getDrawPile(){//#For debugging, gets the discard pile
        return drawPile;
    }
    
    public static Card topCard(){//#Gets the card on top of the discard pile
        return discardPile.get(discardPile.size() - 1);
    }
    
    public Card nextCard(){//#Takes the first card from the draw pile and returns it after deleting it
        Card toReturn = drawPile.get(0);
        drawPile.remove(0);
        if (drawPile.size() <= 3){//Reffills the draw deck if it is low
            refillDrawPile();
        }
        return toReturn;
    }
    
    public void useCard(Card cardPlayed){//#Adds card to the discard pile
        discardPile.add(discardPile.size(), cardPlayed);
    }
    
    public void refillDrawPile(){//#Takes most of the cards from the discard pile and puts them back into the draw pile
        Scanner in = new Scanner(System.in);
        ArrayList<Card> tempPile = new ArrayList<Card>();
        if (discardPile.size() > 1){//Makes sure that there are cards to take
            int pileLength = discardPile.size();
            for (int i = 0; i < pileLength - 2; i++){//Takes all cards exept the last one from the discard pile
                if (discardPile.get(0).getSymbol() == "wild" || discardPile.get(0).getSymbol() == "plus4"){
                    if (discardPile.get(0).getColor() != "wild"){//Removes all temp wild cards
                        discardPile.remove(0);
                    }
                }
                tempPile.add(discardPile.get(0));
                discardPile.remove(0);
            }
        }
        else{//Runs if there are not enough cards
            System.out.println("There are not enough cards left to refill the draw pile.\nWould you like to add another deck to the pile?");
            String addDeckIn = "Yes";
            boolean addDeck = false;
            while (!addDeckIn.equals("no") && !addDeckIn.equals("yes")){//Loops until valid input
                if (addDeckIn != "Yes"){
                    System.out.println("Invalid Input, please type yes or no");
                }
                addDeckIn = in.nextLine().toLowerCase();
            }
            if (addDeckIn.equals("no")){
                addDeck = false;
            }
            else if(addDeckIn.equals("yes")){
                addDeck = true;
            }
            System.out.println();
            if (addDeck){
                for (int q = 0; q < fullDeck.size(); q++){//Adds another full deck to the pile
                    tempPile.add(fullDeck.get(q));
                }
            }
            else{
                Game.removeCards();//Takes the first card from all players hands
            }
        }
        tempPile = shuffleDeck(tempPile);//Shuffles the temp pile
        int tempLength = tempPile.size();
        for (int i = 0; i < tempLength - 2; i++){//Puts all cards from the temp pile into the draw pile
            drawPile.add(drawPile.size(),tempPile.get(0));
            tempPile.remove(0);
        }
    }
    
    public ArrayList<Card> shuffleDeck(ArrayList<Card> toShuffle){//#Semi-realistically shuffles the deck
        System.out.println("Shuffling");
        ArrayList<Card> toReturn = new ArrayList<Card>();
        ArrayList<Card> addHalf = new ArrayList<Card>();
        for (int s = 0; s < 5 + (Math.random() * 5); s++){//Randomizes the amount of times it shuffles
            int shuffleLength = toShuffle.size();
            for (int i = 0; i < shuffleLength / 2; i++){//Splits the deck in half
                toReturn.add(toShuffle.get(i));
            }
            for (int i = shuffleLength / 2; i < shuffleLength; i++){
                addHalf.add(toShuffle.get(i));
            }
            for (int i = 0; i < shuffleLength / 2; i++){//Randomly puts cards back into the pile
                int insertIndex = i + (int) (5 * ((Math.random() * 2) - 1));
                if (insertIndex < 0){//Prevents out of bounds
                    insertIndex = 0;
                }
                else if (insertIndex > toReturn.size()){//Prevents out of bounds
                    insertIndex = toReturn.size();
                }
                toReturn.add(insertIndex, addHalf.get(0));
                addHalf.remove(0);
            }
        }
        return toReturn;
    }
    
    public ArrayList<Card> randomizeDeck(ArrayList<Card> toRandomize){//#Randomizes the deck
        ArrayList<Card> toReturn = new ArrayList<Card>();
        int deckSize = toRandomize.size();
        for (int i = 0; i < deckSize; i++){//Loops through all the cards
            int insertIndex = (int) (((double) toReturn.size() - 0.5) * Math.random());//Adds the next card at a random index
            if (insertIndex < 0){//Prevents out of bounds
                insertIndex = 0;
            }
            else if (insertIndex > toReturn.size()){//Prevents out of bounds
                insertIndex = toReturn.size();
            }
            toReturn.add(insertIndex, toRandomize.get(0));
            toRandomize.remove(0);
        }
        return toReturn;
    }
    
    public String toString(){//#For debugging, Gets both the discard and draw pile
        return "Discard Pile: " + discardPile + "\nDraw Pile: " + drawPile;
    }
}