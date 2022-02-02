package january_chat.chat_client;



import january_chat.chat_client.network.MessageProcessor;
import january_chat.chat_client.network.NetworkService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainChatController implements Initializable, MessageProcessor {
    public static final String REGEX = "%!%";

    private String nick;
    private NetworkService networkService;

    @FXML
    public VBox loginPanel;

    @FXML
    public TextField loginField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public VBox mainChatPanel;

    @FXML
    public TextArea mainChatArea;
    @FXML
    public ListView contactList;
    @FXML
    public TextField inputField;
    @FXML
    public Button btnSend;

    public void connectToServer(ActionEvent actionEvent) {
    }
    public void disconnectFromServer(ActionEvent actionEvent) {
    }

    public void mockAction(ActionEvent actionEvent) {
    }
    public void exit(ActionEvent actionEvent) {
        System.exit(1);
    }
    public void showHelp(ActionEvent actionEvent) {
    }
    public void showAbout(ActionEvent actionEvent) {
    }
    public void sendMessage(ActionEvent actionEvent) {
        var message = inputField.getText();
        if (message.isBlank()) {
            return;
        }
/*
var recipient = contactList.getSelectionModel().getSelectedItem();
mainChatArea.appendText(recipient + ": " + message + System.lineSeparator());
 */
        networkService.sendMessage("/broadcast" + REGEX  + message);
        inputField.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


     this.networkService = new NetworkService(this);
}

    @Override
    public void processMessage(String message) {
        Platform.runLater(() -> parseIncomingMessage(message));
    }

    private void parseIncomingMessage(String message) {
        var splitMessage = message.split(REGEX);
        switch (splitMessage[0]) {
            //вариант кейсов
            case "/auth_ok" :
                this.nick = splitMessage[1];
                loginPanel.setVisible(false);
                mainChatPanel.setVisible(true);
                break;
            case "/broadcast" :
                mainChatArea.appendText(splitMessage[1] + ": " + splitMessage[2] + System.lineSeparator());
                break;
            case "/error" :
                showError(splitMessage[1]);
                System.out.println("got error " + splitMessage[1]);
                break;
                //список контактов
            case "/list" :
                //создаем contacts /добавляем
                var contacts = new ArrayList<String>();
                contacts.add("ALL");
                for (int i = 1; i < splitMessage.length; i++) {
                    contacts.add(splitMessage[i]);
                }
                contactList.setItems(FXCollections.observableList(contacts));
                break;
        }
    }

    private void showError(String message) {
        var alert = new Alert(Alert.AlertType.ERROR,
                "An error occured: " + message,
                ButtonType.OK);
        alert.showAndWait();
    }

    //отправка авторизации
    public void sendAuth(ActionEvent actionEvent) {
        //полученный логин из getText();
        var login = loginField.getText();
        var password = passwordField.getText();
        //если логин или пароль из бланка то
        if (login.isBlank() || password.isBlank()) {
            return;
        }

        var message = "/auth" + REGEX + login + REGEX + password;

        //
        if (!networkService.isConnected()) {
            try {
                networkService.connect();
            } catch (IOException e) {
                e.printStackTrace();
                showError(e.getMessage());

            }
        }
        networkService.sendMessage(message);//подключаемся и шлем авторизацию
    }

    public void mockAktion(ActionEvent actionEvent) {
    }
}

/*public class MainChatController implements Initializable {

    @FXML
    public VBox mainChatPanel;
    @FXML
    public TextArea mainChatArea;
    @FXML
    public ListView contactList;
    @FXML
    public TextField inputField;
    @FXML
    public Button btnSend;

    public void connectToServer(ActionEvent actionEvent) {
    }

    public void disconnectFromServer(ActionEvent actionEvent) {
    }

    public void mockAktion(ActionEvent actionEvent) {
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(1);//код выхода
    }

    public void showHelp(ActionEvent actionEvent) {
    }

    public void showAbout(ActionEvent actionEvent) {
    }

    public void sendMessage(ActionEvent actionEvent) {
        var message  = inputField.getText();
        //если пустое сообщение то
        if (message.isBlank()){
            return;//ни чего не происходит
        }

        //но если есть что то, он не пустой то дастаем из контакт листа ListView, через
        //getSelectionModel() достаем выбранный объект
        //далее добавляем этот объект recipient + а так же сообщение
               var recipient = contactList.getSelectionModel().getSelectedItem();
        mainChatArea.appendText(recipient + ":\n" + message  + System.lineSeparator());
        //и очистим поле ввода после ввода
        inputField.clear();


    }

    @Override     //выполняется на старте контроллера, заполняется..
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //на старте приложения вложем фейковый список контактов
        var contacts = new ArrayList<String>();
        contacts.add("ALL:");    //выбор всех контактов
        for (int i = 0; i < 10; i ++) {
            contacts.add("Контакт#" + (i + 1) );
        }
        //добавим список в
        contactList.setItems(FXCollections.observableList(contacts));
        //выбор из списка
        contactList.getSelectionModel().selectFirst();
        //contactList.getSelectionModel().
    }
}
*/