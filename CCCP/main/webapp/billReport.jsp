<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="cccp.model.Bill" %>
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
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bill Report - Financial Overview</title>
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
            <div class="max-w-6xl mx-auto bg-white dark:bg-gray-800 shadow-lg rounded-lg p-6 mt-10">
                <h1 class="text-2xl font-bold mb-6 text-center dark:text-white">Bill Report - Financial Overview</h1>

                <div class="overflow-x-auto">
                    <table class="min-w-full text-sm text-left text-gray-700 dark:text-gray-200">
                        <thead class="text-xs uppercase bg-gray-100 dark:bg-gray-700">
                            <tr>
                                <th class="px-6 py-3">Bill ID</th>
                                <th class="px-6 py-3">Date</th>
                                <th class="px-6 py-3">Total Amount</th>
                                <th class="px-6 py-3">Cash Tendered</th>
                                <th class="px-6 py-3">Change Given</th>
                                <th class="px-6 py-3">Discount</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-600">
                            <%
                                List<Bill> bills = (List<Bill>) request.getAttribute("bills");
                                if (bills != null && !bills.isEmpty()) {
                                    for (Bill bill : bills) {
                            %>
                                <tr class="hover:bg-gray-100 dark:hover:bg-gray-700">
                                    <td class="px-6 py-4"><%= bill.getBillId() %></td>
                                    <td class="px-6 py-4"><%= bill.getBillDate() %></td>
                                    <td class="px-6 py-4">$<%= String.format("%.2f", bill.getTotalPrice()) %></td>
                                    <td class="px-6 py-4">$<%= String.format("%.2f", bill.getCashTendered()) %></td>
                                    <td class="px-6 py-4">$<%= String.format("%.2f", bill.getChangeAmount()) %></td>
                                    <td class="px-6 py-4">$<%= String.format("%.2f", bill.getDiscount()) %></td>
                                </tr>
                                <% if (bill.getBillItems() != null && !bill.getBillItems().isEmpty()) { %>
                                    <tr class="bg-gray-50 dark:bg-gray-900">
                                        <td colspan="6" class="px-6 py-4">
                                            <strong class="text-gray-700 dark:text-gray-200">Bill Items:</strong>
                                            <div class="overflow-x-auto mt-2">
                                                <table class="min-w-full text-sm text-left text-gray-700 dark:text-gray-200">
                                                    <thead class="text-xs uppercase bg-gray-100 dark:bg-gray-700">
                                                        <tr>
                                                            <th class="px-4 py-2">Product ID</th>
                                                            <th class="px-4 py-2">Product Name</th>
                                                            <th class="px-4 py-2">Quantity</th>
                                                            <th class="px-4 py-2">Price</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody class="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-600">
                                                        <% for (Bill.BillItem item : bill.getBillItems()) { %>
                                                            <tr class="hover:bg-gray-100 dark:hover:bg-gray-700">
                                                                <td class="px-4 py-2"><%= item.getProductCode() %></td>
                                                                <td class="px-4 py-2"><%= item.getproductName() %></td>
                                                                <td class="px-4 py-2"><%= item.getQuantity() %></td>
                                                                <td class="px-4 py-2">$<%= String.format("%.2f", item.getPrice()) %></td>
                                                            </tr>
                                                        <% } %>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </td>
                                    </tr>
                                <% } %>
                            <% } } else { %>
                                <tr>
                                    <td colspan="6" class="text-center text-gray-500 dark:text-gray-400 italic py-6">No billing records found for this period</td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>

               
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
            loading: true,
            isDark: getTheme(),
            toggleTheme() {
                this.isDark = !this.isDark;
                setTheme(this.isDark);
            },
        };
    };
    // Start polling every 5 seconds
    document.addEventListener('alpine:init', () => {
        setInterval(() => {
            Alpine.data('setup')().checkRestockEvents();
        }, 5000);
    });
</script>
</body>
</html>