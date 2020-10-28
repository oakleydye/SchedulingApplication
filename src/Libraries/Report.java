package Libraries;

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

    public Report(String cust, String month, String type, String appts){
        this.Customer = cust;
        this.Month = month;
        this.Type = type;
        this.Appointments = appts;
    }

    public Report(String username, String apptId, String title, String desc, String start, String end, String custId){
        this.UserName = username;
        this.Appointment_ID = apptId;
        this.Title = title;
        this.Description = desc;
        this.Start = start;
        this.End = end;
        this.Customer_ID = custId;
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
