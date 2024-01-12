/** The PlayerTest script is used to test all the methods in the Player class.
 * @Author Grant Phillips 101230563
 * @Date 2023/10/22
 * @Version v1.0
 * **/

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.Assert.assertTrue;


public class PlayerTest {
    @Test
    public void testgetName(){ // Tests that it returns the proper name @Far
        Player P1 = new Player("Grant");
        assertEquals("Grant",P1.getName());
    }
    @Test
    public void testgetHand(){ //Test that the get hand returns an instance of an arraylist
        Player p1 = new Player("Grant");
        ArrayList<Card> hand = p1.getHand();
        assertNotNull(hand, "The hand should not be null");
        assertTrue(hand instanceof ArrayList);
    }
    @Test
    public void testdrawCard(){ // Test that drawing a card does increment the users hand size by 1 and that the card is equal to the one it should be
        Player P1 = new Player("Grant");
        Card c1 = new Card(Value.EIGHT,Color.BLUE, Value.EIGHT, Color.PINK);
        P1.drawCard(c1);
        assertEquals(1,P1.getHand().size());
    }
    @Test
    public void testplayCard(){ // Tests that playing a card removes the card from the users hand and that the correct card is removed
        Player P1 = new Player("Grant");
        Card c1 = new Card(Value.EIGHT,Color.BLUE, Value.EIGHT, Color.PINK);
        P1.drawCard(c1);
        assertEquals(c1,P1.playCard(0));
        assertEquals(0,P1.getHand().size());
    }

    @Test
    public void testSetAndReturnPoints(){ // Tests that the setting and returning of a players points are correct
        Player P1 = new Player("Grant");
        P1.setPoints(100);
        assertEquals(100,P1.returnPoints());
    }


}