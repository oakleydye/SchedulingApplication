package Forms;

import Libraries.ConnectionManager;
import Libraries.Report;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author oakleydye
 *
 * FXML Controller for ReportViewer.fxml
 */
public class ReportViewerController {
    @FXML TableView grdReport;

    /**
     * Method called on init of the form. This dynamically adds columns to the TableView
     * for the report being generated
     * @param columns List of Strings, column headers as they should appear on the report
     */
    public void init(List<String> columns){
        for (String column : columns){
            TableColumn col = new TableColumn(column);
            col.setCellValueFactory(new PropertyValueFactory<>(column.replace("# of ", "").replace(' ', '_')));
            grdReport.getColumns().add(col);
        }
    }

    /**
     * Method to generate the report of event counts by client and type
     */
    public void buildEventReport(){
        try{
            List<String> customers = new ArrayList<>();
            Connection conn = ConnectionManager.GetConnection();
            if (conn != null){
                String query = "CALL CustomerInfoGet()";
                CallableStatement stmt = conn.prepareCall(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()){
                    customers.add(rs.getString("Customer_Name"));
                }

                if (customers.size() > 0){
                    List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
                    HashMap<String, String> types = new HashMap<>();
                    query = "CALL AppointmentTypeGet()";
                    stmt = conn.prepareCall(query);
                    rs = stmt.executeQuery();
                    while (rs.next()){
                        types.put(rs.getString("Customer_Name"), rs.getString("Type"));
                    }

                    if (types.size() > 0){
                        List<String> customerTypes;
                        for (String customer : customers){
                            /** discussion of lambda */
                            customerTypes = types.entrySet().stream().filter(x -> x.getKey().equals(customer)).map(Map.Entry::getValue).collect(Collectors.toList());
                            if (customerTypes.size() > 0){
                                for (int i = 0; i < months.size(); i++){
                                    for (String type : customerTypes){
                                        int count = 0;
                                        query = "CALL AppointmentByCustomerMonthTypeGet(?, ?, ?)";
                                        stmt = conn.prepareCall(query);
                                        stmt.setString(1, customer);
                                        stmt.setInt(2, i + 1);
                                        stmt.setString(3, type);
                                        rs = stmt.executeQuery();
                                        while (rs.next()){
                                            count = rs.getInt("Count");
                                        }

                                        grdReport.getItems().add(new Report(customer, months.get(i), type, Integer.toString(count)));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Method to generate the schedule of each employee
     */
    public void buildScheduleReport(){
        try{
            List<String> users = new ArrayList<>();
            Connection conn = ConnectionManager.GetConnection();
            if (conn != null){
                String query = "CALL AllUsersGet()";
                CallableStatement stmt = conn.prepareCall(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()){
                    users.add(rs.getString("User_Name"));
                }

                if (users.size() > 0){
                    for (String user : users){
                        int userId = GetUserId(user);
                        String query2 = "CALL GetAppointmentsByUser(?, ?)";
                        CallableStatement stmt2 = conn.prepareCall(query2);
                        stmt2.setInt(1, userId);
                        stmt2.setInt(2, 0);
                        ResultSet rs2 = stmt2.executeQuery();
                        while (rs2.next()){
                            Report report = new Report(
                                    user,
                                    rs2.getString("Appointment_ID"),
                                    rs2.getString("Title"),
                                    rs2.getString("Description"),
                                    rs2.getString("Start"),
                                    rs2.getString("End"),
                                    rs2.getString("Customer_ID")
                            );
                            grdReport.getItems().add(report);
                        }
                    }
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Helper method, returns a user id from a given username
     * @param user String username
     * @return int user id
     */
    private int GetUserId(String user){
        try{
            Connection conn = ConnectionManager.GetConnection();
            if (conn != null){
                String query = "CALL GetUserId(?)";
                CallableStatement stmt = conn.prepareCall(query);
                stmt.setString(1, user);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()){
                    return rs.getInt("User_ID");
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * Method that builds the contact directory
     */
    public void buildReport(){
        try {
            Connection conn = ConnectionManager.GetConnection();
            if (conn != null){
                String query = "CALL ContactsGet()";
                CallableStatement stmt = conn.prepareCall(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()){
                    Report report = new Report(
                            rs.getString("Contact_ID"),
                            rs.getString("Contact_Name"),
                            rs.getString("Email")
                    );
                    grdReport.getItems().add(report);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
