<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="org.owasp.encoder.Encode" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Climate Records Management</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f9f9f9;
            color: #333;
            margin: 0;
            padding: 0;
        }
        h1 {
            color: #0066cc;
        }
        .container {
            width: 80%;
            margin: auto;
            text-align: center;
            padding: 20px;
        }
        .links a {
            text-decoration: none;
            color: #0066cc;
            font-weight: bold;
            padding: 10px 15px;
            border: 1px solid #0066cc;
            border-radius: 4px;
            margin: 0 10px;
            transition: all 0.3s;
        }
        .links a:hover {
            background-color: #0066cc;
            color: #fff;
        }
        form {
            background-color: #fff;
            border: 1px solid #ddd;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1);
            width: 60%;
            margin: 20px auto;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        table th, table td {
            padding: 10px;
            text-align: left;
        }
        table th {
            text-align: right;
            color: #555;
            width: 30%;
        }
        input[type="text"], input[type="date"], input[type="number"], select, input[type="submit"] {
            padding: 8px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 4px;
            width: 100%;
        }
        input[type="submit"] {
            background-color: #0066cc;
            color: #fff;
            font-weight: bold;
            cursor: pointer;
            width: auto;
        }
        input[type="submit"]:hover {
            background-color: #004a99;
        }
        .error-message {
            color: red;
            font-size: 12px;
            margin-top: 5px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Climate Records Management</h1>
        <div class="links">
            <a href="new">Add New Record</a>
            <a href="list">List All Records</a>
        </div>

        <c:if test="${record != null}">
            <form id="climateForm" action="update" method="post">
        </c:if>
        <c:if test="${record == null}">
            <form id="climateForm" action="insert" method="post">
        </c:if>

            <h2>
                <c:if test="${record != null}">Edit Climate Record</c:if>
                <c:if test="${record == null}">Add New Climate Record</c:if>
            </h2>

            <c:if test="${record != null}">
                <input type="hidden" name="id" value="${Encode.forHtml(record.id)}" />
            </c:if>

            <table>
                <tr>
                    <th>Date:</th>
                    <td>
                        <input type="date" id="date" name="date" value="${Encode.forHtml(record.date)}" required />
                        <div id="error-date" class="error-message"></div>
                    </td>
                </tr>
                <tr>
                    <th>Location:</th>
                    <td>
                        <select id="location" name="location" required>
                            <option value="">-- Select a Location --</option>
                            <c:forEach var="loc" items="${locations}">
                                <option value="${Encode.forHtml(loc)}" 
                                        <c:if test="${record != null && record.location == loc}">selected</c:if>>
                                    ${Encode.forHtml(loc)}
                                </option>
                            </c:forEach>
                        </select>
                        <div id="error-location" class="error-message"></div>
                    </td>
                </tr>
                <tr>
                    <th>Temperature:</th>
                    <td>
                        <input type="number" id="temperature" name="temperature" step="0.01" min="-100" max="100" 
                               value="${Encode.forHtml(record.temperature)}" required />
                        <div id="error-temperature" class="error-message"></div>
                    </td>
                </tr>
                <tr>
                    <th>Wind:</th>
                    <td>
                        <input type="number" id="wind" name="wind" step="0.1" min="0" max="300" 
                               value="${Encode.forHtml(record.wind)}" required />
                        <div id="error-wind" class="error-message"></div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align: center;">
                        <input type="submit" value="Save" />
                    </td>
                </tr>
            </table>
        </form>
    </div>
</body>
</html>
