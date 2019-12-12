package fr.epsi.jconte;

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
     * Text to prompt the user for input
     */
    public static final String TERMINAL_PROMPT = "Welcome!\n" +
            "1. Connect to a person\n" +
            "2. Wait for someone to connect\n" +
            "3. Exit\n" +
            "Select what to do: ";

    /**
     * Entry point of the application
     * @param args The command line arguments passed. We don't use them here though
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        BasicConfigurator.configure();
        prompt();
    }

    /**
     * Draws the terminal prompt, and loops through to provide the menu to the user
     */

    public static void prompt() throws IOException, InterruptedException {
        ConnectionManager connectionManager = null;
        Scanner s = new Scanner(System.in);
        boolean run = true;

        while(run) {
            LOGGER.info(TERMINAL_PROMPT);
            int n = s.nextInt();
            try {
                switch (n) {
                    case 1: {
                        Sender sender = Sender.createSenderFromStdin();
                        connectionManager = new ConnectionManager(sender);
                        connectionManager.connect();
                        LOGGER.info("Connected! Start sending messages now!");
                        break;
                    }

                    case 2: {
                        connectionManager = new ConnectionManager();
                        LOGGER.info("Your IP address is:\n" + ConnectionManager.getMyInetAddress());
                        LOGGER.info("Waiting for connection...");
                        connectionManager.waitForConnection();
                        LOGGER.info("Connected! Start conversation now!");
                        break;
                    }

                    case 3: {
                        run = false;
                        break;
                    }

                    default: {
                        LOGGER.info("Error! Incorrect input, try again");
                        run = false;
                        break;
                    }
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
        }
    }
}
