import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class UnoGameTest {

    @Test
    public void testSetupPlayers(){ // testing adding AI players, checks that the AI player is added and is an instance of AI player


        UnoGameModel game = new UnoGameModel();
        String[] names = new String[2];
        names[0] = ("grant");
        names[1] = ("AI1");
        game.setPlayerNames(names,2);
        ArrayList<Player> players = game.getGamePlayers();
        assertEquals(2, players.size()); // Check if the correct number of players was created
        assertTrue(players.get(1) instanceof PlayerAI);


    }
    @Test
    public void testplayerTurnForAI(){ // Testing that the AI takes it turns and plays a card from its hand or draws
        UnoGameModel game = new UnoGameModel();
        String[] names = new String[2];
        names[0] = ("grant");
        names[1] = ("AI1");
        game.setPlayerNames(names,2);
        ArrayList<Player> players = game.getGamePlayers();
        assertEquals(2, players.size()); // Check if the correct number of players was created
        assertEquals(7,players.get(1).getHand().size()); // Check the AI has cards

        PlayerAI p1 = (PlayerAI) players.get(1);

        game.playerTurn(players.get(1),p1.getNextPlay());

        //game.playerTurn(players.get(0), );
        game.tickPlayer();
        // Reset System.in after the test
        PlayerAI player = (PlayerAI) players.get(1);
        int cardIndex = player.getNextPlay();

        assertTrue(cardIndex > -2 || cardIndex < 7);
        assertEquals(1,game.returnPlayerIndex());
    }
    @Test
    public void testcalculateScore(){ // Tests the calculating score works with the cards on their dark side and with AI Players
        UnoGameModel game = new UnoGameModel();
        String[] names = new String[3];
        names[0] = ("grant");
        names[1] = ("AI1");
        names[2] = ("AI2");
        game.setPlayerNames(names,2);
        ArrayList<Player> players = game.getGamePlayers();

        game.setTempSide(Side.DARK);

        Card c1 = new Card(Value.ONE,Color.BLUE, Value.ONE,Color.PINK);
        Card c2 = new Card(Value.SKIP, Color.RED, Value.SKIP, Color.ORANGE);
        Card c3 = new Card(Value.DRAW_ONE, Color.BLUE, Value.DRAW_ONE, Color.PINK);
        Card c4 = new Card(Value.WILD, Color.WILD, Value.WILD, Color.DARK);
        Card c5 = new Card(Value.WILD_DRAW_TWO, Color.WILD, Value.WILD_DRAW_COLOR, Color.WILD);
        Card c6 = new Card(Value.REVERSE, Color.YELLOW,Value.REVERSE, Color.PURPLE);


        ArrayList<Card> P1hand = new ArrayList<>();
        ArrayList<Card> P2hand = new ArrayList<>();
        ArrayList<Card> P3hand = new ArrayList<>();

        game.playerClearHandTest(players.get(1));
        game.playerClearHandTest(players.get(2));

        P2hand.add(c1); // Has Blue 1 (score 1)

        P3hand.add(c2); // Has one of each special and wild type (score 140)
        P3hand.add(c3);
        P3hand.add(c4);
        P3hand.add(c5);
        P3hand.add(c6);

        game.setPlayerHand(P2hand,players.get(1));
        game.setPlayerHand(P3hand,players.get(2));

        game.playerClearHand(players.get(0));

        for(Card cards : players.get(0).getHand()){
            System.out.print(cards.cardString(game.returnSide()));
        }

        assertEquals(game.calculateScore(players.get(0)), 151);

    }

    @Test
    public void testhandleSpecial(){ // Tests the handling of the special cards for the dark side, including the wild draw color, +5 and skip everyone
        UnoGameModel game = new UnoGameModel();
        String[] names = new String[2];
        names[0] = ("grant");
        names[1] = ("AI1");
        game.setPlayerNames(names,2);
        ArrayList<Player> players = game.getGamePlayers();

        game.setTempSide(Side.DARK);

        Player nextPlayer = game.getGamePlayers().get((game.returnPlayerIndex() + 1)  % game.getGamePlayers().size());

        Card c1 = new Card(Value.SKIP,Color.RED, Value.SKIP_EVERYONE,Color.ORANGE);
        Card c2 = new Card(Value.REVERSE, Color.RED, Value.REVERSE, Color.ORANGE);
        Card c3 = new Card(Value.DRAW_ONE, Color.RED, Value.DRAW_FIVE, Color.ORANGE);
        Card c4 = new Card(Value.WILD_DRAW_TWO, Color.WILD, Value.WILD_DRAW_COLOR, Color.WILD);
        Card c5 = new Card(Value.WILD, Color.WILD, Value.WILD, Color.DARK);


        game.tickPlayer();
        game.handleSpecial(c1);
        assertEquals(0,game.returnPlayerIndex());

        game.handleSpecial(c3);
        assertEquals(12,players.get(1).getHand().size());

        game.setTempColor("TEAL");
        game.handleSpecial(c4);
        int size = players.get(1).getHand().size() -1;
        assertTrue((players.get(1).getHand().get(size).getColor(Side.DARK) == Color.TEAL));

        Collections.reverse(players);

        game.handleSpecial(c2);
        assertEquals(game.getGamePlayers(), players);

        game.setTempColor("ORANGE");
        game.handleSpecial(c5);
        assertEquals(game.getTempColor(), Color.ORANGE);

    }

    @Test
    public void testisValidPlay(){ // Tests that the dark side colors are still a valid play
        Player P1 = new Player("Grant");
        Card c1 = new Card(Value.EIGHT,Color.BLUE, Value.EIGHT,Color.PINK);
        Card c2 = new Card(Value.EIGHT, Color.RED, Value.EIGHT, Color.ORANGE);
        Card c3 = new Card(Value.ONE, Color.BLUE, Value.ONE, Color.PINK);
        Card c4 = new Card(Value.SEVEN, Color.YELLOW, Value.SEVEN, Color.PURPLE);


        UnoGameModel game = new UnoGameModel();

        game.setTempSide(Side.DARK);

        game.setTopDiscarded(c1);

        assertTrue(game.isValidPlay(c2));
        assertTrue(game.isValidPlay(c3));
        assertFalse(game.isValidPlay(c4));


    }

    @Test
    public void testwildColor(){ // Tests that the dark side wild cards work
        UnoGameModel game = new UnoGameModel();
        String[] names = new String[2];
        names[0] = ("grant");
        names[1] = ("AI1");
        game.setPlayerNames(names,2);
        ArrayList<Player> players = game.getGamePlayers();
        assertEquals(2, players.size()); // Check if the correct number of players was created
        assertEquals(7,players.get(0).getHand().size());


        game.settempColour(Color.ORANGE);

        Card toAdd = new Card(Value.WILD, game.getTempColor(), Value.WILD, game.getTempColor() );
        game.setTopDiscarded(toAdd);



        assertEquals(Color.ORANGE, game.getTempColor());
        assertEquals(game.getTempColor(),game.returnTop().getColor(game.returnSide()));



    }
    @Test
    public void testaddDiscardToDeck(){
        UnoGameModel game = new UnoGameModel();
        String[] names = new String[2];
        names[0] = ("grant");
        names[1] = ("AI1");
        game.setPlayerNames(names,2);
        ArrayList<Player> players = game.getGamePlayers();
        Card topCard = game.returnTop();

        String input2 = "0";
        ByteArrayInputStream in2 = new ByteArrayInputStream(input2.getBytes());
        System.setIn(in2);

        Card c1 = new Card(Value.EIGHT,Color.BLUE, Value.EIGHT, Color.BLUE);
        Card c2 = new Card(Value.FIVE,Color.GREEN, Value.FIVE,Color.TEAL);

        Deck deck = game.returnDeck();

        game.addToDiscard(c1);
        game.addToDiscard(c2);

        assertEquals(3,game.returndiscardedCards().size());


        game.addDiscardToDeck();
        Card topCard2 = game.returnTop();


        assertEquals(1,game.returndiscardedCards().size());
        assertEquals(topCard, topCard2);

        System.setIn(System.in);
    }

    @Test
    public void testCheckPoints(){
        UnoGameModel game = new UnoGameModel();
        String[] names = new String[2];
        names[0] = ("grant");
        names[1] = ("AI1");
        game.setPlayerNames(names,2);
        ArrayList<Player> players = game.getGamePlayers();


        players.get(1).setPoints(400);

        assertTrue(game.hasWon());

        players.get(0).setPoints(500);

        assertFalse(game.hasWon());
    }

    @Test
    public void testNewRoundDiscardToDeck(){
        UnoGameModel game = new UnoGameModel();

        Card c1 = new Card(Value.EIGHT,Color.BLUE, Value.EIGHT,Color.PINK);
        Card c2 = new Card(Value.FIVE,Color.GREEN, Value.FIVE,Color.TEAL);

        Deck deck = game.returnDeck();

        game.addToDiscard(c1);
        game.addToDiscard(c2);

        assertEquals(3,game.returndiscardedCards().size());


        game.NewRoundDiscardToDeck();
        Card topCard2 = game.returnTop();


        assertEquals(1,game.returndiscardedCards().size());

        System.setIn(System.in);


    }



}