package Contact;

/**
 * Contact class represents a contact with validation for all fields.
 * All fields are required and have specific constraints.
 */
public class Contact {
    private final String contactId;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;

    /**
     * Constructor for Contact object with full validation.
     * 
     * @param contactId Unique identifier, max 10 characters, not null, not updatable
     * @param firstName First name, max 10 characters, not null
     * @param lastName Last name, max 10 characters, not null
     * @param phone Phone number, exactly 10 digits, not null
     * @param address Address, max 30 characters, not null
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Contact(String contactId, String firstName, String lastName, String phone, String address) {
        // Validate and set contactId (immutable field)
        if (contactId == null) {
            throw new IllegalArgumentException("Contact ID cannot be null");
        }
        if (contactId.isEmpty()) {
            throw new IllegalArgumentException("Contact ID cannot be empty");
        }
        if (contactId.length() > 10) {
            throw new IllegalArgumentException("Contact ID cannot exceed 10 characters");
        }
        this.contactId = contactId;

        // Validate and set other fields using setters for consistency
        setFirstName(firstName);
        setLastName(lastName);
        setPhone(phone);
        setAddress(address);
    }

    /**
     * Gets the contact ID.
     * 
     * @return the unique contact ID
     */
    public String getContactId() {
        return contactId;
    }

    /**
     * Gets the first name.
     * 
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name with validation.
     * 
     * @param firstName the first name to set, max 10 characters, not null
     * @throws IllegalArgumentException if firstName is invalid
     */
    public void setFirstName(String firstName) {
        if (firstName == null) {
            throw new IllegalArgumentException("First name cannot be null");
        }
        if (firstName.isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (firstName.length() > 10) {
            throw new IllegalArgumentException("First name cannot exceed 10 characters");
        }
        this.firstName = firstName;
    }

    /**
     * Gets the last name.
     * 
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name with validation.
     * 
     * @param lastName the last name to set, max 10 characters, not null
     * @throws IllegalArgumentException if lastName is invalid
     */
    public void setLastName(String lastName) {
        if (lastName == null) {
            throw new IllegalArgumentException("Last name cannot be null");
        }
        if (lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        if (lastName.length() > 10) {
            throw new IllegalArgumentException("Last name cannot exceed 10 characters");
        }
        this.lastName = lastName;
    }

    /**
     * Gets the phone number.
     * 
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number with validation.
     * 
     * @param phone the phone number to set, exactly 10 digits, not null
     * @throws IllegalArgumentException if phone is invalid
     */
    public void setPhone(String phone) {
        if (phone == null) {
            throw new IllegalArgumentException("Phone number cannot be null");
        }
        if (phone.isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }
        if (phone.length() != 10) {
            throw new IllegalArgumentException("Phone number must be exactly 10 digits");
        }
        if (!phone.matches("\\d+")) {
            throw new IllegalArgumentException("Phone number must contain only digits");
        }
        this.phone = phone;
    }

    /**
     * Gets the address.
     * 
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address with validation.
     * 
     * @param address the address to set, max 30 characters, not null
     * @throws IllegalArgumentException if address is invalid
     */
    public void setAddress(String address) {
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        if (address.isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }
        if (address.length() > 30) {
            throw new IllegalArgumentException("Address cannot exceed 30 characters");
        }
        this.address = address;
    }
}