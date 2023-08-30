package edu.wctc.dice;

import edu.wctc.dice.iface.Dice;
import edu.wctc.dice.iface.GameInput;
import edu.wctc.dice.iface.GameOutput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class DiceGame {
    private GameInput in;
    private GameOutput out;
    private Dice dice;

    private List<Player> players = new ArrayList<>();
    private int currentRound = 1;


    @Autowired
    public DiceGame(GameInput in, GameOutput out, Dice dice) {
        this.in = in;
        this.out = out;
        this.dice = dice;
        System.out.println("DiceGame created");
    }

    private void beginRound() {
//        logger.info("Begin Round " + currentRound);

        Iterator<Player> playerIterator = players.iterator();
        while (playerIterator.hasNext()) {
            Player currentPlayer = playerIterator.next();
            String choice = getPlayerChoice(currentPlayer.getName());
            if (choice.equals("2")) {
                playerIterator.remove();
//                logger.info("Player cashed out: " + currentPlayer.toString());
            } else {
                int bet = getBet(currentPlayer.getCash());
                currentPlayer.setBet(bet);
            }
        }
    }

    private void endRound(boolean evens) {
        Iterator<Player> playerIterator = players.iterator();

        while (playerIterator.hasNext()) {
            Player currentPlayer = playerIterator.next();
            if (evens) {
                currentPlayer.wins();
            } else {
                currentPlayer.loses();
                if (currentPlayer.isBankrupt()) {
                    playerIterator.remove();
//                    logger.info("Player bankrupt: " + currentPlayer.toString());
                }
            }
        }

        playerReport();

//        logger.info("End Round " + currentRound++);
    }

    private int getBet(int cash) {
        String bet = in.getInput("Bet how much? ($" + cash + " available): ");
        return Integer.parseInt(bet);
    }

    private String getPlayerChoice(String playerName) {
        String menu = playerName.toUpperCase() + "'s turn:\n"
                + "  1. Place Bet\n"
                + "  2. Cash Out (Quit)";
        return in.getInput(menu);
    }

    private void initPlayers() {
        String playerName = in.getInput("Enter player name: ");
        while (players.isEmpty() || !playerName.equalsIgnoreCase("Q")) {
            players.add(new Player(playerName));
//            logger.info("Player " + playerName + " joined game");
            playerName = in.getInput("Enter player name (Q to quit): ");
        }
    }

    public void play() {
//        logger.info("Game start");

        initPlayers();

        while (!players.isEmpty()) {
            beginRound();
            boolean evens = dice.rollDice(out);
            endRound(evens);
            currentRound++;
        }

        out.output("GAME OVER");
//        logger.info("Game end");
    }

    private void playerReport() {
        String report = "Player Report (Round " + currentRound + ")\n";
        if (players.isEmpty()) {
            report += "  No players remain!\n";
        } else {
            for (Player player : players) {
                report += "  " + player.toString() + "\n";
            }
        }
        out.output(report);
    }
}
