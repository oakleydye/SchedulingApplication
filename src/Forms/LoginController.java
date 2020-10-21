package Forms;

import Libraries.ConnectionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML TextField txtUsername;
    @FXML PasswordField txtPassword;

    public static String currentUser;

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
                            currentUser = txtUsername.getText();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("frmCalendar.fxml"));
                            Parent root = loader.load();
                            CalendarController controller = new CalendarController();
                            controller.init(GetUserId(txtUsername.getText()));
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root, 1280, 800));
                            stage.show();

                            Stage stage2 = (Stage) txtUsername.getScene().getWindow();
                            stage2.close();
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

    private int GetUserId(String username){
        try{
            Connection connection = ConnectionManager.GetConnection();
            if (connection != null){
                String query = "CALL GetUserId(?)";
                CallableStatement statement = connection.prepareCall(query);
                statement.setString(1, username);
                ResultSet rs = statement.executeQuery();
                while (rs.next()){
                    return rs.getInt("User_ID");
                }
                connection.close();
            }
            return 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }
}
