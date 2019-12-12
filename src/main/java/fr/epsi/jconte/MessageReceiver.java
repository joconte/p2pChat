package fr.epsi.jconte;


import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This is the thread that works in the background and helps in receiving messages by reading input from the socket
 * and then printing the message to the terminal.
 */

public class MessageReceiver extends Thread {
    private Socket inSocket;
    private int inPort;
    private boolean done;
    private List<Message> messageList;
    public static final Logger LOGGER = Logger.getLogger(MessageReceiver.class);

    /**
     * The default port for incoming communication
     */
    public static final int DEFAULTINPORT = 9990;

    /**
     * Create a MessageReceiver with the default port.
     * @param inSocket The socket over which we will communicate.
     * @throws IOException
     * @throws InterruptedException
     */
    public MessageReceiver(Socket inSocket){
        this(inSocket, DEFAULTINPORT);
    }

    /**
     * Create a MessageReceiver with the specified port
     * @param inSocket The socket over which we communicate
     * @param inPort The port over which we look for input
     * @throws IOException
     * @throws InterruptedException
     */
    public MessageReceiver(Socket inSocket, int inPort) {
        super();
        this.inPort = inPort;
        this.done = false;
        this.messageList = new LinkedList<>();
        this.inSocket = inSocket;
    }

    public Socket getInSocket() {
        return inSocket;
    }

    public int getInPort() {
        return inPort;
    }

    /**
     * If we want to stop the thread, set done to true. The thread will only stop when it wakes up from
     * readLine().
     * @param done Set whether the thread is done its job or not.
     */
    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean hasNextMessage() {
        return !messageList.isEmpty();
    }

    public Message getNextMessage() {
        return messageList.remove(0);
    }

    public void addMessage(Message m) {
        messageList.add(m);
    }

    /**
     * Run in the background and keep reading input from the Socket, and printing it on the screen
     */
    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(inSocket.getInputStream());
            BufferedReader br = new BufferedReader(isr);

            String senderName = Sender.getCurrentSender().getName();

            while(true) {
                //If someone (usually the connection manager) sets done, stop execution
                if(done) {
                    break;
                }

                if(inSocket.isClosed()) {
                    break;
                }

                //This is for the terminal output, to signify we are typing the message
                LOGGER.info("You: ");

                String msg;
                msg = br.readLine();

                if(msg == null) {
                    inSocket.close();
                    break;
                }
                addMessage(new Message(msg, Sender.getCurrentSender()));
                Message message = getNextMessage();

                //Prints a bunch of backspaces before printing the message. This is to remove the
                //"You:" that is already there. It will get reprinted when the loop goes through another iteration
                LOGGER.info("\b\b\b\b\b\b");

                LOGGER.info(senderName + ": " + message.getMessage());
            }

            //LOGGER.info();

        }
        catch (IOException ioe) {
            LOGGER.info("Connection Error!");
        }
    }
}
