<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Temperature Trends</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
        canvas {
            margin-top: 20px;
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
    </style>
</head>
<body>
    <div class="container">
        <h1>Temperature Trends</h1>
        <div class="links">
            <a href="list">Back to Records</a>
        </div>
        <canvas id="temperatureChart" width="800" height="400"></canvas>
    </div>

    <script>
        // Fetch temperature data passed from the servlet
        const labels = [
            <c:forEach var="record" items="${recordsList}">
                '${record.date}'<c:if test="${!record.id.equals(recordsList[recordsList.size() - 1].id)}">,</c:if>
            </c:forEach>
        ];

        const data = [
            <c:forEach var="record" items="${recordsList}">
                ${record.temperature}<c:if test="${!record.id.equals(recordsList[recordsList.size() - 1].id)}">,</c:if>
            </c:forEach>
        ];

        // Convert date strings to JavaScript Date objects and pair them with temperatures
        const formattedData = labels.map((label, index) => {
            return {
                date: new Date(label),  // Convert string date to Date object
                temperature: data[index]
            };
        });

        // Sort data by date in ascending order
        formattedData.sort((a, b) => a.date - b.date);  // Sort by Date (ascending)

        // Extract sorted dates and temperatures
        const sortedLabels = formattedData.map(record => record.date.toISOString().split('T')[0]);  // Format date to 'YYYY-MM-DD'
        const sortedTemperatures = formattedData.map(record => record.temperature);

        // Initialize Chart.js chart
        const ctx = document.getElementById('temperatureChart').getContext('2d');
        const temperatureChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: sortedLabels,
                datasets: [{
                    label: 'Temperature (°C)',
                    data: sortedTemperatures,
                    borderColor: 'rgba(75, 192, 192, 1)',
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    borderWidth: 2,
                    tension: 0.3,
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        display: true,
                        position: 'top',
                    },
                },
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: 'Date',
                        }
                    },
                    y: {
                        title: {
                            display: true,
                            text: 'Temperature (°C)',
                        }
                    }
                }
            }
        });
    </script>
</body>
</html>
