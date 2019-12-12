package fr.epsi.jconte.service.impl;

import fr.epsi.jconte.model.Sender;
import fr.epsi.jconte.service.ICreateSender;
import org.apache.log4j.Logger;

import java.util.Scanner;

public class CreateSenderFromConsole implements ICreateSender {

    public static final Logger LOGGER = Logger.getLogger(CreateSenderFromConsole.class);
    @Override
    public Sender createSender() {

        Scanner s = new Scanner(System.in);
        LOGGER.info("Enter the name of the sender: ");
        String senderName = s.nextLine();
        LOGGER.info("Enter the IP address of the sender: ");
        String senderIp = s.next();
        return new Sender(senderName, senderIp);
    }
}
