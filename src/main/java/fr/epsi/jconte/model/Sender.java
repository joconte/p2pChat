package fr.epsi.jconte.model;

import org.apache.log4j.Logger;

import java.net.InetAddress;

/**
 * This class describes the person we are communicating with. It contains the person's name and his IP address
 */
public class Sender {
    private String name;
    private String ip;
    private InetAddress inetAddr;
    public static final Logger LOGGER = Logger.getLogger(Sender.class);

    /**
     * Represents the current sender.
     */
    private static Sender currentSender = null;

    /**
     * Create a new Sender.
     * @param name The name of the Sender
     * @param ip The IP address
     */
    public Sender(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
