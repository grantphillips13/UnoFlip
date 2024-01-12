/** The DeckTest script is used to test all the methods in the Deck class.
 * @Author Sam Wilson 101195493
 * @Date 2023/10/22
 * @Version v1.0
 * **/
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
public class DeckTest {
    @Test
    //Test for make deck function @Faarah
    public void testMakeDeck(){
        //Create 2 new decks
        Deck deck = new Deck();
        Deck deck2 = new Deck();

        //Tests that the decks have the correct number of cards
        assertEquals(112, deck.getCards().size());
        assertEquals(112, deck2.getCards().size());

        //Tests if the two decks are in a different order in at least one place
        //because the makeDeck method also shuffles the deck
        boolean test = false;
        for (int i = 0; i < 112; i++) {
            if(!(deck.getCards().get(i).getColor(Side.LIGHT).equals(deck2.getCards().get(i).getColor(Side.LIGHT))) || (deck.getCards().get(i).getValue(Side.LIGHT).equals(deck2.getCards().get(i).getValue(Side.LIGHT)))){
                test = true;
            }
        }
        assertTrue(test);
    }
    @Test
    //Test for deck shuffle function @Faarah
    public void testShuffleDeck(){
        //Create 2 new decks
        Deck deck = new Deck();
        Deck deck2 = new Deck();

        //shuffle decks
        deck.shuffleDeck();
        deck2.shuffleDeck();

        //Tests that the order of the two decks is different in at least one place
        boolean test = false;
        for (int i = 0; i < 104; i++) {
            if(!(deck.getCards().get(i).getColor(Side.LIGHT).equals(deck2.getCards().get(i).getColor(Side.LIGHT))) || (deck.getCards().get(i).getValue(Side.LIGHT).equals(deck2.getCards().get(i).getValue(Side.LIGHT)))){
                test = true;
            }
        }
        assertTrue(test);
    }
    @Test
    //Test for drawing card function @Faarah
    public void testDrawCard(){
        //Create 2 new decks
        Deck deck1 = new Deck();
        Deck deck2 = new Deck();

        //Tests that a card was drawn and returned
        assertNotNull(deck1.drawCard());
        assertNotNull(deck2.drawCard());

        //Tests that the drawn cards are different
        assertNotEquals(deck1.drawCard(), deck1.drawCard());
    }
    @Test
    //Test for refilling deck from discard pile function @Faarah
    public void testRefillDeck(){
        //Create new deck
        Deck deck = new Deck();
        Deck discard = new Deck();

        //Remove all cards from deck and check that size is zero
        int size = deck.getCards().size();
        for(int i = 0; i < size; i++){
            deck.getCards().remove(0);
        }
        assertEquals(0, deck.getCards().size());

        //Refill deck with all cards from discard and check that size is 104
        deck.refillDeck(discard.getCards());
        assertEquals(112, deck.getCards().size());
    }
    @Test
    public void testSetFirstCard(){
        //Create a new deck and card, new card should be the 112th card @Faarah
        Deck deck = new Deck();
        Card card = new Card(Value.FIVE, Color.RED, Value.FIVE, Color.ORANGE);

        deck.setFirstCard(card);

        //Tests that size is correct and that the new card is the last card
        assertEquals(113, deck.getCards().size());
        assertTrue((deck.getCards().get(112).getValue(Side.LIGHT)==Value.FIVE)&&(deck.getCards().get(112).getColor(Side.LIGHT)==Color.RED));
        assertTrue((deck.getCards().get(112).getValue(Side.DARK)==Value.FIVE)&&(deck.getCards().get(112).getColor(Side.DARK)==Color.ORANGE));

    }
    @Test
    public void testIsEmpty(){
        //Create 2 new decks @Faarah
        Deck deck1 = new Deck();
        Deck deck2 = new Deck();

        //Remove all cards from one deck
        int size = deck2.getCards().size();
        for(int i = 0; i < size; i++){
            deck2.getCards().remove(0);
        }
        assertEquals(0, deck2.getCards().size());

        //Test that deck2 is empty and deck1 is not
        assertFalse(deck1.isEmpty());
        assertTrue(deck2.isEmpty());
    }
    @Test
    public void testGetCards(){
        //Test creating new deck @Faarah
        Deck deck = new Deck();

        //Test that getCards() is not null and has the correct size
        assertNotNull(deck.getCards());
        assertEquals(112, deck.getCards().size());
    }
}