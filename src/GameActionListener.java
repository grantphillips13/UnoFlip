/** The GameActionListener is called by the buttons within the UnoGameView, this serves as part of the Java Event model to handle the user buttons clicks and send their info and clicks
 * to the controller to handle the data and game flow from those respective clicks
 * @Author Grant Phillips
 * @Date 2023/11/13
 * @Version v1.0
 * **/


import javax.swing.*;
public interface GameActionListener {
    void onCardSelected(int cardIndex);
    void onNextPlayer();
    void onDrawCard();
    void onNewGame(JFrame frame);
    void onUndo();
    void onRedo();
    void loadGame();
    void saveGame();
}
