package Forms;

import Libraries.ConnectionManager;
import Libraries.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.Console;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class CustomerController {
    @FXML TableView<Customer> grdCustomers = new TableView<>();
    ObservableList<Customer> customers = FXCollections.observableArrayList();
    ObservableList<String> countries = FXCollections.observableArrayList();
    ObservableList<String> divisions = FXCollections.observableArrayList();

    @FXML TextField txtCustomerID;
    @FXML TextField txtName;
    @FXML TextField txtPhone;
    @FXML TextField txtAddress;
    @FXML TextField txtZip;
    @FXML ComboBox<String> cboFirstLevelDivision;
    @FXML ComboBox<String> cboCountry;

    public void init(){
        try{
            ResultSet rs;
            String query;
            CallableStatement statement;
            Connection connection = ConnectionManager.GetConnection();
            if (connection != null){
                query = "CALL CountriesGet()";
                statement = connection.prepareCall(query);
                rs = statement.executeQuery();
                while (rs.next()){
                    countries.add(rs.getString("Country"));
                }

                query = "CALL DivisionGet()";
                statement = connection.prepareCall(query);
                rs = statement.executeQuery();
                while (rs.next()){
                    divisions.add(rs.getString("Division"));
                }

                query = "CALL CustomerInfoGet()";
                statement = connection.prepareCall(query);
                rs = statement.executeQuery();
                while (rs.next()){
                    Customer customer = new Customer();
                    customer.setCustomerId(rs.getInt("Customer_ID"));
                    customer.setName(rs.getString("Customer_Name"));
                    customer.setAddress(rs.getString("Address"));
                    customer.setZip(rs.getInt("Postal_Code"));
                    customer.setPhone(rs.getString("Phone"));
                    customer.setDivisionId(rs.getInt("Division_ID"));
                    customers.add(customer);
                }

                connection.close();
            } else {
                throw new Exception("Error establishing database connection");
            }

            grdCustomers.setItems(customers);
            cboCountry.setItems(countries);
            cboFirstLevelDivision.setItems(divisions);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void btnAdd_Click(ActionEvent actionEvent) {

    }

    public void btnSave_Click(ActionEvent actionEvent) {
    }

    public void btnDelete_Click(ActionEvent actionEvent) {
    }
}
