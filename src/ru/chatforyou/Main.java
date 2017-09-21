package ru.chatforyou;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import ru.chatforyou.object.Connect;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Calendar;
import java.util.Optional;

public class Main extends Application {
    public static final String ENTER_TO_CHAT = "enterToChat";
    public static String userName = "";
    private Stage primaryStage;
    private long startTime;


    @Override
    public void stop() throws Exception {
        super.stop();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        Connect.connect(primaryStage, this);

        createModalWindow(null);
    }

    private void createModalWindow(String msg) throws IOException {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Вход");
        dialog.setHeaderText("");


        ButtonType loginButtonType = new ButtonType("login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL); //Добавляем кнопки

        //Разметка
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        TextField login = new TextField();
        login.setPromptText("login");
        PasswordField password = new PasswordField();
        password.setPromptText("password");

        gridPane.add(new Label("Login"), 0, 0);
        gridPane.add(login, 1, 0);
        gridPane.add(new Label("Password"), 0, 1);
        gridPane.add(password, 1, 1);
        if (msg != null) {
            gridPane.add(new Label(msg), 1, 2);
        }
        // Конец разметки

        //Включить/отключить кнопку взависемости от поля логин
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        //слушатель поля логин
        login.textProperty().addListener(((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        }));

        dialog.getDialogPane().setContent(gridPane);

        //фокус на поле username по умолчанию
        Platform.runLater(() -> login.requestFocus());

        if (startTime == 0) {
            startTime = Calendar.getInstance().getTime().getTime();
        }

        //Попытка решить через потоки
//        Thread thread = new Thread(() -> {
//            while (true) {
//                if (Calendar.getInstance().getTime().getTime() - startTime >= 3000) {
//                    dialog.close();
//                }
//            }
//        });
//        thread.setDaemon(true);
//        thread.start();

        //Попытка решить через таск задачу
//        Task task = new Task<Void>() {
//            @Override
//            public Void call() {
//                while (true) {
//                    if (Calendar.getInstance().getTime().getTime() - startTime >= 3000) {
//                        dialog.close();
//                        System.out.println("!!!!!!!!!!!!");
//                        return null;
//                    }
//                }
//            }
//        };
//        new Thread(task).start();


        // Понимает по нажатию, что пора отключить поток
        if (Calendar.getInstance().getTime().getTime() - startTime >= 120000) {
            dialog.close();
        } else {
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == loginButtonType) {
                    return new Pair<>(login.getText(), password.getText());
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();
            if (result.isPresent()) {
                String answerServer = Connect.sentAuth(result.get().getKey() + " " + result.get().getValue(), result.get().getKey());

                if (answerServer.equalsIgnoreCase(ENTER_TO_CHAT)) {
                    try {
                        createFx(); //создание основного окна
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else createModalWindow(answerServer);
            }
        }

    }


    public void createFx() throws java.io.IOException {

        Parent root = FXMLLoader.load(getClass().getResource("gui/sample.fxml"));

        primaryStage.setTitle("ChatForYou");
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add("ru/chatforyou/gui/styles/jfxtras.css");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
