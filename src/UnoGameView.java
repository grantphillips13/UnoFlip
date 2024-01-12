/** The UnoGameView class is the view class that is the main source of GUI content responsible for all the displaying and interaction with the user, the view class
 * takes in the user input and works with the data and game flow of the controller to allow the game to be playable
 * @Author Grant Phillips, Sam Wilson, Ethan Stewart and Faareh Bashir
 * @Date 2023/11/13
 * @Version v1.0
 * **/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class UnoGameView extends JPanel {
    //declaring java swing GUI components
    private GameActionListener actionListener;

    private ArrayList<JButton> cardButtons = new ArrayList<>();
    JFrame frame;
    JLabel playerLabel, topCardWord, topCard, status, statusSpecial;
    JPanel topCardPanel, structurePanel, buttonPanel, menuHolderPanel, statusPanel, cardPanel, playerLabelPanel, undoRedoPanel;
    JButton nextPlayerButton, drawCardButton, undoButton, redoButton;
    JScrollPane cardScrollPane;
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem newGameMenuItem, quitMenuItem, saveMenuItem, loadMenuItem;
    UnoGameController controller;

    public UnoGameView() {
        topCardWord = new JLabel();
        topCardWord.setText("Top Card");
        Dimension size = topCardWord.getPreferredSize();
        topCardWord.setBounds(170, 4, size.width, size.height);


        playerLabel = new JLabel();

        topCard = new JLabel();

        status = new JLabel();
        status.setText("Status:");

        statusSpecial = new JLabel();
        statusSpecial.setText("");


        topCardPanel = new JPanel();
        topCardPanel.setBounds(300,0, 650, 20);
        topCardPanel.setLayout(null);

        structurePanel = new JPanel();
        structurePanel.setBounds(0,20 , 1000, 200);

        buttonPanel = new JPanel();
        buttonPanel.setBounds(0,220, 1000, 430);

        undoRedoPanel = new JPanel();
        undoRedoPanel.setBounds(57,415, 70, 100);

        menuHolderPanel = new JPanel();
        menuHolderPanel.setBounds(0,0,100,20);

        statusPanel = new JPanel();
        statusPanel.setBounds(700,0,300,60);

        cardPanel = new JPanel();

        playerLabelPanel = new JPanel();
        playerLabelPanel.setBounds(100,0,250,20);

        //JButtons and ScrollPane setup @Sam
        nextPlayerButton = new JButton("End Turn");
        nextPlayerButton.setEnabled(false);

        redoButton = new JButton("redo");
        redoButton.setEnabled(false);

        undoButton = new JButton("Undo");
        undoButton.setEnabled(false);
        drawCardButton = new JButton("Draw Card");
        nextPlayerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(actionListener != null) {
                    actionListener.onNextPlayer();
                }
            }
        });
        drawCardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(actionListener != null) {
                    actionListener.onDrawCard();
                }
            }
        });

        undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(actionListener != null) {
                    actionListener.onUndo();
                }
            }
        });

        redoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(actionListener != null) {
                    actionListener.onRedo();
                }
            }
        });

        // Setup scroll pane holding card buttons @Sam
        cardScrollPane = new JScrollPane();
        cardScrollPane.setWheelScrollingEnabled(true);
        cardScrollPane.setPreferredSize(new Dimension(700, 350));
        cardScrollPane.setViewportView(cardPanel);

        //CREATING MENU COMPONENTS @Sam
        menuBar = new JMenuBar();
        menu = new JMenu("Game Menu");
        newGameMenuItem = new JMenuItem("New Game..");
        saveMenuItem = new JMenuItem("Save Game..");
        loadMenuItem = new JMenuItem("Load Game..");
        quitMenuItem = new JMenuItem("Quit..");
        //GIVING MENU ITEMS ACTION LISTENERS @Sam
        newGameMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(actionListener != null) {
                    actionListener.onNewGame(frame); // start new game
                }
            }
        });
        quitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); //quit game
            }
        });

        saveMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionListener.saveGame();
            }
        });

        loadMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionListener.loadGame();
            }
        });

        //PUTTING MENU COMPONENTS TOGETHER @Sam
        menu.add(newGameMenuItem);
        menu.add(saveMenuItem);
        menu.add(loadMenuItem);
        menu.add(quitMenuItem);
        menuBar.add(menu);

        // Setup frame and panels @Ethan
        frame = new JFrame("Uno Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setSize(1000,650);
        frame.setVisible(true);
        frame.add(undoRedoPanel);
        frame.add(topCardPanel);
        frame.add(structurePanel);
        frame.add(buttonPanel);
        frame.add(playerLabelPanel);
        frame.add(menuHolderPanel);
        frame.add(statusPanel);


        // Adding buttons and labels to panels @Ethan, @Sam
        topCardPanel.add(topCardWord);
        playerLabelPanel.add(playerLabel);
        undoRedoPanel.add(redoButton);
        buttonPanel.add(nextPlayerButton);
        buttonPanel.add(cardScrollPane);
        buttonPanel.add(drawCardButton);
        undoRedoPanel.add(undoButton);
        menuHolderPanel.add(menuBar);
        statusPanel.add(status);
        statusPanel.add(statusSpecial);

        frame.setVisible(false);
        frame.setResizable(false);
    }


    public void setController(UnoGameController controller) {
        this.controller = controller;
    }

    public void setActionListener(GameActionListener listener) {
        this.actionListener = listener;
    }

    // adding card buttons to the card panel @Sam, @Grant
    public void addCardsToScrollPane(ArrayList<Card> cardArrayList){
        cardPanel.removeAll(); //remove all previously existing buttons
        cardButtons.clear();

        // instantiate button for each card in current player's hand
        for(int i = 0; i < cardArrayList.size(); i++) {
            Card card = cardArrayList.get(i);
            JButton buttonToAdd = new JButton(card.getImage(controller.getCardSide())); // set button icon
            buttonToAdd.setPreferredSize(new Dimension(150, 320)); // set button size
            final int cardIndex = i;
            buttonToAdd.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(actionListener != null) {
                        actionListener.onCardSelected(cardIndex); // play selected card
                    }
                }
            });
            cardButtons.add(buttonToAdd); // add button to list of buttons
            cardPanel.add(buttonToAdd); // add button to cardPanel
        }
        cardPanel.revalidate(); // Tell the layout manager to reset based on the new component list
        cardPanel.repaint(); // Tell the panel to repaint itself with the new components
    }
    public ArrayList<JButton> getCardButtons() { // Returns card buttons @Faareh
        return cardButtons;
    }
    public String promptWildCard(){ // Prompt for choosing wildcard color @Faareh
        String[] possibleValues = {"RED","BLUE","GREEN","YELLOW"};
        String selectedValue = (String) JOptionPane.showInputDialog(
                this,
                "What colour would you like to switch it to?",
                "Colour Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                possibleValues,
                possibleValues[0]);

        if (selectedValue == null) {
            controller.onWildDraw(null);
        } else {
            controller.onWildDraw(selectedValue);
        }
        return selectedValue;
    }

    public String promptWildCardDark(){ // Prompt for choosing wildcard color @Faareh
        String[] possibleValues = {"TEAL","ORANGE","PINK","PURPLE"};
        String selectedValue = (String) JOptionPane.showInputDialog(
                this,
                "What colour would you like to switch it to?",
                "Colour Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                possibleValues,
                possibleValues[0]);

        if (selectedValue == null) {
            //controller.onWildDrawDark(null);
        } else {
            controller.onWildDrawDark(selectedValue);
        }
        return selectedValue;
    }

    public void promptForNumberOfPlayers() { // Prompt for choosing number of players @Grant
        Integer[] possibleValues = {1 ,2 ,3 , 4};
        Integer selectedValue = (Integer) JOptionPane.showInputDialog(
                null, // assuming this is within a context where 'this' is appropriate
                "Choose the number of players:",
                "Player Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                possibleValues,
                possibleValues[0]);

        // Check for null to see if the user clicked "Cancel" or closed the dialog
        if(selectedValue == null) {
            // If cancel was pressed, we'll call the method with a 0 to indicate cancellation
            controller.onNumberOfPlayersSelected(0);
        } else {
            // If a valid selection was made, we pass it on
            controller.onNumberOfPlayersSelected(selectedValue);
        }
    }

    public void promptForNumberOfAIPlayers() { // Prompt for choosing number of players @Grant
        Integer[] possibleValues = {0, 1 ,2 ,3 , 4};
        Integer selectedValue = (Integer) JOptionPane.showInputDialog(
                null, // assuming this is within a context where 'this' is appropriate
                "Choose the number of AI players:",
                "Player Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                possibleValues,
                possibleValues[0]);

        // Check for null to see if the user clicked "Cancel" or closed the dialog
        if (selectedValue == null) {
            // If cancel was pressed, we'll call the method with a 0 to indicate cancellation
            controller.onNumberOfAI(0);
        } else {
            // If a valid selection was made, we pass it on
            controller.onNumberOfAI(selectedValue);
        }
    }

    public void promptForNumberOfAIPlayersIfOnlyOnePlayer() { // Prompt for choosing number of players @Grant
        Integer[] possibleValues = {1 ,2 ,3 , 4};
        Integer selectedValue = (Integer) JOptionPane.showInputDialog(
                null, // assuming this is within a context where 'this' is appropriate
                "Choose the number of AI players:",
                "AI Player Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                possibleValues,
                possibleValues[0]);

        // Check for null to see if the user clicked "Cancel" or closed the dialog
        if (selectedValue == null) {
            // If cancel was pressed, we'll call the method with a 0 to indicate cancellation
            controller.onNumberOfAI(0);
        } else {
            // If a valid selection was made, we pass it on
            controller.onNumberOfAI(selectedValue);
        }
    }



    public void updateTopCard(){ // Update top card icon in GUI @Grant
        ImageIcon newIcon = controller.getTopCard().getImage(controller.getCardSide());
        if (newIcon != null) {
            topCard.setIcon(newIcon);
            topCard.revalidate();
            topCard.repaint();
        }
    }

    public void disableCardButtons(){ // Disable card buttons in scroll pane @Grant
        for (JButton button : cardButtons) {
            button.setEnabled(false);
        }
        cardPanel.revalidate();
        cardPanel.repaint();

    }

    public void disableDrawCard(){ // Disable draw card button @Grant
        drawCardButton.setEnabled(false);
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    public void enableDrawCard(){ // Enable draw card button @Grant
        drawCardButton.setEnabled(true);
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    public void enableEndTurn(){ // Enable end turn button @Grant
        nextPlayerButton.setEnabled(true);
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    public void enableUndo(){ // Enable undo button @Grant
        undoButton.setEnabled(true);
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    public void disableUndo(){ // Disable undo button @Grant
        undoButton.setEnabled(false);
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    public void enableRedo(){ // Enable redo button @Grant
        redoButton.setEnabled(true);
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    public void disableRedo(){ // Disable redo button @Grant
        redoButton.setEnabled(false);
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    public void disableEndTurn(){ // Disable end turn button @Grant
        nextPlayerButton.setEnabled(false);
        cardPanel.revalidate();
        cardPanel.repaint();
    }
    public void displayTopCard(){ // Display the top card icon in GUI @Grant
        //topCard.setText("Top Card");
        topCard.setIcon(controller.getTopCard().getImage(controller.getCardSide()));
        structurePanel.add(topCard);
    }


    public void showMainFrame() {
        frame.setVisible(true);
    } // Make main frame visible @Grant

    public void promptForNamesOfPlayers(int numPlayers, int num) { // Prompt for player names @Grant
        int val = numPlayers + num;
        String[] playerNames = new String[val];
        for (int i = 0; i < numPlayers; i++) {
            // Repeatedly show the input dialog until a valid name is entered (non-empty and non-null)
            String name;
            do {
                name = JOptionPane.showInputDialog(
                        frame,
                        "Enter name for Player " + (i + 1) + ":",
                        "Player Name",
                        JOptionPane.QUESTION_MESSAGE
                );
                // If 'Cancel' is pressed, you can decide if you want to exit or set a default name
                if (name == null) {
                    System.out.println("Player name entry cancelled.");
                    name = "Player " + (i + 1); // Optionally set a default name
                    break; // Optionally break out of the loop
                } else if (name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter a valid name.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                }
            } while (name.trim().isEmpty());

            playerNames[i] = name.trim();
        }
        controller.setPlayerNames(playerNames, num);

    }

    public static void main(String[] args) { // Grant
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Your GUI initialization and updates go here.
                UnoGameModel model = new UnoGameModel();
                UnoGameView view = new UnoGameView();
                UnoGameController controller = new UnoGameController(model, view);

                view.setActionListener(controller);
                view.setController(controller);
                controller.startGame();
                view.displayTopCard();
                view.showMainFrame(); // This is now inside the EDT
            }
        });
    }

}
