package Forms;

import Libraries.ConnectionManager;
import Libraries.TranslationManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * @author oakleydye
 *
 * Controller class for frmLogin.fxml
 */
public class LoginController {
    @FXML TextField txtUsername;
    @FXML PasswordField txtPassword;
    @FXML Label lblUsername;
    @FXML Label lblPwd;
    @FXML Button btnLogin;

    public static String currentUser;
    public static int userID;

    /**
     * method called on startup, handles translation
     *
     * CHANGE LOCALE HERE FOR LANGUAGE TESTING
     */
    public void init(){
        //// TODO: 10/21/20 Add in ability to show user location here
        //Locale.setDefault(new Locale("fr"));
        if (!Locale.getDefault().getLanguage().equals("en")){
            lblUsername.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), lblUsername.getText()));
            lblPwd.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), lblPwd.getText()));
            btnLogin.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), btnLogin.getText()));
        }
    }

    /**
     * Event handler, validates input and allows user into the application.
     * Also handles logging of attempts to login and assigning global user variables
     * @param actionEvent
     */
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
                    int count = 0;
                    while (rs.next()){
                        count++;
                        if (rs.getString("Valid").equals("True")){
                            currentUser = txtUsername.getText();
                            userID = GetUserId(currentUser);
                            WriteToFile(true);
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("frmCalendar.fxml"));
                            Parent root = loader.load();
                            Stage stage = new Stage();
                            stage.setTitle("Scheduling Manager");
                            stage.setScene(new Scene(root, 1280, 800));
                            CalendarController controller = loader.getController();
                            controller.init();
                            stage.show();

                            Stage stage2 = (Stage) txtUsername.getScene().getWindow();
                            stage2.close();
                        }
                        else{
                            WriteToFile(false);
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Incorrect password");
                            alert.setHeaderText("");
                            alert.showAndWait();
                        }
                    }
                    if (count == 0) {
                        WriteToFile(false);
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Incorrect password");
                        alert.setHeaderText("");
                        alert.showAndWait();
                    }
                    connection.close();
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Method to handle writing login attempts to a file
     * @param isSuccessful dictates what text is written based on whether or not the attempt was successful
     */
    private void WriteToFile(boolean isSuccessful){
        try {
            File file = new File("login_activity.txt");
            if (!file.exists()) { file.createNewFile(); }
            FileWriter writer = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write("Login attempt by " + txtUsername.getText() + " on " + LocalDateTime.now() + " was " + (isSuccessful ? "successful." : "not successful."));
            bw.newLine();
            bw.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Helper method to get a user id from a username
     * @param username string value, current username
     * @return int, userId
     */
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
