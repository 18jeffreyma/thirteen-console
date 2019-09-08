import java.io.IOException;
import java.util.*;

/**
 * Class representing an instance of the Thirteen card game
 */
public class ThirteenGame {

    private Deck gameDeck;

    private List<Hand> players;
    private List<String> playerNames;
    private int currentTurn;

    private Pile pile;

    /**
     * Constructor to construct a new Thirteen Game
     */
    public ThirteenGame() {
        this.players = new ArrayList<Hand>();
        this.playerNames = new ArrayList<String>();
        this.gameDeck = new Deck();

        this.pile = new Pile();

    }

    /**
     * Add a new player and player name to the game
     * @param name name of player to be added
     * @return new player's id
     */
    public void addPlayer(String name) {

        players.add(new Hand());
        playerNames.add(name);

    }

    /**
     * Start a game of Thirteen in the console
     */
    public void startGame() {

        int playersFull = 0;

        // deal cards
        while (!this.gameDeck.isEmpty()) {

            if (playersFull >= players.size()) {
                break;
            }

            for (Hand player : players) {
                if (player.size() < 13) {
                    player.addToHand(this.gameDeck.draw());
                } else {
                    playersFull++;
                }
            }
        }

        // select first player randomly
        Random random = new Random();
        this.currentTurn = random.nextInt(players.size());

        Util.lineBreak();
        System.out.println("Welcome to Thirteen by Jeffrey Ma!");
        System.out.println("For best results, please resize your\nconsole window to 50-60 lines.");
        Util.lineBreak();
        Util.promptEnterKey();

        while (true) {

            Util.lineBreak();
            System.out.println("Please pass the computer to " + playerNames.get(this.currentTurn) + ".");
            Util.promptEnterKey();

            Util.lineBreak();
            if (this.pile.getLastPlayerID() == this.currentTurn) {
                // this means everyone passed on the last play
                System.out.println("All players passed on your last play,\n" +
                        "so you win the pile! You start the\n" +
                        "play with whatever you want!");

                this.pile.clearPile();
                Util.lineBreak();
            }

            System.out.println(this.playerNames.get(this.currentTurn) + ", it's your move!");
            Util.lineBreak();

            System.out.println();
            System.out.println(this.pile);
            System.out.println();
            System.out.println(this.players.get(this.currentTurn));


            while (true) {
                Util.lineBreak();
                System.out.println("Type in the indices of the cards\nyou wish to play, separated by\nspaces, or type in 0 to pass:");
                Util.lineBreak();


                Scanner input = new Scanner(System.in);
                String rawInput = input.nextLine().trim();
                Util.lineBreak();

                String[] splitRawInput = rawInput.split("\\s+");

                int[] indices = new int[splitRawInput.length];

                try {
                    for (int i = 0 ; i < splitRawInput.length ; i++) {
                        // we subtract 1 because our displayed indices start at 1
                        indices[i] = Integer.parseInt(splitRawInput[i]) - 1;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("ERROR: Input does not consist of\nnumbers, please try again!");
                    continue;
                }

                // if you make a decision to pass, break from player loop
                if (indices[0] == -1) {
                    break;
                }

                // try getting play cards from player's hand
                List<Card> playCards;
                try {
                    playCards = this.players.get(this.currentTurn).playHand(indices);

                } catch (IllegalArgumentException e) {
                    System.out.println("ERROR: Input indices are out of\nrange, please try again!");
                    continue;
                }


                // check if game move is successful, if so, break from player loop
                if (this.pile.updatePile(this.currentTurn, playCards)) {
                    this.players.get(this.currentTurn).removeAll(playCards);
                    break;
                } else {
                    System.out.println("ERROR: Not a valid game move, \nplease try again!");
                    continue;
                }

            }

            // if you run out of cards first, you win, so break out of game loop
            if (this.players.get(this.currentTurn).size() == 0) {
                System.out.println("CONGRATS!!! " + this.playerNames.get(this.currentTurn) + ", you win!");
                System.out.println("THANKS FOR PLAYING!");
                Util.lineBreak();
                break;
            }

            // next players turn
            this.currentTurn = (this.currentTurn + 1) % players.size();

            // hide previous output
            System.out.println("SUCCESS: Valid move! Your turn is over!");
            Util.promptEnterKey();
            Util.newPage();
        }

    }

    /**
     * Main method to run the argument from command line
     * @param args player names
     */
    public static void main(String[] args) {
        ThirteenGame my_game = new ThirteenGame();

        if (args.length == 0) {
            System.out.println("No players inputted, exiting....");
            System.exit(-1);
        }

        Util.newPage();
        for (String s : args) {
            my_game.addPlayer(s);
        }

        my_game.startGame();
    }

}
