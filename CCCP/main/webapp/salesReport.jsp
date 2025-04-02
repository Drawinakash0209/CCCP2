<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="cccp.SaleReport, cccp.SaleReport.Summary" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sales Report</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.0.0/dist/tailwind.min.css" rel="stylesheet">
      <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>
<body>
<div x-data="setup()" :class="{ 'dark': isDark }">
    <div class="min-h-screen flex flex-col antialiased bg-white dark:bg-gray-700 text-black dark:text-white">
        <!-- Header -->
        <jsp:include page="employee_dashboard_header.jsp" />
        
        <!-- Sidebar -->
        <jsp:include page="employee_dashboard_sidebar.jsp" />
        
        <div class="h-full ml-14 mt-14 mb-10 md:ml-64">
            <div class="container mx-auto px-4 py-8">
                <h1 class="text-3xl font-bold text-center mb-8">Generate Sales Report</h1>
                
                <form action="SalesReportServlet" method="post" class="bg-white p-6 rounded shadow-md max-w-md mx-auto">
                    <label for="date" class="block text-gray-700">Enter Date (yyyy-MM-dd):</label>
                    <input type="text" id="date" name="date" required
                           class="w-full px-4 py-2 mt-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500">
                    <input type="submit" value="Generate Report"
                           class="mt-4 w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600 transition duration-300">
                </form>
                
                <%
                    String error = (String) request.getAttribute("error");
                    SaleReport saleReport = (SaleReport) request.getAttribute("saleReport");
                    String inputDate = (String) request.getAttribute("inputDate");
                %>
                
                <% if (error != null) { %>
                <p class="text-red-500 text-center mt-4"><%= error %></p>
                <% } else if (saleReport != null && saleReport.getSummary() != null && !saleReport.getSummary().isEmpty()) { %>
                
                <h2 class="text-2xl font-bold text-center mt-8">Daily Sales Report for <%= inputDate %></h2>
                <div class="overflow-x-auto bg-white rounded-lg shadow mt-6">
                    <table class="w-full table-auto border-collapse">
                        <thead>
                            <tr class="bg-gray-200 text-gray-600 uppercase text-sm">
                                <th class="py-3 px-6 text-left">Product Name</th>
                                <th class="py-3 px-6 text-left">Product Code</th>
                                <th class="py-3 px-6 text-left">Quantity</th>
                                <th class="py-3 px-6 text-left">Revenue</th>
                            </tr>
                        </thead>
                        <tbody class="text-gray-700">
                            <% for (Summary summary : saleReport.getSummary().values()) { %>
                            <tr class="border-b border-gray-200 hover:bg-gray-100">
                                <td class="py-3 px-6"><%= summary.getProductName() %></td>
                                <td class="py-3 px-6"><%= summary.getProductCode() %></td>
                                <td class="py-3 px-6"><%= summary.getTotalQuantity() %></td>
                                <td class="py-3 px-6"><%= String.format("%.2f", summary.getTotalRevenue()) %></td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
                
                <% } else if (inputDate != null) { %>
                <p class="text-center text-gray-600 mt-4">No sales data found for the specified date.</p>
                <% } %>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/gh/alpinejs/alpine@v2.8.0/dist/alpine.min.js" defer></script>
<script>
    const setup = () => {
        const getTheme = () => {
            if (window.localStorage.getItem('dark')) {
                return JSON.parse(window.localStorage.getItem('dark'));
            }
            return !!window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;
        };
        const setTheme = (value) => {
            window.localStorage.setItem('dark', value);
        };
        return {
            isDark: getTheme(),
            toggleTheme() {
                this.isDark = !this.isDark;
                setTheme(this.isDark);
            },
        };
    };
</script>
</body>
</html>