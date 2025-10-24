package Contact;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.Objects;

/**
 * ContactService manages a collection of contacts with add, update, and delete operations.
 * Uses in-memory storage with unique contact IDs.
 * Thread-safe implementation with comprehensive error handling.
 */
public class ContactService {
    private final Map<String, Contact> contacts = new HashMap<>();
    private static final int MAX_CONTACTS = 10000;

    /**
     * Custom exception for contact-related errors.
     */
    public static class ContactServiceException extends RuntimeException {
        public ContactServiceException(String message) {
            super(message);
        }
        
        public ContactServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Exception thrown when a contact is not found.
     */
    public static class ContactNotFoundException extends ContactServiceException {
        public ContactNotFoundException(String contactId) {
            super("Contact not found with ID: " + contactId);
        }
    }

    /**
     * Exception thrown when a duplicate contact ID is detected.
     */
    public static class DuplicateContactException extends ContactServiceException {
        public DuplicateContactException(String contactId) {
            super("Contact ID already exists: " + contactId);
        }
    }

    /**
     * Exception thrown when service capacity is exceeded.
     */
    public static class ServiceCapacityException extends ContactServiceException {
        public ServiceCapacityException() {
            super("Maximum contact capacity of " + MAX_CONTACTS + " reached");
        }
    }

    /**
     * Adds a new contact to the service.
     * 
     * @param contact the contact to add, must not be null and must have unique ID
     * @throws IllegalArgumentException if contact is null or has invalid ID
     * @throws DuplicateContactException if contact ID already exists
     * @throws ServiceCapacityException if maximum capacity reached
     */
    public synchronized void addContact(Contact contact) {
        // Null check
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }

        // Validate contact ID
        String contactId = contact.getContactId();
        validateContactId(contactId, "add");

        // Check for duplicates
        if (contacts.containsKey(contactId)) {
            throw new DuplicateContactException(contactId);
        }

        // Check capacity
        if (contacts.size() >= MAX_CONTACTS) {
            throw new ServiceCapacityException();
        }

        // Validate contact data integrity
        validateContactData(contact);

        // Deep validation - ensure contact is fully valid
        try {
            contact.getFirstName();
            contact.getLastName();
            contact.getPhone();
            contact.getAddress();
        } catch (Exception e) {
            throw new IllegalArgumentException("Contact has invalid internal state: " + e.getMessage(), e);
        }

        contacts.put(contactId, contact);
    }

    /**
     * Deletes a contact by contact ID.
     * 
     * @param contactId the ID of the contact to delete
     * @throws IllegalArgumentException if contactId is invalid
     * @throws ContactNotFoundException if contact is not found
     */
    public synchronized void deleteContact(String contactId) {
        validateContactId(contactId, "delete");

        if (!contacts.containsKey(contactId)) {
            throw new ContactNotFoundException(contactId);
        }

        try {
            contacts.remove(contactId);
        } catch (Exception e) {
            throw new ContactServiceException("Failed to delete contact: " + e.getMessage(), e);
        }
    }

    /**
     * Updates the first name of a contact.
     * 
     * @param contactId the ID of the contact to update
     * @param firstName the new first name
     * @throws IllegalArgumentException if parameters are invalid
     * @throws ContactNotFoundException if contact is not found
     */
    public synchronized void updateFirstName(String contactId, String firstName) {
        validateContactId(contactId, "update first name");
        validateFieldValue(firstName, "First name");
        
        Contact contact = getContactOrThrow(contactId);
        
        String originalName = contact.getFirstName();
        try {
            contact.setFirstName(firstName);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to update first name: " + e.getMessage(), e);
        } catch (Exception e) {
            // Rollback on unexpected errors
            try {
                contact.setFirstName(originalName);
            } catch (Exception rollbackError) {
                // Log rollback failure in production
            }
            throw new ContactServiceException("Unexpected error updating first name", e);
        }
    }

    /**
     * Updates the last name of a contact.
     * 
     * @param contactId the ID of the contact to update
     * @param lastName the new last name
     * @throws IllegalArgumentException if parameters are invalid
     * @throws ContactNotFoundException if contact is not found
     */
    public synchronized void updateLastName(String contactId, String lastName) {
        validateContactId(contactId, "update last name");
        validateFieldValue(lastName, "Last name");
        
        Contact contact = getContactOrThrow(contactId);
        
        String originalName = contact.getLastName();
        try {
            contact.setLastName(lastName);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to update last name: " + e.getMessage(), e);
        } catch (Exception e) {
            // Rollback on unexpected errors
            try {
                contact.setLastName(originalName);
            } catch (Exception rollbackError) {
                // Log rollback failure in production
            }
            throw new ContactServiceException("Unexpected error updating last name", e);
        }
    }

    /**
     * Updates the phone number of a contact.
     * 
     * @param contactId the ID of the contact to update
     * @param phone the new phone number
     * @throws IllegalArgumentException if parameters are invalid
     * @throws ContactNotFoundException if contact is not found
     */
    public synchronized void updatePhone(String contactId, String phone) {
        validateContactId(contactId, "update phone");
        validateFieldValue(phone, "Phone");
        
        Contact contact = getContactOrThrow(contactId);
        
        String originalPhone = contact.getPhone();
        try {
            contact.setPhone(phone);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to update phone: " + e.getMessage(), e);
        } catch (Exception e) {
            // Rollback on unexpected errors
            try {
                contact.setPhone(originalPhone);
            } catch (Exception rollbackError) {
                // Log rollback failure in production
            }
            throw new ContactServiceException("Unexpected error updating phone", e);
        }
    }

    /**
     * Updates the address of a contact.
     * 
     * @param contactId the ID of the contact to update
     * @param address the new address
     * @throws IllegalArgumentException if parameters are invalid
     * @throws ContactNotFoundException if contact is not found
     */
    public synchronized void updateAddress(String contactId, String address) {
        validateContactId(contactId, "update address");
        validateFieldValue(address, "Address");
        
        Contact contact = getContactOrThrow(contactId);
        
        String originalAddress = contact.getAddress();
        try {
            contact.setAddress(address);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to update address: " + e.getMessage(), e);
        } catch (Exception e) {
            // Rollback on unexpected errors
            try {
                contact.setAddress(originalAddress);
            } catch (Exception rollbackError) {
                // Log rollback failure in production
            }
            throw new ContactServiceException("Unexpected error updating address", e);
        }
    }

    /**
     * Updates multiple fields of a contact atomically.
     * Either all updates succeed or none do.
     * 
     * @param contactId the ID of the contact to update
     * @param firstName new first name (null to skip)
     * @param lastName new last name (null to skip)
     * @param phone new phone (null to skip)
     * @param address new address (null to skip)
     * @throws ContactNotFoundException if contact is not found
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public synchronized void updateContact(String contactId, String firstName, 
                                         String lastName, String phone, String address) {
        validateContactId(contactId, "update contact");
        Contact contact = getContactOrThrow(contactId);

        // Store original values for rollback
        String origFirst = contact.getFirstName();
        String origLast = contact.getLastName();
        String origPhone = contact.getPhone();
        String origAddr = contact.getAddress();

        try {
            if (firstName != null) {
                validateFieldValue(firstName, "First name");
                contact.setFirstName(firstName);
            }
            if (lastName != null) {
                validateFieldValue(lastName, "Last name");
                contact.setLastName(lastName);
            }
            if (phone != null) {
                validateFieldValue(phone, "Phone");
                contact.setPhone(phone);
            }
            if (address != null) {
                validateFieldValue(address, "Address");
                contact.setAddress(address);
            }
        } catch (Exception e) {
            // Rollback all changes
            try {
                contact.setFirstName(origFirst);
                contact.setLastName(origLast);
                contact.setPhone(origPhone);
                contact.setAddress(origAddr);
            } catch (Exception rollbackError) {
                throw new ContactServiceException("Critical error: failed to rollback changes", rollbackError);
            }
            throw new ContactServiceException("Failed to update contact: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a contact by ID.
     * 
     * @param contactId the ID of the contact to retrieve
     * @return the contact with the specified ID, or null if not found
     */
    public synchronized Contact getContact(String contactId) {
        if (contactId == null || contactId.trim().isEmpty()) {
            return null;
        }
        
        try {
            return contacts.get(contactId);
        } catch (Exception e) {
            // Handle any unexpected errors during retrieval
            throw new ContactServiceException("Error retrieving contact: " + e.getMessage(), e);
        }
    }

    /**
     * Safely retrieves a contact by ID with exception on failure.
     * 
     * @param contactId the ID of the contact to retrieve
     * @return the contact with the specified ID
     * @throws ContactNotFoundException if contact is not found
     */
    public synchronized Contact getContactSafe(String contactId) {
        validateContactId(contactId, "retrieve");
        return getContactOrThrow(contactId);
    }

    /**
     * Checks if a contact exists with the given ID.
     * 
     * @param contactId the ID to check
     * @return true if a contact with this ID exists, false otherwise
     */
    public synchronized boolean containsContact(String contactId) {
        if (contactId == null || contactId.trim().isEmpty()) {
            return false;
        }
        
        try {
            return contacts.containsKey(contactId);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets all contacts in the service.
     * 
     * @return an unmodifiable collection of all contacts
     */
    public synchronized Collection<Contact> getAllContacts() {
        try {
            return Collections.unmodifiableCollection(contacts.values());
        } catch (Exception e) {
            throw new ContactServiceException("Error retrieving contacts", e);
        }
    }

    /**
     * Gets all contact IDs in the service.
     * 
     * @return an unmodifiable set of all contact IDs
     */
    public synchronized Set<String> getAllContactIds() {
        try {
            return Collections.unmodifiableSet(contacts.keySet());
        } catch (Exception e) {
            throw new ContactServiceException("Error retrieving contact IDs", e);
        }
    }

    /**
     * Gets the total number of contacts in the service.
     * 
     * @return the number of contacts
     */
    public synchronized int getContactCount() {
        return contacts.size();
    }

    /**
     * Gets the remaining capacity before max is reached.
     * 
     * @return number of additional contacts that can be added
     */
    public synchronized int getRemainingCapacity() {
        return MAX_CONTACTS - contacts.size();
    }

    /**
     * Checks if the service has no contacts.
     * 
     * @return true if there are no contacts, false otherwise
     */
    public synchronized boolean isEmpty() {
        return contacts.isEmpty();
    }

    /**
     * Checks if the service is at maximum capacity.
     * 
     * @return true if at max capacity, false otherwise
     */
    public synchronized boolean isFull() {
        return contacts.size() >= MAX_CONTACTS;
    }

    /**
     * Clears all contacts from the service.
     * Useful for testing purposes.
     */
    public synchronized void clearAllContacts() {
        try {
            contacts.clear();
        } catch (Exception e) {
            throw new ContactServiceException("Error clearing contacts", e);
        }
    }

    /**
     * Validates that a contact ID meets all requirements.
     * 
     * @param contactId the ID to validate
     * @param operation the operation being performed (for error messages)
     * @throws IllegalArgumentException if contactId is invalid
     */
    private void validateContactId(String contactId, String operation) {
        if (contactId == null) {
            throw new IllegalArgumentException(
                "Contact ID cannot be null for operation: " + operation);
        }
        
        if (contactId.isEmpty()) {
            throw new IllegalArgumentException(
                "Contact ID cannot be empty for operation: " + operation);
        }
        
        if (contactId.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Contact ID cannot be whitespace only for operation: " + operation);
        }
        
        if (!contactId.equals(contactId.trim())) {
            throw new IllegalArgumentException(
                "Contact ID cannot have leading or trailing whitespace for operation: " + operation);
        }
        
        if (contactId.length() > 100) {
            throw new IllegalArgumentException(
                "Contact ID is too long (max 100 characters) for operation: " + operation);
        }
    }

    /**
     * Validates a field value is not null or whitespace only.
     * 
     * @param value the value to validate
     * @param fieldName the name of the field (for error messages)
     * @throws IllegalArgumentException if value is invalid
     */
    private void validateFieldValue(String value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
        
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty or whitespace only");
        }
    }

    /**
     * Validates the integrity of contact data.
     * 
     * @param contact the contact to validate
     * @throws IllegalArgumentException if contact data is invalid
     */
    private void validateContactData(Contact contact) {
        Objects.requireNonNull(contact, "Contact cannot be null");
        Objects.requireNonNull(contact.getContactId(), "Contact ID cannot be null");
        
        // Additional validation can be added here based on business rules
        if (contact.getContactId().trim().isEmpty()) {
            throw new IllegalArgumentException("Contact ID cannot be empty");
        }
    }

    /**
     * Retrieves a contact or throws an exception if not found.
     * 
     * @param contactId the ID of the contact to retrieve
     * @return the contact with the specified ID
     * @throws ContactNotFoundException if contact is not found
     */
    private Contact getContactOrThrow(String contactId) {
        Contact contact = contacts.get(contactId);
        if (contact == null) {
            throw new ContactNotFoundException(contactId);
        }
        return contact;
    }
}