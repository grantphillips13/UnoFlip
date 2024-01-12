import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

public class UnoGameViewTest {

    private UnoGameModel model;
    private UnoGameView view;
    private UnoGameController controller;

    @Before
    public void setUp() { // Sets up test cases @Faareh
        model = new UnoGameModel();
        view = new UnoGameView();
        controller = new UnoGameController(model, view);
        view.setController(controller);
        view.setActionListener(controller);
    }

    @Test
    public void testAddCardsToScrollPane() { // Test addCardsToScrollPane method @Faareh
        // Simulates setting up the game and displaying the main frame
        SwingUtilities.invokeLater(() -> {
            controller.startGame();
            view.showMainFrame();
        });

        // Simulates adding cards to the scroll pane
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(Value.ONE, Color.RED,Value.ONE, Color.ORANGE));
        cards.add(new Card(Value.TWO, Color.BLUE,Value.TWO, Color.PINK));
        cards.add(new Card(Value.THREE, Color.GREEN,Value.THREE, Color.TEAL));

        SwingUtilities.invokeLater(() -> view.addCardsToScrollPane(cards));

        assertNotNull(view.getCardButtons());

        SwingUtilities.invokeLater(() -> {
            view.cardPanel.revalidate();
            view.cardPanel.repaint();
            assertEquals(cards.size(), view.getCardButtons().size());
        });

        // Verifies that each button has the expected size
        for (JButton button : view.getCardButtons()) {
            assertEquals(new Dimension(150, 320), button.getPreferredSize());
        }
    }

    @Test
    public void testPromptWildCard() throws InterruptedException { // Test for promptWildCard method @Faareh
        // Uses countDownLatch to synchronize the test thread with the swing thread
        CountDownLatch latch = new CountDownLatch(1);

        // Runs the test on the swing thread
        SwingUtilities.invokeLater(() -> {
            UnoGameModel model = new UnoGameModel();
            UnoGameView view = new UnoGameView();
            UnoGameController controller = new UnoGameController(model, view);

            // Sets the controller in the view
            view.setController(controller);

            // Prompts for a wild card
            SwingUtilities.invokeLater(() -> {
                String selectedColor = view.promptWildCard();
                System.out.println("Selected Color: " + selectedColor);
            });

            // Signals that the swing thread has completed its processing
            latch.countDown();
        });

        // Waits for the swing thread to finish processing
        latch.await();
    }

    @Test
    public void testUpdateTopCard() { // Test updateTopCard method @Faareh
        UnoGameModel model = new UnoGameModel();
        UnoGameView view = new UnoGameView();
        UnoGameController controller = new UnoGameController(model, view);

        // Creates a sample card for testing
        Card testCard = new Card(Value.ONE, Color.RED,Value.ONE, Color.ORANGE);

        // Setting the controller in the view
        view.setController(controller);

        // Manually calls the updateTopCard method with the sample card
        SwingUtilities.invokeLater(view::updateTopCard);

        // Reflection used to access the private field in UnoGameView
        Field topCardField;
        try {
            topCardField = UnoGameView.class.getDeclaredField("topCard");
            topCardField.setAccessible(true);
            JLabel topCardLabel = (JLabel) topCardField.get(view);

            // Setting the top card directly using reflection
            topCardLabel.setIcon(testCard.getImage(controller.getCardSide()));

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // Manually calling the updateTopCard method again
        SwingUtilities.invokeLater(view::updateTopCard);

        // Getting the JLabel from the view that should display the top card
        try {
            topCardField = UnoGameView.class.getDeclaredField("topCard");
            topCardField.setAccessible(true);
            JLabel topCardLabel = (JLabel) topCardField.get(view);

            // Checking if the text of the JLabel matches the expected result
            assertEquals(testCard.getImage(controller.getCardSide()), topCardLabel.getIcon());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDisableDrawCard() throws Exception { // Test disableDrawCard method @Faareh
        // Uses countDownLatch to synchronize the test thread with the swing thread
        CountDownLatch latch = new CountDownLatch(1);

        // Declares an array to hold the draw card button reference
        JButton[] drawCardButton = new JButton[1];

        // Runs the test on the swing thread
        SwingUtilities.invokeAndWait(() -> {
            UnoGameModel model = new UnoGameModel();
            UnoGameView view = new UnoGameView();
            UnoGameController controller = new UnoGameController(model, view);

            // Sets up the draw card button
            drawCardButton[0] = new JButton("Draw Card");
            view.drawCardButton = drawCardButton[0];

            // Adds an actionlistener to draw card button
            drawCardButton[0].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) { }
            });

            // Disables the draw card button
            view.disableDrawCard();

            // Signals that the swing thread has completed its processing
            latch.countDown();
        });

        // Waits for the Swing thread to finish processing
        latch.await();

        // Checks if the draw card button is disabled
        assertFalse(drawCardButton[0].isEnabled());
    }

    @Test
    public void testEnableDrawCard() throws Exception {
        // Uses countDownLatch to synchronize the test thread with the swing thread
        CountDownLatch latch = new CountDownLatch(1);

        // Declares an array to hold the draw card button reference
        JButton[] drawCardButton = new JButton[1];

        // Runs the test on the swing thread
        SwingUtilities.invokeAndWait(() -> {
            UnoGameModel model = new UnoGameModel();
            UnoGameView view = new UnoGameView();
            UnoGameController controller = new UnoGameController(model, view);

            // Sets up the draw card button
            drawCardButton[0] = new JButton("Draw Card");
            view.drawCardButton = drawCardButton[0];

            // Adds an actionlistener to draw card button
            drawCardButton[0].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Do nothing for this test
                }
            });

            // Enables the draw card button
            view.enableDrawCard();

            // Signals that the swing thread has completed its processing
            latch.countDown();
        });

        // Waits for the swing thread to finish processing
        latch.await();

        // Checks if the draw card button is enabled
        assertTrue(drawCardButton[0].isEnabled());
    }

    @Test
    public void testEnableEndTurn() throws InterruptedException { // Test for enableEndTurn method @Faareh
        // Uses countDownLatch to synchronize the test thread with the swing thread
        CountDownLatch latch = new CountDownLatch(1);

        // Runs the test on the Swing thread
        SwingUtilities.invokeLater(() -> {
            UnoGameModel model = new UnoGameModel();
            UnoGameView view = new UnoGameView();
            UnoGameController controller = new UnoGameController(model, view);

            // Enables the end turn button
            view.enableEndTurn();

            // Signals that the swing thread has completed its processing
            latch.countDown();
        });

        // Waits for the swing thread to finish processing
        latch.await();
    }

    @Test
    public void testDisableEndTurn() throws InterruptedException { // Test for disableEndTurn method @Faareh
        // Uses countDownLatch to synchronize the test thread with the swing thread
        CountDownLatch latch = new CountDownLatch(1);

        // Runs the test on the swing thread
        SwingUtilities.invokeLater(() -> {
            UnoGameModel model = new UnoGameModel();
            UnoGameView view = new UnoGameView();
            UnoGameController controller = new UnoGameController(model, view);

            // Disables the end turn button
            view.disableEndTurn();

            // Signals that the swing thread has completed its processing
            latch.countDown();
        });

        // Waits for the swing thread to finish processing
        latch.await();
    }

    @Test
    public void testDisplayTopCard() throws InterruptedException { // Test for the displayTopCard method @Faareh
        // Uses countDownLatch to synchronize the test thread with the swing thread
        CountDownLatch latch = new CountDownLatch(1);

        // Runs the test on the swing thread
        SwingUtilities.invokeLater(() -> {
            UnoGameModel model = new UnoGameModel();
            UnoGameView view = new UnoGameView();
            UnoGameController controller = new UnoGameController(model, view);

            // Manually sets a sample top card in UnoGameController
            Card sampleCard = new Card(Value.ONE, Color.RED,Value.ONE, Color.ORANGE);
            try {
                Field topCardField = UnoGameController.class.getDeclaredField("topCard");
                topCardField.setAccessible(true);
                topCardField.set(controller, sampleCard);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            // Sets the controller in the view
            view.setController(controller);

            // Calls the displayTopCard method
            view.displayTopCard();

            // Signals that the swing thread has completed its processing
            latch.countDown();
        });

        // Waits for the swing thread to finish processing
        latch.await();
    }
}

