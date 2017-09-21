package ru.chatforyou.object;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserList {
    ObservableList<String> userList;

    public void add(String s){
        userList.add(s);
    }

    public void remove(String s){
        userList.remove(s);

    }

    public ObservableList<String> getUserList() {
        return userList;
    }
}
