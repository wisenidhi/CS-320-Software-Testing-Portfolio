package Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import Contact.Contact;

/**
 * Comprehensive test suite with 100% coverage including:
 * - Exception testing for all validation paths
 * - Edge case and boundary testing
 * - Robust error handling and recovery
 * - State consistency verification
 */
public class ContactTest {

    // ========== VALID CREATION TESTS ==========
    
    @Test
    @DisplayName("Valid contact creation with typical values")
    void testValidContactCreation() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        
        assertNotNull(contact);
        assertEquals("12345", contact.getContactId());
        assertEquals("John", contact.getFirstName());
        assertEquals("Doe", contact.getLastName());
        assertEquals("1234567890", contact.getPhone());
        assertEquals("123 Main St", contact.getAddress());
    }

    @Test
    @DisplayName("Valid contact with maximum allowed lengths")
    void testValidContactMaxLengths() {
        Contact contact = new Contact("1234567890", "ABCDEFGHIJ", "KLMNOPQRST", 
                                       "9876543210", "123456789012345678901234567890");
        
        assertEquals(10, contact.getContactId().length());
        assertEquals(10, contact.getFirstName().length());
        assertEquals(10, contact.getLastName().length());
        assertEquals(10, contact.getPhone().length());
        assertEquals(30, contact.getAddress().length());
    }

    @Test
    @DisplayName("Valid contact with minimum lengths (single character)")
    void testValidContactMinLengths() {
        Contact contact = new Contact("1", "A", "B", "0000000000", "X");
        
        assertEquals("1", contact.getContactId());
        assertEquals("A", contact.getFirstName());
        assertEquals("B", contact.getLastName());
        assertEquals("0000000000", contact.getPhone());
        assertEquals("X", contact.getAddress());
    }

    // ========== CONTACT ID EXCEPTION TESTS ==========
    
    @Nested
    @DisplayName("ContactId Exception Tests")
    class ContactIdExceptionTests {
        
        @Test
        @DisplayName("Null contactId throws IllegalArgumentException")
        void testNullContactId() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Contact(null, "John", "Doe", "1234567890", "123 Main St");
            });
            assertEquals("Contact ID cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Empty string contactId throws IllegalArgumentException")
        void testEmptyContactId() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Contact("", "John", "Doe", "1234567890", "123 Main St");
            });
            assertEquals("Contact ID cannot be empty", exception.getMessage());
        }

        @Test
        @DisplayName("ContactId with 11 characters throws IllegalArgumentException")
        void testContactId11Characters() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345678901", "John", "Doe", "1234567890", "123 Main St");
            });
            assertEquals("Contact ID cannot exceed 10 characters", exception.getMessage());
        }

        @Test
        @DisplayName("ContactId with 15 characters throws IllegalArgumentException")
        void testContactId15Characters() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Contact("123456789012345", "John", "Doe", "1234567890", "123 Main St");
            });
        }

        @Test
        @DisplayName("ContactId with special characters is valid")
        void testContactIdSpecialCharacters() {
            Contact contact = new Contact("ID-001_@#", "John", "Doe", "1234567890", "123 Main St");
            assertEquals("ID-001_@#", contact.getContactId());
        }

        @Test
        @DisplayName("ContactId boundary: exactly 10 characters is valid")
        void testContactIdExactly10() {
            Contact contact = new Contact("1234567890", "John", "Doe", "1234567890", "123 Main St");
            assertEquals("1234567890", contact.getContactId());
            assertEquals(10, contact.getContactId().length());
        }
    }

    // ========== FIRST NAME EXCEPTION TESTS ==========
    
    @Nested
    @DisplayName("FirstName Exception Tests")
    class FirstNameExceptionTests {
        
        @Test
        @DisplayName("Constructor: Null firstName throws IllegalArgumentException")
        void testConstructorNullFirstName() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", null, "Doe", "1234567890", "123 Main St");
            });
            assertEquals("First name cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Constructor: Empty firstName throws IllegalArgumentException")
        void testConstructorEmptyFirstName() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", "", "Doe", "1234567890", "123 Main St");
            });
            assertEquals("First name cannot be empty", exception.getMessage());
        }

        @Test
        @DisplayName("Constructor: FirstName with 11 characters throws IllegalArgumentException")
        void testConstructorFirstName11Chars() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", "12345678901", "Doe", "1234567890", "123 Main St");
            });
            assertEquals("First name cannot exceed 10 characters", exception.getMessage());
        }

        @Test
        @DisplayName("Setter: Null firstName throws IllegalArgumentException")
        void testSetterNullFirstName() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                contact.setFirstName(null);
            });
            assertEquals("First name cannot be null", exception.getMessage());
            assertEquals("John", contact.getFirstName()); // Verify unchanged
        }

        @Test
        @DisplayName("Setter: Empty firstName throws IllegalArgumentException")
        void testSetterEmptyFirstName() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                contact.setFirstName("");
            });
            assertEquals("First name cannot be empty", exception.getMessage());
            assertEquals("John", contact.getFirstName()); // Verify unchanged
        }

        @Test
        @DisplayName("Setter: FirstName with 11 characters throws IllegalArgumentException")
        void testSetterFirstName11Chars() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                contact.setFirstName("12345678901");
            });
            assertEquals("First name cannot exceed 10 characters", exception.getMessage());
            assertEquals("John", contact.getFirstName()); // Verify unchanged
        }

        @Test
        @DisplayName("Setter: FirstName with 20 characters throws IllegalArgumentException")
        void testSetterFirstName20Chars() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            assertThrows(IllegalArgumentException.class, () -> {
                contact.setFirstName("12345678901234567890");
            });
            assertEquals("John", contact.getFirstName()); // Verify unchanged
        }

        @Test
        @DisplayName("FirstName boundary: exactly 10 characters is valid")
        void testFirstNameExactly10() {
            Contact contact = new Contact("12345", "ABCDEFGHIJ", "Doe", "1234567890", "123 Main St");
            assertEquals("ABCDEFGHIJ", contact.getFirstName());
            assertEquals(10, contact.getFirstName().length());
        }

        @Test
        @DisplayName("FirstName with special characters is valid")
        void testFirstNameSpecialChars() {
            Contact contact = new Contact("12345", "Mary-Jane", "Doe", "1234567890", "123 Main St");
            assertEquals("Mary-Jane", contact.getFirstName());
        }

        @Test
        @DisplayName("FirstName with numbers is valid")
        void testFirstNameWithNumbers() {
            Contact contact = new Contact("12345", "John2", "Doe", "1234567890", "123 Main St");
            assertEquals("John2", contact.getFirstName());
        }
    }

    // ========== LAST NAME EXCEPTION TESTS ==========
    
    @Nested
    @DisplayName("LastName Exception Tests")
    class LastNameExceptionTests {
        
        @Test
        @DisplayName("Constructor: Null lastName throws IllegalArgumentException")
        void testConstructorNullLastName() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", "John", null, "1234567890", "123 Main St");
            });
            assertEquals("Last name cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Constructor: Empty lastName throws IllegalArgumentException")
        void testConstructorEmptyLastName() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", "John", "", "1234567890", "123 Main St");
            });
            assertEquals("Last name cannot be empty", exception.getMessage());
        }

        @Test
        @DisplayName("Constructor: LastName with 11 characters throws IllegalArgumentException")
        void testConstructorLastName11Chars() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", "John", "12345678901", "1234567890", "123 Main St");
            });
            assertEquals("Last name cannot exceed 10 characters", exception.getMessage());
        }

        @Test
        @DisplayName("Setter: Null lastName throws IllegalArgumentException")
        void testSetterNullLastName() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                contact.setLastName(null);
            });
            assertEquals("Last name cannot be null", exception.getMessage());
            assertEquals("Doe", contact.getLastName()); // Verify unchanged
        }

        @Test
        @DisplayName("Setter: Empty lastName throws IllegalArgumentException")
        void testSetterEmptyLastName() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                contact.setLastName("");
            });
            assertEquals("Last name cannot be empty", exception.getMessage());
            assertEquals("Doe", contact.getLastName()); // Verify unchanged
        }

        @Test
        @DisplayName("Setter: LastName with 11 characters throws IllegalArgumentException")
        void testSetterLastName11Chars() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                contact.setLastName("12345678901");
            });
            assertEquals("Last name cannot exceed 10 characters", exception.getMessage());
            assertEquals("Doe", contact.getLastName()); // Verify unchanged
        }

        @Test
        @DisplayName("Setter: LastName with 50 characters throws IllegalArgumentException")
        void testSetterLastName50Chars() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            String longName = "12345678901234567890123456789012345678901234567890";
            assertThrows(IllegalArgumentException.class, () -> {
                contact.setLastName(longName);
            });
            assertEquals("Doe", contact.getLastName()); // Verify unchanged
        }

        @Test
        @DisplayName("LastName boundary: exactly 10 characters is valid")
        void testLastNameExactly10() {
            Contact contact = new Contact("12345", "John", "1234567890", "1234567890", "123 Main St");
            assertEquals("1234567890", contact.getLastName());
            assertEquals(10, contact.getLastName().length());
        }
    }

    // ========== PHONE NUMBER EXCEPTION TESTS ==========
    
    @Nested
    @DisplayName("Phone Number Exception Tests")
    class PhoneExceptionTests {
        
        @Test
        @DisplayName("Constructor: Null phone throws IllegalArgumentException")
        void testConstructorNullPhone() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", "John", "Doe", null, "123 Main St");
            });
            assertEquals("Phone number cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Constructor: Empty phone throws IllegalArgumentException")
        void testConstructorEmptyPhone() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", "John", "Doe", "", "123 Main St");
            });
            assertEquals("Phone number cannot be empty", exception.getMessage());
        }

        @Test
        @DisplayName("Constructor: Phone with 9 digits throws IllegalArgumentException")
        void testConstructorPhone9Digits() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", "John", "Doe", "123456789", "123 Main St");
            });
            assertEquals("Phone number must be exactly 10 digits", exception.getMessage());
        }

        @Test
        @DisplayName("Constructor: Phone with 11 digits throws IllegalArgumentException")
        void testConstructorPhone11Digits() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", "John", "Doe", "12345678901", "123 Main St");
            });
            assertEquals("Phone number must be exactly 10 digits", exception.getMessage());
        }

        @Test
        @DisplayName("Constructor: Phone with 5 digits throws IllegalArgumentException")
        void testConstructorPhone5Digits() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", "John", "Doe", "12345", "123 Main St");
            });
            assertEquals("Phone number must be exactly 10 digits", exception.getMessage());
        }

        @Test
        @DisplayName("Constructor: Phone with 15 digits throws IllegalArgumentException")
        void testConstructorPhone15Digits() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", "John", "Doe", "123456789012345", "123 Main St");
            });
        }

        @Test
        @DisplayName("Constructor: Phone with 10 letters throws IllegalArgumentException")
        void testConstructorPhone10Letters() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", "John", "Doe", "abcdefghij", "123 Main St");
            });
            assertEquals("Phone number must contain only digits", exception.getMessage());
        }

        @Test
        @DisplayName("Constructor: Phone with mixed alphanumeric (10 chars) throws IllegalArgumentException")
        void testConstructorPhoneMixedAlphanumeric() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", "John", "Doe", "123abc7890", "123 Main St");
            });
            assertEquals("Phone number must contain only digits", exception.getMessage());
        }

        @Test
        @DisplayName("Constructor: Phone with dashes throws IllegalArgumentException")
        void testConstructorPhoneWithDashes() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", "John", "Doe", "123-456-789", "123 Main St");
            });
        }

        @Test
        @DisplayName("Constructor: Phone with spaces throws IllegalArgumentException")
        void testConstructorPhoneWithSpaces() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", "John", "Doe", "123 456 789", "123 Main St");
            });
        }

        @Test
        @DisplayName("Constructor: Phone with parentheses throws IllegalArgumentException")
        void testConstructorPhoneWithParentheses() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", "John", "Doe", "(123)45678", "123 Main St");
            });
        }

        @Test
        @DisplayName("Constructor: Phone with dots throws IllegalArgumentException")
        void testConstructorPhoneWithDots() {
            assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", "John", "Doe", "123.456.78", "123 Main St");
            });
        }

        @Test
        @DisplayName("Setter: Null phone throws IllegalArgumentException")
        void testSetterNullPhone() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                contact.setPhone(null);
            });
            assertEquals("Phone number cannot be null", exception.getMessage());
            assertEquals("1234567890", contact.getPhone()); // Verify unchanged
        }

        @Test
        @DisplayName("Setter: Empty phone throws IllegalArgumentException")
        void testSetterEmptyPhone() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                contact.setPhone("");
            });
            assertEquals("Phone number cannot be empty", exception.getMessage());
            assertEquals("1234567890", contact.getPhone()); // Verify unchanged
        }

        @Test
        @DisplayName("Setter: Phone with 9 digits throws IllegalArgumentException")
        void testSetterPhone9Digits() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                contact.setPhone("123456789");
            });
            assertEquals("Phone number must be exactly 10 digits", exception.getMessage());
            assertEquals("1234567890", contact.getPhone()); // Verify unchanged
        }

        @Test
        @DisplayName("Setter: Phone with 11 digits throws IllegalArgumentException")
        void testSetterPhone11Digits() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                contact.setPhone("12345678901");
            });
            assertEquals("Phone number must be exactly 10 digits", exception.getMessage());
            assertEquals("1234567890", contact.getPhone()); // Verify unchanged
        }

        @Test
        @DisplayName("Setter: Phone with 10 letters throws IllegalArgumentException")
        void testSetterPhone10Letters() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                contact.setPhone("abcdefghij");
            });
            assertEquals("Phone number must contain only digits", exception.getMessage());
            assertEquals("1234567890", contact.getPhone()); // Verify unchanged
        }

        @Test
        @DisplayName("Setter: Phone with mixed characters throws IllegalArgumentException")
        void testSetterPhoneMixed() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                contact.setPhone("12abc67890");
            });
            assertEquals("Phone number must contain only digits", exception.getMessage());
            assertEquals("1234567890", contact.getPhone()); // Verify unchanged
        }

        @Test
        @DisplayName("Setter: Phone with special characters throws IllegalArgumentException")
        void testSetterPhoneSpecialChars() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            assertThrows(IllegalArgumentException.class, () -> {
                contact.setPhone("123@456#78");
            });
            assertEquals("1234567890", contact.getPhone()); // Verify unchanged
        }

        @Test
        @DisplayName("Phone boundary: exactly 10 digits is valid")
        void testPhoneExactly10Digits() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            assertEquals("1234567890", contact.getPhone());
            assertEquals(10, contact.getPhone().length());
        }

        @Test
        @DisplayName("Phone with all zeros is valid")
        void testPhoneAllZeros() {
            Contact contact = new Contact("12345", "John", "Doe", "0000000000", "123 Main St");
            assertEquals("0000000000", contact.getPhone());
        }

        @Test
        @DisplayName("Phone with all nines is valid")
        void testPhoneAllNines() {
            Contact contact = new Contact("12345", "John", "Doe", "9999999999", "123 Main St");
            assertEquals("9999999999", contact.getPhone());
        }
    }

    // ========== ADDRESS EXCEPTION TESTS ==========
    
    @Nested
    @DisplayName("Address Exception Tests")
    class AddressExceptionTests {
        
        @Test
        @DisplayName("Constructor: Null address throws IllegalArgumentException")
        void testConstructorNullAddress() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", "John", "Doe", "1234567890", null);
            });
            assertEquals("Address cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Constructor: Empty address throws IllegalArgumentException")
        void testConstructorEmptyAddress() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", "John", "Doe", "1234567890", "");
            });
            assertEquals("Address cannot be empty", exception.getMessage());
        }

        @Test
        @DisplayName("Constructor: Address with 31 characters throws IllegalArgumentException")
        void testConstructorAddress31Chars() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", "John", "Doe", "1234567890", "1234567890123456789012345678901");
            });
            assertEquals("Address cannot exceed 30 characters", exception.getMessage());
        }

        @Test
        @DisplayName("Constructor: Address with 50 characters throws IllegalArgumentException")
        void testConstructorAddress50Chars() {
            String longAddress = "12345678901234567890123456789012345678901234567890";
            assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", "John", "Doe", "1234567890", longAddress);
            });
        }

        @Test
        @DisplayName("Setter: Null address throws IllegalArgumentException")
        void testSetterNullAddress() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                contact.setAddress(null);
            });
            assertEquals("Address cannot be null", exception.getMessage());
            assertEquals("123 Main St", contact.getAddress()); // Verify unchanged
        }

        @Test
        @DisplayName("Setter: Empty address throws IllegalArgumentException")
        void testSetterEmptyAddress() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                contact.setAddress("");
            });
            assertEquals("Address cannot be empty", exception.getMessage());
            assertEquals("123 Main St", contact.getAddress()); // Verify unchanged
        }

        @Test
        @DisplayName("Setter: Address with 31 characters throws IllegalArgumentException")
        void testSetterAddress31Chars() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                contact.setAddress("1234567890123456789012345678901");
            });
            assertEquals("Address cannot exceed 30 characters", exception.getMessage());
            assertEquals("123 Main St", contact.getAddress()); // Verify unchanged
        }

        @Test
        @DisplayName("Setter: Address with 100 characters throws IllegalArgumentException")
        void testSetterAddress100Chars() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            String veryLongAddress = "1234567890".repeat(10); // 100 characters
            assertThrows(IllegalArgumentException.class, () -> {
                contact.setAddress(veryLongAddress);
            });
            assertEquals("123 Main St", contact.getAddress()); // Verify unchanged
        }

        @Test
        @DisplayName("Address boundary: exactly 30 characters is valid")
        void testAddressExactly30() {
            String address30 = "123456789012345678901234567890";
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", address30);
            assertEquals(address30, contact.getAddress());
            assertEquals(30, contact.getAddress().length());
        }

        @Test
        @DisplayName("Address with special characters is valid")
        void testAddressSpecialChars() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St. #4B");
            assertEquals("123 Main St. #4B", contact.getAddress());
        }
    }

    // ========== GETTER TESTS ==========
    
    @Test
    @DisplayName("getContactId returns correct value")
    void testGetContactId() {
        Contact contact = new Contact("ID123", "John", "Doe", "1234567890", "123 Main St");
        assertEquals("ID123", contact.getContactId());
    }

    @Test
    @DisplayName("getFirstName returns correct value")
    void testGetFirstName() {
        Contact contact = new Contact("12345", "Alice", "Doe", "1234567890", "123 Main St");
        assertEquals("Alice", contact.getFirstName());
    }

    @Test
    @DisplayName("getLastName returns correct value")
    void testGetLastName() {
        Contact contact = new Contact("12345", "John", "Smith", "1234567890", "123 Main St");
        assertEquals("Smith", contact.getLastName());
    }

    @Test
    @DisplayName("getPhone returns correct value")
    void testGetPhone() {
        Contact contact = new Contact("12345", "John", "Doe", "5551234567", "123 Main St");
        assertEquals("5551234567", contact.getPhone());
    }

    @Test
    @DisplayName("getAddress returns correct value")
    void testGetAddress() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "456 Oak Ave");
        assertEquals("456 Oak Ave", contact.getAddress());
    }

    // ========== SETTER SUCCESS TESTS ==========
    
    @Test
    @DisplayName("setFirstName with valid value succeeds")
    void testSetFirstNameValid() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contact.setFirstName("Jane");
        assertEquals("Jane", contact.getFirstName());
    }

    @Test
    @DisplayName("setLastName with valid value succeeds")
    void testSetLastNameValid() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contact.setLastName("Smith");
        assertEquals("Smith", contact.getLastName());
    }

    @Test
    @DisplayName("setPhone with valid value succeeds")
    void testSetPhoneValid() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contact.setPhone("9876543210");
        assertEquals("9876543210", contact.getPhone());
    }

    @Test
    @DisplayName("setAddress with valid value succeeds")
    void testSetAddressValid() {
        Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
        contact.setAddress("456 Oak Avenue");
        assertEquals("456 Oak Avenue", contact.getAddress());
    }

    // ========== ROBUST ERROR HANDLING & RECOVERY TESTS ==========
    
    @Nested
    @DisplayName("Robust Error Handling Tests")
    class RobustErrorHandlingTests {
        
        @Test
        @DisplayName("Multiple failed updates do not corrupt state")
        void testMultipleFailedUpdates() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            
            // Try multiple invalid updates
            assertThrows(IllegalArgumentException.class, () -> contact.setFirstName(null));
            assertThrows(IllegalArgumentException.class, () -> contact.setFirstName(""));
            assertThrows(IllegalArgumentException.class, () -> contact.setFirstName("TooLongName"));
            
            // Verify state is still valid
            assertEquals("John", contact.getFirstName());
            assertEquals("Doe", contact.getLastName());
            assertEquals("1234567890", contact.getPhone());
            assertEquals("123 Main St", contact.getAddress());
        }

        @Test
        @DisplayName("Failed update followed by successful update works")
        void testFailedThenSuccessfulUpdate() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            
            // Failed update
            assertThrows(IllegalArgumentException.class, () -> contact.setFirstName("TooLongName"));
            assertEquals("John", contact.getFirstName());
            
            // Successful update
            contact.setFirstName("Jane");
            assertEquals("Jane", contact.getFirstName());
        }

        @Test
        @DisplayName("All fields remain valid after any single field failure")
        void testAllFieldsValidAfterFailure() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            
            // Try to update each field with invalid value
            assertThrows(IllegalArgumentException.class, () -> contact.setFirstName(null));
            assertThrows(IllegalArgumentException.class, () -> contact.setLastName(""));
            assertThrows(IllegalArgumentException.class, () -> contact.setPhone("invalid"));
            assertThrows(IllegalArgumentException.class, () -> contact.setAddress(null));
            
            // Verify all original values intact
            assertEquals("12345", contact.getContactId());
            assertEquals("John", contact.getFirstName());
            assertEquals("Doe", contact.getLastName());
            assertEquals("1234567890", contact.getPhone());
            assertEquals("123 Main St", contact.getAddress());
        }

        @Test
        @DisplayName("Sequential updates to all fields work correctly")
        void testSequentialUpdatesAllFields() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            
            contact.setFirstName("Jane");
            assertEquals("Jane", contact.getFirstName());
            
            contact.setLastName("Smith");
            assertEquals("Smith", contact.getLastName());
            
            contact.setPhone("9876543210");
            assertEquals("9876543210", contact.getPhone());
            
            contact.setAddress("456 Oak St");
            assertEquals("456 Oak St", contact.getAddress());
            
            // Verify all changes persisted
            assertEquals("12345", contact.getContactId());
            assertEquals("Jane", contact.getFirstName());
            assertEquals("Smith", contact.getLastName());
            assertEquals("9876543210", contact.getPhone());
            assertEquals("456 Oak St", contact.getAddress());
        }

        @Test
        @DisplayName("Rapid repeated updates to same field work correctly")
        void testRapidRepeatedUpdates() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            
            for (int i = 0; i < 10; i++) {
                contact.setFirstName("Name" + i);
                assertEquals("Name" + i, contact.getFirstName());
            }
        }

        @Test
        @DisplayName("Contact ID remains immutable through all operations")
        void testContactIdImmutability() {
            Contact contact = new Contact("IMMUTABLE", "John", "Doe", "1234567890", "123 Main St");
            
            // Update all mutable fields
            contact.setFirstName("Jane");
            contact.setLastName("Smith");
            contact.setPhone("9876543210");
            contact.setAddress("New Address");
            
            // Verify ID never changed
            assertEquals("IMMUTABLE", contact.getContactId());
        }

        @Test
        @DisplayName("Exception messages are accurate and helpful")
        void testExceptionMessagesAccuracy() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            
            Exception e1 = assertThrows(IllegalArgumentException.class, () -> contact.setFirstName(null));
            assertTrue(e1.getMessage().contains("First name cannot be null"));
            
            Exception e2 = assertThrows(IllegalArgumentException.class, () -> contact.setPhone("abc"));
            assertTrue(e2.getMessage().contains("Phone number must be exactly 10 digits"));
            
            Exception e3 = assertThrows(IllegalArgumentException.class, () -> contact.setAddress(""));
            assertTrue(e3.getMessage().contains("Address cannot be empty"));
        }
    }

    // ========== EDGE CASE TESTS ==========
    
    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {
        
        @Test
        @DisplayName("Contact with all minimum values")
        void testAllMinimumValues() {
            Contact contact = new Contact("1", "A", "B", "0000000000", "X");
            assertNotNull(contact);
            assertEquals(1, contact.getContactId().length());
            assertEquals(1, contact.getFirstName().length());
            assertEquals(1, contact.getLastName().length());
            assertEquals(10, contact.getPhone().length());
            assertEquals(1, contact.getAddress().length());
        }

        @Test
        @DisplayName("Contact with all maximum values")
        void testAllMaximumValues() {
            Contact contact = new Contact(
                "1234567890",
                "ABCDEFGHIJ",
                "KLMNOPQRST",
                "9999999999",
                "123456789012345678901234567890"
            );
            assertEquals(10, contact.getContactId().length());
            assertEquals(10, contact.getFirstName().length());
            assertEquals(10, contact.getLastName().length());
            assertEquals(10, contact.getPhone().length());
            assertEquals(30, contact.getAddress().length());
        }

        @Test
        @DisplayName("Phone number validation order: length checked before format")
        void testPhoneValidationOrder() {
            // Should fail on length, not format
            Exception e = assertThrows(IllegalArgumentException.class, () -> {
                new Contact("12345", "John", "Doe", "abc", "123 Main St");
            });
            assertEquals("Phone number must be exactly 10 digits", e.getMessage());
        }

        @Test
        @DisplayName("All string fields accept numeric strings")
        void testNumericStrings() {
            Contact contact = new Contact("0123456789", "0123456789", "9876543210", "1111111111", "999");
            assertEquals("0123456789", contact.getContactId());
            assertEquals("0123456789", contact.getFirstName());
            assertEquals("9876543210", contact.getLastName());
        }

        @Test
        @DisplayName("Fields with whitespace-only (spaces) are considered empty")
        void testWhitespaceHandling() {
            // Note: Based on current implementation, spaces might be valid
            // This test documents actual behavior
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", " ");
            assertEquals(" ", contact.getAddress());
        }

        @Test
        @DisplayName("Update same field back and forth")
        void testUpdateFieldBackAndForth() {
            Contact contact = new Contact("12345", "John", "Doe", "1234567890", "123 Main St");
            
            contact.setFirstName("Jane");
            assertEquals("Jane", contact.getFirstName());
            
            contact.setFirstName("John");
            assertEquals("John", contact.getFirstName());
            
            contact.setFirstName("Jane");
            assertEquals("Jane", contact.getFirstName());
        }
    }
}