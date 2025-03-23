<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="org.owasp.encoder.Encode" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
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
        .btn {
            padding: 8px 12px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .btn-edit {
            background-color: #4CAF50;
            color: white;
        }
        .btn-delete {
            background-color: #f44336;
            color: white;
        }
        .btn-trends {
            background-color: #007BFF;
            color: white;
            margin-top: 20px;
            padding: 10px 20px;
        }
        .btn:hover {
            opacity: 0.9;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Climate Records Management</h1>
        <div class="links">
            <a href="new">Add New Record</a>
            <a href="list">List All Records</a>
            <a href="search">Search Records</a> <!-- Added this line -->
        </div>
        <table>
            <caption><h2>List of Climate Records</h2></caption>
            <tr>
                <th>ID</th>
                <th>Date</th>
                <th>Location</th>
                <th>Temperature</th>
                <th>Wind</th>
                <th>Actions</th>
            </tr>
            <c:forEach var="record" items="${listRecord}">
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
        <button class="btn btn-trends" onclick="location.href='temperatureTrends';">View Temperature Trends</button>
    </div>
</body>
</html>
