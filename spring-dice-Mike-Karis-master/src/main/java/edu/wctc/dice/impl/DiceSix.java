package edu.wctc.dice.impl;

import edu.wctc.dice.iface.Dice;
import edu.wctc.dice.iface.GameOutput;

import java.util.Random;

public class DiceSix implements Dice {
    public boolean rollDice(GameOutput out) {
        int die1 = rollDie();
        int die2 = rollDie();

        // Players win on even totals
        boolean even = (die1 + die2) % 2 == 0;

        String outcome = "Roll was " + die1 + ", " + die2;

        out.output(outcome + (even ? "\nPlayers WIN!" : "\nPlayers LOSE!"));

//        logger.info(outcome);

        return even;
    }

    public int rollDie() {
        Random random = new Random();
        return random.nextInt(6) + 1;
//        return dieRoller.rollDie();
    }
}
