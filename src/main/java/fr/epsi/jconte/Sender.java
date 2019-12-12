package fr.epsi.jconte;

import org.apache.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * This class describes the person we are communicating with. It contains the person's name and his IP address
 */
public class Sender {
    private String name;
    private InetAddress inetAddr;
    public final static Logger LOGGER = Logger.getLogger(Sender.class);

    /**
     * Represents the current sender.
     */
    private static Sender currentSender = null;

    /**
     * Create a new Sender.
     * @param name The name of the Sender
     * @param inetAddr The IP address
     */
    public Sender(String name, InetAddress inetAddr) {
        this.name = name;
        this.inetAddr = inetAddr;
    }

    public String getName() {
        return name;
    }

    public InetAddress getInetAddr() {
        return inetAddr;
    }

    public void setInetAddr(InetAddress inetAddr) {
        this.inetAddr = inetAddr;
    }

    /**
     * Create a new Sender by taking input from the standard input.
     * @return The Sender we created.
     */
    public static Sender createSenderFromStdin() {
        String senderName;
        String senderIp;
        InetAddress senderAddr;

        Scanner s = new Scanner(System.in);

        LOGGER.info("Enter the name of the sender: ");
        senderName = s.nextLine();
        LOGGER.info("Enter the IP address of the sender: ");
        senderIp = s.next();

        try {
            senderAddr = InetAddress.getByName(senderIp);
        }
        catch (UnknownHostException uhe) {
            LOGGER.info("Error! Invalid IP address!");
            return null;
        }
        return new Sender(senderName, senderAddr);
    }

    public static Sender getCurrentSender() {
        return currentSender;
    }

    public static void setCurrentSender(Sender s) {
        currentSender = s;
    }

    public static boolean currentSenderExists() {
        return currentSender != null;
    }
}
