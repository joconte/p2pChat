package fr.epsi.jconte;

import fr.epsi.jconte.model.Choice;
import fr.epsi.jconte.model.Sender;
import fr.epsi.jconte.service.*;
import fr.epsi.jconte.service.impl.AskUserChoiceConsole;
import fr.epsi.jconte.service.impl.ConnectionManager;
import fr.epsi.jconte.service.impl.CreateSenderFromConsole;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Scanner;


/**
 * This class has the main() method, and handles the start of the program
 */

public class Driver {

    public static final Logger LOGGER = Logger.getLogger(Driver.class);

    /**
     * Entry point of the application
     * @param args The command line arguments passed. We don't use them here though
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        // Configure logger
        BasicConfigurator.configure();

        // Init param
        ICreateSender createSender = new CreateSenderFromConsole();
        IValidateSenderIp validateSenderIp = new ValidateSenderIp();
        ConnectionManager connectionManager = null;

        // Get user choice
        IAskUserChoice askUserChoice = new AskUserChoiceConsole();
        Choice userChoice = askUserChoice.askForUserChoice();

        // Repeat until good choice
        while (userChoice == null) {
            LOGGER.info("Bad choice, try again please.");
            userChoice = askUserChoice.askForUserChoice();
        }

        switch (userChoice) {
            case CONNECT:
                // Get sender name and ip from console
                Sender sender = createSender.createSender();

                // Validate ip
                while (sender.getInetAddr() == null) {
                    validateSenderIp.validate(sender);
                }

                // Connection
                connectionManager = new ConnectionManager(sender);
                connectionManager.validateSocket();
                connectionManager.connect();

                LOGGER.info("Connected! Start sending messages now!");
                break;

            case WAIT_FOR_CONNECTION:

                connectionManager = new ConnectionManager();
                LOGGER.info("Your IP address is:\n" + ConnectionManager.getMyInetAddress());
                LOGGER.info("Waiting for connection...");
                connectionManager.waitForConnection();
                LOGGER.info("Connected! Start conversation now!");
                break;

            case EXIT:
                break;

            default:
                LOGGER.info("Error! Incorrect input, try again");
                break;
        }

        if(connectionManager != null) {
            connectionManager.getMessageReceiver().join();
            connectionManager.getMessageSender().join();
        }
        LOGGER.info("Connection Terminated!");

    }

    /**
     * Draws the terminal prompt, and loops through to provide the menu to the user
     */
    public static void prompt() throws IOException, InterruptedException {
        ConnectionManager connectionManager = null;
        Scanner s = new Scanner(System.in);
        boolean run = true;
/*
        while(run) {
            LOGGER.info(TERMINAL_PROMPT);
            int n = s.nextInt();
            try {
                switch (n) {
                    case 1:
                        Sender sender = Sender.createSenderFromStdin();
                        connectionManager = new ConnectionManager(sender);
                        connectionManager.connect();
                        LOGGER.info("Connected! Start sending messages now!");
                        break;

                    case 2:
                        connectionManager = new ConnectionManager();
                        LOGGER.info("Your IP address is:\n" + ConnectionManager.getMyInetAddress());
                        LOGGER.info("Waiting for connection...");
                        connectionManager.waitForConnection();
                        LOGGER.info("Connected! Start conversation now!");
                        break;

                    case 3:
                        run = false;
                        break;

                    default:
                        LOGGER.info("Error! Incorrect input, try again");
                        break;
                }

                if(connectionManager != null) {
                    connectionManager.getMessageReceiver().join();
                    connectionManager.getMessageSender().join();
                }
                LOGGER.info("Connection Terminated!");
            } catch (IOException | InterruptedException e) { //If any of these exception is raised, disconnect and redraw prompt
                LOGGER.info("Connection error! Disconnecting...");
                if(connectionManager != null) {
                    connectionManager.disconnect();
                }
                throw e;
            }
        }*/
    }
}
