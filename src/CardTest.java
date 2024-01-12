/** The CardTest script is used to test all the methods in the Card class.
 * @Author Ethan, Faareh
 * @Date 2023/10/22
 * @Version v1.0
 * **/

import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.assertEquals;

public class CardTest {

    //create 2 cards to test with
    Card card1 = new Card(Value.ONE, Color.GREEN, Value.ONE, Color.TEAL);
    Card card2 = new Card(Value.FIVE, Color.RED, Value.FIVE, Color.ORANGE);

    //test getValue method @Faareh
    @Test
    public void test_getValue(){
        assertEquals(card1.getValue(Side.LIGHT), Value.ONE);
        assertEquals(card2.getValue(Side.LIGHT), Value.FIVE);
        assertEquals(card1.getValue(Side.DARK), Value.ONE);
        assertEquals(card2.getValue(Side.DARK), Value.FIVE);
    }

    //test getColor method @Faarah
    @Test
    public void test_getColor(){
        assertEquals(card1.getColor(Side.LIGHT), Color.GREEN);
        assertEquals(card2.getColor(Side.LIGHT), Color.RED);
        assertEquals(card1.getColor(Side.DARK), Color.TEAL);
        assertEquals(card2.getColor(Side.DARK), Color.ORANGE);
    }

    //test cardString value @Faarah
    @Test
    public void test_cardString(){
        assertEquals(card1.cardString(Side.LIGHT), "GREEN ONE \n");
        assertEquals(card2.cardString(Side.LIGHT), "RED FIVE \n");
        assertEquals(card1.cardString(Side.DARK), "TEAL ONE \n");
        assertEquals(card2.cardString(Side.DARK), "ORANGE FIVE \n");
    }

    //test getImage method @Faarah
    @Test
    public void test_getImage(){
        assertEquals(String.valueOf(card1.getImage(Side.LIGHT)), "card images/GREEN ONE.png");
        assertEquals(String.valueOf(card1.getImage(Side.DARK)), "card images/TEAL ONE.png");
        assertEquals(String.valueOf(card2.getImage(Side.LIGHT)), "card images/RED FIVE.png");
        assertEquals(String.valueOf(card2.getImage(Side.DARK)), "card images/ORANGE FIVE.png");
    }

}
