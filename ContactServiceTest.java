package Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import Contact.Contact;
import Contact.ContactService;
import Contact.ContactService.ContactServiceException;
import Contact.ContactService.ContactNotFoundException;
import Contact.ContactService.DuplicateContactException;
import Contact.ContactService.ServiceCapacityException;

import java.util.Collection;
import java.util.Set;

public class ContactServiceTest {

    private ContactService contactService;

    @BeforeEach
    void setUp() {
        contactService = new ContactService();
    }

    // ==================== ADD CONTACT TESTS ====================
    
    @Test
    @DisplayName("Test adding a valid contact")
    void testAddValidContact() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        assertNotNull(contactService.getContact("12345"));
        assertEquals("John", contactService.getContact("12345").getFirstName());
    }

    @Test
    @DisplayName("Test adding contact with duplicate ID throws DuplicateContactException")
    void testAddDuplicateContactId() {
        Contact contact1 = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        Contact contact2 = new Contact("12345", "Jane", "Smith", "0987654321", "456 Oak Ave");
        
        contactService.addContact(contact1);
        assertThrows(DuplicateContactException.class, () -> {
            contactService.addContact(contact2);
        });
    }

    @Test
    @DisplayName("Test adding null contact throws exception")
    void testAddNullContact() {
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.addContact(null);
        });
    }

    @Test
    @DisplayName("Test adding contact with null ID throws exception")
    void testAddContactWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact(null, "John", "Doe", "1234567890", "123 Main St");
        });
    }

    @Test
    @DisplayName("Test adding contact with ID longer than 10 characters")
    void testAddContactWithLongId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("12345678901", "John", "Doe", "1234567890", "123 Main St");
        });
    }

    @Test
    @DisplayName("Test adding contact with ID exactly 10 characters - boundary test")
    void testAddContactWithMaxLengthId() {
        Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        assertNotNull(contactService.getContact("1234567890"));
    }

    @Test
    @DisplayName("Test adding contact with whitespace-only ID throws exception")
    void testAddContactWithWhitespaceOnlyId() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        // Now try to add contact with whitespace-only ID via direct service validation
        assertThrows(IllegalArgumentException.class, () -> {
            Contact badContact = new Contact("   ", "Jane", "Smith", "0987654321", "456 Oak Ave");
            contactService.addContact(badContact);
        });
    }

    @Test
    @DisplayName("Test adding contact with leading/trailing whitespace in ID throws exception")
    void testAddContactWithWhitespaceInId() {
        assertThrows(IllegalArgumentException.class, () -> {
            Contact contact = new Contact(" 12345 ", "John", "Doe", "1234567890", "123 Main St");
            contactService.addContact(contact);
        });
    }

    @Test
    @DisplayName("Test adding contact with ID over 100 characters throws exception")
    void testAddContactWithVeryLongId() {
        String longId = "a".repeat(101);
        assertThrows(IllegalArgumentException.class, () -> {
            Contact contact = new Contact(longId, "John", "Doe", "1234567890", "123 Main St");
            contactService.addContact(contact);
        });
    }

    // ==================== DELETE CONTACT TESTS ====================

    @Test
    @DisplayName("Test deleting an existing contact")
    void testDeleteExistingContact() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        contactService.deleteContact("12345");
        assertNull(contactService.getContact("12345"));
    }

    @Test
    @DisplayName("Test deleting non-existent contact throws ContactNotFoundException")
    void testDeleteNonExistentContact() {
        assertThrows(ContactNotFoundException.class, () -> {
            contactService.deleteContact("99999");
        });
    }

    @Test
    @DisplayName("Test deleting contact with null ID throws exception")
    void testDeleteContactWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.deleteContact(null);
        });
    }

    @Test
    @DisplayName("Test deleting contact with empty ID throws exception")
    void testDeleteContactWithEmptyId() {
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.deleteContact("");
        });
    }

    @Test
    @DisplayName("Test deleting contact with whitespace-only ID throws exception")
    void testDeleteContactWithWhitespaceId() {
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.deleteContact("   ");
        });
    }

    // ==================== UPDATE FIRST NAME TESTS ====================

    @Test
    @DisplayName("Test updating first name with valid value")
    void testUpdateFirstNameValid() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        contactService.updateFirstName("12345", "Jane");
        assertEquals("Jane", contactService.getContact("12345").getFirstName());
    }

    @Test
    @DisplayName("Test updating first name with null value throws exception")
    void testUpdateFirstNameNull() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updateFirstName("12345", null);
        });
    }

    @Test
    @DisplayName("Test updating first name longer than 10 characters throws exception")
    void testUpdateFirstNameTooLong() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updateFirstName("12345", "Christopher");
        });
    }

    @Test
    @DisplayName("Test updating first name with exactly 10 characters - boundary test")
    void testUpdateFirstNameMaxLength() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        contactService.updateFirstName("12345", "Johnathan1");
        assertEquals("Johnathan1", contactService.getContact("12345").getFirstName());
    }

    @Test
    @DisplayName("Test updating first name for non-existent contact throws ContactNotFoundException")
    void testUpdateFirstNameNonExistentContact() {
        assertThrows(ContactNotFoundException.class, () -> {
            contactService.updateFirstName("99999", "Jane");
        });
    }

    @Test
    @DisplayName("Test updating first name with empty string throws exception")
    void testUpdateFirstNameEmpty() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updateFirstName("12345", "");
        });
    }

    @Test
    @DisplayName("Test updating first name with whitespace-only string throws exception")
    void testUpdateFirstNameWhitespaceOnly() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updateFirstName("12345", "   ");
        });
    }

    // ==================== UPDATE LAST NAME TESTS ====================

    @Test
    @DisplayName("Test updating last name with valid value")
    void testUpdateLastNameValid() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        contactService.updateLastName("12345", "Smith");
        assertEquals("Smith", contactService.getContact("12345").getLastName());
    }

    @Test
    @DisplayName("Test updating last name with null value throws exception")
    void testUpdateLastNameNull() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updateLastName("12345", null);
        });
    }

    @Test
    @DisplayName("Test updating last name longer than 10 characters throws exception")
    void testUpdateLastNameTooLong() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updateLastName("12345", "Williamson");
        });
    }

    @Test
    @DisplayName("Test updating last name with exactly 10 characters - boundary test")
    void testUpdateLastNameMaxLength() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        contactService.updateLastName("12345", "Washington");
        assertEquals("Washington", contactService.getContact("12345").getLastName());
    }

    @Test
    @DisplayName("Test updating last name for non-existent contact throws ContactNotFoundException")
    void testUpdateLastNameNonExistentContact() {
        assertThrows(ContactNotFoundException.class, () -> {
            contactService.updateLastName("99999", "Smith");
        });
    }

    @Test
    @DisplayName("Test updating last name with empty string throws exception")
    void testUpdateLastNameEmpty() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updateLastName("12345", "");
        });
    }

    @Test
    @DisplayName("Test updating last name with whitespace-only string throws exception")
    void testUpdateLastNameWhitespaceOnly() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updateLastName("12345", "   ");
        });
    }

    // ==================== UPDATE PHONE TESTS ====================

    @Test
    @DisplayName("Test updating phone with valid 10-digit number")
    void testUpdatePhoneValid() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        contactService.updatePhone("12345", "0987654321");
        assertEquals("0987654321", contactService.getContact("12345").getPhone());
    }

    @Test
    @DisplayName("Test updating phone with null value throws exception")
    void testUpdatePhoneNull() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updatePhone("12345", null);
        });
    }

    @Test
    @DisplayName("Test updating phone with less than 10 digits throws exception")
    void testUpdatePhoneTooShort() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updatePhone("12345", "12345");
        });
    }

    @Test
    @DisplayName("Test updating phone with more than 10 digits throws exception")
    void testUpdatePhoneTooLong() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updatePhone("12345", "12345678901");
        });
    }

    @Test
    @DisplayName("Test updating phone with non-numeric characters throws exception")
    void testUpdatePhoneNonNumeric() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updatePhone("12345", "123abc7890");
        });
    }

    @Test
    @DisplayName("Test updating phone for non-existent contact throws ContactNotFoundException")
    void testUpdatePhoneNonExistentContact() {
        assertThrows(ContactNotFoundException.class, () -> {
            contactService.updatePhone("99999", "0987654321");
        });
    }

    @Test
    @DisplayName("Test updating phone with empty string throws exception")
    void testUpdatePhoneEmpty() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updatePhone("12345", "");
        });
    }

    @Test
    @DisplayName("Test updating phone with whitespace-only string throws exception")
    void testUpdatePhoneWhitespaceOnly() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updatePhone("12345", "   ");
        });
    }

    // ==================== UPDATE ADDRESS TESTS ====================

    @Test
    @DisplayName("Test updating address with valid value")
    void testUpdateAddressValid() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        contactService.updateAddress("12345", "456 Oak Avenue");
        assertEquals("456 Oak Avenue", contactService.getContact("12345").getAddress());
    }

    @Test
    @DisplayName("Test updating address with null value throws exception")
    void testUpdateAddressNull() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updateAddress("12345", null);
        });
    }

    @Test
    @DisplayName("Test updating address longer than 30 characters throws exception")
    void testUpdateAddressTooLong() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updateAddress("12345", "1234 Very Long Street Name That Exceeds Limit");
        });
    }

    @Test
    @DisplayName("Test updating address with exactly 30 characters - boundary test")
    void testUpdateAddressMaxLength() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        String maxAddress = "123456789012345678901234567890"; // Exactly 30 characters
        contactService.updateAddress("12345", maxAddress);
        assertEquals(maxAddress, contactService.getContact("12345").getAddress());
    }

    @Test
    @DisplayName("Test updating address for non-existent contact throws ContactNotFoundException")
    void testUpdateAddressNonExistentContact() {
        assertThrows(ContactNotFoundException.class, () -> {
            contactService.updateAddress("99999", "456 Oak Avenue");
        });
    }

    @Test
    @DisplayName("Test updating address with empty string throws exception")
    void testUpdateAddressEmpty() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updateAddress("12345", "");
        });
    }

    @Test
    @DisplayName("Test updating address with whitespace-only string throws exception")
    void testUpdateAddressWhitespaceOnly() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.updateAddress("12345", "   ");
        });
    }

    // ==================== ATOMIC UPDATE CONTACT TESTS ====================

    @Test
    @DisplayName("Test atomic update of multiple fields succeeds")
    void testUpdateContactMultipleFieldsSuccess() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        contactService.updateContact("12345", "Jane", "Smith", "0987654321", "456 Oak Ave");
        
        Contact updated = contactService.getContact("12345");
        assertEquals("Jane", updated.getFirstName());
        assertEquals("Smith", updated.getLastName());
        assertEquals("0987654321", updated.getPhone());
        assertEquals("456 Oak Ave", updated.getAddress());
    }

    @Test
    @DisplayName("Test atomic update with null values skips those fields")
    void testUpdateContactWithNullValues() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        contactService.updateContact("12345", "Jane", null, null, "456 Oak Ave");
        
        Contact updated = contactService.getContact("12345");
        assertEquals("Jane", updated.getFirstName());
        assertEquals("Doe", updated.getLastName()); // Unchanged
        assertEquals("1234567890", updated.getPhone()); // Unchanged
        assertEquals("456 Oak Ave", updated.getAddress());
    }

    @Test
    @DisplayName("Test atomic update for non-existent contact throws ContactNotFoundException")
    void testUpdateContactNonExistent() {
        assertThrows(ContactNotFoundException.class, () -> {
            contactService.updateContact("99999", "Jane", "Smith", "0987654321", "456 Oak Ave");
        });
    }

    @Test
    @DisplayName("Test atomic update with invalid field value throws exception and rolls back")
    void testUpdateContactRollbackOnFailure() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        // Try to update with an invalid phone number (should rollback all changes)
        assertThrows(ContactServiceException.class, () -> {
            contactService.updateContact("12345", "Jane", "Smith", "invalid", "456 Oak Ave");
        });
        
        // Verify original values are preserved
        Contact unchanged = contactService.getContact("12345");
        assertEquals("John", unchanged.getFirstName());
        assertEquals("Doe", unchanged.getLastName());
        assertEquals("1234567890", unchanged.getPhone());
        assertEquals("123 Main St", unchanged.getAddress());
    }

    // ==================== GET CONTACT SAFE TESTS ====================

    @Test
    @DisplayName("Test getContactSafe returns contact when exists")
    void testGetContactSafeExists() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        Contact retrieved = contactService.getContactSafe("12345");
        assertNotNull(retrieved);
        assertEquals("John", retrieved.getFirstName());
    }

    @Test
    @DisplayName("Test getContactSafe throws ContactNotFoundException when not exists")
    void testGetContactSafeNotExists() {
        assertThrows(ContactNotFoundException.class, () -> {
            contactService.getContactSafe("99999");
        });
    }

    @Test
    @DisplayName("Test getContactSafe throws exception for null ID")
    void testGetContactSafeNullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.getContactSafe(null);
        });
    }

    // ==================== CAPACITY TESTS ====================

    @Test
    @DisplayName("Test service capacity limits")
    void testServiceCapacityLimit() {
        // Add contacts up to near capacity (test with smaller number for speed)
        for (int i = 0; i < 100; i++) {
            Contact contact = new Contact("ID" + i, "First" + i, "Last" + i, 
                                        "1234567890", "Address " + i);
            contactService.addContact(contact);
        }
        
        assertEquals(100, contactService.getContactCount());
        assertFalse(contactService.isEmpty());
    }

    @Test
    @DisplayName("Test getRemainingCapacity decreases as contacts are added")
    void testRemainingCapacity() {
        int initialCapacity = contactService.getRemainingCapacity();
        assertEquals(10000, initialCapacity);
        
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        assertEquals(9999, contactService.getRemainingCapacity());
    }

    @Test
    @DisplayName("Test isFull returns false when not at capacity")
    void testIsFullWhenNotFull() {
        assertFalse(contactService.isFull());
        
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        assertFalse(contactService.isFull());
    }

    @Test
    @DisplayName("Test isEmpty returns true initially")
    void testIsEmptyInitially() {
        assertTrue(contactService.isEmpty());
    }

    @Test
    @DisplayName("Test isEmpty returns false after adding contact")
    void testIsEmptyAfterAdd() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        assertFalse(contactService.isEmpty());
    }

    // ==================== CONTAINS CONTACT TESTS ====================

    @Test
    @DisplayName("Test containsContact returns true when contact exists")
    void testContainsContactExists() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        assertTrue(contactService.containsContact("12345"));
    }

    @Test
    @DisplayName("Test containsContact returns false when contact doesn't exist")
    void testContainsContactNotExists() {
        assertFalse(contactService.containsContact("99999"));
    }

    @Test
    @DisplayName("Test containsContact returns false for null ID")
    void testContainsContactNullId() {
        assertFalse(contactService.containsContact(null));
    }

    @Test
    @DisplayName("Test containsContact returns false for empty ID")
    void testContainsContactEmptyId() {
        assertFalse(contactService.containsContact(""));
    }

    // ==================== GET ALL CONTACTS TESTS ====================

    @Test
    @DisplayName("Test getAllContacts returns all contacts")
    void testGetAllContacts() {
        Contact contact1 = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        Contact contact2 = new Contact("67890", "Jane", "Smith", "0987654321", "456 Oak Ave");
        
        contactService.addContact(contact1);
        contactService.addContact(contact2);
        
        Collection<Contact> allContacts = contactService.getAllContacts();
        assertEquals(2, allContacts.size());
        assertTrue(allContacts.contains(contact1));
        assertTrue(allContacts.contains(contact2));
    }

    @Test
    @DisplayName("Test getAllContacts returns empty collection when no contacts")
    void testGetAllContactsEmpty() {
        Collection<Contact> allContacts = contactService.getAllContacts();
        assertNotNull(allContacts);
        assertTrue(allContacts.isEmpty());
    }

    @Test
    @DisplayName("Test getAllContacts returns unmodifiable collection")
    void testGetAllContactsUnmodifiable() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        Collection<Contact> allContacts = contactService.getAllContacts();
        
        assertThrows(UnsupportedOperationException.class, () -> {
            allContacts.clear();
        });
    }

    // ==================== GET ALL CONTACT IDS TESTS ====================

    @Test
    @DisplayName("Test getAllContactIds returns all IDs")
    void testGetAllContactIds() {
        Contact contact1 = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        Contact contact2 = new Contact("67890", "Jane", "Smith", "0987654321", "456 Oak Ave");
        
        contactService.addContact(contact1);
        contactService.addContact(contact2);
        
        Set<String> allIds = contactService.getAllContactIds();
        assertEquals(2, allIds.size());
        assertTrue(allIds.contains("12345"));
        assertTrue(allIds.contains("67890"));
    }

    @Test
    @DisplayName("Test getAllContactIds returns empty set when no contacts")
    void testGetAllContactIdsEmpty() {
        Set<String> allIds = contactService.getAllContactIds();
        assertNotNull(allIds);
        assertTrue(allIds.isEmpty());
    }

    @Test
    @DisplayName("Test getAllContactIds returns unmodifiable set")
    void testGetAllContactIdsUnmodifiable() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        Set<String> allIds = contactService.getAllContactIds();
        
        assertThrows(UnsupportedOperationException.class, () -> {
            allIds.clear();
        });
    }

    // ==================== CLEAR ALL CONTACTS TESTS ====================

    @Test
    @DisplayName("Test clearAllContacts removes all contacts")
    void testClearAllContacts() {
        Contact contact1 = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        Contact contact2 = new Contact("67890", "Jane", "Smith", "0987654321", "456 Oak Ave");
        
        contactService.addContact(contact1);
        contactService.addContact(contact2);
        assertEquals(2, contactService.getContactCount());
        
        contactService.clearAllContacts();
        assertEquals(0, contactService.getContactCount());
        assertTrue(contactService.isEmpty());
        assertNull(contactService.getContact("12345"));
        assertNull(contactService.getContact("67890"));
    }

    // ==================== CONTACT ID IMMUTABILITY TESTS ====================

    @Test
    @DisplayName("Test contact ID is not updatable")
    void testContactIdImmutability() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        // Verify that contact ID remains unchanged after updates
        contactService.updateFirstName("12345", "Jane");
        assertEquals("12345", contactService.getContact("12345").getContactId());
    }

    // ==================== MULTIPLE OPERATIONS TESTS ====================

    @Test
    @DisplayName("Test adding multiple contacts")
    void testAddMultipleContacts() {
        Contact contact1 = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        Contact contact2 = new Contact("67890", "Jane", "Smith", "0987654321", "456 Oak Ave");
        Contact contact3 = new Contact("11111", "Bob", "Johnson", "5555555555", "789 Pine Rd");
        
        contactService.addContact(contact1);
        contactService.addContact(contact2);
        contactService.addContact(contact3);
        
        assertNotNull(contactService.getContact("12345"));
        assertNotNull(contactService.getContact("67890"));
        assertNotNull(contactService.getContact("11111"));
        assertEquals(3, contactService.getContactCount());
    }

    @Test
    @DisplayName("Test updating multiple fields for same contact")
    void testUpdateMultipleFields() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contactService.addContact(contact);
        
        contactService.updateFirstName("12345", "Jane");
        contactService.updateLastName("12345", "Smith");
        contactService.updatePhone("12345", "0987654321");
        contactService.updateAddress("12345", "456 Oak Avenue");
        
        Contact updatedContact = contactService.getContact("12345");
        assertEquals("Jane", updatedContact.getFirstName());
        assertEquals("Smith", updatedContact.getLastName());
        assertEquals("0987654321", updatedContact.getPhone());
        assertEquals("456 Oak Avenue", updatedContact.getAddress());
        assertEquals("12345", updatedContact.getContactId()); // ID should remain unchanged
    }

    @Test
    @DisplayName("Test getContact returns null for non-existent ID")
    void testGetContactReturnsNullForNonExistent() {
        assertNull(contactService.getContact("99999"));
    }

    @Test
    @DisplayName("Test getContact returns null for null ID")
    void testGetContactReturnsNullForNullId() {
        assertNull(contactService.getContact(null));
    }

    @Test
    @DisplayName("Test getContact returns null for empty ID")
    void testGetContactReturnsNullForEmptyId() {
        assertNull(contactService.getContact(""));
    }

    @Test
    @DisplayName("Test getContactCount after add and delete operations")
    void testGetContactCountAfterOperations() {
        assertEquals(0, contactService.getContactCount());
        
        Contact contact1 = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        Contact contact2 = new Contact("67890", "Jane", "Smith", "0987654321", "456 Oak Ave");
        
        contactService.addContact(contact1);
        assertEquals(1, contactService.getContactCount());
        
        contactService.addContact(contact2);
        assertEquals(2, contactService.getContactCount());
        
        contactService.deleteContact("12345");
        assertEquals(1, contactService.getContactCount());
        
        contactService.deleteContact("67890");
        assertEquals(0, contactService.getContactCount());
    }
}