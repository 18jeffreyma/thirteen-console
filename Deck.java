import java.util.Collections;
import java.util.LinkedList;

/**
 * A class representing a Deck of Cards
 *
 * NOTE: I chose to use a LinkedList due to how it best represents a Deck.
 * We only care about efficiently removing values from the top, thus, we don't
 * need to access the values in between the ends of the Deck.
 */
public class Deck extends LinkedList<Card> {

    /**
     * Constructs a shuffled Deck of Cards
     */
    public Deck() {

        int[] values = {3, 4, 5, 6, 7, 8, 9, 10,
                Constants.Values.JACK, Constants.Values.QUEEN, Constants.Values.KING,
                Constants.Values.ACE, Constants.Values.TWO};

        int[] suits = {Constants.Suits.CLUBS, Constants.Suits.DIAMONDS,
                Constants.Suits.HEARTS, Constants.Suits.SPADES};

        for (int val : values) {
            for (int suit : suits) {
                this.add(new Card(val, suit));
            }
        }

        Collections.shuffle(this);
    }

    public Card draw() {
        return this.removeFirst();
    }

    public int cardsLeft() {
        return this.size();
    }

}