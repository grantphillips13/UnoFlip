/** The Player class is used to define the players and their functuinality for the Uno Game
 * @Author Faareh
 * @Date 2023/10/22
 * @Version v1.0
 * **/

import java.io.Serializable;
import java.util.ArrayList;



public class Player implements Serializable {

    // Players points
    private int points;

    // String Name
    private String name;

    // List<card> hand
    private ArrayList<Card> hand;

    // int cards
    int cards;

    //Constructor
    public Player(){
        hand = new ArrayList<>();
        cards = 0;
    }
    public Player(String name) {
        this.name = name;
        hand = new ArrayList<>();
        cards = 0;
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

    public void setHand(ArrayList<Card> cards){
        hand.clear();
        hand.addAll(cards);
    }

    //set the players points
    public void setPoints(int roundPoints){
        points += roundPoints;
    } // Grant

    //return an int of the players points
    public int returnPoints(){
        return points;
    } // Grant


    // playCard
    public Card playCard(int cardNum) {
        return this.hand.remove(cardNum);
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