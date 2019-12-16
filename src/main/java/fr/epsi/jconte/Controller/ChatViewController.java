/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.epsi.jconte.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author thomas.laspougeas
 */
public class ChatViewController implements Initializable
{

    @FXML
    TextArea textAreaChat;
    @FXML
    TextField textFieldChat;
    @FXML
    VBox vboxFriendList;



    @FXML
    public void handleActionButtonSend()
    {
        textAreaChat.appendText("You : " + textFieldChat.getText() + "\n");
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
