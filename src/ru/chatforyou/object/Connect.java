package ru.chatforyou.object;

import javafx.collections.ObservableList;
import javafx.stage.Stage;
import ru.chatforyou.Main;
import ru.chatforyou.controlers.Controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connect {
    public static final int PORT = 8191;
    private static final String MESSAGE = "Message:";
    public static final String MESSAGE_USER = "messageUser";
    public static final String ADD_USER = "Вошел: ";
    public static final String DELETE_USER = "Покинул чат: ";
    public static final String SYSTEM = "System:";


    private static Stage primaryStage;
    private static Socket socket;
    private static Controller controller;
    public static Thread messageListener;
    private static DataOutputStream out;
    private static DataInputStream inputStream;
    private static Main main;
    public static ObservableList userList;

    public static void connect(Stage primaryStage, Main main) {
        try {
            Connect.primaryStage = primaryStage;
            Connect.main = main;
            socket = new Socket("localHost", PORT);
            out = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());


        } catch (IOException e) {
            System.out.println("сервер не доступен!");
            primaryStage.close();
        }
    }

    public static String sentAuth(String auth, String name) {
        String answer = "";

        try {
            outMsg(auth);
            answer = inputStream.readUTF();
            System.out.println(answer);
            if (answer.equals(name)) {
                Main.userName = name;
                return Main.ENTER_TO_CHAT;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }

    private static void outMsg(String s) throws IOException {
        out.writeUTF(s);
        out.flush();
    }

    public static void messageListener(Controller controller) {
        Connect.controller = controller;
        Thread thread = new Thread(() -> {
            while (true) {
                String message = inMessage();
                if (message == null) break;

                String[] element = message.split("/");

//                for (String s : element) {
                    System.out.println("---------------- "+ message);
//                }

                if (element[0].equals(MESSAGE)) {
                    if (element[1].equals(MESSAGE_USER)) {
                        printMessage(controller, element[2]);
                    } else if (element[1].equals(ADD_USER)) {
//                        userList.add(element[2]);
                        printMessage(controller, ADD_USER + element[2]);
                    } else if (element[1].equals(DELETE_USER)) {
//                        userList.remove(element[2]);
                        printMessage(controller, DELETE_USER + element[2]);
                    }
                } else if (element[0].equals(SYSTEM)) {
//                    userList.add(element[2]);
                    updateUser(controller, element[1]);
                    System.out.println("Список пользователей " + element[1]);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private static void printMessage(Controller controller, String value) {
        controller.chat.appendText(value + "\n");
    }

    private static void updateUser(Controller controller, String value) {
        controller.userArea.setText("");
        String[] element = value.split(" ");
        for (String s : element) {
            controller.userArea.appendText(s + "\n");
        }
    }

    public static void sentMessage(String message) {
        try {
            outMsg(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public static String inMessage() {
        String s = null;
        try {
            s = inputStream.readUTF();
        } catch (IOException e) {
            System.out.println("Нет связи с сервером");
        }
        return s;
    }


}
