package Forms;

import Libraries.ConnectionManager;
import Libraries.Customer;
import Libraries.TranslationManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Locale;

/**
 * @author oakleydye
 *
 * Controller for frmCustomer.fxml
 */
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
    @FXML ComboBox<String> cboFirstLevelDivision = new ComboBox<>();
    @FXML ComboBox<String> cboCountry = new ComboBox<>();

    @FXML TableColumn colId;
    @FXML TableColumn colName;
    @FXML TableColumn colPhone;
    @FXML TableColumn colAddress;
    @FXML TableColumn colZip;
    @FXML TableColumn colDivision;
    @FXML Label lblId;
    @FXML Label lblName;
    @FXML Label lblPhone;
    @FXML Label lblAddress;
    @FXML Label lblZip;
    @FXML Label lblDivision;
    @FXML Label lblCountry;
    @FXML Button btnAdd;
    @FXML Button btnSave;
    @FXML Button btnDelete;

    /**
     * Method called on startup of frmCustomer.fxml
     * Handles binding of objects to comboboxes and translation of the page
     */
    public void init() {
        try {
            cboCountry.setItems(countries);
            cboFirstLevelDivision.setItems(divisions);
            grdCustomers.setItems(customers);

            GetAllCountries();
            GetAllDivisions();
            GetAllCustomers();

            if (!Locale.getDefault().getLanguage().equals("en")){
                colId.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), colId.getText()));
                colName.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), colName.getText()));
                colPhone.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), colPhone.getText()));
                colAddress.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), colAddress.getText()));
                colZip.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), colZip.getText()));
                colDivision.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), colDivision.getText()));
                lblId.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), lblId.getText()));
                lblName.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), lblName.getText()));
                lblPhone.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), lblPhone.getText()));
                lblAddress.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), lblAddress.getText()));
                lblZip.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), lblZip.getText()));
                lblDivision.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), lblDivision.getText()));
                lblCountry.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), lblCountry.getText()));
                btnAdd.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), btnAdd.getText()));
                btnSave.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), btnSave.getText()));
                btnDelete.setText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), btnDelete.getText()));
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Helper method to get all countries from the database
     */
    private void GetAllCountries() {
        try {
            Connection connection = ConnectionManager.GetConnection();
            if (connection != null) {
                String query = "CALL CountriesGet()";
                CallableStatement statement = connection.prepareCall(query);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    countries.add(rs.getString("Country"));
                }
                connection.close();
            } else {
                throw new Exception("Error establishing database connection");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Helper method to get all first level divisions from the database
     */
    private void GetAllDivisions() {
        try{
            Connection conn = ConnectionManager.GetConnection();
            if (conn != null){
                String query = "CALL DivisionGet()";
                CallableStatement statement = conn.prepareCall(query);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    divisions.add(rs.getString("Division"));
                }
                conn.close();
            } else {
                throw new Exception("Error establishing database connection");
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Helper method to get all customers from the database
     */
    private void GetAllCustomers(){
        try{
            Connection conn = ConnectionManager.GetConnection();
            if (conn != null){
                String query = "CALL CustomerInfoGet()";
                CallableStatement stmt = conn.prepareCall(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Customer customer = new Customer();
                    customer.setCustomerId(rs.getInt("Customer_ID"));
                    customer.setName(rs.getString("Customer_Name"));
                    customer.setAddress(rs.getString("Address"));
                    customer.setZip(rs.getInt("Postal_Code"));
                    customer.setPhone(rs.getString("Phone"));
                    customer.setDivisionId(rs.getInt("Division_ID"));
                    customers.add(customer);
                }
                conn.close();
            } else {
                throw new Exception("Error establishing database connection");
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Event handler, clears all text fields to allow for creation of a new customer
     * @param actionEvent
     */
    public void btnAdd_Click(ActionEvent actionEvent) {
        txtCustomerID.clear();
        txtName.clear();
        txtPhone.clear();
        txtAddress.clear();
        txtZip.clear();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("To create a new customer, please fill out the form on the right of the screen and click Save.");
        alert.showAndWait();
    }

    /**
     * Event handler, updates and creates records in the database for customers
     * @param actionEvent
     */
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
                    stmt.setInt(6, GetDivisionIdFromDivision(cboFirstLevelDivision.getSelectionModel().getSelectedItem()));
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
                    stmt.setInt(7, GetDivisionIdFromDivision(cboFirstLevelDivision.getSelectionModel().getSelectedItem()));
                    stmt.executeQuery();
                }
                conn.close();
            }
            GetAllCustomers();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Event handler, deletes selected customer from the database
     * @param actionEvent
     */
    public void btnDelete_Click(ActionEvent actionEvent) {
        try{
            Connection conn = ConnectionManager.GetConnection();
            if (conn != null){
                String query = "CALL CustomerDelete(?)";
                CallableStatement stmt = conn.prepareCall(query);
                stmt.setString(1, txtCustomerID.getText());
                stmt.executeQuery();
                conn.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("");
                alert.setContentText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), "Customer successfully deleted"));
                alert.setTitle("Success!");
                alert.showAndWait();
            }
            GetAllCustomers();
        }
        catch (Exception ex){
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("");
            alert.setContentText(TranslationManager.translate("en", Locale.getDefault().getLanguage(), "Error deleting customer:\r\n" + ex.getMessage()));
            alert.showAndWait();
        }
    }

    /**
     * Event handler, populates selected row into the text fields to allow for editing
     * @param mouseEvent
     *
     * discussion on lambda
     *
     * The following block of code utilizes lambda to
     * select the customer's region and country
     * from the options in the combobox
     */
    public void grdCustomers_Click(MouseEvent mouseEvent) {
        Customer selectedCustomer = grdCustomers.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null){
            txtCustomerID.setText(Integer.toString(selectedCustomer.getCustomerId()));
            txtName.setText(selectedCustomer.getName());
            txtPhone.setText(selectedCustomer.getPhone());
            txtAddress.setText(selectedCustomer.getAddress());
            txtZip.setText(Integer.toString(selectedCustomer.getZip()));
            String customerCountry = GetCountryFromDivisionId(selectedCustomer.getDivisionId());
            String currentCountry = cboCountry.getItems().stream().filter(x -> x.equals(customerCountry)).findFirst().orElse("");
            cboCountry.getSelectionModel().select(currentCountry);
            String customerDivision = GetDivisionFromDivisionId(selectedCustomer.getDivisionId());
            String currentDivision = cboFirstLevelDivision.getItems().stream().filter(x -> x.equals(customerDivision)).findFirst().orElse("");
            cboFirstLevelDivision.getSelectionModel().select(currentDivision);
        }
    }

    /**
     * Helper method to get a country for a given division
     * @param divisionId int, division of the needed country
     * @return String, country name
     */
    private String GetCountryFromDivisionId(int divisionId){
        try{
            Connection conn = ConnectionManager.GetConnection();
            if (conn != null){
                String query = "CALL CountryByDivisionIdGet(?)";
                CallableStatement stmt = conn.prepareCall(query);
                stmt.setInt(1, divisionId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()){
                    return rs.getString("Country");
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * Helper method, gets the name of a division by the id
     * @param divisionId int division id
     * @return String, name of the division
     */
    private String GetDivisionFromDivisionId(int divisionId){
        try{
            Connection conn = ConnectionManager.GetConnection();
            if (conn != null){
                String query = "CALL DivisionByDivisionIdGet(?)";
                CallableStatement stmt = conn.prepareCall(query);
                stmt.setInt(1, divisionId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()){
                    return rs.getString("Division");
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * Helper method, gets a division id from the division name
     * @param division String, name of the division
     * @return integer
     */
    private int GetDivisionIdFromDivision(String division){
        try{
            Connection conn = ConnectionManager.GetConnection();
            if (conn != null){
                String query = "CALL DivisionIdByDivisionGet(?)";
                CallableStatement stmt = conn.prepareCall(query);
                stmt.setString(1, division);
                ResultSet rs = stmt.executeQuery();
                while(rs.next()){
                    return rs.getInt("Division_ID");
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * Event handler, allows for filtering of divisions based on selected country
     * @param actionEvent
     */
    public void cboCountry_SelectionChanged(ActionEvent actionEvent) {
        String currentCountry = cboCountry.getSelectionModel().getSelectedItem();
        if (!currentCountry.equals("")){
            try{
                Connection conn = ConnectionManager.GetConnection();
                if (conn != null){
                    String query = "CALL DivisionByCountryGet(?)";
                    CallableStatement stmt = conn.prepareCall(query);
                    stmt.setString(1, cboCountry.getSelectionModel().getSelectedItem());
                    ResultSet rs = stmt.executeQuery();
                    divisions = null;
                    divisions = FXCollections.observableArrayList();
                    while (rs.next()){
                        divisions.add(rs.getString("Division"));
                    }
                    cboFirstLevelDivision.setItems(divisions);
                    conn.close();
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
