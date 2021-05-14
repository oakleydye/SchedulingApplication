package Forms;

import Libraries.ConnectionManager;
import Libraries.LocationManager;
import Libraries.Report;
import Libraries.TranslationManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author oakleydye
 *
 * FXML Controller for ReportViewer.fxml
 */
public class ReportViewerController {
    @FXML TableView grdReport;
    @FXML Button btnModUser;
    @FXML Button btnDeleteUser;

    /**
     * Method called on init of the form. This dynamically adds columns to the TableView
     * for the report being generated
     * @param columns List of Strings, column headers as they should appear on the report
     */
    public void init(List<String> columns){
        for (String column : columns){
            String translatedCol = "";
            if (!Locale.getDefault().getLanguage().equals("en")){
                translatedCol = TranslationManager.translate("en", Locale.getDefault().getLanguage(), column);
            }
            TableColumn col = new TableColumn(Locale.getDefault().getLanguage().equals("en") ? column : translatedCol);
            col.setCellValueFactory(new PropertyValueFactory<>(column.replace("# of ", "").replace(' ', '_')));
            grdReport.getColumns().add(col);
        }
    }

    public void init(List<String> columns, boolean userReport){
        for (String column : columns){
            String translatedCol = "";
            if (!Locale.getDefault().getLanguage().equals("en")){
                translatedCol = TranslationManager.translate("en", Locale.getDefault().getLanguage(), column);
            }
            TableColumn col = new TableColumn(Locale.getDefault().getLanguage().equals("en") ? column : translatedCol);
            col.setCellValueFactory(new PropertyValueFactory<>(column.replace("# of ", "").replace(' ', '_')));
            grdReport.getColumns().add(col);
        }
        if (userReport){
            btnModUser.setVisible(true);
            btnDeleteUser.setVisible(true);
        }
    }

    public void btnModUser_Click(){
        try{
            Report report = (Report) grdReport.getSelectionModel().getSelectedItem();
            if (report != null){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("frmNewUser.fxml"));
                Parent root = loader.load();
                NewUserController controller = loader.getController();
                controller.init(report);
                Stage stage = new Stage();
                stage.setScene(new Scene(root, 1280, 800));
                stage.show();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void btnDeleteUser_Click(){
        try{
            Report report = (Report) grdReport.getSelectionModel().getSelectedItem();
            Connection conn = ConnectionManager.GetConnection();
            if (conn != null && report != null){
                String query = "CALL UserDelete(?)";
                CallableStatement stmt = conn.prepareCall(query);
                stmt.setString(1, report.getUserId());
                stmt.executeQuery();
                grdReport.refresh();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Method to generate the report of event counts by client and type
     *
     * discussion of lambda
     *
     * This line is used to get each type of appointment for a given customer
     * used to build the report
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
                    long setOffset = LocationManager.GetOffsetFromComputerSetting();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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
                                    rs2.getString("Type"),
                                    rs2.getString("Description"),
                                    LocalDateTime.parse(rs2.getString("Start"), formatter).plus(setOffset, ChronoField.MILLI_OF_DAY.getBaseUnit()).toString(),
                                    LocalDateTime.parse(rs2.getString("End"), formatter).plus(setOffset, ChronoField.MILLI_OF_DAY.getBaseUnit()).toString(),
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

    public void buildUserReport(){
        try{
            Connection conn = ConnectionManager.GetConnection();
            if (conn != null){
                String query = "CALL UsersGet()";
                CallableStatement stmt = conn.prepareCall(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()){
                    Report report = new Report(
                            rs.getInt("User_ID"),
                            rs.getString("User_Name"),
                            rs.getString("Password")
                    );
                    grdReport.getItems().add(report);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
