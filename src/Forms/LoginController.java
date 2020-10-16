package Forms;

import Libraries.ConnectionManager;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class LoginController {
    @FXML TextField txtUsername;
    @FXML PasswordField txtPassword;

    public void init(){

    }

    public void btnLogin_Click(ActionEvent actionEvent) {
        try{
            if (!txtUsername.getText().equals("") && !txtPassword.getText().equals("")){
                Connection connection = ConnectionManager.GetConnection();
                if (connection != null){
                    String query = "CALL ValidateUser(?, ?)";
                    CallableStatement stmt = connection.prepareCall(query);
                    stmt.setString(1, txtUsername.getText());
                    stmt.setString(2, txtPassword.getText());
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()){
                        if (rs.getString("Valid").equals("True")){
                            //TODO: show main winodw here
                            
                        }
                        else{
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Incorrect password");
                            alert.setHeaderText("");
                            alert.showAndWait();
                        }
                    }
                    connection.close();
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
