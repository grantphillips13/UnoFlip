Project Title: SYSC3110 Project - Uno Flip!
Version: 4.0
Authors: Faareh Bashir, Grant Phillips, Ethan Stewart, Sam Wilson

Overview:

This project is the fourth version of an Uno Flip! game. Version 4.0 now includes
new features such as saving and loading a game, undoing and redoing moves, and 
restarting a game at any time.



Version Additions:

- Added undo and redo functionality for playing cards
- Added a Game Menu dropdown with the following options:
	- New Game: Restarts the game
	- Save Game: Saves the current state of the Uno game
	- Load Game: Loads the most recently saved state of the Uno game
	- Quit: Closes the game

Game Details:

Players: 1-4
AI Players: 0-4
Card Set: Official Uno Flip! card set is used.

Light Side of cards:
 - Two of each number card of each light color (RED, BLUE, GREEN, YELLOW) (72)
 - Two of each light action card of each light color (DRAW ONE, REVERSE, SKIP, FLIP) (32)
 - Four of each light wild card (WILD, WILD DRAW TWO) (8)
 - Total: 112 cards

Dark Side of cards:
 - Two of each number card of each dark color (ORANGE, PINK, TEAL, PURPLE) (72)
 - Two of each dark action card of each dark color (DRAW FIVE, REVERSE, SKIP EVERYONE, FLIP) (32)
 - Four of each dark wild card (WILD, WILD DRAW COLOR) (8)
 - Total: 112 cards



Execution Instructions:

1. Execute the UnoGameView class to run the Uno Game (IDE)

or

2. Execute the executable java file

* Card images folder has to be within folder as 3110-Project to see the cards on the 
buttons and the top card. **Note: Card Images is also a source should you open it up in an IDE



Gameplay:

1. Once game is running input amount of players (1-4)
2. Then input number of AI players (0-4)
	- If only one real player is playing, at least one AI player is required
3. Give each of those players a name
4. Players take turns playing or drawing a card
5. Play a card by selecting a card from your hand
	- Card must either match the colour or value of the previously played card
	- See 'Action Card Functionality' for info on the effects of each action card
6. Or, use the 'Draw Card' button to draw a card from the deck
7. Use 'End Turn' button to end turn after playing or drawing card
8. AI players will automatically take their turn and the next player must push the 'End Turn' button to continue.
9. Players continue taking turns until a player places their last card
10. If draw pile runs out of cards, the discard pile is shuffled and recycled.
11. Once a round ends the points are tallyed and displayed, then a new round begins
	- See 'Scoring'
12. First player to 500 points wins the game



Action Card Functionality:

- WILD cards: The player must pick a colour which then becomes the current colour
- DRAW cards: The next player must draw the specified number of cards (+1, +2, +5)
	- DRAW COLOR cards require the next player to continue drawing cards until the specified color is drawn.	
- SKIP cards: The next player's turn is skipped
- SKIP EVERYONE cards: All player's turns are skipped and the player who played this card plays again.
- REVERSE cards: The order of players is switched.
- FLIP cards: All cards are flipped! If cards are on light side: change to dark, and vice-versa



Scoring:

CARD VALUES:
- All number cards (1-9): Face value
- Draw One: 10
- Draw Five, Reverse, Skip, Flip: 20
- Skip Everyone: 30
- Wild: 40
- Wild Draw Two: 50
- Wild Draw Color: 60

When a player/AI places their last remaining card and wins the round, the values of all remaining cards in all other players'
hands are added to the winning player's score. First player to 500 points wins.

* If game ends on light side: tally up only light side. If game ends on dark side: tally up only dark side


Other Features:

- Game Menu: A dropdown menu which allows players to select the following buttons:
	- New Game: Restarts the game allowing the user to set the number of players and AI players and starts a new game.
	- Save Game: Saves the current state of the game, icluding previous moves made, the score for all players, and the cards the players have when the save is made.
	- Load Game: Loads the most recently saved game, icluding previous moves made, the score for all players, and the cards the players have when the save was made.
	- Quit Game: Closes the game.
- Undo Button: Allows a non-AI player to undo a move once they select a card, so the card they selected before hitting undo will be deselected and they can select a new card.
- Redo Button: Allows a non-AI player to redo the most recent move that they have undone, so the card they selected before hitting undo will be selected again. 
