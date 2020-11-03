package Forms;

import Libraries.ConnectionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.sql.CallableStatement;
import java.sql.Connection;

/**
 * @author oakleydye
 *
 * Controller for frmContact.fxml
 */
public class ContactController {
    @FXML Label lblId;
    @FXML Label lblName;
    @FXML Label lblEmail;
    @FXML TextField txtId;
    @FXML TextField txtName;
    @FXML TextField txtEmail;
    @FXML Button btnSave;

    /**
     * Event handler, saves new contacts to the database
     * @param mouseEvent
     */
    public void btnSave_Click(MouseEvent mouseEvent) {
        try{
            Connection conn = ConnectionManager.GetConnection();
            if (conn != null){
                String query = "CALL ContactSave(?,?,?)";
                CallableStatement stmt = conn.prepareCall(query);
                stmt.setString(1, txtId.getText().equals("") ? "0" : txtId.getText());
                stmt.setString(2, txtName.getText());
                stmt.setString(3, txtEmail.getText());
                stmt.executeQuery();
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
