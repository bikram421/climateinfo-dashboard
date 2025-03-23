package climateinfoapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinylog.Logger;

/**
 * Servlet class that handles requests and manages the climate information dashboard.
 * It provides functionalities such as listing, adding, editing, updating, and deleting climate records,
 * as well as viewing temperature trends.
 */
@WebServlet("/")
public class ClimateInfoDashboard extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Predefined list of locations for the climate records
    protected static final List<String> LOCATIONS = Arrays.asList("Victoria", "Nanaimo", "Port Alberni", "Duncan", "Tofino");
    
    // DAO for interacting with the climate records database
    protected ClimateRecordDAO climateRecordDAO;

    /**
     * Initializes the servlet, sets up the database connection, and initializes the DAO.
     * 
     * @throws ServletException if an error occurs during initialization
     */
    @Override
    public void init() throws ServletException {
        try {
            DBUtils.init(getServletContext());
            climateRecordDAO = new ClimateRecordDAO();
            Logger.info("ClimateRecordDAO initialized successfully.");
        } catch (Exception e) {
            Logger.error(e, "Failed to initialize ClimateRecordDAO");
            throw new ServletException("Error initializing DAO. Please contact support.", e);
        }
    }

    /**
     * Handles POST requests. It delegates to the doGet method to handle the request.
     * 
     * @param request the HTTP request
     * @param response the HTTP response
     * @throws ServletException if an error occurs during the processing of the request
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Handles GET requests, routing the request to the appropriate action.
     * 
     * @param request the HTTP request
     * @param response the HTTP response
     * @throws ServletException if an error occurs during the processing of the request
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        try {
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/insert":
                    insertClimateRecord(request, response);
                    break;
                case "/delete":
                    deleteClimateRecord(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/update":
                    updateClimateRecord(request, response);
                    break;
                case "/temperatureTrends":
                    showTemperatureTrends(request, response);
                    break;
                case "/search":
                	String city = request.getParameter("city");
                	
                    // Fetch records by city if a city is selected, else pass an empty list
                    List<ClimateRecord> cityRecords = null;
                    if (city != null && !city.isEmpty()) {
                        cityRecords = climateRecordDAO.getRecordsByCity(city);
                    } else {
                        cityRecords = new ArrayList<>(); // Empty list if no city is selected
                    }

                    // Pass the records and city to the JSP
                    request.setAttribute("cityRecords", cityRecords);
                    request.setAttribute("city", city); // Optional: Display city name on the page
                    request.getRequestDispatcher("SearchByCity.jsp").forward(request, response);
                    break;
   
                default:
                    listClimateRecords(request, response);
                    break;
            }
        } catch (Exception e) {
            handleException(request, response, e);
        }
    }

    /**
     * Lists all climate records and forwards the data to the appropriate JSP page.
     * 
     * @param request the HTTP request
     * @param response the HTTP response
     * @throws ServletException if an error occurs during the request handling
     * @throws IOException if an I/O error occurs
     * @throws DatabaseException if an error occurs while fetching data from the database
     * @throws InvalidArgumentsException 
     */
    private void listClimateRecords(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException, InvalidArgumentsException {
        List<ClimateRecord> records = climateRecordDAO.listAllClimateRecords();
        request.setAttribute("listRecord", records);
        forwardToPage(request, response, "ClimateRecordList.jsp");
        Logger.info("Listed all climate records.");
    }

    /**
     * Displays the form to add a new climate record.
     * 
     * @param request the HTTP request
     * @param response the HTTP response
     * @throws ServletException if an error occurs during the request handling
     * @throws IOException if an I/O error occurs
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("locations", LOCATIONS);
        forwardToPage(request, response, "ClimateRecordForm.jsp");
    }

    /**
     * Displays the form to edit an existing climate record.
     * 
     * @param request the HTTP request
     * @param response the HTTP response
     * @throws ServletException if an error occurs during the request handling
     * @throws IOException if an I/O error occurs
     * @throws DatabaseException if an error occurs while retrieving the record from the database
     * @throws InvalidArgumentsException 
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException, InvalidArgumentsException {
        int id = parseInteger(request.getParameter("id"), "ID");
        ClimateRecord record = climateRecordDAO.getClimateRecord(id);

        if (record == null) {
            Logger.warn("No record found for ID: {}", id);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Climate record not found.");
            return;
        }

        request.setAttribute("locations", LOCATIONS);
        request.setAttribute("record", record);
        forwardToPage(request, response, "ClimateRecordForm.jsp");
    }

    /**
     * Displays temperature trend data in a graphical form.
     * 
     * @param request the HTTP request
     * @param response the HTTP response
     * @throws ServletException if an error occurs during the request handling
     * @throws IOException if an I/O error occurs
     * @throws DatabaseException if an error occurs while fetching the data from the database
     * @throws InvalidArgumentsException 
     */
    private void showTemperatureTrends(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException, InvalidArgumentsException {
        List<ClimateRecord> records = climateRecordDAO.listAllClimateRecords();
        request.setAttribute("recordsList", records);
        forwardToPage(request, response, "TempTrendsGraph.jsp");
    }

    /**
     * Inserts a new climate record into the database.
     * 
     * @param request the HTTP request
     * @param response the HTTP response
     * @throws ServletException if an error occurs during the request handling
     * @throws IOException if an I/O error occurs
     * @throws DatabaseException if an error occurs while inserting the record into the database
     * @throws InvalidArgumentsException 
     */
    private void insertClimateRecord(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException, InvalidArgumentsException {
        ClimateRecord record = parseClimateRecord(request, 0);
        boolean success = climateRecordDAO.insertClimateRecord(record);
        if (success) {
            Logger.info("Inserted new record: {}", record);
        } else {
            Logger.error("Failed to insert record: {}", record);
        }
        response.sendRedirect("list");
    }

    /**
     * Updates an existing climate record in the database.
     * 
     * @param request the HTTP request
     * @param response the HTTP response
     * @throws ServletException if an error occurs during the request handling
     * @throws IOException if an I/O error occurs
     * @throws DatabaseException if an error occurs while updating the record in the database
     * @throws InvalidArgumentsException 
     */
    private void updateClimateRecord(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException, InvalidArgumentsException {
        int id = parseInteger(request.getParameter("id"), "ID");
        ClimateRecord record = parseClimateRecord(request, id);
        boolean success = climateRecordDAO.updateClimateRecord(record);
        if (success) {
            Logger.info("Updated record: {}", record);
        } else {
            Logger.error("Failed to update record: {}", record);
        }
        response.sendRedirect("list");
    }

    /**
     * Deletes a climate record from the database based on its ID.
     * 
     * @param request the HTTP request
     * @param response the HTTP response
     * @throws ServletException if an error occurs during the request handling
     * @throws IOException if an I/O error occurs
     * @throws DatabaseException if an error occurs while deleting the record from the database
     */
    private void deleteClimateRecord(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseException {
        int id = parseInteger(request.getParameter("id"), "ID");
        boolean success = climateRecordDAO.deleteClimateRecord(id);
        if (success) {
            Logger.info("Deleted record with ID: {}", id);
        } else {
            Logger.error("Failed to delete record with ID: {}", id);
        }
        response.sendRedirect("list");
    }

    /**
     * Parses the input data from the request to create a ClimateRecord object.
     * 
     * @param request the HTTP request
     * @param id the ID of the climate record (0 for a new record)
     * @return a newly created or updated ClimateRecord object
     * @throws InvalidArgumentsException 
     */
    private ClimateRecord parseClimateRecord(HttpServletRequest request, int id) throws InvalidArgumentsException {
        String date = request.getParameter("date");
        String location = request.getParameter("location");
        float temperature = parseFloat(request.getParameter("temperature"), "Temperature");
        float wind = parseFloat(request.getParameter("wind"), "Wind");
        return new ClimateRecord(id, date, location, temperature, wind);
    }

    /**
     * Parses a string as an integer, throwing an exception if the conversion fails.
     * 
     * @param value the string value to be parsed
     * @param fieldName the name of the field for error reporting
     * @return the parsed integer value
     * @throws IllegalArgumentException if the string cannot be parsed as an integer
     */
    private int parseInteger(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid integer: " + value, e);
        }
    }

    /**
     * Parses a string as a float, throwing an exception if the conversion fails.
     * 
     * @param value the string value to be parsed
     * @param fieldName the name of the field for error reporting
     * @return the parsed float value
     * @throws IllegalArgumentException if the string cannot be parsed as a float
     */
    private float parseFloat(String value, String fieldName) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid number: " + value, e);
        }
    }

    /**
     * Forwards the request and response to a given JSP page.
     * 
     * @param request the HTTP request
     * @param response the HTTP response
     * @param page the JSP page to forward to
     * @throws ServletException if an error occurs during the request forwarding
     * @throws IOException if an I/O error occurs
     */
    private void forwardToPage(HttpServletRequest request, HttpServletResponse response, String page)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(page);
        dispatcher.forward(request, response);
    }

    /**
     * Handles any exceptions that occur during the processing of a request.
     * 
     * @param request the HTTP request
     * @param response the HTTP response
     * @param e the exception that occurred
     * @throws ServletException if an error occurs during the exception handling
     * @throws IOException if an I/O error occurs
     */
    protected void handleException(HttpServletRequest request, HttpServletResponse response, Exception e)
             {
        Logger.error(e, "Unhandled exception occurred.");
        request.setAttribute("errorTitle", "Unexpected Error");
        request.setAttribute("errorMessage", e.getMessage());
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        
        try {
			forwardToPage(request, response, "Error.jsp");
		} catch (ServletException | IOException e1) {
			
			e1.printStackTrace();
		}
    }
}
