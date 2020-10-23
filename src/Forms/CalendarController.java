package Forms;

import Libraries.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;

public class CalendarController {
    @FXML TableView<Appointment> grdAppointment = new TableView<>();
    @FXML TextField txtAppointmentId;
    @FXML TextField txtTitle;
    @FXML TextField txtDescription;
    @FXML TextField txtLocation;
    @FXML ComboBox<Contact> cboContact;
    @FXML TextField txtType;
    @FXML TextField txtStart;
    @FXML TextField txtEnd;
    @FXML TextField txtCustomerId;
    @FXML Button btnAdd;
    @FXML Button btnSave;
    @FXML Button btnDelete;
    @FXML Button btnCustomers;

    @FXML TableColumn colID;
    @FXML TableColumn colTitle;
    @FXML TableColumn colDesc;
    @FXML TableColumn colLocation;
    @FXML TableColumn colContact;
    @FXML TableColumn colType;
    @FXML TableColumn colStart;
    @FXML TableColumn colEnd;
    @FXML TableColumn colCustId;
    @FXML Label lblId;
    @FXML Label lblTitle;
    @FXML Label lblDesc;
    @FXML Label lblLocation;
    @FXML Label lblContact;
    @FXML Label lblType;
    @FXML Label lblStart;
    @FXML Label lblEnd;
    @FXML Label lblCustId;
    @FXML RadioButton rbMonth;
    @FXML RadioButton rbWeek;

    public void init(int userId){
        FilteredList<Appointment> appointments = new FilteredList<Appointment>(Objects.requireNonNull(GetAllAppointments(userId)));
        grdAppointment.setItems(appointments);

        if (!Locale.getDefault().getLanguage().equals("en")){
            colID.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), colID.getText()));
            colTitle.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), colTitle.getText()));
            colDesc.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), colDesc.getText()));
            colLocation.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), colLocation.getText()));
            colContact.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), colContact.getText()));
            colType.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), colType.getText()));
            colStart.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), colStart.getText()));
            colEnd.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), colEnd.getText()));
            colCustId.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), colCustId.getText()));
            lblId.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), lblId.getText()));
            lblTitle.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), lblTitle.getText()));
            lblDesc.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), lblDesc.getText()));
            lblLocation.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), lblLocation.getText()));
            lblContact.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), lblContact.getText()));
            lblType.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), lblType.getText()));
            lblStart.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), lblStart.getText()));
            lblEnd.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), lblEnd.getText()));
            lblCustId.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), lblCustId.getText()));
            btnAdd.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), btnAdd.getText()));
            btnSave.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), btnSave.getText()));
            btnDelete.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), btnDelete.getText()));
            btnCustomers.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), btnCustomers.getText()));
            rbMonth.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), rbMonth.getText()));
            rbWeek.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), rbWeek.getText()));
        }
    }

    private ObservableList<Appointment> GetAllAppointments(int userId){
        try{
            ObservableList<Appointment> appointments = FXCollections.observableArrayList();
            Connection connection = ConnectionManager.GetConnection();
            if (connection != null){
                String query = "CALL GetAppointmentsByUser(?)";
                CallableStatement stmt = connection.prepareCall(query);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()){
                    Appointment appt = new Appointment();
                    appt.setAppointmentId(rs.getInt("Appointment_ID"));
                    appt.setTitle(rs.getString("Title"));
                    appt.setDescription(rs.getString("Description"));
                    appt.setLocation(rs.getString("Location"));
                    Contact contact = new Contact();
                    contact.setName(rs.getString("Contact_Name"));
                    contact.setEmail(rs.getString("Email"));
                    appt.setContact(contact);
                    appt.setType(rs.getString("Type"));
                    appt.setStartTime(LocalDateTime.parse(rs.getString("Start")));
                    appt.setEndTime(LocalDateTime.parse(rs.getString("End")));
                    appt.setCustomer(GetCustomer(rs.getInt("Customer_ID")));
                    appointments.add(appt);
                }
                connection.close();
            }
            return appointments;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Customer GetCustomer(int customerId){
        try{
            Customer customer = new Customer();
            Connection connection = ConnectionManager.GetConnection();
            if (connection != null){
                String query = "CALL GetCustomerById(?)";
                CallableStatement statement = connection.prepareCall(query);
                statement.setInt(1, customerId);
                ResultSet rs = statement.executeQuery();
                while (rs.next()){
                    customer.setCustomerId(customerId);
                    customer.setName(rs.getString("Customer_Name"));
                    customer.setAddress(rs.getString("Address"));
                    customer.setZip(rs.getInt("Postal_Code"));
                    customer.setPhone(rs.getString("Phone"));
                    customer.setCreatedOn(LocalDate.parse(rs.getString("Create_Date")));
                    customer.setCreatedBy(rs.getString("Created_By"));
                    customer.setLastUpdate(LocalDateTime.parse(rs.getString("Last_Update")));
                    customer.setLastUpdateBy(rs.getString("Last_Updated_By"));
                    customer.setDivisionId(rs.getInt("Division_ID"));
                }
                connection.close();
            }
            return customer;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void btnAdd_Click(ActionEvent actionEvent) {
        txtAppointmentId.clear();
        txtTitle.clear();
        txtDescription.clear();
        txtLocation.clear();
        txtType.clear();
        txtStart.clear();
        txtEnd.clear();
        txtCustomerId.clear();
    }

    public void btnSave_Click(ActionEvent actionEvent) {
        try{
            if (txtAppointmentId.getText().equals("")){
                InsertNewAppointment();
            } else {
                UpdateExistingAppointment();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void InsertNewAppointment(){
        try{
            Connection conn = ConnectionManager.GetConnection();
            if (conn != null){
                String query = "CALL AppointmentInsert(?,?,?,?,?,?,?,?,?)";
                CallableStatement stmt = conn.prepareCall(query);
                stmt.setString(1, txtTitle.getText());
                stmt.setString(2, txtDescription.getText());
                stmt.setString(3, txtLocation.getText());
                stmt.setString(4, txtType.getText());
                stmt.setString(5, txtStart.getText());
                stmt.setString(6, txtEnd.getText());
                stmt.setString(7, LoginController.currentUser);
                stmt.setString(8, txtCustomerId.getText());
                stmt.setInt(9, cboContact.getSelectionModel().getSelectedItem().getContactId());
                stmt.executeQuery();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void UpdateExistingAppointment(){
        try{
            Connection conn = ConnectionManager.GetConnection();
            if (conn != null){
                String query = "CALL AppointmentUpdate(?,?,?,?,?,?,?,?,?,?)";
                CallableStatement stmt = conn.prepareCall(query);
                stmt.setString(1, txtAppointmentId.getText());
                stmt.setString(2, txtTitle.getText());
                stmt.setString(3, txtDescription.getText());
                stmt.setString(4, txtLocation.getText());
                stmt.setString(5, txtType.getText());
                stmt.setString(6, txtStart.getText());
                stmt.setString(7, txtEnd.getText());
                stmt.setString(8, LoginController.currentUser);
                stmt.setString(9, txtCustomerId.getText());
                stmt.setInt(10, cboContact.getSelectionModel().getSelectedItem().getContactId());
                stmt.executeQuery();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void btnDelete_Click(ActionEvent actionEvent) {
        try{
            if (!txtAppointmentId.getText().equals("")){
                Connection conn = ConnectionManager.GetConnection();
                if (conn != null){
                    String query = "CALL AppointmentDelete(?)";
                    CallableStatement stmt = conn.prepareCall(query);
                    stmt.setString(1, txtAppointmentId.getText());
                    stmt.executeQuery();
                } else {
                    throw new Exception("Error establishing database connections");
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void btnCustomers_Click(ActionEvent actionEvent) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("frmCustomer.fxml"));
            Parent root = loader.load();
            CustomerController controller = loader.getController();
            controller.init();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1280, 800));
            stage.show();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void rbMonth_Click(ActionEvent actionEvent) {

    }

    public void rbWeek_Click(ActionEvent actionEvent) {

    }
}
