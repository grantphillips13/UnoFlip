/** The UnoGame class is the main class in which the uno game runs, this class utilizes all the other classes in order to
 * have a functional game
 * @Author Main author Grant Phillips 101230563 with some methods from Ethan Stewart 101223533 and Faareh Bashir
 * @Date 2023/10/22
 * @Version v1.0
 * **/


import java.io.Serializable;
import java.lang.System;
import java.util.ArrayList;
import java.util.Collections;




public class UnoGameModel implements Serializable {

    private Deck deck = new Deck();
    private ArrayList<Card> discardedCards = new ArrayList<>();
    private ArrayList<Player> gamePlayers;
    private Color tempForAI;
    private Color tempColor;

    private int isGameRunning = 0;
    private Card top;
    private Side tempSide = Side.LIGHT;
    private int PlayerIndex = 0;

    public int tempIndex, tempIndex2;
    public Side temporySide, temporySide2;
    public ArrayList<Card> tempDiscardPile = new ArrayList<>();

    public ArrayList<Card> tempDiscardPile2 = new ArrayList<>();
    public Deck savedDeckState, savedDeckState2;
    public ArrayList<Player> tempPlayers = new ArrayList<>();
    public ArrayList<Player> tempPlayers2 = new ArrayList<>();

    private static final int HAND_NUM_CARDS = 7;

    public UnoGameModel() { // Constructor, setups the deck and the first card. @Grant Phillips
        Card initalCard;
        initalCard = this.deck.drawCard();
        while(true){
            if(initalCard.getColor(tempSide) == Color.WILD){
                this.deck.setFirstCard(initalCard);
                initalCard = this.deck.drawCard();
            }
            else{
                break;
            }
        }


        this.discardedCards.add(initalCard);
        top = initalCard;
        System.out.println("Starting card is: " + this.discardedCards.get(0).cardString(tempSide));
    }

    public ArrayList<Player> SetupPlayers(int num) { // Setup all the players in the game based off of input amount of players and fill their hand with cards @Grant Phillips

        ArrayList<Player> players = new ArrayList<>();

        return players;
    }

    public void SetupAIPlayers(int num){ // Setup all the AI players in the game based off of input amount of players and fill their hand with cards @Grant Phillips

    }

    public ArrayList<Player> setPlayerNames(String[] playerNames, int num){ // Create and return Arraylist of player @Grant
        ArrayList<Player> players = new ArrayList<>();
        for(int i = 0; i < playerNames.length; i++){
            if(playerNames[i].matches("AI1") || playerNames[i].matches("AI2") || playerNames[i].matches("AI3") || playerNames[i].matches("AI4"))
            {
                PlayerAI player = new PlayerAI(playerNames[i], this);
                for(int j =0; j < HAND_NUM_CARDS; j +=1){
                    player.drawCard(this.deck.drawCard());
                }
                players.add(player);
                System.out.println("ADDED AN AI PLAYER");
            }else {
                Player player = new Player(playerNames[i]);
                for(int j =0; j < HAND_NUM_CARDS; j +=1){
                    player.drawCard(this.deck.drawCard());
                }
                players.add(player);
            }

        }
        gamePlayers = players;
        System.out.println(players.get(0).getName());
        return players;
    }

    public int playerTurn(Player player, int index){ // Take players turn, this method handles choosing the card to be played, playing the card, alerting the game to when a player placed their last card @Grant Phillips
        String playerName = player.getName();
        System.out.println("It is " + playerName + "'s turn.");
        int toReturn = 0;



            Card potentialPlay;

            if(index >= 0 && index <= player.getHand().size()){ // If player is choosing a card from their hand
                top = this.discardedCards.get(0);
                Card selected = player.getHand().get(index);
                if(selected.getColor(tempSide).equals(Color.WILD) && !(selected.getValue(tempSide).equals(Value.WILD_DRAW_COLOR))){
                    toReturn = 2;
                }
                if(selected.getColor(tempSide).equals(Color.DARK)){
                    toReturn = 4;
                }
                if(this.isValidPlay(selected)){
                    Card played = player.playCard(index);
                    this.discardedCards.add(0,played);
                    //this.tempColor = null;
                    System.out.println("Played this card " + played.cardString(tempSide));
                    this.handleSpecial(played);
                    if(player.getHand().isEmpty()){
                        System.out.println(player.getName() + " has won!");
                        player.setPoints(this.calculateScore(player));
                        return 3;
                    }
                    if(toReturn == 2){
                        return 2;
                    }
                    if(toReturn == 4){
                        return 4;
                    }
                    else {
                        return 1;
                    }
                }
            }
        System.out.println("The card isn't a valid play, please try again or draw a card");
        return 0;
    }

    public int calculateScore(Player winningPlayer) { // Calculate the score of the winning player and return it to be saved @Faareh & Grant
        int scoreTotal = 0;

        for (Player player : this.gamePlayers) {
            if (player != winningPlayer) {
                for (Card card : player.getHand()) {
                    Value value = card.getValue(tempSide);
                    switch (value) {
                        case ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE -> scoreTotal += value.ordinal() + 1;
                        case DRAW_ONE -> scoreTotal += 10;
                        case DRAW_FIVE -> scoreTotal += 20;
                        case REVERSE, SKIP -> scoreTotal += 20;
                        case FLIP -> scoreTotal += 20;
                        case WILD -> scoreTotal += 40;
                        case WILD_DRAW_TWO -> scoreTotal += 50;
                        case WILD_DRAW_COLOR -> scoreTotal += 60;
                        case SKIP_EVERYONE -> scoreTotal += 30;
                    }
                }
            }
        }
        System.out.println(winningPlayer.getName() + " has scored " + scoreTotal + " points this round.\n");
        return scoreTotal;
    }



    public void handleSpecial(Card card){ // Handle the special cards and their actions on the game @Ethan @Sam
        //ADD FLIP LOGIC HERE TO CHANGE CARDVALUE TO APPROPRIATE SIZE
        Value cardValue = card.getValue(tempSide);
        Player nextPlayer = gamePlayers.get((PlayerIndex + 1)  % this.gamePlayers.size());

        if (cardValue == Value.WILD_DRAW_TWO){ // Wild Draw 2 for Bonus
            if(this.deck.getSize() <= 2){
                System.out.println("Shuffling Deck");
                this.addDiscardToDeck();
            }
            nextPlayer.drawCard(this.deck.drawCard());
            nextPlayer.drawCard(this.deck.drawCard());
            System.out.println(nextPlayer.getName() + " draws 2 cards");
        }
        if (cardValue == Value.REVERSE){ // Handling reverse
            Collections.reverse(gamePlayers);
            PlayerIndex = gamePlayers.size() -1 - PlayerIndex;
        }
        if (cardValue == Value.SKIP){ // Handle skip player
            this.PlayerIndex = (this.PlayerIndex + 1) % this.gamePlayers.size();
        }
        if (cardValue == Value.DRAW_ONE){ // Handle draw one card
            if(this.deck.isEmpty()){
                System.out.println("Shuffling Deck");
                this.addDiscardToDeck();
            }
            nextPlayer.drawCard(this.deck.drawCard());
            System.out.println(nextPlayer.getName() + " draws 1 card");
        }
        if (cardValue == Value.WILD){ // Handle wild card
            //this.wildColor();
        }
        if (cardValue == Value.DRAW_FIVE){ // Handle draw 5 cards
            if(this.deck.getSize() <= 5){
                System.out.println("Shuffling Deck");
                this.addDiscardToDeck();
            }
            nextPlayer.drawCard(this.deck.drawCard());
            nextPlayer.drawCard(this.deck.drawCard());
            nextPlayer.drawCard(this.deck.drawCard());
            nextPlayer.drawCard(this.deck.drawCard());
            nextPlayer.drawCard(this.deck.drawCard());
            System.out.println(nextPlayer.getName() + " draws 5 cards");
        }
        if (cardValue == Value.SKIP_EVERYONE){
            this.PlayerIndex = (this.PlayerIndex + this.gamePlayers.size()) % this.gamePlayers.size() - 1;
            if(this.PlayerIndex == -1){
                this.PlayerIndex = (this.gamePlayers.size()-1);
            }
        }
        if (cardValue == Value.FLIP){
            if(tempSide == Side.DARK){
                tempSide = Side.LIGHT;
            }else{
                tempSide = Side.DARK;
            }
        }
        if (cardValue == Value.WILD_DRAW_COLOR){
            //nextPlayer.drawCard(this.drawCard());
            do{
                nextPlayer.drawCard(this.deck.drawCard());
            }while(nextPlayer.getHand().get((nextPlayer.getHand().size())-1).getColor(tempSide) != this.tempColor);
        }
    }


    public boolean isValidPlay(Card card) { // Check if the card that is being placed is a valid placement based off uno rules. @Ethan

        Card previousCard = discardedCards.get(0);

        if (previousCard.getColor(tempSide) == Color.WILD && card.getColor(tempSide) == this.tempColor){
            return true;
        }

        if (previousCard.getColor(tempSide) == Color.DARK && card.getColor(tempSide) == this.tempColor){
            return true;
        }
        if (card.getColor(tempSide) == Color.WILD || card.getColor(tempSide) == Color.DARK){
            return true;
        }
        if (card.getColor(tempSide) == previousCard.getColor(tempSide)){
            return true;
        }
        if (card.getValue(tempSide) == previousCard.getValue(tempSide)){
            return true;
        }
        else {
            System.out.println("Invalid card");
            return false;
        }
    }

    public void setTempColor(String sentColor){ // Set the temporary card color when a wild card is played @Faareh
        if(sentColor.equals("RED")){
            this.tempColor = Color.RED;
        }
        if(sentColor.equals("BLUE")){
            this.tempColor = Color.BLUE;
        }
        if(sentColor.equals("GREEN")){
            this.tempColor = Color.GREEN;
        }
        if(sentColor.equals("YELLOW")){
            this.tempColor = Color.YELLOW;
        }
        if(sentColor.equals("TEAL")){
            this.tempColor = Color.TEAL;
            System.out.println("TEAL BRO");
        }
        if(sentColor.equals("PINK")){
            this.tempColor = Color.PINK;
        }
        if(sentColor.equals("ORANGE")){
            this.tempColor = Color.ORANGE;
        }
        if(sentColor.equals("PURPLE")){
            this.tempColor = Color.PURPLE;
        }
    }

    public void addDiscardToDeck(){ // Refill the deck from the discard pile @Grant_Phillips
        top = this.discardedCards.remove(0);
        System.out.println(top.cardString(tempSide));
        this.deck.refillDeck(this.discardedCards);
        this.discardedCards.clear();
        this.discardedCards.add(top);
    }

    public void NewRoundDiscardToDeck(){ // Refill the deck from the discard pile for new rounds, @Grant and Ethan
        this.deck.refillDeck(this.discardedCards);
        this.discardedCards.clear();
        top = this.deck.drawCard();
        while(true){
            if(top.getColor(tempSide) == Color.WILD){
                this.deck.addCard(top);
                top = this.deck.drawCard();
            }
            else{
                break;
            }
        }

        this.discardedCards.add(0, top);
    }

    public int[] checkPoints() { // Returns an array of points of all players @Grant
        int[] pointsArray = new int[gamePlayers.size()];
        int index = 0;
        for (Player player : gamePlayers) {
            System.out.println(player.getName() + "'s points " + player.returnPoints());
            pointsArray[index] = player.returnPoints();
            index++;
        }
        return pointsArray;
    }


    public boolean hasWon() { // Checks the amount of points all players have and returns false if a player has won @Grant and Ethan
        boolean returnValue = true;
        for (Player player : gamePlayers) {
            System.out.println(player.getName()+"'s points "+player.returnPoints());
            if (player.returnPoints() >= 500) {
                returnValue = false;
            }
        }
        return returnValue;
    }

    public void newRound(){ // This method is responsible for setup of a new round, it clears players hands into the discard pile, and then shuffles the discard pile into the deck, then fills players hands again @Grant and Ethan
        for (Player player : gamePlayers) {
            int size = player.getHand().size();
            for(int i =0; i < size; i++){
                this.discardedCards.add(player.clearHand());
            }
        }

        this.NewRoundDiscardToDeck();

        for (Player player : gamePlayers) {
            for(int j =0; j < HAND_NUM_CARDS; j +=1){
                player.drawCard(this.deck.drawCard());
            }
        }
    }


    public Card drawCard(){ // Draw a card and return it @Grant
        Player currentPlayer = this.getGamePlayers().get(this.returnPlayerIndex());
        Card potentialPlay;
        if(this.deck.isEmpty()){
            System.out.println("Shuffling Deck");
            this.addDiscardToDeck();
        }
        potentialPlay = this.deck.drawCard();
        currentPlayer.drawCard(potentialPlay);
        System.out.println("The drawn card is " + potentialPlay.cardString(tempSide));
        return potentialPlay;
    }

    public void saveState() { // saves current game state when undo is pressed @Grant
        tempIndex = this.PlayerIndex;
        temporySide = this.tempSide;
        tempDiscardPile = new ArrayList<>(this.discardedCards);
        savedDeckState = deck.deepCopy();
        tempPlayers = new ArrayList<>();
        for (Player player : this.gamePlayers) {
            if(player instanceof PlayerAI){
                Player copiedPlayer = new PlayerAI(player.getName(), this);
                copiedPlayer.setHand(new ArrayList<>(player.getHand()));
                tempPlayers.add(copiedPlayer);
            }
            else{
                Player copiedPlayer = new Player(player.getName());
                copiedPlayer.setHand(new ArrayList<>(player.getHand()));
                tempPlayers.add(copiedPlayer);
            }
            //copiedPlayer.setHand(new ArrayList<>(player.getHand()));
            //tempPlayers.add(copiedPlayer);
        }
    }

    public void saveStateRedo() { // saves current game state when redo is pressed @Grant
        tempIndex2 = this.PlayerIndex;
        temporySide2 = this.tempSide;
        tempDiscardPile2 = new ArrayList<>(this.discardedCards);
        savedDeckState2 = deck.deepCopy();
        tempPlayers2 = new ArrayList<>();
        for (Player player : this.gamePlayers) {
            if(player instanceof PlayerAI){
                Player copiedPlayer = new PlayerAI(player.getName(), this);
                copiedPlayer.setHand(new ArrayList<>(player.getHand()));
                tempPlayers2.add(copiedPlayer);
            }
            else{
                Player copiedPlayer = new Player(player.getName());
                copiedPlayer.setHand(new ArrayList<>(player.getHand()));
                tempPlayers2.add(copiedPlayer);
            }
            //copiedPlayer.setHand(new ArrayList<>(player.getHand()));
            //tempPlayers.add(copiedPlayer);
        }
    }


    public void loadState() { // Reload game state after an undo @Grant
        PlayerIndex = tempIndex;
        tempSide = temporySide;
        deck = savedDeckState.deepCopy();
        // Create new instances for discardedCards and gamePlayers to avoid reference sharing
        discardedCards = new ArrayList<>(tempDiscardPile);
        gamePlayers = new ArrayList<>();

        // Deep copy each player and their hands
        for (Player player : tempPlayers) {
            if(player instanceof PlayerAI){
                Player copiedPlayer = new PlayerAI(player.getName(),this);
                copiedPlayer.setHand(new ArrayList<>(player.getHand())); // Assuming Player class has a setHand method
                gamePlayers.add(copiedPlayer);
            }else {
                Player copiedPlayer = new Player(player.getName());
                copiedPlayer.setHand(new ArrayList<>(player.getHand())); // Assuming Player class has a setHand method
                gamePlayers.add(copiedPlayer);
            }
        }
    }

    public void loadStateRedo() { // reload game state when redoing a move @Grant
        PlayerIndex = tempIndex2;
        tempSide = temporySide2;
        deck = savedDeckState2.deepCopy();
        // Create new instances for discardedCards and gamePlayers to avoid reference sharing
        discardedCards = new ArrayList<>(tempDiscardPile2);
        gamePlayers = new ArrayList<>();

        // Deep copy each player and their hands
        for (Player player : tempPlayers2) {
            if(player instanceof PlayerAI){
                Player copiedPlayer = new PlayerAI(player.getName(),this);
                copiedPlayer.setHand(new ArrayList<>(player.getHand())); // Assuming Player class has a setHand method
                gamePlayers.add(copiedPlayer);
            }else {
                Player copiedPlayer = new Player(player.getName());
                copiedPlayer.setHand(new ArrayList<>(player.getHand())); // Assuming Player class has a setHand method
                gamePlayers.add(copiedPlayer);
            }
        }
    }

    public void loadStateSerialization(ArrayList<Object> data) { // Reload game state when Load Game is pressed @Ethan
        PlayerIndex = (int) data.get(0);
        tempSide = (Side) data.get(1);
        deck = (Deck) data.get(3);
        // load undo game state
        tempIndex = (int) data.get(9);
        temporySide = (Side) data.get(10);
        tempDiscardPile = new ArrayList<>((ArrayList<Card>) data.get(11));
        savedDeckState = (Deck) data.get(12);
        // load redo game state
        tempIndex2 = (int) data.get(14);
        temporySide2 = (Side) data.get(15);
        tempDiscardPile2 = new ArrayList<>((ArrayList<Card>) data.get(16));
        savedDeckState2 = (Deck) data.get(17);

        // Create new instances for discardedCards and gamePlayers to avoid reference sharing
        discardedCards = new ArrayList<>((ArrayList<Card>)data.get(2));
        gamePlayers = new ArrayList<>();
        tempPlayers = new ArrayList<>();
        tempPlayers2 = new ArrayList<>();

        // Deep copy each player and their hands
        for (Player player : (ArrayList<Player>)data.get(4)) {
            if(player instanceof PlayerAI){
                Player copiedPlayer = new PlayerAI(player.getName(),this);
                copiedPlayer.setHand(new ArrayList<>(player.getHand())); // Assuming Player class has a setHand method
                gamePlayers.add(copiedPlayer);
            }else {
                Player copiedPlayer = new Player(player.getName());
                copiedPlayer.setHand(new ArrayList<>(player.getHand())); // Assuming Player class has a setHand method
                gamePlayers.add(copiedPlayer);
            }
        }
        // Deep copy each player and their hands for undo
        for (Player player : (ArrayList<Player>)data.get(13)) {
            if(player instanceof PlayerAI){
                Player copiedPlayer = new PlayerAI(player.getName(),this);
                copiedPlayer.setHand(new ArrayList<>(player.getHand())); // Assuming Player class has a setHand method
                tempPlayers.add(copiedPlayer);
            }else {
                Player copiedPlayer = new Player(player.getName());
                copiedPlayer.setHand(new ArrayList<>(player.getHand())); // Assuming Player class has a setHand method
                tempPlayers.add(copiedPlayer);
            }
        }
        // Deep copy each player and their hands for redo
        for (Player player : (ArrayList<Player>)data.get(18)) {
            if(player instanceof PlayerAI){
                Player copiedPlayer = new PlayerAI(player.getName(),this);
                copiedPlayer.setHand(new ArrayList<>(player.getHand())); // Assuming Player class has a setHand method
                tempPlayers2.add(copiedPlayer);
            }else {
                Player copiedPlayer = new Player(player.getName());
                copiedPlayer.setHand(new ArrayList<>(player.getHand())); // Assuming Player class has a setHand method
                tempPlayers2.add(copiedPlayer);
            }
        }
    }

    public ArrayList<Player> getGamePlayers(){ return gamePlayers;} //@Grant and Ethan

    public ArrayList<Card> getCurrentPlayerHand(Player player) {return player.getHand();}




    public int returnPlayerIndex(){return PlayerIndex;} //@Grant and Ethan

    public Deck returnDeck(){return deck;} //@Grant and Ethan

    public ArrayList<Card> returndiscardedCards(){return discardedCards;} //@Grant and Ethan

    public void addToDiscard(Card card1){
        discardedCards.add(card1);
    } //@Grant and Ethan

    public Card returnTop(){
        top = this.discardedCards.get(0);
        return top;
    } //@Grant and Ethan

    public void setTopDiscarded(Card card){ //@Grant and Ethan
        this.discardedCards.add(0, card);
        top = this.discardedCards.get(0);
    }

    public Color getTempColor(){
        return tempColor;
    } //@Grant and Ethan

    public Color getTempColorForAI(){
        return tempForAI;
    } //@Grant and Ethan

    public void makeTempNull(){
        this.tempColor = null;
    }

    public void settempColour(Color color){
        this.tempColor = color;
    } //@Grant and Ethan

    public void tickPlayer(){this.PlayerIndex = (this.PlayerIndex + 1) % this.gamePlayers.size();} //@Grant and Ethan

    public void playerClearHand(Player player){
        player.clearHand();
    } //@Grant and Ethan

    public void playerClearHandTest(Player player){
        player.clearHandForTest();
    } //@Grant and Ethan

    public Side returnSide(){
        return tempSide;
    }

    public void setTempSide(Side tempSide) {
        this.tempSide = tempSide;
    }

    public void setPlayerHand(ArrayList<Card> theList, Player player){ //@Grant and Ethan
        for(Card cards: theList){
            player.drawCard(cards);
        }
    }



}
