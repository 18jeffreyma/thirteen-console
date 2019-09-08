import java.util.Comparator;


/**
 * A class which represent singular cards in a deck
 */
public class Card {

    /**
     * Note that integers are used for all variables
     * due to the nature of Thirteen, simplifies
     * comparison later on
     */
    private int number; // 2 through 10 + 11 (Jack), 12 (Queen), 13 (King), 14 (Ace)
    private int color; // RED or BLACK
    private int suit; // CLUBS, DIAMONDS, HEARTS, or SPADES

    /**
     * Constructs a Card object
     * @param number the value of the card (2 - 10, Jack, Queen, King, Ace)
     * @param suit the suit of the card
     */
    public Card(int number, int suit) {

        this.number = number;
        this.suit = suit;

        if (suit == Constants.Suits.CLUBS || suit == Constants.Suits.SPADES) {
            this.color = Constants.Colors.BLACK;
        } else {
            this.color = Constants.Colors.RED;
        }

    }

    /**
     * Accessor Functions
     */
    public int getNumber() {
        return this.number;
    }

    public int getSuit() {
        return this.suit;
    }

    public int getColor() {
        return this.color;
    }


    /**
     * toString method to print out the Card in a readable format
     *
     * NOTE: I used StringBuilder because it's more efficient than
     * copying and adding Strings, as Strings are immutable, leading
     * to many unnecessary allocations.
     *
     * @return a String describing the Card
     */
    @Override
    public String toString() {

        // toStringBuilder is more efficient than adding strings together
        StringBuilder toStringBuilder = new StringBuilder();

        toStringBuilder.append(this.getNumberString());
        toStringBuilder.append(" of ");
        toStringBuilder.append(this.getSuitString());

        if (this.color == Constants.Colors.RED) {
            toStringBuilder.append(" (RED)");
        } else {
            toStringBuilder.append(" (BLACK)");
        }

        return toStringBuilder.toString();

    }

    public String getNumberString() {
        String[] upperCardNames = {"Jack", "Queen", "King", "Ace", "2"};

        if (this.number > 10) {
            return upperCardNames[this.number - 11];
        } else {
            return Integer.toString(this.number);
        }
    }

    public String getSuitString() {
        switch (this.suit) {

            case (Constants.Suits.CLUBS):
                return "Clubs";

            case (Constants.Suits.HEARTS):
                return "Hearts";

            case (Constants.Suits.DIAMONDS):
                return "Diamonds";

            case (Constants.Suits.SPADES):
                return "Spades";

            default:
                return "Invalid Suit";

        }
    }

    /**
     * A implementation of the Comparator interface for sorting
     * Cards with the Collections.sort() utility
     */
    public static class CardSort implements Comparator<Card> {

        @Override
        public int compare(Card c1, Card c2) {
            if (c1.getNumber() != c2.getNumber()) {
                return (c1.getNumber() - c2.getNumber());
            } else {
                return (c1.getSuit() - c2.getSuit());
            }
        }
    }
}