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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author oakleydye
 *
 * Controller class for frmCalendar.fxml
 */
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
    @FXML RadioButton rbAll;
    @FXML Button btnReport;
    @FXML Button btnEventReport;
    @FXML Button btnScheduleReport;

    /**
     * Method called on startup, handles binding of appointments to grid and translation of page elements
     *
     */
    public void init(){
        FilteredList<Appointment> appointments = new FilteredList<Appointment>(Objects.requireNonNull(GetAllAppointments(LoginController.userID, 1)));
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
            rbAll.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), rbAll.getText()));
            btnReport.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), btnReport.getText()));
            btnEventReport.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), btnEventReport.getText()));
            btnScheduleReport.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), btnScheduleReport.getText()));
        }
    }

    /**
     * Method to get all appointments by user
     * @param userId current user id
     * @param flag tells the proc what appointments to get, 1 = month, 2 = week, 0 = all
     * @return list of appointments to add to grdAppointments
     */
    private ObservableList<Appointment> GetAllAppointments(int userId, int flag){
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

    /**
     * Gets a customer object from a given customer id
     * @param customerId
     * @return Libraries.Customer
     */
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

    /**
     * Event handler, clears all the fields in the form so that a new appointment can be created
     * @param actionEvent
     */
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

    /**
     * Event handler, saves values in all the fields. Takes care of both new appointments and updates
     * @param actionEvent
     */
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

    /**
     * Inserts a new appointment into the database
     */
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

    /**
     * Updates an existing appointment in the database
     */
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

    /**
     * Event handler, handles deleting of a selected appointment
     * @param actionEvent
     */
    public void btnDelete_Click(ActionEvent actionEvent) {
        try{
            if (!txtAppointmentId.getText().equals("")){
                Connection conn = ConnectionManager.GetConnection();
                if (conn != null){
                    String query = "CALL AppointmentDelete(?)";
                    CallableStatement stmt = conn.prepareCall(query);
                    stmt.setString(1, txtAppointmentId.getText());
                    stmt.executeQuery();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Appointment " + txtTitle.getText() + " cancelled");
                    alert.showAndWait();
                    btnAdd_Click(actionEvent);
                } else {
                    throw new Exception("Error establishing database connections");
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Event handler, loads up frmCustomers.fxml to manage customers
     * @param actionEvent
     */
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

    /**
     * Event handler, gets appointments for the current month and adds them to the grid
     * @param actionEvent
     */
    public void rbMonth_Click(ActionEvent actionEvent) {
        FilteredList<Appointment> appointments = new FilteredList<>(Objects.requireNonNull(GetAllAppointments(LoginController.userID, 1)));
        grdAppointment.setItems(appointments);
    }

    /**
     * Event handler, gets appointments for the current week and adds them to the grid
     * @param actionEvent
     */
    public void rbWeek_Click(ActionEvent actionEvent) {
        FilteredList<Appointment> appointments = new FilteredList<>(Objects.requireNonNull(GetAllAppointments(LoginController.userID, 2)));
        grdAppointment.setItems(appointments);
    }

    /**
     * Event handler, gets all appointments and adds them to the grid
     * @param actionEvent
     */
    public void rbAll_Click(ActionEvent actionEvent) {
        FilteredList<Appointment> appointments = new FilteredList<>(Objects.requireNonNull(GetAllAppointments(LoginController.userID, 0)));
        grdAppointment.setItems(appointments);
    }

    /**
     * Event handler, builds report for events
     * @param actionEvent
     */
    public void btnEventReport_Click(ActionEvent actionEvent) {
        try{
            List<String> columns = Arrays.asList("Customer", "Month", "Type", "# of Appointments");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReportViewer.fxml"));
            Parent root = loader.load();
            ReportViewerController controller = loader.getController();
            controller.init(columns);
            controller.buildEventReport();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Event handler, builds report for schedule
     * @param actionEvent
     */
    public void btnScheduleReport_Click(ActionEvent actionEvent) {
        try{
            List<String> columns = Arrays.asList("Appointment ID", "Title", "Type", "Description", "Start", "End", "Customer ID");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReportViewer.fxml"));
            Parent root = loader.load();
            ReportViewerController controller = loader.getController();
            controller.init(columns);
            controller.buildScheduleReport();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void btnReport_Click(ActionEvent actionEvent) {
        try{
            List<String> columns = Arrays.asList();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReportViewer.fxml"));
            Parent root = loader.load();
            ReportViewerController controller = loader.getController();
            controller.init(columns);
            controller.buildReport();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Event handler, populates appointment info from the grid to the text fields for editing
     * @param mouseEvent
     */
    public void grdAppointment_Click(MouseEvent mouseEvent) {
        Appointment selectedAppointment = grdAppointment.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null){
            txtAppointmentId.setText(Integer.toString(selectedAppointment.getAppointmentId()));
            txtTitle.setText(selectedAppointment.getTitle());
            txtDescription.setText(selectedAppointment.getDescription());
            txtLocation.setText(selectedAppointment.getLocation());
            /** discussion of lambda */
            cboContact.getSelectionModel().select(cboContact.getItems().stream().filter(x -> x.getContactId() == selectedAppointment.getContact().getContactId()).findFirst().orElse(null));
            txtType.setText(selectedAppointment.getType());
            txtStart.setText(selectedAppointment.getStartTime().toString());
            txtEnd.setText(selectedAppointment.getEndTime().toString());
            txtCustomerId.setText(Integer.toString(selectedAppointment.getCustomer().getCustomerId()));
        }
    }
}
