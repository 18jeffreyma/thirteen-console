import java.util.List;

/**
 * A class representing the top of the pile in a game of Thirteen
 */
public class Pile {

    private int lastPlayerID;
    private List<Card> lastPlay;
    private int lastPlayType;

    /**
     * Constructs a new empty pile
     */
    public Pile() {
        this.lastPlayerID = -1;
        this.lastPlayType = -1;
        this.lastPlay = null;
    }

    /**
     * Gets the player id of the player who made the play currently on the pile
     * @return player id
     */
    public int getLastPlayerID() {
        return lastPlayerID;
    }

    /**
     * Clear the pile, resetting the play type, and resetting the instance variables
     * @return the player who had his/her play on top of the pile before it was reset
     */
    public int clearPile() {
        int pileWinner = lastPlayerID;

        this.lastPlayerID = -1;
        this.lastPlayType = -1;
        this.lastPlay = null;

        return pileWinner;
    }

    /**
     * Given a list of cards and a player ID, check if the play is valid against the
     * pile, and if so, update the pile
     * @param playerID player ID
     * @param play List of Cards of a play to be made on the pile
     * @return true if successful, false otherwise
     */
    public boolean updatePile(int playerID, List<Card> play) {

        int playType = validAgainstPile(play);
        if (playType >= 0) {

            this.lastPlayerID = playerID;
            this.lastPlayType = playType;
            this.lastPlay = play;

            return true;

        } else {
            return false;
        }

    }

    /* TODO LIST RULES IN SPEC */

    /**
     * Checks if a play is valid at all in the game Thirteen
     * @param play List of Cards representing a play to be validated
     * @return -1 if invalid, or >=0 corresponding to the types of plays in Constants file
     */
    public static int validAnyMove(List<Card> play) {

        if (play == null || play.size() == 0) {
            // invalid if no cards submitted
            return -1;
        }

        switch (play.size()) {
            case 1:
                // any single card value is valid play in general
                return Constants.PilePatterns.SINGLE;

            case 2:
                // any 2 card play must be a double to be valid
                if (play.get(0).getNumber() == play.get(1).getNumber()) {
                    return Constants.PilePatterns.DOUBLES;
                } else {
                    return -1;
                }

            default:
                // for 3 and 4 card plays, they are valid if all cards are the same #
                if (play.size() <= 4) {

                    boolean isValid = true;

                    int temp = play.get(0).getNumber();
                    for (int i = 1; i < play.size(); i++) {
                        isValid = isValid && (temp == play.get(i).getNumber());
                    }

                    if (isValid) {
                        if (play.size() == 3) {
                            return Constants.PilePatterns.TRIPLES;
                        } else if (play.size() == 4) {
                            return Constants.PilePatterns.QUADS;
                        }
                    }
                }


                // otherwise, check if cards are a straight (3+ consecutively increasing cards) they are also valid
                for (int i = 0; i < play.size() - 1; i++) {

                    if (play.get(i).getNumber() != (play.get(i + 1).getNumber() - 1)) {

                        return -1;
                    }
                }
                return Constants.PilePatterns.STRAIGHTS;

        }

    }

    /**
     * Validate a play against both the rules of Thirteen and the given pile
     * @param play List of Cards representing a play to be validated
     * @return < 0 if invalid, >= 0 corresponding to the type of play it is
     */
    public int validAgainstPile(List<Card> play) {
        if (this.lastPlayType == -1) {
            return validAnyMove(play);
        }

        // check if play is a bomb, and if bomb is valid
        if (play.size() == 4) {
            boolean correctQuadSize = play.size() == 4;
            boolean isQuad = play.get(0).getNumber() == play.get(1).getNumber() &&
                    play.get(0).getNumber() == play.get(2).getNumber() &&
                    play.get(0).getNumber() == play.get(3).getNumber();;

            if (correctQuadSize && isQuad && this.lastPlayType != Constants.PilePatterns.QUADS) {
                return Constants.PilePatterns.QUADS;
            } else if (correctQuadSize && isQuad) {
                if (play.get(0).getNumber() > this.lastPlay.get(0).getNumber()) {
                    return Constants.PilePatterns.QUADS;
                }
            }
        }

        // otherwise handle other cases
        switch (this.lastPlayType) {
            case Constants.PilePatterns.SINGLE:
                boolean correctSingleSize = play.size() == 1;
                boolean largerSingle = (play.get(0).getNumber() > this.lastPlay.get(0).getNumber());
                boolean largerSingleSuit = (play.get(0).getNumber() == this.lastPlay.get(0).getNumber() &&
                        play.get(0).getSuit() > this.lastPlay.get(0).getSuit());

                if (correctSingleSize && (largerSingle || largerSingleSuit)) {
                    return Constants.PilePatterns.SINGLE;
                } else {
                    return -1;
                }


            case Constants.PilePatterns.DOUBLES:
                boolean correctDoubleSize = play.size() == 2;
                boolean isDouble = play.get(0).getNumber() == play.get(1).getNumber();
                boolean largerDouble = play.get(0).getNumber() > this.lastPlay.get(0).getNumber();

                if (correctDoubleSize && isDouble && largerDouble) {
                    return Constants.PilePatterns.DOUBLES;
                } else {
                    return -1;
                }

            case Constants.PilePatterns.TRIPLES:
                boolean correctTripleSize = play.size() == 3;
                boolean isTriple = play.get(0).getNumber() == play.get(1).getNumber()
                        && play.get(0).getNumber() == play.get(2).getNumber();
                boolean largerTriple = play.get(0).getNumber() > this.lastPlay.get(0).getNumber();

                if (correctTripleSize && isTriple && largerTriple) {
                    return Constants.PilePatterns.TRIPLES;
                } else {
                    return -1;
                }


            case Constants.PilePatterns.STRAIGHTS:
                boolean sameSize = (play.size() == lastPlay.size());

                boolean isConsecutive = true;
                for (int i = 0; i < play.size() - 1; i++) {
                    if (play.get(i).getNumber() != play.get(i + 1).getNumber() - 1) {
                        isConsecutive = false;
                        break;
                    }
                }

                boolean largerStraight = play.get(0).getNumber() > this.lastPlay.get(0).getNumber();

                if (sameSize && isConsecutive && largerStraight) {
                    return Constants.PilePatterns.STRAIGHTS;
                } else {
                    return -1;
                }

            default:
                System.out.println("invalid pile type, exiting....");
                System.exit(-1);
                return -1;
        }

    }

    /**
     * toString method to print the pile in a readable format
     * @return String describing the contents of the Pile
     */
    @Override
    public String toString() {
        // toStringBuilder is more efficient than adding strings together

        String[] mode = {"Single", "Pair", "Triples", "Bombs", "Straights"};
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append(Constants.Formatting.LINE_BREAK);
        toStringBuilder.append("Top of Pile: ");

        if (this.lastPlayType < 0) {
            toStringBuilder.append("No Play Type Yet");
        } else {
            toStringBuilder.append(mode[this.lastPlayType]);

        }

        toStringBuilder.append("\n");
        toStringBuilder.append(Constants.Formatting.LINE_BREAK);

        if (lastPlay != null) {
            for (Card c : lastPlay) {
                toStringBuilder.append(c.toString());
                toStringBuilder.append("\n");
            }
        } else {
            toStringBuilder.append("No Cards in Play");
        }

        return toStringBuilder.toString();
    }
}