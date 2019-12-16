package fr.epsi.jconte.Util;


import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Scanner;

/**
 * Manage the incoming and outgoing connections. We use two helper classes, both of which extend
 * {@link Thread}. They are two threads that run in the background to read and send messages, respectively.
 */

public class ConnectionManager {
    private MessageSender messageSender;
    private MessageReceiver messageReceiver;
    private ServerSocket inboundConnectionListener; //The socket to connect to
    private Socket mSocket; //The socket for transmitting the strings
    private Sender sender;
    public static final Logger LOGGER = Logger.getLogger(ConnectionManager.class);

    /**
     * The default port for communication
     */
    public static final int DEFAULT_PORT = 9990;

    /**
     * The maximum number of failed tries after we give up trying to connect
     */
    public static final int MAX_NUM_TRIES = 10;

    /**
     * Get the current user's IP address.
     * @return The IPv4 address of the user
     */
    public static String getMyInetAddress() throws SocketException {
        StringBuilder stringBuilder = new StringBuilder();
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface iface = interfaces.nextElement();
            // filters out 127.0.0.1 and inactive interfaces
            if (iface.isLoopback() || !iface.isUp())
                continue;

            Enumeration<InetAddress> addresses = iface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                stringBuilder.append(iface.getDisplayName());
                stringBuilder.append(": ");
                stringBuilder.append(addr.getHostAddress());
                stringBuilder.append("\n");
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Create a ConnectionManager with the details of the given Sender. The port is set to the default port.
     * @param s The sender, which has the IP address and other details about the peer
     * @throws IOException In case the connection fails
     * @throws InterruptedException We sleep for 1000ms after every failed connection try. This is in case the thread is interrupted
     */

    public ConnectionManager(Sender s) throws IOException, InterruptedException {
        this.sender = s;

        //In case the connection fails, we retry MAX_NUM_TRIES times. If we still don't connect, throw
        //an IOException
        int numTries = ConnectionManager.MAX_NUM_TRIES;
        while(numTries > 0) {
            try {
                mSocket = new Socket(this.sender.getInetAddr(), DEFAULT_PORT);
            } catch (ConnectException ce) {
                LOGGER.info("Connection Failed. " + numTries + " tries left...");
                mSocket = null;
                Thread.sleep(1000);
            }

            if (mSocket == null) {
                numTries--;
            } else {
                break;
            }
        }

        //If mSocket is null, it means we failed to connect. So throw an IOException
        if(mSocket == null) {
            throw new IOException("Connection failed!");
        }

        messageSender = new MessageSender(mSocket);
        messageReceiver = new MessageReceiver(mSocket);
        inboundConnectionListener = null;
    }

    /**
     * Create an empty connection manager that we can configure later
     */
    public ConnectionManager() {
        this.sender = null;
        this.messageSender = null;
        this.messageReceiver = null;
        this.inboundConnectionListener = null;
    }


    /**
     * Get the {@link MessageSender} associated with this connection manager.
     * @return The {@link MessageSender} associated with this connection manager.
     */
    public MessageSender getMessageSender() {
        return messageSender;
    }

    /**
     * Get the {@link MessageReceiver} associated with this connection manager.
     * @return The {@link MessageReceiver} associated with this connection manager.
     */
    public MessageReceiver getMessageReceiver() {
        return messageReceiver;
    }

    /**
     * Connect to the current {@link Sender}
     */
    public void connect() {
        Sender.setCurrentSender(sender);

        messageSender.start();
        messageReceiver.start();

    }

    /**
     * Wait for someone to connect to you.
     * @throws InterruptedException We sleep when waiting for someone to connect. In case the thread gets interrupted.
     */

    public void waitForConnection() throws InterruptedException{
        try {
            inboundConnectionListener = new ServerSocket(9990);
            Socket s = inboundConnectionListener.accept();

            Scanner scanner = new Scanner(System.in);
            LOGGER.info("Enter the person's name: ");
            String senderName = scanner.nextLine();

            InetAddress senderAddr = s.getInetAddress();
            this.sender = new Sender(senderName, senderAddr);
            Sender.setCurrentSender(sender);

            mSocket = s;

            messageReceiver = new MessageReceiver(mSocket);
            messageSender = new MessageSender(mSocket);

            connect();
        }
        catch (IOException ioe) {
            LOGGER.info("\nIO Error during waiting!");
        }
    }

    /**
     * Disconnect
     */
    public void disconnect() {
        try {
            messageSender.setDone(true);
            messageReceiver.setDone(true);

            mSocket.close();

            Sender.setCurrentSender(null);
        }
        catch (IOException ioe) {
            LOGGER.info("\nUnable to close connection to " + sender.getName());
        }
    }
}
