<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="cccp.model.Bill" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Bill Report - Financial Overview</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.0.0/dist/tailwind.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>
<body>

<div x-data="setup()" :class="{ 'dark': isDark }">
    <div class="min-h-screen flex flex-col flex-auto flex-shrink-0 antialiased bg-gray-100 text-gray-800">
        
        <!-- Header -->
        <jsp:include page="employee_dashboard_header.jsp" />

        <!-- Sidebar -->
        <jsp:include page="employee_dashboard_sidebar.jsp" />

        <!-- Main Content -->
        <div class="h-full ml-14 mt-14 mb-10 md:ml-64">
            <div class="max-w-6xl mx-auto bg-white shadow-lg rounded-lg p-6">
                <h1 class="text-3xl font-semibold text-center text-blue-700 border-b-2 pb-4">Bill Report - Financial Overview</h1>

                <table class="w-full border-collapse mt-6">
                    <thead>
                        <tr class="bg-blue-600 text-white">
                            <th class="p-3 text-left">Bill ID</th>
                            <th class="p-3 text-left">Date</th>
                            <th class="p-3 text-left">Total Amount</th>
                            <th class="p-3 text-left">Cash Tendered</th>
                            <th class="p-3 text-left">Change Given</th>
                            <th class="p-3 text-left">Discount</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-gray-300">
                        <%
                            List<Bill> bills = (List<Bill>) request.getAttribute("bills");
                            if (bills != null && !bills.isEmpty()) {
                                for (Bill bill : bills) {
                        %>
                 <tr class="odd:bg-blue-100 even:bg-blue-200 mb-4"> 
				    <td class="p-4"><%= bill.getBillId() %></td>
				    <td class="p-4"><%= bill.getBillDate() %></td>
				    <td class="p-4">$<%= String.format("%.2f", bill.getTotalPrice()) %></td>
				    <td class="p-4">$<%= String.format("%.2f", bill.getCashTendered()) %></td>
				    <td class="p-4">$<%= String.format("%.2f", bill.getChangeAmount()) %></td>
				    <td class="p-4">$<%= String.format("%.2f", bill.getDiscount()) %></td>
				</tr>


                        <% if (bill.getBillItems() != null && !bill.getBillItems().isEmpty()) { %>
                        <tr class="bg-gray-50 mb-4">
                            <td colspan="6" class="p-4">
                                <strong>Bill Items:</strong>
                                <table class="w-full mt-2 border border-gray-300">
                                    <% for (Bill.BillItem item : bill.getBillItems()) { %>
                                    <tr class="odd:bg-gray-200 even:bg-gray-100">
                                        <td class="p-2">ID: <%= item.getProductCode() %></td>
                                        <td class="p-2"><%= item.getproductName() %></td>
                                        <td class="p-2">Qty: <%= item.getQuantity() %></td>
                                        <td class="p-2">$<%= String.format("%.2f", item.getPrice()) %></td>
                                    </tr>
                                    <% } %>
                                </table>
                            </td>
                        </tr>
                        <% } %>

                        <% } } else { %>
                        <tr><td colspan="6" class="text-center text-gray-500 p-6 italic">No billing records found for this period</td></tr>
                        <% } %>
                    </tbody>
                </table>

                <div class="text-center mt-6">
                    <a href="index.html" class="px-6 py-3 bg-green-500 text-white rounded-lg shadow-md hover:bg-green-600 transition">Return to Dashboard</a>
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
