package climateinfoapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClimateRecordTest {
    private ClimateRecord record;

    @BeforeEach
    public void setUp() {
        record = new ClimateRecord();
    }

    // Test constructor with all fields
    @Test
    public void testConstructorWithAllFields() throws InvalidArgumentsException {
        record = new ClimateRecord(1, "2024-11-24", "New York", 25.5f, 12.5f);
        assertEquals(1, record.getId());
        assertEquals("2024-11-24", record.getDate());
        assertEquals("New York", record.getLocation());
        assertEquals(25.5f, record.getTemperature());
        assertEquals(12.5f, record.getWind());
    }

    // Test constructor without ID
    @Test
    public void testConstructorWithoutID() throws InvalidArgumentsException {
        record = new ClimateRecord("2024-11-24", "Los Angeles", 30.0f, 10.0f);
        assertEquals("2024-11-24", record.getDate());
        assertEquals("Los Angeles", record.getLocation());
        assertEquals(30.0f, record.getTemperature());
        assertEquals(10.0f, record.getWind());
    }

    // Test constructor with only ID
    @Test
    public void testConstructorWithIdOnly() {
        record = new ClimateRecord(5);
        assertEquals(5, record.getId());
    }

    // Test default constructor
    @Test
    public void testDefaultConstructor() {
        assertNotNull(record);
        assertEquals(0, record.getId());  // Default value for id
        assertNull(record.getDate());
        assertNull(record.getLocation());
        assertEquals(0.0f, record.getTemperature());
        assertEquals(0.0f, record.getWind());
    }

    // Test setter and getter for ID
    @Test
    public void testSetId() {
        record.setId(5);
        assertEquals(5, record.getId());
    }

    // Test setter and getter for Date
    @Test
    public void testSetDate() throws InvalidArgumentsException {
        record.setDate("2024-11-24");
        assertEquals("2024-11-24", record.getDate());
    }

    // Test setter and getter for Location
    @Test
    public void testSetLocation() throws InvalidArgumentsException {
        record.setLocation("Chicago");
        assertEquals("Chicago", record.getLocation());
    }

    // Test setter and getter for Temperature
    @Test
    public void testSetTemperature() throws InvalidArgumentsException {
        record.setTemperature(35.0f);
        assertEquals(35.0f, record.getTemperature());
    }

    // Test setter and getter for Wind
    @Test
    public void testSetWind() throws InvalidArgumentsException {
        record.setWind(25.0f);
        assertEquals(25.0f, record.getWind());
    }

    // Test toString method
    @Test
    public void testToString() throws InvalidArgumentsException {
        record = new ClimateRecord(1, "2024-11-24", "New York", 25.5f, 12.5f);
        String expected = "ClimateRecord{id=1, date='2024-11-24', location='New York', temperature=25.5, wind=12.5}";
        assertEquals(expected, record.toString());
    }

    // Test valid date, location, temperature, and wind values in constructors
    @Test
    public void testValidClimateRecordConstructor() throws InvalidArgumentsException {
        record = new ClimateRecord(1, "2024-11-28", "New York", 25.5f, 15.2f);
        assertEquals(1, record.getId());
        assertEquals("2024-11-28", record.getDate());
        assertEquals("New York", record.getLocation());
        assertEquals(25.5f, record.getTemperature());
        assertEquals(15.2f, record.getWind());

        record = new ClimateRecord("2024-11-28", "New York", 25.5f, 15.2f);
        assertEquals("2024-11-28", record.getDate());
        assertEquals("New York", record.getLocation());
        assertEquals(25.5f, record.getTemperature());
        assertEquals(15.2f, record.getWind());
    }

    // Test invalid date format
    @Test
    public void testInvalidDateFormat() {
    	InvalidArgumentsException exception = assertThrows(InvalidArgumentsException.class, () -> {
            new ClimateRecord("28-11-2024", "New York", 25.5f, 15.2f);
        });
        assertEquals("Invalid date format. Expected yyyy-MM-dd.", exception.getMessage());
    }

    // Test invalid location (null or empty)
    @Test
    public void testInvalidLocation() {
    	InvalidArgumentsException exception = assertThrows(InvalidArgumentsException.class, () -> {
            new ClimateRecord("2024-11-28", null, 25.5f, 15.2f);
        });
        assertEquals("Invalid location. Location cannot be empty or null.", exception.getMessage());

        exception = assertThrows(InvalidArgumentsException.class, () -> {
            new ClimateRecord("2024-11-28", "", 25.5f, 15.2f);
        });
        assertEquals("Invalid location. Location cannot be empty or null.", exception.getMessage());
    }

    // Test invalid temperature values
    @Test
    public void testInvalidTemperature() {
    	InvalidArgumentsException exception = assertThrows(InvalidArgumentsException.class, () -> {
            new ClimateRecord("2024-11-28", "New York", -150f, 15.2f);
        });
        assertEquals("Invalid temperature. Must be between -100.0 and 100.0 degrees.", exception.getMessage());

        exception = assertThrows(InvalidArgumentsException.class, () -> {
            new ClimateRecord("2024-11-28", "New York", 150f, 15.2f);
        });
        assertEquals("Invalid temperature. Must be between -100.0 and 100.0 degrees.", exception.getMessage());
    }

    // Test invalid wind values
    @Test
    public void testInvalidWind() {
    	InvalidArgumentsException exception = assertThrows(InvalidArgumentsException.class, () -> {
            new ClimateRecord("2024-11-28", "New York", 25.5f, -10f);
        });
        assertEquals("Invalid wind speed. Must be between 0.0 and 200.0 km/h.", exception.getMessage());

        exception = assertThrows(InvalidArgumentsException.class, () -> {
            new ClimateRecord("2024-11-28", "New York", 25.5f, 300f);
        });
        assertEquals("Invalid wind speed. Must be between 0.0 and 200.0 km/h.", exception.getMessage());
    }

    // Test null temperature value
    @Test
    public void testNullTemperature() {
        InvalidArgumentsException exception = assertThrows(InvalidArgumentsException.class, () -> {
            record.setTemperature(Float.NaN); // Using NaN as invalid value
        });
        assertEquals("Invalid temperature. Must be between -100.0 and 100.0 degrees.", exception.getMessage());
    }

    // Test null wind value
    @Test
    public void testNullWind() {
    	InvalidArgumentsException exception = assertThrows(InvalidArgumentsException.class, () -> {
            record.setWind(Float.NaN); // Using NaN as invalid value
        });
        assertEquals("Invalid wind speed. Must be between 0.0 and 200.0 km/h.", exception.getMessage());
    }
}
