<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Temperature Trends</title>
    <script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f9f9f9;
            color: #333;
            margin: 0;
            padding: 0;
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
            margin: auto;
            transition: all 0.3s;
        }
        .links a:hover {
            background-color: #0066cc;
            color: #fff;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Temperature Trends</h1>
        <div class="links">
            <a href="list">Back to Records</a>
        </div>
        <div id="temperatureChart" style="text-align: center; margin-top:20px; padding:20px; height: 400px; width: 100%;"></div>
    </div>

    <script>
        // Fetch temperature data passed from the servlet
        const records = [
            <c:forEach var="record" items="${recordsList}">
                { date: new Date('${record.date}'), temperature: ${record.temperature} 
                }<c:if test="${!record.id.equals(recordsList[recordsList.size() - 1].id)}">,</c:if>
            </c:forEach>
        ];

        // Sort records by date in ascending order
        records.sort((a, b) => a.date - b.date);

        // Prepare data points for the chart
        const dataPoints = records.map(record => ({
            x: record.date,
            y: record.temperature
        }));

        // Initialize CanvasJS Chart
        const chart = new CanvasJS.Chart("temperatureChart", {
            animationEnabled: true,
            theme: "light2",
            title: {
                text: "Temperature Trends"
            },
            axisX: {
                title: "Date",
                valueFormatString: "YYYY-MM-DD"
            },
            axisY: {
                title: "Temperature (°C)",
                minimum: Math.min(0, Math.min(...dataPoints.map(dp => dp.y)))
            },
            data: [{
                type: "line",
                xValueType: "dateTime",
                dataPoints: dataPoints
            }]
        });

        chart.render();
    </script>
</body>
</html>
