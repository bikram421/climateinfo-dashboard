<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f8f9fa;
        }
        .container {
            max-width: 600px;
            margin: 50px auto;
            background-color: #fff;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
            border-radius: 5px;
            padding: 20px;
        }
        h1 {
            color: #dc3545;
            font-size: 24px;
        }
        p {
            margin: 10px 0;
            color: #333;
        }
        pre {
            background-color: #f1f1f1;
            border: 1px solid #ddd;
            padding: 10px;
            border-radius: 5px;
            overflow-x: auto;
            color: #666;
        }
        .footer {
            text-align: center;
            margin-top: 20px;
            font-size: 14px;
            color: #777;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>${errorTitle}</h1>
        <p>${errorMessage}</p>
        <c:if test="${not empty errorDetails}">
            <h2>Details:</h2>
            <pre>${errorDetails}</pre>
        </c:if>
    </div>
    <div class="footer">
        <p>&copy; 2024 ClimateInfoApp. All rights reserved.</p>
    </div>
</body>
</html>
