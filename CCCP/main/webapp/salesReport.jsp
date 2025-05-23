<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="cccp.SaleReport, cccp.SaleReport.Summary" %>
<%@ page import="cccp.model.User" %>
<%
    // Retrieve the user object from the session
    User user = (User) session.getAttribute("user");

    // Basic check if user is logged in
    if (user == null) {
        response.sendRedirect("login.jsp?error=Please login first");
        return; // Stop further processing of the page
    }

    // Assuming User has a getUsername() method
    String username = user.getUsername();
%>

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
    <div class="min-h-screen flex flex-col flex-auto flex-shrink-0 antialiased bg-white dark:bg-gray-700 text-black dark:text-white">
        <!-- Header -->
        <jsp:include page="employee_dashboard_header.jsp" />

        <!-- Sidebar -->
        <jsp:include page="employee_dashboard_sidebar.jsp" />

        <!-- Main Content -->
        <div class="h-full ml-14 mt-14 mb-10 md:ml-64 px-4">
            <div class="max-w-md mx-auto bg-white dark:bg-gray-800 shadow-lg rounded-lg p-6 mt-10">
                <h1 class="text-2xl font-bold mb-6 text-center dark:text-white">Generate Sales Report</h1>
                
                <form action="SalesReportServlet" method="post" class="space-y-4">
                    <div>
                        <label for="date" class="block font-bold mb-2 text-gray-700 dark:text-gray-200">Enter Date (yyyy-MM-dd):</label>
                        <input type="text" id="date" name="date" required
                               class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500">
                    </div>
                    <input type="submit" value="Generate Report"
                           class="w-full bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition duration-300">
                </form>
                
                <%
                    String error = (String) request.getAttribute("error");
                    SaleReport saleReport = (SaleReport) request.getAttribute("saleReport");
                    String inputDate = (String) request.getAttribute("inputDate");
                %>
                
                <% if (error != null) { %>
                    <div class="mt-6 p-4 rounded-md bg-red-100 text-red-700 text-center">
                        <%= error %>
                    </div>
                <% } else if (saleReport != null && saleReport.getSummary() != null && !saleReport.getSummary().isEmpty()) { %>
                    <h2 class="text-xl font-bold text-center mt-8 text-gray-700 dark:text-gray-200">Daily Sales Report for <%= inputDate %></h2>
                    <div class="overflow-x-auto mt-6">
                        <table class="min-w-full text-sm text-left text-gray-700 dark:text-gray-200">
                            <thead class="text-xs uppercase bg-gray-100 dark:bg-gray-700">
                                <tr>
                                    <th class="px-6 py-3">Product Name</th>
                                    <th class="px-6 py-3">Product Code</th>
                                    <th class="px-6 py-3">Quantity</th>
                                    <th class="px-6 py-3">Revenue</th>
                                </tr>
                            </thead>
                            <tbody class="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-600">
                                <% for (Summary summary : saleReport.getSummary().values()) { %>
                                    <tr class="hover:bg-gray-100 dark:hover:bg-gray-700">
                                        <td class="px-6 py-4"><%= summary.getProductName() %></td>
                                        <td class="px-6 py-4"><%= summary.getProductCode() %></td>
                                        <td class="px-6 py-4"><%= summary.getTotalQuantity() %></td>
                                        <td class="px-6 py-4">$<%= String.format("%.2f", summary.getTotalRevenue()) %></td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                <% } else if (inputDate != null) { %>
                    <p class="text-center text-gray-500 dark:text-gray-400 italic mt-6">No sales data found for the specified date.</p>
                <% } %>
            </div>
        </div>
    </div>
</div>

<!-- Alpine.js for dark mode toggle -->
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