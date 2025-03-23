package climateinfoapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class ClimateInfoDashboardTest {

    private ClimateInfoDashboard servlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private RequestDispatcher mockDispatcher;
    private ClimateRecordDAO mockClimateRecordDAO;

    @BeforeEach
    void setUp() {
        servlet = new ClimateInfoDashboard();
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockDispatcher = mock(RequestDispatcher.class);
        mockClimateRecordDAO = mock(ClimateRecordDAO.class);

        // Inject the mock DAO into the servlet
        servlet.climateRecordDAO = mockClimateRecordDAO;
    }

    @Test
    void testListClimateRecords() throws Exception {
        // Arrange
        List<ClimateRecord> mockRecords = Arrays.asList(
                new ClimateRecord(1, "2024-11-01", "Victoria", 12.5f, 5.0f),
                new ClimateRecord(2, "2024-11-02", "Nanaimo", 13.0f, 4.5f)
        );
        when(mockClimateRecordDAO.listAllClimateRecords()).thenReturn(mockRecords);
        when(mockRequest.getRequestDispatcher("ClimateRecordList.jsp")).thenReturn(mockDispatcher);

        // Act
        when(mockRequest.getServletPath()).thenReturn("/");
        servlet.doGet(mockRequest, mockResponse);

        // Assert
        verify(mockRequest).setAttribute("listRecord", mockRecords);
        verify(mockDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    void testShowNewForm() throws Exception {
        // Arrange
        when(mockRequest.getRequestDispatcher("ClimateRecordForm.jsp")).thenReturn(mockDispatcher);
        when(mockRequest.getServletPath()).thenReturn("/new");

        // Act
        servlet.doGet(mockRequest, mockResponse);

        // Assert
        verify(mockRequest).setAttribute("locations", ClimateInfoDashboard.LOCATIONS);
        verify(mockDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    void testInsertClimateRecord() throws Exception {
        // Arrange
        when(mockRequest.getParameter("date")).thenReturn("2024-11-01");
        when(mockRequest.getParameter("location")).thenReturn("Victoria");
        when(mockRequest.getParameter("temperature")).thenReturn("12.5");
        when(mockRequest.getParameter("wind")).thenReturn("5.0");

        when(mockClimateRecordDAO.insertClimateRecord(any(ClimateRecord.class))).thenReturn(true);

        // Act
        when(mockRequest.getServletPath()).thenReturn("/insert");
        servlet.doPost(mockRequest, mockResponse);

        // Assert
        ArgumentCaptor<ClimateRecord> recordCaptor = ArgumentCaptor.forClass(ClimateRecord.class);
        verify(mockClimateRecordDAO).insertClimateRecord(recordCaptor.capture());
        ClimateRecord capturedRecord = recordCaptor.getValue();

        assertEquals("2024-11-01", capturedRecord.getDate());
        assertEquals("Victoria", capturedRecord.getLocation());
        assertEquals(12.5f, capturedRecord.getTemperature());
        assertEquals(5.0f, capturedRecord.getWind());

        verify(mockResponse).sendRedirect("list");
    }

    @Test
    void testDeleteClimateRecord() throws Exception {
        // Arrange
        when(mockRequest.getParameter("id")).thenReturn("1");
        when(mockClimateRecordDAO.deleteClimateRecord(1)).thenReturn(true);

        // Act
        when(mockRequest.getServletPath()).thenReturn("/delete");
        servlet.doPost(mockRequest, mockResponse);

        // Assert
        // Verify the correct method on the DAO was called with the correct ID
        verify(mockClimateRecordDAO).deleteClimateRecord(1);

        // Verify that the response redirects to "list"
        verify(mockResponse).sendRedirect("list");
    }

    
    @Test
    void testUpdateClimateRecord() throws Exception {
        // Arrange
        when(mockRequest.getParameter("id")).thenReturn("1");
        when(mockRequest.getParameter("date")).thenReturn("2024-11-01");
        when(mockRequest.getParameter("location")).thenReturn("Victoria");
        when(mockRequest.getParameter("temperature")).thenReturn("15.0");
        when(mockRequest.getParameter("wind")).thenReturn("10.0");

        when(mockClimateRecordDAO.updateClimateRecord(any(ClimateRecord.class))).thenReturn(true);

        // Act
        when(mockRequest.getServletPath()).thenReturn("/update");
        servlet.doPost(mockRequest, mockResponse);

        // Assert
        ArgumentCaptor<ClimateRecord> recordCaptor = ArgumentCaptor.forClass(ClimateRecord.class);
        verify(mockClimateRecordDAO).updateClimateRecord(recordCaptor.capture());
        ClimateRecord capturedRecord = recordCaptor.getValue();

        assertEquals(1, capturedRecord.getId());
        assertEquals("2024-11-01", capturedRecord.getDate());
        assertEquals("Victoria", capturedRecord.getLocation());
        assertEquals(15.0f, capturedRecord.getTemperature());
        assertEquals(10.0f, capturedRecord.getWind());

        verify(mockResponse).sendRedirect("list");
    }

    @Test
    void testShowTemperatureTrends() throws Exception {
        // Arrange
        List<ClimateRecord> mockRecords = Arrays.asList(
                new ClimateRecord(1, "2024-11-01", "Victoria", 12.5f, 5.0f),
                new ClimateRecord(2, "2024-11-02", "Nanaimo", 13.0f, 4.5f)
        );
        when(mockClimateRecordDAO.listAllClimateRecords()).thenReturn(mockRecords);
        when(mockRequest.getRequestDispatcher("TempTrendsGraph.jsp")).thenReturn(mockDispatcher);

        // Act
        when(mockRequest.getServletPath()).thenReturn("/temperatureTrends");
        servlet.doGet(mockRequest, mockResponse);

        // Assert
        verify(mockRequest).setAttribute("recordsList", mockRecords);
        verify(mockDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    void testShowEditForm() throws Exception {
        // Arrange
        ClimateRecord mockRecord = new ClimateRecord(1, "2024-11-01", "Victoria", 12.5f, 5.0f);
        when(mockRequest.getParameter("id")).thenReturn("1");
        when(mockClimateRecordDAO.getClimateRecord(1)).thenReturn(mockRecord);
        when(mockRequest.getRequestDispatcher("ClimateRecordForm.jsp")).thenReturn(mockDispatcher);

        // Act
        when(mockRequest.getServletPath()).thenReturn("/edit");
        servlet.doGet(mockRequest, mockResponse);

        // Assert
        verify(mockRequest).setAttribute("locations", ClimateInfoDashboard.LOCATIONS);
        verify(mockRequest).setAttribute("record", mockRecord);
        verify(mockDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    void testShowEditForm_RecordNotFound() throws Exception {
        // Arrange
        when(mockRequest.getParameter("id")).thenReturn("999");
        when(mockClimateRecordDAO.getClimateRecord(999)).thenReturn(null);

        // Act
        when(mockRequest.getServletPath()).thenReturn("/edit");
        servlet.doGet(mockRequest, mockResponse);

        // Assert
        verify(mockResponse).sendError(HttpServletResponse.SC_NOT_FOUND, "Climate record not found.");
    }


    @Test
    void testHandleException() throws Exception {
        // Arrange
        when(mockRequest.getRequestDispatcher("Error.jsp")).thenReturn(mockDispatcher);

        doThrow(new RuntimeException("Test exception"))
                .when(mockClimateRecordDAO).listAllClimateRecords();

        // Act
        when(mockRequest.getServletPath()).thenReturn("/");
        servlet.doGet(mockRequest, mockResponse);

        // Assert
        verify(mockRequest).setAttribute(eq("errorTitle"), eq("Unexpected Error"));
        verify(mockRequest).setAttribute(eq("errorMessage"), eq("Test exception"));
        verify(mockDispatcher).forward(mockRequest, mockResponse);
    }
}
