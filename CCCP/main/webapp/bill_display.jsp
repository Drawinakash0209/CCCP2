<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="cccp.model.Bill, java.util.List" %>
<%@ page import="cccp.model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp?error=Please login first");
        return;
    }
    String username = user.getUsername();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bill Details</title>
    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        /* Custom styles for modern UI */
        .bill-item {
            transition: all 0.3s ease;
        }
        .bill-item:hover {
            background-color: #f9fafb;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
        }
        .btn-secondary {
            background: linear-gradient(to right, #6b7280, #4b5563);
            transition: background 0.3s ease;
        }
        .btn-secondary:hover {
            background: linear-gradient(to right, #4b5563, #6b7280);
        }
        /* Dark mode support */
        @media (prefers-color-scheme: dark) {
            body {
                background-color: #1f2937;
                color: #f9fafb;
            }
            .bill-item {
                background-color: #374151;
                border-color: #4b5563;
            }
            .message-success, .message-error {
                color: #f9fafb;
            }
            .bill-summary, .bill-details {
                background-color: #374151;
            }
        }
        /* Responsive layout */
        .bill-item {
            display: grid;
            grid-template-columns: 1fr 2fr 1fr 1fr 1fr;
            gap: 1rem;
            align-items: center;
        }
        @media (max-width: 640px) {
            .bill-item {
                grid-template-columns: 1fr;
                gap: 0.5rem;
                padding: 1rem;
            }
            .bill-item > div::before {
                content: attr(data-label);
                font-weight: 600;
                color: #4b5563;
                margin-right: 0.5rem;
            }
        }
    </style>
</head>
<body class="bg-gray-50 font-sans antialiased">
    <header class="bg-gradient-to-r from-blue-600 to-indigo-700 text-white py-6">
      <div class="container mx-auto px-4 flex justify-between items-center">
        <h1 class="text-3xl font-extrabold tracking-tight">Syntex - Bill</h1>
  	 <a href="<%= request.getContextPath() %>/customer.jsp" 
   class="btn-secondary text-white px-6 py-2 rounded-lg font-medium hover:shadow-lg transition-all">
    Go Back
	</a>
    </header>
    <main class="container mx-auto px-4 py-10">
        <% 
            String message = (String) request.getSession().getAttribute("message");
            Bill bill = (Bill) request.getSession().getAttribute("bill");
            
            // Debugging: Check if attributes are present
            if (message == null && bill == null) {
        %>
            <div class="message-error bg-red-50 border-l-4 border-red-500 text-red-700 p-4 mb-8 rounded-lg shadow-md" role="alert">
                Error: No bill or message found in session. Please try checking out again.
            </div>
        <% 
            } else {
                if (message != null && !message.isEmpty()) {
        %>
            <div class="message-success bg-green-50 border-l-4 border-green-500 text-green-700 p-4 mb-8 rounded-lg shadow-md" role="alert">
                <%= message %>
            </div>
        <% 
                }
                if (bill != null) {
        %>
            <div class="bill-details bg-white rounded-xl shadow-lg p-6 mb-6">
                <h2 class="text-xl font-semibold text-gray-800 mb-4">Bill ID: <%= bill.getBillId() %></h2>
                <p class="text-gray-600">Date: <%= bill.getBillDate() %></p>
            </div>

            <div class="bg-white rounded-xl shadow-lg overflow-hidden">
                <div class="hidden sm:grid grid-cols-5 gap-4 bg-gray-100 p-4 font-semibold text-gray-600">
                    <div>Product Code</div>
                    <div>Product Name</div>
                    <div>Quantity</div>
                    <div>Unit Price</div>
                    <div>Total Price</div>
                </div>
                <% 
                    List<Bill.BillItem> billItems = bill.getBillItems();
                    if (billItems != null && !billItems.isEmpty()) {
                        for (Bill.BillItem item : billItems) {
                %>
                    <div class="bill-item bg-white border-b border-gray-200 p-4">
                        <div data-label="Product Code" class="text-gray-600"><%= item.getProductCode() %></div>
                        <div data-label="Product Name" class="text-gray-800 font-medium"><%= item.getproductName() %></div>
                        <div data-label="Quantity" class="text-gray-600"><%= item.getQuantity() %></div>
                        <div data-label="Unit Price" class="text-gray-600">$<%= String.format("%.2f", item.getPrice()) %></div>
                        <div data-label="Total Price" class="text-gray-800 font-semibold">$<%= String.format("%.2f", item.getTotalPrice()) %></div>
                    </div>
                <% 
                        }
                    } else {
                %>
                    <div class="text-center py-10 text-red-600 text-lg">
                        No items found in bill.
                    </div>
                <% 
                    }
                %>
            </div>

            <div class="bill-summary bg-white rounded-xl shadow-lg p-6 mt-6">
                <div class="flex justify-between text-lg font-bold text-gray-800">
                    <span>Total Price:</span>
                    <span>$<%= String.format("%.2f", bill.getTotalPrice()) %></span>
                </div>
                <div class="flex justify-between text-gray-600 mt-2">
                    <span>Cash Tendered:</span>
                    <span>$<%= String.format("%.2f", bill.getCashTendered()) %></span>
                </div>
                <% 
                    if (bill.getDiscount() > 0) {
                %>
                    <div class="flex justify-between text-gray-600 mt-2">
                        <span>Discount:</span>
                        <span>$<%= String.format("%.2f", bill.getDiscount()) %></span>
                    </div>
                <% 
                    }
                %>
                <div class="flex justify-between text-lg font-bold text-gray-800 mt-2">
                    <span>Change Amount:</span>
                    <span>$<%= String.format("%.2f", bill.getChangeAmount()) %></span>
                </div>
            </div>
        <% 
                } else {
        %>
            <div class="message-error bg-red-50 border-l-4 border-red-500 text-red-700 p-4 mb-8 rounded-lg shadow-md" role="alert">
                Error: Bill details are not available.
            </div>
        <% 
                }
            }
        %>
        <div class="mt-8 text-center">
            <a href="<%= request.getContextPath() %>/onlineShop?action=viewProducts" 
               class="btn-secondary inline-block text-white px-8 py-3 rounded-lg font-medium hover:shadow-lg transition-all">
                Return to Shopping
            </a>
        </div>
    </main>
</body>
</html>