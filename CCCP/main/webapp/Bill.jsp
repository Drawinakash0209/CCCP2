<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="cccp.model.Bill, cccp.model.Bill.BillItem" %>
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
    <title>Create Bill</title>
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
            <div class="max-w-2xl mx-auto bg-white dark:bg-gray-800 shadow-lg rounded-lg p-6 mt-10">
                <h1 class="text-2xl font-bold mb-6 text-center dark:text-white">Create New Bill</h1>
                <form action="BillServlet" method="POST" class="space-y-6">
                    <div id="itemsContainer" class="space-y-4">
                        <div class="flex gap-4">
                            <input type="text" name="productId" placeholder="Product Code" required class="w-1/2 px-4 py-2 rounded-md border border-gray-300 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500">
                            <input type="number" name="quantity" placeholder="Quantity" min="1" required class="w-1/2 px-4 py-2 rounded-md border border-gray-300 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500">
                        </div>
                    </div>
                    <button type="button" onclick="addItemRow()" class="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition duration-300 w-full md:w-auto">Add Another Item</button>

                    <div>
                        <label for="discountRate" class="block font-bold mb-2 text-gray-700 dark:text-gray-200">Discount Rate (0-100%)</label>
                        <input type="number" id="discountRate" name="discountRate" min="0" max="100" step="0.01" value="0" class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500">
                    </div>

                    <div>
                        <label for="cashTendered" class="block font-bold mb-2 text-gray-700 dark:text-gray-200">Cash Tendered</label>
                        <input type="number" id="cashTendered" name="cashTendered" min="0" step="0.01" required class="w-full px-4 py-2 rounded-md border border-gray-300 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500">
                    </div>

                    <div class="flex items-center justify-center">
                        <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition duration-300">Create Bill</button>
                    </div>
                </form>

                <%
                    String message = (String) request.getAttribute("success");
                    String error = (String) request.getAttribute("error");
                    Bill bill = (Bill) request.getAttribute("bill");
                %>
                <% if (message != null && !message.isEmpty()) { %>
                    <div class="mt-6 p-4 rounded-md bg-green-100 text-green-700 text-center">
                        <%= message %>
                    </div>
                <% } %>
                <% if (error != null && !error.isEmpty()) { %>
                    <div class="mt-6 p-4 rounded-md bg-red-100 text-red-700 text-center">
                        <%= error %>
                    </div>
                <% } %>
                <% if (bill != null) { %>
                    <div class="mt-6">
                        <h2 class="text-xl font-bold mb-4 text-gray-700 dark:text-gray-200">Bill Details</h2>
                        <div class="space-y-2 text-gray-700 dark:text-gray-200">
                            <p><strong>Bill ID:</strong> <%= bill.getBillId() %></p>
                            <p><strong>Bill Date:</strong> <%= bill.getBillDate() %></p>
                            <p><strong>Total Price (Before Discount):</strong> <%= bill.getTotalPrice() + bill.getDiscount() %></p>
                            <p><strong>Discount Applied:</strong> <%= bill.getDiscount() %></p>
                            <p><strong>Total Price (After Discount):</strong> <%= bill.getTotalPrice() %></p>
                            <p><strong>Cash Tendered:</strong> <%= bill.getCashTendered() %></p>
                            <p><strong>Change Amount:</strong> <%= bill.getChangeAmount() %></p>
                        </div>
                        <h3 class="font-bold mt-4 mb-2 text-gray-700 dark:text-gray-200">Bill Items:</h3>
                        <div class="overflow-x-auto">
                            <table class="min-w-full text-sm text-left text-gray-700 dark:text-gray-200">
                                <thead class="text-xs uppercase bg-gray-100 dark:bg-gray-700">
                                    <tr>
                                        <th class="px-6 py-3">Product ID</th>
                                        <th class="px-6 py-3">Product Name</th>
                                        <th class="px-6 py-3">Quantity</th>
                                        <th class="px-6 py-3">Price</th>
                                        <th class="px-6 py-3">Total Price</th>
                                    </tr>
                                </thead>
                                <tbody class="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-600">
                                    <%
                                        for (BillItem item : bill.getBillItems()) {
                                    %>
                                        <tr class="hover:bg-gray-100 dark:hover:bg-gray-700">
                                            <td class="px-6 py-4"><%= item.getProductCode() %></td>
                                            <td class="px-6 py-4"><%= item.getproductName() %></td>
                                            <td class="px-6 py-4"><%= item.getQuantity() %></td>
                                            <td class="px-6 py-4"><%= item.getPrice() %></td>
                                            <td class="px-6 py-4"><%= item.getTotalPrice() %></td>
                                        </tr>
                                    <%
                                        }
                                    %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                <% } %>
            </div>
        </div>
    </div>
</div>

<script>
    function addItemRow() {
        const container = document.getElementById("itemsContainer");
        const row = document.createElement("div");
        row.className = "flex gap-4";
        row.innerHTML = `
            <input type="text" name="productId" placeholder="Product Code" required class="w-1/2 px-4 py-2 rounded-md border border-gray-300 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500">
            <input type="number" name="quantity" placeholder="Quantity" min="1" required class="w-1/2 px-4 py-2 rounded-md border border-gray-300 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500">
        `;
        container.appendChild(row);
    }

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
<script src="https://cdn.jsdelivr.net/gh/alpinejs/alpine@v2.8.0/dist/alpine.min.js" defer></script>
</body>
</html>