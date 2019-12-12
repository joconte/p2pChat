package fr.epsi.jconte.service;

import fr.epsi.jconte.model.Sender;
import org.apache.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ValidateSenderIp implements IValidateSenderIp {

    public static final Logger LOGGER = Logger.getLogger(ValidateSenderIp.class);

    @Override
    public Sender validate(Sender sender) {

        try {
            sender.setInetAddr(InetAddress.getByName(sender.getIp()));
        }
        catch (UnknownHostException uhe) {
            LOGGER.info("Error! Invalid IP address!");
        }
        return sender;
    }
}
