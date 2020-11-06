package Libraries;

/**
 * @author oakleydye
 *
 * Helper class, represents a report as displayed in the application
 */
public class Report {
    private String Customer;
    private String Month;
    private String Type;
    private String Appointments;
    private String Appointment_ID;
    private String Title;
    private String Description;
    private String Start;
    private String End;
    private String Customer_ID;
    private String UserName;
    private String Contact_ID;
    private String Contact_Name;
    private String Email;

    public Report(String cust, String month, String type, String appts){
        this.Customer = cust;
        this.Month = month;
        this.Type = type;
        this.Appointments = appts;
    }

    public Report(String username, String apptId, String title, String type, String desc, String start, String end, String custId){
        this.UserName = username;
        this.Appointment_ID = apptId;
        this.Title = title;
        this.Type = type;
        this.Description = desc;
        this.Start = start;
        this.End = end;
        this.Customer_ID = custId;
    }

    public Report(String contactId, String contactName, String email){
        this.Contact_ID = contactId;
        this.Contact_Name = contactName;
        this.Email = email;
    }

    public String getContact_ID() {
        return Contact_ID;
    }

    public void setContact_ID(String contact_ID) {
        Contact_ID = contact_ID;
    }

    public String getContact_Name() {
        return Contact_Name;
    }

    public void setContact_Name(String contact_Name) {
        Contact_Name = contact_Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getCustomer() {
        return Customer;
    }

    public void setCustomer(String customer) {
        Customer = customer;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getAppointments() {
        return Appointments;
    }

    public void setAppointments(String appointments) {
        Appointments = appointments;
    }

    public String getAppointment_ID() {
        return Appointment_ID;
    }

    public void setAppointment_ID(String appointment_ID) {
        Appointment_ID = appointment_ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getStart() {
        return Start;
    }

    public void setStart(String start) {
        Start = start;
    }

    public String getEnd() {
        return End;
    }

    public void setEnd(String end) {
        End = end;
    }

    public String getCustomer_ID() {
        return Customer_ID;
    }

    public void setCustomer_ID(String customer_ID) {
        Customer_ID = customer_ID;
    }
}
