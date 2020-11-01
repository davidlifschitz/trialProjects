import java.util.Arrays;

/*

    There are four suits.
    Each suit has 13 numbers.
    each card has a 1/52 chance of being drawn.
    2-9 players, each player gets 2 cards.

    What I need:
    Object called player- has three variables:
        1) a hand which can hold up 2 cards(array of length 2 of type card.)
        2) a wallet containing how much money the player has.
        3) a "gameplay" wallet containing how much money the player has at a table (max and min limits apply 
            - make sure the player can leave with correct amount if:
                {money made + original gW > max limit} or if 
                {money lost + original gW < min limit}
    object called card which has 2 variables - number(A-K) and suit(spades,hearts,clubs,diamonds - should be enums) 
    Set up functionality to create a game. when the person quits the game ends.
    each game has rounds that are continuously going as long as there are at least 2 players playing - if not then don't deal hands.
    at the beginning of each round, each player is given two cards in their hand. they c

*/

class TexasHoldEm {
    public static void main(String[] args) {
        
    }
}