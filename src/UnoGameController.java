/** The UnoGameController class is the controller class that is the main source of communication between the view and the model, the controller handles the transfer of data
 * and game flow between the model and the view
 * @Author Main author Grant Phillips 101230563 with some methods from Ethan Stewart 101223533 and Faareh Bashir
 * @Date 2023/11/13
 * @Version v1.0
 * **/

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class UnoGameController implements GameActionListener  {

    private UnoGameModel model;
    private UnoGameView view;

    private int numPlayers;

    private int numAi;


    public UnoGameController(UnoGameModel model, UnoGameView view) {// Grant
        this.model = model;
        this.view = view;
    }


    public void startGame() { // Grant
        view.promptForNumberOfPlayers(); // This should trigger the dialogs
        view.promptForNamesOfPlayers(numPlayers, numAi);
        updatePlayerLabel();
    }


    public void onNumberOfPlayersSelected(int selectedValue) { // Grant
        if (selectedValue != 0) {
            numPlayers = selectedValue;
            System.out.println("Number of players selected: " + selectedValue);
            model.SetupPlayers(selectedValue);
        } else {
            // The code for showing the confirmation dialog goes here, as per the previous example
            int response = JOptionPane.showConfirmDialog(
                    null, // assuming this is within a context where 'this' is appropriate
                    "No number of players was selected. Would you like to try again?",
                    "Selection Needed",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                view.promptForNumberOfPlayers();
            } else {
                handleCancellation();
            }
        }
        if(selectedValue != 1){
            view.promptForNumberOfAIPlayers();
        }
        else{
            view.promptForNumberOfAIPlayersIfOnlyOnePlayer();
        }
    }


    public void onNumberOfAI(int selectedValue) {
        if (selectedValue != 0) {
            numAi = selectedValue;
            System.out.println("Number of players selected: " + selectedValue);
            model.SetupAIPlayers(selectedValue);
        }
    }

    private void handleCancellation() { // Grant
        // Handle the user's decision to cancel
        System.out.println("Player selection was cancelled.");
        // Potentially exit the game or disable certain features.
    }


    public void setPlayerNames(String[] playerNames, int num) { // Set the players names at the start of game and send their hands to be printed as buttons @Grant
        ArrayList <Card> cards;
        System.out.println(Arrays.toString(playerNames));
        int namesWithoutAI;
        if(num > playerNames.length){
            namesWithoutAI = num - playerNames.length;
        }else{
            namesWithoutAI = playerNames.length - num;
        }
        int aiCount = 1;
        for(int i = namesWithoutAI; i < playerNames.length; i++){
            playerNames[i] = "AI" + aiCount;
            aiCount++;
        }
        model.setPlayerNames(playerNames,num);
        SwingUtilities.invokeLater(() -> view.showMainFrame());
        cards = model.getCurrentPlayerHand(model.getGamePlayers().get(model.returnPlayerIndex()));
        System.out.println(cards);
        view.addCardsToScrollPane(cards);
        model.saveState();
    }

    public Card getTopCard(){
        return model.returnTop();
    } // Return the current top card @Grant

    public Side getCardSide() {
        return model.returnSide();
    } // Get the current card side @Grant

    public void takePlayerTurn(int index) { // This method is responsible for handling the play card action, it receives its call from the actionlistener and uses the given index communicate with the model and play cards Grant
        final int[] canPlay = {1};
        SwingWorker<Void, Void> worker =
                new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Perform long-running task here
                ArrayList<Player> players = model.getGamePlayers();
                Player currentPlayer = players.get(model.returnPlayerIndex());
                int toUndo = 0;

                if (!(currentPlayer instanceof PlayerAI)) {
                    if (model.getCurrentPlayerHand(model.getGamePlayers().get(model.returnPlayerIndex())).get(index).getValue(model.returnSide()) == Value.WILD_DRAW_COLOR) {
                        view.promptWildCardDark();
                    }
                }
                if ((currentPlayer instanceof PlayerAI)) {
                    toUndo = 1;
                    if (model.getCurrentPlayerHand(model.getGamePlayers().get(model.returnPlayerIndex())).get(index).getValue(model.returnSide()) == Value.WILD_DRAW_COLOR) {
                        model.setTempColor(((PlayerAI) currentPlayer).getPlayerTemp().toString());
                    }
                }

                int value = model.playerTurn(model.getGamePlayers().get(model.returnPlayerIndex()), index);

                if(value == 0){
                    canPlay[0] = 0;
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            JOptionPane.showMessageDialog(view,
                                    "Invalid card selected. Please select another card.",
                                    "Invalid Selection",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    return null;
                }
                if(value == 2){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            ArrayList<Player> players = model.getGamePlayers();
                            Player currentPlayer = players.get(model.returnPlayerIndex());

                            if (currentPlayer instanceof PlayerAI) {
                                model.setTempColor(((PlayerAI) currentPlayer).getPlayerTemp().toString());
                                Color temp = model.getTempColor();
                                updateStatus("Wild Color: " + temp.toString());
                                view.updateTopCard();
                                ArrayList<Card> cards = model.getCurrentPlayerHand(model.getGamePlayers().get(model.returnPlayerIndex()));
                                view.addCardsToScrollPane(cards);
                                view.disableCardButtons();
                            }else {
                                String selectedColor = view.promptWildCard();
                                updateStatus("Wild Color: " + selectedColor);
                                view.updateTopCard();
                                ArrayList<Card> cards = model.getCurrentPlayerHand(model.getGamePlayers().get(model.returnPlayerIndex()));
                                view.addCardsToScrollPane(cards);
                                view.disableCardButtons();
                            }
                        }
                    });
                }
                if(value == 4){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            ArrayList<Player> players = model.getGamePlayers();
                            Player currentPlayer = players.get(model.returnPlayerIndex());

                            if (currentPlayer instanceof PlayerAI) {
                                model.setTempColor(((PlayerAI) currentPlayer).getPlayerTemp().toString());
                                Color temp = model.getTempColor();
                                updateStatus("Wild Color: " + temp.toString());
                                view.updateTopCard();
                                ArrayList<Card> cards = model.getCurrentPlayerHand(model.getGamePlayers().get(model.returnPlayerIndex()));
                                view.addCardsToScrollPane(cards);
                                view.disableCardButtons();
                            }else {
                                String selectedColor = view.promptWildCardDark();
                                updateStatus("Wild Color: " + selectedColor);
                                ArrayList<Card> cards = model.getCurrentPlayerHand(model.getGamePlayers().get(model.returnPlayerIndex()));
                                view.addCardsToScrollPane(cards);
                                view.disableCardButtons();
                                view.updateTopCard();
                            }
                        }
                    });
                }
                if(value == 3){
                    updateStatus(model.getGamePlayers().get(model.returnPlayerIndex()).getName() + " has won!");
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            startNewRound();
                        }
                    });

                }
                else {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            view.updateTopCard();
                            updateStatus("Card Played: " + getTopCard().cardString(model.returnSide()));
                            ArrayList<Card> cards = model.getCurrentPlayerHand(model.getGamePlayers().get(model.returnPlayerIndex()));
                            view.addCardsToScrollPane(cards);
                            view.disableCardButtons();
                        }
                    });
                }

                return null;
            }

            @Override
            protected void done() {
                ArrayList<Card> cards = model.getCurrentPlayerHand(model.getGamePlayers().get(model.returnPlayerIndex()));
                view.addCardsToScrollPane(cards);
                if(canPlay[0] != 0) {
                    view.disableCardButtons();
                    view.disableDrawCard();
                    view.enableEndTurn();
                    ArrayList<Player> players = model.getGamePlayers();
                    Player currentPlayer = players.get(model.returnPlayerIndex());
                    if(currentPlayer instanceof PlayerAI){
                        view.disableUndo();
                        view.disableRedo();
                        clearSpecialStatus();
                    }else{
                        view.enableUndo();
                        view.disableRedo();
                        clearSpecialStatus();
                    }
                }
            }
        };
        worker.execute();
        model.saveStateRedo();
    }


    public void startNewRound(){ // Start a new round, checks points for winner and displays points each round @Grant
        // Getting the points array
        int[] points = model.checkPoints();

        // Constructing the message with player names and their points
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < model.getGamePlayers().size(); i++) {
            Player player = model.getGamePlayers().get(i);
            message.append(player.getName()).append(": ").append(points[i]).append(" points\n");
        }
        JOptionPane.showMessageDialog(null, message.toString(), "Round Summary", JOptionPane.INFORMATION_MESSAGE);
        if(!model.hasWon())
        {
            System.exit(0);
        }
        model.newRound();
        view.updateTopCard();
    }


    public void onWildDraw(String selectedValue){ // Handles the color selection for wild cards @Faareh
        if (selectedValue != null) {
            model.setTempColor(selectedValue);

        } else {
            System.out.println("No selection was made.");
            view.promptWildCard();
        }
    }

    public void onWildDrawDark(String selectedValue){ // Handles the color selection for wild cards @Faareh
        if (selectedValue != null) {
            model.setTempColor(selectedValue);

        } else {
            System.out.println("No selection was made.");
            view.promptWildCard();
        }
    }


    public void endTurn() { // End players turn, also handles the playing of the AI cards @Grant
        model.tickPlayer();
        model.saveState();

        ArrayList<Player> players = model.getGamePlayers();
        Player currentPlayer = players.get(model.returnPlayerIndex());

        if (currentPlayer instanceof PlayerAI) {
            PlayerAI aiPlayer = (PlayerAI) currentPlayer;
            // Now you can use PlayerAI specific methods
            int val = aiPlayer.getNextPlay();
            System.out.println("AI IS PLAYING INDEX "+val);
            if(val == -1){
                drawCard();
            }else{
                takePlayerTurn(val);
                //model.makeTempNull();
            }
        }
        ArrayList <Card> cards;
        cards = model.getCurrentPlayerHand(model.getGamePlayers().get(model.returnPlayerIndex()));


        if(!(currentPlayer instanceof PlayerAI)) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    view.addCardsToScrollPane(cards);
                    view.enableDrawCard();
                    view.disableEndTurn();
                    view.disableUndo();
                }
            });
        }
        clearSpecialStatus();
        updatePlayerLabel();

    }

    public void drawCard() { // Draw a card into players hand @Grant
        Card drawnCard = model.drawCard();
        ArrayList<Card> cards = model.getCurrentPlayerHand(model.getGamePlayers().get(model.returnPlayerIndex()));
        System.out.println(cards);
        updateStatus("Drawn Card: " + drawnCard.cardString(model.returnSide()));

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                view.addCardsToScrollPane(cards);
                ArrayList<Player> players = model.getGamePlayers();
                Player currentPlayer = players.get(model.returnPlayerIndex());
                if (currentPlayer instanceof PlayerAI) {
                    view.disableCardButtons();
                    view.disableDrawCard();
                    view.enableEndTurn();
                    view.enableUndo();
                    view.repaint();
                    view.revalidate();
                    System.out.println("AI has drawn a card");
                    clearSpecialStatus();
                } else {
                    // Enable/Disable buttons as needed for non-AI players
                    view.disableCardButtons();
                    view.disableDrawCard();
                    view.enableEndTurn();
                    view.enableUndo();
                    view.disableRedo();
                    clearSpecialStatus();
                }
            }
        });
    }

    public void updatePlayerLabel() { // Update the current player text box in gui @Ethan
        ArrayList <Player> players = model.getGamePlayers();
        String currentPlayer = players.get(model.returnPlayerIndex()).getName();
        view.playerLabel.setText(currentPlayer + "'s Turn");
    }

    public void implementUndo(){ // method to handle undo functionality @Grant
        model.saveStateRedo();
        model.loadState();
        ArrayList<Player> players = model.getGamePlayers();
        Player currentPlayer = players.get(model.returnPlayerIndex());
        ArrayList<Card> cards = model.getCurrentPlayerHand(currentPlayer);
        view.addCardsToScrollPane(cards);
        view.updateTopCard();
        //model.clearPreviousState();
        view.disableUndo();
        view.enableRedo();
        view.enableDrawCard();
        view.disableEndTurn();
        updateSpecialStatus("Undid");
        updateStatus("Card Played: " + getTopCard().cardString(model.returnSide()));
    }

    public void implementRedo(){ // method to handle redo functionality @Grant
        model.loadStateRedo();
        ArrayList<Player> players = model.getGamePlayers();
        Player currentPlayer = players.get(model.returnPlayerIndex());
        ArrayList<Card> cards = model.getCurrentPlayerHand(currentPlayer);
        view.addCardsToScrollPane(cards);
        view.updateTopCard();
        //model.clearPreviousState();
        view.disableRedo();
        view.enableUndo();
        view.disableDrawCard();
        view.disableCardButtons();
        view.enableEndTurn();
        updateSpecialStatus("Redid");
        updateStatus("Card Played: " + getTopCard().cardString(model.returnSide()));
    }

    public void updateStatus(String statusText){ // Update the game status text box in gui @Ethan
        if(model.returnTop().getColor(model.returnSide()) == Color.WILD || model.returnTop().getColor(model.returnSide()) == Color.DARK){
            Color tempColor = model.getTempColor();
            if (tempColor != null) {
                view.status.setText("Current Color: " + tempColor.toString());
            } else {
                // Handle the null case, maybe set a default color or an error message
                view.status.setText("Current Color: not set");
            }
        }else {
            view.status.setText(statusText);
        }
    }

    public void updateSpecialStatus(String statusText){
        view.statusSpecial.setText(statusText);
    }

    public void clearSpecialStatus(){
        view.statusSpecial.setText("");
    }

    @Override
    public void onRedo(){
        implementRedo();
    }

    @Override
    public void onUndo(){
        implementUndo();
    }


    @Override
    public void onCardSelected(int cardIndex) { // Grant
        takePlayerTurn(cardIndex);
        view.disableCardButtons();
    }

    @Override
    public void onNextPlayer() { // Grant
        // Implementation of what happens when the next player button is clicked
        endTurn();
    }
    @Override
    public void onDrawCard() { // Grant
        // Implementation of what happens when the next player button is clicked
        drawCard();
    }

    @Override
    public void onNewGame(JFrame frame) { // Grant
        frame.dispose();
        String[] argsToPass = null;
        view.main(argsToPass);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(view,
                        "New game has been created",
                        "Game Creator",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    @Override
    public void loadGame(){ // deserializes important game elements when using Load Game @Ethan
        ArrayList<Object> deserialized = new ArrayList<Object>();

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("gameSave.ser"))) {
            deserialized = (ArrayList<Object>) objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error importing address book: " + e.getMessage());
        }

        model.loadStateSerialization(deserialized);
        ArrayList<Player> players = model.getGamePlayers();
        Player currentPlayer = players.get(model.returnPlayerIndex());
        ArrayList<Card> cards = model.getCurrentPlayerHand(currentPlayer);
        view.addCardsToScrollPane(cards);
        view.updateTopCard();

        if((boolean)deserialized.get(5)){
            view.enableUndo();
            view.disableCardButtons();
        }
        else{
            view.disableUndo();
        }
        if((boolean)deserialized.get(6)){
            view.enableRedo();
        }
        else{
            view.disableRedo();
        }
        if((boolean)deserialized.get(7)){
            view.enableEndTurn();
        }
        else{
            view.disableEndTurn();
        }
        if((boolean)deserialized.get(8)){
            view.enableDrawCard();
        }
        else{
            view.disableDrawCard();
        }
        updatePlayerLabel();
        if(model.getGamePlayers().get((int)deserialized.get(0)) instanceof PlayerAI){
            view.disableCardButtons();
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(view,
                        "Game has been loaded",
                        "Game Loader",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

    }

    @Override
    public void saveGame() { // serializes important game elements when Save Game is used @Ethan

        ArrayList<Object> data = new ArrayList<Object>();
        data.add(model.returnPlayerIndex()); //0
        data.add(model.returnSide()); //1
        data.add(model.returndiscardedCards()); //2
        data.add(model.returnDeck().deepCopy()); //3
        data.add(model.getGamePlayers()); //4
        data.add(view.undoButton.isEnabled()); //5
        data.add(view.redoButton.isEnabled()); //6
        data.add(view.nextPlayerButton.isEnabled()); //7
        data.add(view.drawCardButton.isEnabled()); //8
        // serialize game data for undo
        data.add(model.tempIndex); //9
        data.add(model.temporySide); //10
        data.add(model.tempDiscardPile); //11
        data.add(model.savedDeckState.deepCopy()); //12
        data.add(model.tempPlayers); //13
        // serialize game data for redo
        data.add(model.tempIndex2); //14
        data.add(model.temporySide2); //15
        data.add(model.tempDiscardPile2); //16
        data.add(model.savedDeckState2.deepCopy()); //17
        data.add(model.tempPlayers2); //18


        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("gameSave.ser"))) {
            objectOutputStream.writeObject(data); // serialize game data
            objectOutputStream.close();

        } catch (IOException e) {
            System.err.println("Error saving game data: " + e.getMessage());
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(view,
                        "Game has been saved",
                        "Game Saver",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

    }
}


