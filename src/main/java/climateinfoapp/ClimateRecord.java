package climateinfoapp;

import java.util.regex.Pattern;

/**
 * Represents a climate record with information about a specific date, location,
 * temperature, and wind speed. Provides validation for input data.
 */
public class ClimateRecord {
    private int id;
    private String date;
    private String location;
    private float temperature;
    private float wind;

    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}"); // For validating date in yyyy-MM-dd format
    private static final float MIN_TEMP = -100.0f; // Minimum temperature (example)
    private static final float MAX_TEMP = 100.0f; // Maximum temperature (example)
    private static final float MIN_WIND = 0.0f; // Minimum wind speed
    private static final float MAX_WIND = 200.0f; // Maximum wind speed

    /**
     * Default constructor for creating an empty climate record.
     */
    public ClimateRecord() {}

    /**
     * Constructs a climate record with all fields.
     *
     * @param id          the unique identifier of the climate record
     * @param date        the date of the climate record in yyyy-MM-dd format
     * @param location    the location of the climate record
     * @param temperature the temperature value (must be between MIN_TEMP and MAX_TEMP)
     * @param wind        the wind speed value (must be between MIN_WIND and MAX_WIND)
     * @throws InvalidArgumentsException for invalid inputs
     */
    public ClimateRecord(int id, String date, String location, float temperature, float wind) throws InvalidArgumentsException {
        setId(id);
        setDate(date);
        setLocation(location);
        setTemperature(temperature);
        setWind(wind);
    }

    /**
     * Constructs a climate record without an ID (for cases where the ID is auto-generated).
     *
     * @param date        the date of the climate record in yyyy-MM-dd format
     * @param location    the location of the climate record
     * @param temperature the temperature value (must be between MIN_TEMP and MAX_TEMP)
     * @param wind        the wind speed value (must be between MIN_WIND and MAX_WIND)
     * @throws InvalidArgumentsException for invalid inputs
     */
    public ClimateRecord(String date, String location, float temperature, float wind) throws InvalidArgumentsException {
        setDate(date);
        setLocation(location);
        setTemperature(temperature);
        setWind(wind);
    }

    /**
     * Constructs a climate record with only an ID.
     *
     * @param id the unique identifier of the climate record
     */
    public ClimateRecord(int id) {
        setId(id);
    }

    /**
     * Gets the unique identifier of the climate record.
     *
     * @return the ID of the climate record
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the climate record.
     *
     * @param id the ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the date of the climate record.
     *
     * @return the date of the climate record in yyyy-MM-dd format
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date of the climate record.
     *
     * @param date the date to set (must be in yyyy-MM-dd format)
     * @throws InvalidArgumentsException if the date is invalid
     */
    public void setDate(String date) throws InvalidArgumentsException {
        if (!isValidDate(date)) {
            throw new InvalidArgumentsException("Invalid date format. Expected yyyy-MM-dd.");
        }
        this.date = date;
    }

    /**
     * Gets the location of the climate record.
     *
     * @return the location of the climate record
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the climate record.
     *
     * @param location the location to set (cannot be null or empty)
     * @throws InvalidArgumentsException if the location is invalid
     */
    public void setLocation(String location) throws InvalidArgumentsException {
        if (!isValidLocation(location)) {
            throw new InvalidArgumentsException("Invalid location. Location cannot be empty or null.");
        }
        this.location = location;
    }

    /**
     * Gets the temperature value of the climate record.
     *
     * @return the temperature value
     */
    public float getTemperature() {
        return temperature;
    }

    /**
     * Sets the temperature value of the climate record.
     *
     * @param temperature the temperature value to set (must be between MIN_TEMP and MAX_TEMP)
     * @throws InvalidArgumentsException if the temperature is invalid
     */
    public void setTemperature(float temperature) throws InvalidArgumentsException {
        if (!isValidTemperature(temperature)) {
            throw new InvalidArgumentsException("Invalid temperature. Must be between " + MIN_TEMP + " and " + MAX_TEMP + " degrees.");
        }
        this.temperature = temperature;
    }

    /**
     * Gets the wind speed value of the climate record.
     *
     * @return the wind speed value
     */
    public float getWind() {
        return wind;
    }

    /**
     * Sets the wind speed value of the climate record.
     *
     * @param wind the wind speed value to set (must be between MIN_WIND and MAX_WIND)
     * @throws InvalidArgumentsException if the wind speed is invalid
     */
    public void setWind(float wind) throws InvalidArgumentsException {
        if (!isValidWind(wind)) {
            throw new InvalidArgumentsException("Invalid wind speed. Must be between " + MIN_WIND + " and " + MAX_WIND + " km/h.");
        }
        this.wind = wind;
    }

    /**
     * Validates the date format.
     *
     * @param date the date to validate
     * @return true if the date is valid, false otherwise
     */
    private boolean isValidDate(String date) {
        return date != null && DATE_PATTERN.matcher(date).matches();
    }

    /**
     * Validates the location.
     *
     * @param location the location to validate
     * @return true if the location is valid, false otherwise
     */
    private boolean isValidLocation(String location) {
        return location != null && !location.trim().isEmpty();
    }

    /**
     * Validates the temperature value.
     *
     * @param temperature the temperature to validate
     * @return true if the temperature is within the valid range, false otherwise
     */
    private boolean isValidTemperature(float temperature) {
        return temperature >= MIN_TEMP && temperature <= MAX_TEMP;
    }

    /**
     * Validates the wind speed value.
     *
     * @param wind the wind speed to validate
     * @return true if the wind speed is within the valid range, false otherwise
     */
    private boolean isValidWind(float wind) {
        return wind >= MIN_WIND && wind <= MAX_WIND;
    }

    /**
     * Returns a string representation of the climate record.
     *
     * @return a string containing the climate record details
     */
    @Override
    public String toString() {
        return "ClimateRecord{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", location='" + location + '\'' +
                ", temperature=" + temperature +
                ", wind=" + wind +
                '}';
    }
}
