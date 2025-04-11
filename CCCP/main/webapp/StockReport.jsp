<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="cccp.model.dao.StockItem" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Stock Report</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
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
            <div class="max-w-4xl mx-auto bg-white dark:bg-gray-800 shadow-lg rounded-lg p-6 mt-10">
                <h1 class="text-2xl font-bold mb-6 text-center dark:text-white">Stock Report</h1>

                <%
                    Object data = request.getAttribute("stockItems");
                    List<StockItem> stockItems = null;
                    boolean hasData = false;

                    if (data instanceof List<?>) {
                        try {
                            stockItems = (List<StockItem>) data;
                            if (stockItems != null && !stockItems.isEmpty()) {
                                hasData = true;
                            }
                        } catch (ClassCastException e) {
                            System.err.println("StockReport.jsp: Could not cast request attribute 'stockItems' to List<StockItem>.");
                            hasData = false;
                        }
                    }
                %>

                <% if (hasData) { %>
                    <div class="overflow-x-auto">
                        <table class="min-w-full text-sm text-left text-gray-700 dark:text-gray-200">
                            <thead class="text-xs uppercase bg-gray-100 dark:bg-gray-700">
                                <tr>
                                    <th class="px-6 py-3">Product ID</th>
                                    <th class="px-6 py-3">Product Name</th>
                                    <th class="px-6 py-3">Batch Code</th>
                                    <th class="px-6 py-3">Quantity</th>
                                    <th class="px-6 py-3">Purchase Date</th>
                                    <th class="px-6 py-3">Expiry Date</th>
                                </tr>
                            </thead>
                            <tbody class="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-600">
                                <% for (StockItem item : stockItems) { %>
                                    <tr class="hover:bg-gray-100 dark:hover:bg-gray-700">
                                        <td class="px-6 py-4"><%= item.getProductId() %></td>
                                        <td class="px-6 py-4"><%= item.getProductName() %></td>
                                        <td class="px-6 py-4"><%= item.getbatchId() %></td>
                                        <td class="px-6 py-4"><%= item.getQuantity() %></td>
                                        <td class="px-6 py-4"><%= item.getPurchaseDate() %></td>
                                        <td class="px-6 py-4"><%= item.getExpiryDate() %></td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                <% } else { %>
                    <p class="text-center text-gray-500 dark:text-gray-400 italic mt-6">No stock data available to display.</p>
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
                return JSON.parse(window.localStorage.getItem('dark'))
            }
            return !!window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches
        }

        const setTheme = (value) => {
            window.localStorage.setItem('dark', value)
        }

        return {
            loading: true,
            isDark: getTheme(),
            toggleTheme() {
                this.isDark = !this.isDark
                setTheme(this.isDark)
            },
        }
    }
</script>
</body>
</html>
