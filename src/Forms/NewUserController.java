package Forms;

import Libraries.ConnectionManager;
import Libraries.Report;
import Libraries.TranslationManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Locale;

public class NewUserController {
    @FXML Label lblUsername;
    @FXML TextField txtUsername;
    @FXML Label lblPassword;
    @FXML PasswordField txtPassword;
    @FXML Button btnCreateUser;
    @FXML Button btnModUser;
    public String UserId;

    public void init(){
        if (!Locale.getDefault().getLanguage().equals("en")){
            lblUsername.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), lblUsername.getText()));
            lblPassword.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), lblPassword.getText()));
            btnCreateUser.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), btnCreateUser.getText()));
        }
    }

    public void init(Report report){
        if (!Locale.getDefault().getLanguage().equals("en")){
            lblUsername.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), lblUsername.getText()));
            lblPassword.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), lblPassword.getText()));
            btnCreateUser.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), btnCreateUser.getText()));
        }

        this.UserId = report.getUserId();
        txtUsername.setText(report.getUsername());
        txtPassword.setText(report.getPassword());
        btnCreateUser.setVisible(false);
        btnModUser.setVisible(true);
    }

    public void btnCreateUser_Click(){
        try{
            if (!txtUsername.getText().equals("") && !txtPassword.getText().equals("")){
                Connection connection = ConnectionManager.GetConnection();
                if (connection != null){
                    String query = "CALL NewUserInsert(?, ?, ?)";
                    CallableStatement stmt = connection.prepareCall(query);
                    stmt.setString(1, txtUsername.getText());
                    stmt.setString(2, txtPassword.getText());
                    stmt.setString(3, LoginController.currentUser);
                    stmt.executeQuery();
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void btnModUser_Click(){
        try{
            if (!txtUsername.getText().equals("") && !txtPassword.getText().equals("")){
                Connection connection = ConnectionManager.GetConnection();
                if (connection != null){
                    String query = "CALL ExistingUserModify(?, ?, ?)";
                    CallableStatement statement = connection.prepareCall(query);
                    statement.setString(1, this.UserId);
                    statement.setString(2, txtUsername.getText());
                    statement.setString(3, txtPassword.getText());
                    statement.executeQuery();
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
