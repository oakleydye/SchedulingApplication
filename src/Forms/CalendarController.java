package Forms;

import CustomDateTimePicker.DateTimePicker;
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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

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
    @FXML DateTimePicker dtStart;
    @FXML Label lblEnd;
    @FXML DateTimePicker dtEnd;
    @FXML Label lblCustId;
    @FXML RadioButton rbMonth;
    @FXML RadioButton rbWeek;
    @FXML RadioButton rbAll;
    @FXML Button btnReport;
    @FXML Button btnEventReport;
    @FXML Button btnScheduleReport;
    @FXML TextField txtSearch;

    /**
     * Method called on startup, handles binding of appointments to grid and translation of page elements
     *
     * discussion of lambda
     *
     * The following uses lambda to filter a list of appointments and get
     * any that are within 15 minutes of the login time
     *
     */
    public void init(){
        ObservableList<Appointment> appointmentsList = GetAllAppointments(LoginController.userID, 1);
        CreateSearchFromList(appointmentsList);
        long setOffset = LocationManager.GetOffsetFromComputerSetting();
        //// TODO: 11/7/20 fix this
        List<Appointment> soonAppointments = appointmentsList.stream().filter(x -> x.getStartTime().isAfter(LocalDateTime.now()) && x.getStartTime().isBefore(LocalDateTime.now().plusMinutes(15))).collect(Collectors.toList());
        if (soonAppointments.size() > 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Upcoming appointments:\n");
            for (Appointment appt : soonAppointments){
                String text = alert.getContentText();
                text += "Appointment ID: " + appt.getAppointmentId() + " (" + appt.getTitle() + ") at " + appt.getStartTime() + "\n";
                alert.setContentText(text);
            }
            alert.setContentText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), alert.getContentText()));
            alert.setHeaderText("");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), "No upcoming events"));
            alert.setHeaderText("");
            alert.showAndWait();
        }

        List<Contact> contacts = GetContacts();
        cboContact.getItems().add(new Contact(0, "--Add New--", ""));
        if (contacts != null && contacts.size() > 0){
            for (Contact contact : contacts){
                cboContact.getItems().add(contact);
            }
        }

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
            txtSearch.setPromptText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), txtSearch.getPromptText()));
        }
    }

    /**
     * Helper method, returns all contacts from the database
     * @return List of contacts
     */
    private List<Contact> GetContacts(){
        try{
            List<Contact> contacts = new ArrayList<>();
            Connection conn = ConnectionManager.GetConnection();
            if (conn != null){
                String query = "CALL ContactGet()";
                CallableStatement stmt = conn.prepareCall(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()){
                    Contact contact = new Contact(rs.getInt("Contact_ID"), rs.getString("Contact_Name"), rs.getString("Email"));
                    contacts.add(contact);
                }
            }
            return contacts;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
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
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String query = "CALL GetAppointmentsByUser(?,?)";
                CallableStatement stmt = connection.prepareCall(query);
                stmt.setInt(1, userId);
                stmt.setInt(2, flag);
                ResultSet rs = stmt.executeQuery();
                long setOffset = LocationManager.GetOffsetFromComputerSetting();
                while (rs.next()){
                    Appointment appt = new Appointment();
                    appt.setAppointmentId(rs.getInt("Appointment_ID"));
                    appt.setTitle(rs.getString("Title"));
                    appt.setDescription(rs.getString("Description"));
                    appt.setLocation(rs.getString("Location"));
                    Contact contact = new Contact(rs.getInt("Contact_ID"), rs.getString("Contact_Name"), rs.getString("Email"));
                    appt.setContact(contact);
                    appt.setType(rs.getString("Type"));
                    appt.setStartTime(LocalDateTime.parse(rs.getString("Start"), format).plus(setOffset, ChronoField.MILLI_OF_DAY.getBaseUnit()));
                    appt.setEndTime(LocalDateTime.parse(rs.getString("End"), format).plus(setOffset, ChronoField.MILLI_OF_DAY.getBaseUnit()));
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
     * @param customerId int customer id
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
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                while (rs.next()){
                    customer.setCustomerId(customerId);
                    customer.setName(rs.getString("Customer_Name"));
                    customer.setAddress(rs.getString("Address"));
                    customer.setZip(rs.getInt("Postal_Code"));
                    customer.setPhone(rs.getString("Phone"));
                    customer.setCreatedOn(LocalDate.parse(rs.getString("Create_Date"), format));
                    customer.setCreatedBy(rs.getString("Created_By"));
                    customer.setLastUpdate(LocalDateTime.parse(rs.getString("Last_Update"), format));
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
        txtCustomerId.clear();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("To create a new event, please fill out the form on the right of the screen and click Save.");
        alert.showAndWait();
    }

    /**
     * Event handler, saves values in all the fields. Takes care of both new appointments and updates
     * @param actionEvent
     */
    public void btnSave_Click(ActionEvent actionEvent) {
        try{
            long setOffset = LocationManager.GetOffsetFromComputerSetting();
            LocalDateTime utc = dtStart.dateTimeProperty().getValue().minus(setOffset, ChronoField.MILLI_OF_DAY.getBaseUnit());
            if (utc.plusHours(-5).getHour() >= 8 && utc.plusHours(-5).getHour() <= 22){
                Connection conn = ConnectionManager.GetConnection();
                if (conn != null){
                    String query = "CALL AppointmentOverlapCheck(?,?,?)";
                    CallableStatement stmt = conn.prepareCall(query);
                    stmt.setInt(1, LoginController.userID);
                    stmt.setString(2, dtStart.dateTimeProperty().getValue().minus(setOffset, ChronoField.MILLI_OF_DAY.getBaseUnit()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    stmt.setString(3, dtEnd.dateTimeProperty().getValue().minus(setOffset, ChronoField.MILLI_OF_DAY.getBaseUnit()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    int count = rs.getInt("OverlapCount");
                    if (count > 0){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Overlapping appointments are not allowed");
                        alert.showAndWait();
                    } else {
                        if (txtAppointmentId.getText().equals("")){
                            InsertNewAppointment();
                        } else {
                            UpdateExistingAppointment();
                        }
                    }
                }
            } else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), "Appointments must be between 8am and 10pm EST"));
                alert.showAndWait();
            }
            CreateSearchFromList(GetAllAppointments(LoginController.userID, (rbMonth.isSelected() ? 1 : (rbWeek.isSelected() ? 2 : 0))));
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
                DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
                long setOffset = LocationManager.GetOffsetFromComputerSetting();
                String query = "CALL AppointmentInsert(?,?,?,?,?,?,?,?,?)";
                CallableStatement stmt = conn.prepareCall(query);
                stmt.setString(1, txtTitle.getText());
                stmt.setString(2, txtDescription.getText());
                stmt.setString(3, txtLocation.getText());
                stmt.setString(4, txtType.getText());
                stmt.setString(5, dtStart.dateTimeProperty().getValue().minus(setOffset, ChronoField.MILLI_OF_DAY.getBaseUnit()).toString());
                stmt.setString(6, dtEnd.dateTimeProperty().getValue().minus(setOffset, ChronoField.MILLI_OF_DAY.getBaseUnit()).toString());
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
                DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
                long setOffset = LocationManager.GetOffsetFromComputerSetting();
                String query = "CALL AppointmentUpdate(?,?,?,?,?,?,?,?,?,?)";
                CallableStatement stmt = conn.prepareCall(query);
                stmt.setString(1, txtAppointmentId.getText());
                stmt.setString(2, txtTitle.getText());
                stmt.setString(3, txtDescription.getText());
                stmt.setString(4, txtLocation.getText());
                stmt.setString(5, txtType.getText());
                stmt.setString(6, dtStart.dateTimeProperty().getValue().minus(setOffset, ChronoField.MILLI_OF_DAY.getBaseUnit()).toString());
                stmt.setString(7, dtEnd.dateTimeProperty().getValue().minus(setOffset, ChronoField.MILLI_OF_DAY.getBaseUnit()).toString());
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
                    alert.setContentText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), "Appointment " + txtTitle.getText() + " cancelled\r\nID: " + txtAppointmentId.getText() + " Type: " + txtType.getText()));
                    alert.showAndWait();
                    btnAdd_Click(actionEvent);
                } else {
                    throw new Exception("Error establishing database connections");
                }
            }
            CreateSearchFromList(GetAllAppointments(LoginController.userID, (rbMonth.isSelected() ? 1 : (rbWeek.isSelected() ? 2 : 0))));
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
        CreateSearchFromList(GetAllAppointments(LoginController.userID, 1));
    }

    /**
     * Event handler, gets appointments for the current week and adds them to the grid
     * @param actionEvent
     */
    public void rbWeek_Click(ActionEvent actionEvent) {
        CreateSearchFromList(GetAllAppointments(LoginController.userID, 2));
        grdAppointment.refresh();
    }

    /**
     * Event handler, gets all appointments and adds them to the grid
     * @param actionEvent
     */
    public void rbAll_Click(ActionEvent actionEvent) {
        CreateSearchFromList(GetAllAppointments(LoginController.userID, 0));
    }

    /**
     * Method to refresh the grid contents
     * @param appointmentsList list of appointments to be put into the grid
     *
     * The following block of code uses several lambda expressions to
     * add a realtime search to the appointments grid
     */
    private void CreateSearchFromList(ObservableList<Appointment> appointmentsList){
        assert appointmentsList != null;
        FilteredList<Appointment> appointments = new FilteredList<>(appointmentsList, p -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            appointments.setPredicate(appointment -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                if (appointment.getTitle().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                else if (Integer.toString(appointment.getAppointmentId()).contains(lowerCaseFilter)){
                    return true;
                }
                else if (appointment.getContact().getName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                else return appointment.getType().toLowerCase().contains(lowerCaseFilter);
            });
        });
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
            List<String> columns = Arrays.asList("UserName", "Appointment ID", "Title", "Type", "Description", "Start", "End", "Customer ID");
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

    /**
     * Event handler, generates the contact directory report
     * @param actionEvent
     */
    public void btnReport_Click(ActionEvent actionEvent) {
        try{
            List<String> columns = Arrays.asList("Contact ID", "Contact Name", "Email");
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
     *
     * discussion of lambda
     * The following method contains code to filter the combobox based on contact assigned to an appointment
     */
    public void grdAppointment_Click(MouseEvent mouseEvent) {
        Appointment selectedAppointment = grdAppointment.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null){
            txtAppointmentId.setText(Integer.toString(selectedAppointment.getAppointmentId()));
            txtTitle.setText(selectedAppointment.getTitle());
            txtDescription.setText(selectedAppointment.getDescription());
            txtLocation.setText(selectedAppointment.getLocation());
            cboContact.getSelectionModel().select(cboContact.getItems().stream().filter(x -> x.getContactId() == selectedAppointment.getContact().getContactId()).findFirst().orElse(null));
            txtType.setText(selectedAppointment.getType());
            dtStart.dateTimeProperty().setValue(selectedAppointment.getStartTime());
            dtEnd.dateTimeProperty().setValue(selectedAppointment.getEndTime());
            txtCustomerId.setText(Integer.toString(selectedAppointment.getCustomer().getCustomerId()));
        }
    }

    /**
     * Event handler, allows for adding new contacts
     * @param mouseEvent
     */
    public void cboContact_Click(MouseEvent mouseEvent) {
        Contact selectedContact = cboContact.getSelectionModel().getSelectedItem();
        if (selectedContact != null){
            if (selectedContact.getName().equals("--Add New--")){
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("frmContact.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root, 800, 600));
                    stage.show();
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }
}
