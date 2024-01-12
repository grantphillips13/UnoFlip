/** The PlayerAI class is used to define the playerAI and their functuinality for the Uno Game
 * @Author Grant
 * @Date 2023/11/25
 * @Version v1.0
 * **/

import java.util.ArrayList;



public class PlayerAI extends Player {

    // Players points
    private int points;

    // String Name
    private String name;

    // List<card> hand
    private ArrayList<Card> hand;

    private UnoGameModel gameModel;

    private Color playerTemp;

    // int cards
    int cards;

    //Constructor
    public PlayerAI(String name, UnoGameModel gameModel) {
        this.gameModel = gameModel;
        this.name = name;
        hand = new ArrayList<>();
        cards = 0;
    }

    public void setHand(ArrayList<Card> cards){
        hand.clear();
        hand.addAll(cards);
    }

    // getName
    public String getName() {
        return name;
    }

    //getHand
    public ArrayList<Card> getHand() {
        return hand;
    }

    //drawCard
    public void drawCard(Card card) {
        hand.add(card);
        cards += 1;
    }

    //set the players points
    public void setPoints(int roundPoints){
        points += roundPoints;
    } // Grant

    //return an int of the players points
    public int returnPoints(){
        return points;
    } // Grant

    public int getNextPlay() { // This method detmerines the index of the card the AI wants to play based on a large variety of different factors about the AIs and and the other players @grant
        ArrayList<Card> hand = this.getHand();
        Card topCard = gameModel.returnTop();
        int chosenIndex = -1;
        int maxPointCardIndex = -1;
        int maxPointValue = -1;
        int wildCardIndex = -1;

        for (int i = 0; i < hand.size(); i++) {
            Card currentCard = hand.get(i);

            // Check if the card is a valid play
            if (!gameModel.isValidPlay(currentCard)) {
                continue;
            }

            // Update the max point card found so far
            if (currentCard.getValue(gameModel.returnSide()).getPointValue() > maxPointValue) {
                if(!(currentCard.getColor(gameModel.returnSide()) == Color.WILD || currentCard.getColor(gameModel.returnSide()) == Color.DARK)){
                    maxPointCardIndex = i;
                    maxPointValue = currentCard.getValue(gameModel.returnSide()).getPointValue();
                }
            }

            if(isWild(currentCard)){
                wildCardIndex = i;
                Color bestColor = chooseBestColorForWild();
                System.out.println("THE TEMP COLOR IS "+bestColor.toString());
                this.playerTemp = bestColor; // Assuming setTempColor takes a String
            }

            // Prioritize special cards when advantageous
            if (shouldPlaySpecialCard(currentCard)) {
                return i;
            }

            // Try to match color first
            if(topCard.getColor(gameModel.returnSide()) == Color.WILD || topCard.getColor(gameModel.returnSide()) == Color.DARK){
                if(currentCard.getColor(gameModel.returnSide()) == gameModel.getTempColor()){
                    chosenIndex = i;
                }
            }
            else if (currentCard.getColor(gameModel.returnSide()) == topCard.getColor(gameModel.returnSide())) {
                chosenIndex = i;
            }


        }

        // If a color match was found, play it
        if (chosenIndex != -1) {
            return chosenIndex;
        }

        if(maxPointCardIndex != -1){
            return maxPointCardIndex;
        } else return wildCardIndex;
    }

    private boolean isSpecialCard(Card card) { // This method returns true if the card is an instance of a special card @grant
        return card.getValue(gameModel.returnSide()) == Value.SKIP || card.getValue(gameModel.returnSide()) == Value.REVERSE || card.getValue(gameModel.returnSide()) == Value.DRAW_ONE || card.getValue(gameModel.returnSide()) == Value.DRAW_FIVE || card.getColor(gameModel.returnSide()) == Color.WILD;
    }

    private boolean isWild(Card card){ // This method returns true if the card is an instance of a wild card @grant
        return card.getValue(gameModel.returnSide()) == Value.WILD || card.getValue(gameModel.returnSide()) == Value.WILD_DRAW_COLOR || card.getValue(gameModel.returnSide()) == Value.WILD_DRAW_TWO;
    }

    private boolean shouldPlaySpecialCard(Card card) { // This method returns true if the card selected is a special card and meets some pre defined parameters sourrounding if its a good idea to play them or not @grant
        // Example logic - this can be expanded or modified based on your game rules

        // Check if the card is a special type like Skip, Reverse, Draw One, Wild, etc.
        boolean isSpecialType = isSpecialCard(card);

        Card topCard = gameModel.returnTop();

        // If it's not a special card, playing it isn't a special scenario
        if (!isSpecialType) {
            return false;
        }

        // Play special cards more aggressively if AI has fewer cards
        if (this.getHand().size() <= 3 && (card.getColor(gameModel.returnSide()) != Color.WILD || card.getColor(gameModel.returnSide()) != Color.WILD )) {
            return true;
        }

        int nextPlayerIndex = (gameModel.returnPlayerIndex() + 1) % this.gameModel.getGamePlayers().size();
        if (gameModel.getCurrentPlayerHand(gameModel.getGamePlayers().get(nextPlayerIndex)).size() < 3) { // AI avoids letting players with a small hand play
            if (card.getValue(gameModel.returnSide()) == Value.REVERSE || card.getValue(gameModel.returnSide()) == Value.SKIP){
                return true;
            }
        }


        // Consider opponents' hand sizes
        for (Player player : gameModel.getGamePlayers()) {
            // Skip if player is the AI itself
            if (player == this) {
                continue;
            }

            // If an opponent has very few cards, it might be good to play special cards to disrupt their play
            if (player.getHand().size() <= 3) {
                if(card.getColor(gameModel.returnSide()) == Color.WILD || card.getColor(gameModel.returnSide()) == Color.DARK){
                    Color bestColor = chooseBestColorForWild();
                    System.out.println("THE TEMP COLOR IS "+bestColor.toString());
                    this.playerTemp = bestColor; // Assuming setTempColor takes a String
                    return true;
                }
                return true;
            }
        }



        // might want to save Wild or Wild Draw cards for very specific scenarios

        // Default to not playing the special card
        return false;
    }

    public Color chooseBestColorForWild() { // This method determines the optimal color to play for a wild card based on what side it is and how many cards of each color are in the AIs hand @grant
        int redCount = 0, blueCount = 0, greenCount = 0, yellowCount = 0, tealCount = 0, pinkCount = 0, purpleCount = 0, orangeCount = 0;
        for (Card card : this.getHand()) {
            switch (card.getColor(gameModel.returnSide())) {
                case RED:
                    redCount++;
                    break;
                case BLUE:
                    blueCount++;
                    break;
                case GREEN:
                    greenCount++;
                    break;
                case YELLOW:
                    yellowCount++;
                    break;
                case TEAL:
                    tealCount++;
                    break;
                case PINK:
                    pinkCount++;
                    break;
                case PURPLE:
                    purpleCount++;
                    break;
                case ORANGE:
                    orangeCount++;
                    break;
                default:
                    // No action for Wild cards
                    break;
            }
        }

        // Determine which color is most common in hand
        Color bestColor = Color.RED;
        int maxCount = redCount;
        if (blueCount > maxCount) {
            bestColor = Color.BLUE;
            maxCount = blueCount;
        }
        if (greenCount > maxCount) {
            bestColor = Color.GREEN;
            maxCount = greenCount;
        }
        if (yellowCount > maxCount) {
            bestColor = Color.YELLOW;
        }
        if (tealCount > maxCount) {
            bestColor = Color.TEAL;
        }
        if (pinkCount > maxCount) {
            bestColor = Color.PINK;
        }
        if (orangeCount > maxCount) {
            bestColor = Color.ORANGE;
        }
        if (purpleCount > maxCount) {
            bestColor = Color.PURPLE;
        }
        return bestColor;
    }

    // playCard
    public Card playCard(int cardNum) {
        return this.hand.remove(cardNum);
    }

    public Color getPlayerTemp(){
        return playerTemp;
    }

    //toString
    public String toString() {
        return hand.toString();
    }


    // METHODS FOR TESTING PURPOSES ONLY
    public Card clearHand() {
        return hand.remove(0);
    }

    public void clearHandForTest(){
        hand.clear();
    }
}
