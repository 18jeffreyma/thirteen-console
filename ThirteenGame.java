import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

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

    public void dealCards() {

        int playersFull = 0;

        // deal cards
        while (!this.gameDeck.isEmpty() && playersFull < players.size()) {

            for (Hand player : players) {
                if (player.size() < 13) {
                    player.addToHand(this.gameDeck.draw());
                } else {
                    playersFull++;
                }
            }
        }
    }

    public int startingPlayer() {
        // player with the 3 of spades starts, or the player with the smallest card starts
        int startingPlayer = 0;
        Card minCard = null;

        int counter = 0;
        for (Hand player : players) {

            if (minCard == null) {
                startingPlayer = counter;
                minCard = player.get(0);
            } else if (player.get(0).getNumber() < minCard.getNumber()) {
                startingPlayer = counter;
                minCard = player.get(0);
            } else if (player.get(0).getNumber() == minCard.getNumber() && player.get(0).getSuit() < minCard.getSuit()) {
                startingPlayer = counter;
                minCard = player.get(0);
            }

            counter++;
        }

        return startingPlayer;
    }

    /**
     * Start a game of Thirteen in the console
     */
    public void startGame() {

        this.dealCards();

        this.currentTurn = this.startingPlayer();

        Util.lineBreak();
        System.out.println("Welcome to Thirteen by Jeffrey Ma!");
        System.out.println("For best results, please resize your\nconsole window height to 50-60 lines.");
        Util.lineBreak();
        Util.promptEnterKey();
        Util.newPage();

        // enter game loop
        while (true) {

            // anti-cheat
            Util.lineBreak();
            System.out.println("Please pass the computer to " + playerNames.get(this.currentTurn) + ".");
            Util.promptEnterKey();
            Util.lineBreak();

            // check if everyone passed on the last play, if so current player has won the pile
            if (this.pile.getLastPlayerID() == this.currentTurn) {
                System.out.println("All players passed on your last\n" +
                        "play so you win the pile! You \n" +
                        "start the play with whatever\n" +
                        "you want!");
                this.pile.clearPile();
                Util.lineBreak();
            }

            // print game information
            System.out.println(this.playerNames.get(this.currentTurn) + ", it's your move!");
            Util.lineBreak();
            System.out.println();
            System.out.println(this.pile);
            System.out.println();
            System.out.println(this.players.get(this.currentTurn));

            // enter player loop
            while (true) {
                Util.lineBreak();
                System.out.println("Type in the indices of the cards\nyou wish to play, " +
                        "separated by\nspaces, or type in 0 to pass:");
                Util.lineBreak();

                // get console input
                Scanner input = new Scanner(System.in);
                String rawInput = input.nextLine().trim();
                Util.lineBreak();

                String[] splitRawInput = rawInput.split("\\s+");

                // verify input and remove duplicates
                List<Integer> indices = new ArrayList<Integer>();
                Set<Integer> hashSet = new HashSet<Integer>();
                try {
                    for (int i = 0 ; i < splitRawInput.length ; i++) {
                        // we subtract 1 because our displayed indices start at 1
                        int index = Integer.parseInt(splitRawInput[i]) - 1;
                        if (hashSet.add(index)) {
                            indices.add(index);
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("ERROR: Input does not consist of\nnumbers, " +
                            "please try again!");
                    continue;
                }

                // if you make a decision to pass, break from player loop
                if (indices.contains(-1)) {
                    System.out.println("You passed! Your turn is over!");
                    break;
                }

                // try getting play cards from player's hand
                List<Card> playCards;
                try {
                    playCards = this.players.get(this.currentTurn).playHand(indices);

                } catch (IllegalArgumentException e) {
                    System.out.println("ERROR: Input indices are out of\nrange, please " +
                            "try again!");
                    continue;
                }

                // check if game move is successful, if so, break from player loop
                if (this.pile.updatePile(this.currentTurn, playCards)) {
                    this.players.get(this.currentTurn).removeAll(playCards);
                    System.out.println("Valid play! Your turn is over!");
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
