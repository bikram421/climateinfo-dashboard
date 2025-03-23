<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="org.owasp.encoder.Encode" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search and Display Climate Records</title>
    <style>
        /* Same styles as in the previous JSP or add new ones for consistency */
        .container {
            width: 80%;
            margin: auto;
            text-align: center;
            padding: 20px;
        }
        .btn {
            padding: 8px 12px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .btn-search {
            background-color: #007BFF;
            color: white;
            margin-top: 20px;
        }
        .btn-back {
            background-color: #f44336;
            color: white;
            margin-top: 20px;
        }
        .btn:hover {
            opacity: 0.9;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
            background-color: #fff;
            box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1);
        }
        table th, table td {
            padding: 10px;
            text-align: center;
            border: 1px solid #ddd;
        }
        table th {
            background-color: #f4f4f4;
        }
        table tr:hover {
            background-color: #f1f1f1;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Search and Display Climate Records</h1>
        
        <!-- Search Form -->
        <form action="search" method="get">
            <label for="city">Select City:</label>
            <select name="city" id="city">
                <option value="">--Select City--</option>
                <option value="Victoria">Victoria</option>
                <option value="Nanaimo">Nanaimo</option>
                <option value="Duncan">Duncan</option>
                <option value="Tofino">Tofino</option>
                <!-- Add more cities as necessary -->
            </select>
            <button class="btn btn-search" type="submit">Display Records</button>
        </form>

        <!-- Display Records Table if records exist -->
        <c:if test="${not empty cityRecords}">
            <table>
                <caption><h2>List of Records for ${param.city}</h2></caption>
                <tr>
                    <th>ID</th>
                    <th>Date</th>
                    <th>Location</th>
                    <th>Temperature</th>
                    <th>Wind</th>
                    <th>Actions</th>
                </tr>
                <c:forEach var="record" items="${cityRecords}">
                    <tr>
                        <td>${Encode.forHtml(record.id)}</td>
                        <td>${Encode.forHtml(record.date)}</td>
                        <td>${Encode.forHtml(record.location)}</td>
                        <td>${Encode.forHtml(record.temperature)}</td>
                        <td>${Encode.forHtml(record.wind)}</td>
                        <td>
                            <button class="btn btn-edit">
                                <a href="edit?id=${Encode.forHtml(record.id)}" style="color:white;">Edit</a>
                            </button>
                            <button class="btn btn-delete">
                                <a href="delete?id=${Encode.forHtml(record.id)}" style="color:white;">Delete</a>
                            </button>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
        
        <!-- If no city is selected or no records are found, display message -->
        <c:if test="${empty cityRecords}">
            <p>No records available for the selected city. Please select a city and try again.</p>
        </c:if>

        <!-- Back button -->
        <br>
        <button class="btn btn-back" onclick="location.href='list';">Back to All Records</button>
    </div>
</body>
</html>
