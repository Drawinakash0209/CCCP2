<!DOCTYPE html>
<%@page import="cccp.model.Batch"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Batch Management</title>
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
            
            <div class="h-full ml-14 mt-14 mb-10 md:ml-64">
                <div class="container mx-auto px-4 py-8">
                    <h1 class="text-3xl font-bold text-center mb-8">Batch Management</h1>
                <!-- Add Batch Form -->
<div class="flex flex-col md:flex-row justify-between items-center mb-6">
    <form action="BatchServlet" method="post" class="w-full md:w-1/2 mb-4 md:mb-0">
        <input type="hidden" name="action" value="create">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <input type="text" name="product_id" placeholder="Product ID" class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500" required>
            <input type="text" name="batch_id" placeholder="Batch ID" class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500" required>
            <input type="number" name="quantity" placeholder="Quantity" class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500" required>
            <input type="date" name="purchase_date" class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500" required>
            <input type="date" name="expiry_date" class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500" required>
        </div>
        <button type="submit" class="mt-4 bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 transition duration-300 w-full md:w-auto">Add Batch</button>
    </form>
</div>

<!-- Existing Search Form -->
<div class="flex flex-col md:flex-row justify-between items-center mb-6">
    <form action="BatchServlet" method="post" class="w-full md:w-1/3 mb-4 md:mb-0">
        <input type="hidden" name="action" value="searchBatch">
        <input type="text" id="product_id" name="product_id" placeholder="Enter Product ID..." class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500" required>
        <button type="submit" class="mt-2 bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition duration-300 w-full md:w-auto">Search</button>
    </form>
</div>
                    
                    <!-- Batches Table -->
                    <div class="overflow-x-auto bg-white rounded-lg shadow">
                        <table class="w-full table-auto">
                            <thead>
                                <tr class="bg-gray-200 text-gray-600 uppercase text-sm leading-normal">
                                    <th class="py-3 px-6 text-left">Batch ID</th>
                                    <th class="py-3 px-6 text-left">Quantity</th>
                                    <th class="py-3 px-6 text-left">Purchase Date</th>
                                    <th class="py-3 px-6 text-left">Expiry Date</th>
                                </tr>
                            </thead>
                            <tbody class="text-gray-600 text-sm">
                                <% 
                                    List<Batch> batchList = (List<Batch>) request.getAttribute("batchList");
                                    if (batchList != null && !batchList.isEmpty()) {
                                        for (Batch batch : batchList) {
                                %>
                                    <tr class="border-b border-gray-200 hover:bg-gray-100">
                                        <td class="py-3 px-6 text-left"><%= batch.getBatchcode() %></td>
                                        <td class="py-3 px-6 text-left"><%= batch.getQuantity() %></td>
                                        <td class="py-3 px-6 text-left"><%= batch.getPurchaseDate() %></td>
                                        <td class="py-3 px-6 text-left"><%= batch.getExpiryDate() %></td>
                                    </tr>
                                <% 
                                        }
                                    } else { 
                                %>
                                    <tr><td colspan="4" class="text-center py-3">No batches found.</td></tr>
                                <% 
                                    } 
                                %>
                                
                                <% 
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null) { 
%>
    <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">
        <span class="block sm:inline"><%= errorMessage %></span>
    </div>
<% 
    } 
%>
                            </tbody>
                        </table>
                    </div>
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