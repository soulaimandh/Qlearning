package com.enset.sequentiel;

import java.util.Random;
import static com.enset.QLUtils.*;

public class QLearning {
    private double[][] qTable = new double[GRID_SIZE * GRID_SIZE][ACTIONS_SIZE];

    private int stateI;
    private int stateJ;

    private void resetState() {
        stateI = 0;
        stateJ = 0;
    }

    private boolean finished() {
        return GRID[stateI][stateJ] == 1;
    }

    private int chooseAction(double epsilon) {
        Random random = new Random();
        int bestAction = 0;
        double bestQValue = Double.MIN_VALUE;
        if (random.nextDouble() < epsilon) {
            // Exploration:
            bestAction = random.nextInt(ACTIONS_SIZE);
            int[] chosenAction = ACTIONS[bestAction];
        } else {
            // Exploitation:
            int state = stateI * GRID_SIZE + stateJ;
            for (int i = 0; i < ACTIONS_SIZE; i++) {
                if (qTable[state][i] > bestQValue) {
                    bestQValue = qTable[state][i];
                    bestAction = i;
                }
            }

        }
        return bestAction;
    }

    private int executeAction(int action) {
        stateI = Math.max(0, Math.min(stateI + ACTIONS[action][0], GRID_SIZE - 1));
        stateJ = Math.max(0, Math.min(stateJ + ACTIONS[action][1], GRID_SIZE - 1));
        return stateI * GRID_SIZE + stateJ;
    }

    private void printQTable() {
        System.out.println("***********qTable***********");
        for (double[] row : qTable) {
            System.out.print("|");
            for (double value : row) {
                System.out.print(value + "|\t");
            }
            System.out.println("|");
        }
        resetState();
        while (!finished()) {
            int currentState = stateI * GRID_SIZE + stateJ;
            int action = chooseAction(0); // No exploration
            System.out.println("State: (" + currentState + ") | Best action: " + action);

            int newState = executeAction(action);
            System.out.println("New state: (" + newState + ")");

        }
    }

    public void runQLearning() {
        int iteration = 0;
        int currentState;
        int newState;
        resetState();
        while (iteration < MAX_EPOCHS) {
            resetState();
            while (!finished()) {
                currentState = stateI * GRID_SIZE + stateJ;
                int action = chooseAction(EPSILON);

                newState = executeAction(action);
                int action2 = chooseAction(0);

                qTable[currentState][action] = qTable[currentState][action]
                        + ALPHA * (GRID[stateI][stateJ]
                                + GAMMA * qTable[newState][action2]
                                - qTable[currentState][action]);
            }
            iteration++;
        }
        printQTable();
    }

}
