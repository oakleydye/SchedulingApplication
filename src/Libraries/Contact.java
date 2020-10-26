package Libraries;

/**
 * @author oakleydye
 *
 * Object class, represents entries in the contacts table
 * All methods are getters and setters for private class members
 */
public class Contact {
    private int ContactId;
    private String name;
    private String email;

    public int getContactId() {
        return ContactId;
    }

    public void setContactId(int contactId) {
        ContactId = contactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
