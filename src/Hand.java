import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class representing a player's hand in Thirteen
 *
 * NOTE: I used an ArrayList as the backing structure for this, due
 * to how we needed to efficiently access values within the hand.
 */
public class Hand extends ArrayList<Card> {

    public Hand() {
        super();
    }

    /**
     * Adds a card to the correct sorted position in your hand, just
     * like we do naturally when playing Thirteen
     * @param c Card to be added
     * @return Card added
     */
    public Card addToHand(Card c) {

        int i = 0;

        for (i = 0; i < this.size(); i++) {
            if (c.getNumber() < this.get(i).getNumber()) {
                break;

            } else if (c.getNumber() == this.get(i).getNumber()) {
                if (c.getSuit() < this.get(i).getSuit());
                break;

            }
        }
        super.add(i, c);

        return c;
    }

    /**
     * Given a set of indices, return the cards corresponding to those indices
     * @param indices indicies of cards in the hand
     * @return a List of Cards
     * @throws IllegalArgumentException if the indices are not within the bounds of the ArrayList
     */
    public List<Card> playHand(int[] indices) throws IllegalArgumentException {
        List<Card> play = new ArrayList<Card>();

        for (int i : indices) {
            try {
                play.add(this.get(i));
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("invalid indicies for play");
            }
        }

        Collections.sort(play, new Card.CardSort());

        return play;
    }

    /**
     * toString method to print our hand in a readable format
     *
     * NOTE: I used StringBuilder because it's more efficient than
     * copying and adding Strings, as Strings are immutable, leading
     * to many unnecessary allocations.
     *
     * @return a String describing the cards in the Hand
     */
    @Override
    public String toString() {
        // toStringBuilder is more efficient than adding strings together
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append(Constants.Formatting.LINE_BREAK);
        toStringBuilder.append("Your Hand:\n");
        toStringBuilder.append(Constants.Formatting.LINE_BREAK);

        for (int i = 0; i < this.size(); i++) {
            toStringBuilder.append(i+1);
            toStringBuilder.append(": ");
            toStringBuilder.append(this.get(i).toString());
            toStringBuilder.append("\n");
        }

        return toStringBuilder.toString();
    }
}