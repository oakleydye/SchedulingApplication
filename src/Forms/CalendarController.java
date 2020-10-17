package Forms;

import Libraries.Appointment;
import Libraries.ConnectionManager;
import Libraries.Contact;
import Libraries.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class CalendarController {
    @FXML TableView<Appointment> grdAppointment = new TableView<>();

    public void init(int userId){
        FilteredList<Appointment> appointments = new FilteredList<Appointment>(Objects.requireNonNull(GetAllAppointments(userId)));
        grdAppointment.setItems(appointments);
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
            }
            return customer;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
