/**
 * Stores information on any specific card
 *
 * @author Brandon Moyer
 * @version 1.1.0
 */

public class Card{
    private String color;
    private String symbol;
    
    public Card(String color, String symbol){//#Constructor
        this.color = color;
        this.symbol = symbol;
    }
    
    public boolean isWild(){//#Checks to see if the card is a wild
        boolean toReturn = false;
        if (color == "wild"){
            toReturn = true;
        }
        return toReturn;
    }
    
    public boolean isPlusFour(){//#Checks to see if the card is a plus 4
        boolean toReturn = false;
        if (symbol == "plus4"){
            toReturn = true;
        }
        return toReturn;
    }
    
    public String getColor(){//#Gets the cards color
        return color;
    }
    
    public String getSymbol(){//#Gets the cards symbol
        return symbol;
    }
    
    public boolean playable(Card topCard){//#Checks to see if the card is playable
        boolean toReturn = false;
        if (color.equals("wild")){//Checks for wilds
            toReturn = true;
        }
        else if (color.equals(topCard.getColor())){//Checks color
            toReturn = true;
        }
        else if (symbol.equals(topCard.getSymbol())){//Checks symbol
            toReturn = true;
        }
        return toReturn;
    }
    
    public String toString(){//#Gets details about the card
        String toReturn = "";
        if (symbol == "plus4"){//Checks for plus 4
            if (color == "wild"){
                toReturn = "plus 4";
            }
            else{
                toReturn = color + " plus 4";
            }
        }
        else if (symbol == "wild"){//Checks for wilds
            if (color == "wild"){
                toReturn = "wild";
            }
            else{
                toReturn = color + " wild";
            }
        }
        else if (symbol == "plus2"){//Checks for plus 2
            toReturn = color + " plus 2";
        }
        else{//All other types
            toReturn = color + " " + symbol;
        }
        return toReturn;
    }
}