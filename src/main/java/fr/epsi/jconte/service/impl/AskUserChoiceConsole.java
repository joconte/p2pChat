package fr.epsi.jconte.service.impl;

import fr.epsi.jconte.model.Choice;
import fr.epsi.jconte.service.IAskUserChoice;
import org.apache.log4j.Logger;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AskUserChoiceConsole implements IAskUserChoice {

    private static final Logger LOGGER = Logger.getLogger(AskUserChoiceConsole.class);

    /**
     * Text to prompt the user for input
     */
    public static final String TERMINAL_PROMPT = "Welcome!\n" +
            "1. Connect to a person\n" +
            "2. Wait for someone to connect\n" +
            "3. Exit\n" +
            "Select what to do: ";

    @Override
    public Choice askForUserChoice() {
        LOGGER.info(TERMINAL_PROMPT);
        Scanner s = new Scanner(System.in);
        int n;
        try {
            n = s.nextInt();
        } catch (InputMismatchException e) {
            return null;
        }

        Choice returnChoice;
        switch (n) {
            case 1:
                returnChoice = Choice.CONNECT;
                break;

            case 2:
                returnChoice = Choice.WAIT_FOR_CONNECTION;
                break;

            case 3:
                returnChoice = Choice.EXIT;
                break;

            default:
                returnChoice = null;
                break;
        }
        return returnChoice;
    }
}
