import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.Assert.assertTrue;


public class PlayerAITest {

    Card c1 = new Card(Value.ONE, Color.RED,Value.ONE, Color.ORANGE);
    Card c2 = new Card(Value.TWO, Color.BLUE,Value.TWO, Color.PINK);
    Card c3 = new Card(Value.THREE, Color.GREEN,Value.THREE, Color.TEAL);
    Card c4 = new Card(Value.THREE, Color.BLUE,Value.THREE, Color.PINK);
    Card c5 = new Card(Value.WILD, Color.WILD,Value.WILD, Color.DARK);

    @Test
    // Test playerAI's getNextPlay scenarios @Ethan
    public void testgetNextPlay(){
        UnoGameModel game = new UnoGameModel();
        String[] names = new String[2];
        names[0] = ("grant");
        names[1] = ("AI1");
        game.setPlayerNames(names,2);
        ArrayList<Player> players = game.getGamePlayers();
        PlayerAI player = (PlayerAI) players.get(1);

        players.get(0).clearHandForTest();
        players.get(0).drawCard(c1);
        players.get(0).drawCard(c2);
        players.get(1).clearHandForTest();
        players.get(1).drawCard(c4);
        players.get(1).drawCard(c5);

        int cardIndex = player.getNextPlay();
        game.setTopDiscarded(c2);

        assertEquals(1, cardIndex);

        players.get(0).clearHandForTest();
        players.get(0).drawCard(c1);
        players.get(0).drawCard(c2);
        players.get(0).drawCard(c3);
        players.get(0).drawCard(c3);
        players.get(0).drawCard(c1);

        game.playerTurn(players.get(0),1);
        System.out.println(game.returnTop().cardString(game.returnSide()));
        game.tickPlayer();

        players.get(1).clearHandForTest();
        players.get(1).drawCard(c4);
        players.get(1).drawCard(c5);
        players.get(1).drawCard(c3);
        players.get(1).drawCard(c1);

        cardIndex = player.getNextPlay();

        assertEquals(0, cardIndex);
    }

    @Test
    // Test PlayerAI's ability to choose the color for wild cards @Ethan
    public void testchooseBestColorForWild(){
        UnoGameModel game = new UnoGameModel();
        String[] names = new String[2];
        names[0] = ("grant");
        names[1] = ("AI1");
        game.setPlayerNames(names,2);
        ArrayList<Player> players = game.getGamePlayers();
        players.get(1).clearHandForTest();
        players.get(1).drawCard(c4);
        players.get(1).drawCard(c4);
        players.get(1).drawCard(c3);
        players.get(1).drawCard(c3);
        players.get(1).drawCard(c3);

        PlayerAI player = (PlayerAI) players.get(1);
        Color color = player.chooseBestColorForWild();

        assertEquals(Color.GREEN, color);

    }

}
