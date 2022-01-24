package januari_chat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ExampleController {
@FXML
    public Button btnClick;
@FXML
    public Label labelText;

    public void click(ActionEvent actionEvent) {
        System.out.println("Click");
        labelText.setText("Hello");
        btnClick.setText("Presedddd");
        btnClick.setScaleX(5.0);
        btnClick.setScaleY(5.0);

    }
}
