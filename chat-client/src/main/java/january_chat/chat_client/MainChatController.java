package january_chat.chat_client;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainChatController implements Initializable {

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
