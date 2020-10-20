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
import javafx.scene.input.MouseEvent;

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
        txtCustomerID.clear();
        txtName.clear();
        txtPhone.clear();
        txtAddress.clear();
        txtZip.clear();
    }

    public void btnSave_Click(ActionEvent actionEvent) {
        try{
            Connection conn = ConnectionManager.GetConnection();
            if (conn != null){
                if (txtCustomerID.getText().equals("")){ //call insert proc
                    String query = "CALL CustomerInsert(?,?,?,?,?,?)";
                    CallableStatement stmt = conn.prepareCall(query);
                    stmt.setString(1, txtName.getText());
                    stmt.setString(2, txtAddress.getText());
                    stmt.setString(3, txtZip.getText());
                    stmt.setString(4, txtPhone.getText());
                    stmt.setString(5, LoginController.currentUser);
                    stmt.setString(6, cboFirstLevelDivision.getSelectionModel().getSelectedItem());
                    stmt.executeQuery();
                } else { //Call update proc here
                    String query = "CALL CustomerUpdate(?,?,?,?,?,?,?)";
                    CallableStatement stmt = conn.prepareCall(query);
                    stmt.setString(1, txtCustomerID.getText());
                    stmt.setString(2, txtName.getText());
                    stmt.setString(3, txtAddress.getText());
                    stmt.setString(4, txtZip.getText());
                    stmt.setString(5, txtPhone.getText());
                    stmt.setString(6, LoginController.currentUser);
                    stmt.setString(7, cboFirstLevelDivision.getSelectionModel().getSelectedItem());
                    stmt.executeQuery();
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void btnDelete_Click(ActionEvent actionEvent) {
        try{
            Connection conn = ConnectionManager.GetConnection();
            if (conn != null){
                String query = "CALL CustomerDelete(?)";
                CallableStatement stmt = conn.prepareCall(query);
                stmt.setString(1, txtCustomerID.getText());
                stmt.executeQuery();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void grdCustomers_Click(MouseEvent mouseEvent) {
        Customer selectedCustomer = grdCustomers.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null){
            txtCustomerID.setText(Integer.toString(selectedCustomer.getCustomerId()));
            txtName.setText(selectedCustomer.getName());
            txtPhone.setText(selectedCustomer.getPhone());
            txtAddress.setText(selectedCustomer.getAddress());
            txtZip.setText(Integer.toString(selectedCustomer.getZip()));
            cboCountry.getSelectionModel().select(1); //// TODO: 10/19/20 fix this to make the division name display
            cboFirstLevelDivision.getSelectionModel().select(1); //// TODO: 10/19/20 fix this one as well
        }
    }
}
