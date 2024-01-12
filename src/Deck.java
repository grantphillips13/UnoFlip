/** The Deck class represents the full deck of Uno cards. It consists of an ArrayList of Card objects
 * that can be drawn from.
 * @Author Sam Wilson 101195493
 * @Date 2023/10/22
 * @Version v1.4
 * **/

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
public class Deck implements Serializable {

    //Arraylist of cards in deck
    private ArrayList<Card> cards = new ArrayList<>();

    //deck constructor
    public Deck() {
        this.makeDeck();
    }

    //creates a deck with all required cards
    public void makeDeck(){
        //Adding 2 of each number of each color
        for(int i = 0; i < 2; i++) {
            for (int j = 0; j <= 8; j++) {
                for (int p = 0; p <=3; p++){
                    this.cards.add(new Card(Value.values()[j], Color.values()[p], Value.values()[j], Color.values()[p+5]));
                }
            }
            //Adding 2 of each action card of each color
            for (int p = 0; p < 3; p++){
                this.cards.add(new Card(Value.DRAW_ONE, Color.values()[p], Value.DRAW_FIVE, Color.values()[p+5]));
                this.cards.add(new Card(Value.REVERSE, Color.values()[p], Value.REVERSE, Color.values()[p+5]));
                this.cards.add(new Card(Value.SKIP, Color.values()[p], Value.SKIP_EVERYONE, Color.values()[p+5]));
                this.cards.add(new Card(Value.FLIP, Color.values()[p], Value.FLIP, Color.values()[p+5]));
            }
            //Adding 4 of each wild card
            for (int j = 0; j < 4; j++) {
                this.cards.add(new Card(Value.WILD, Color.WILD, Value.WILD, Color.DARK));
                this.cards.add(new Card(Value.WILD_DRAW_TWO, Color.WILD, Value.WILD_DRAW_COLOR, Color.WILD));
            }
        }
        this.shuffleDeck();
    }

    //shuffle the deck
    public void shuffleDeck(){
        Collections.shuffle(this.cards);
    }

    //draw a card from the deck
    public Card drawCard(){
        return this.cards.remove(0);
    }

    //check if deck is empty
    public boolean isEmpty(){
        return this.cards.isEmpty();
    }

    //return size of deck
    public int getSize() {
        return cards.size();
    }

    //get the arraylist of cards
    public ArrayList<Card> getCards(){
        return this.cards;
    }

    //add card to deck
    public void addCard(Card card) {
        cards.add(card);
    }

    public Deck deepCopy() {
        Deck newDeck = new Deck();
        newDeck.cards.clear(); // Clear the initial cards

        for (Card card : this.cards) {
            newDeck.cards.add(card.cloneCard());
        }
        return newDeck;
    }

    //refill deck with a given arraylist of card, and shuffle
    public void refillDeck(ArrayList<Card> cards){
        for(Card card: cards) {
            this.cards.add(card);
        }
        this.shuffleDeck();
    }

    //set first card in deck
    public void setFirstCard(Card card){
        this.cards.add(card);
    }
}