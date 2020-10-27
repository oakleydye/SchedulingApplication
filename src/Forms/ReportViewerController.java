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

public class ReportViewerController {
    @FXML TableView grdReport;

    public void init(List<String> columns){
        for (String column : columns){
            TableColumn col = new TableColumn(column);
            col.setCellValueFactory(new PropertyValueFactory<>(column.replace("# of ", "").replace(' ', '_')));
            grdReport.getColumns().add(col);
        }
    }

    public void buildEventReport(){
        try{
            List<String> customers = new ArrayList<String>();
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
                        List<String> customerTypes = new ArrayList<>();
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

    public void buildScheduleReport(){

    }

    public void buildReport(){

    }
}
