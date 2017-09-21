package ru.chatforyou.controlers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ru.chatforyou.Main;
import ru.chatforyou.object.Connect;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public TextField myText;
    public TextArea chat;
    public TextArea userArea;


    public void sendEnter(ActionEvent actionEvent) {
        sendMessage(Main.userName, myText.getText());
    }

    public void sendBtn(ActionEvent actionEvent) {
        sendMessage(Main.userName, myText.getText());
    }

    private void sendMessage(String userName, String messageText) {
        if (!messageText.equals("")) {
            SimpleDateFormat format = new SimpleDateFormat("KK:MM");
            Connect.sentMessage("[" + format.format(Calendar.getInstance().getTime()) + "] " + userName + ": " + messageText);
        }

        myText.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        Connect.userList = FXCollections.observableArrayList();
//        userList.setItems(Connect.userList);
        Connect.messageListener(this);
    }

}
