<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, cccp.model.Product" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Catalog</title>
    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        /* Custom styles for additional polish */
        .table-container {
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            border-radius: 0.5rem;
            overflow: hidden;
        }
        .cart-button:hover {
            transform: scale(1.05);
            transition: transform 0.2s ease-in-out;
        }
        .quantity-input:focus {
            outline: none;
            ring: 2px solid #3b82f6;
        }
        /* Responsive table for mobile */
        @media (max-width: 640px) {
            .table-header {
                display: none;
            }
            .table-row {
                display: flex;
                flex-direction: column;
                border-bottom: 1px solid #e5e7eb;
                padding: 1rem;
            }
            .table-cell {
                display: flex;
                justify-content: space-between;
                padding: 0.5rem 0;
            }
            .table-cell::before {
                content: attr(data-label);
                font-weight: 600;
                color: #4b5563;
            }
        }
    </style>
</head>
<body class="bg-gray-100 font-sans">
    <header class="bg-blue-600 text-white py-4">
        <div class="container mx-auto px-4">
            <h1 class="text-2xl font-bold">Online Shop - Product Catalog</h1>
        </div>
    </header>

    <main class="container mx-auto px-4 py-8">
        <% 
            String message = (String) request.getAttribute("message");
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (message != null && !message.isEmpty()) {
        %>
            <div class="bg-green-100 border-l-4 border-green-500 text-green-700 p-4 mb-6 rounded" role="alert">
                <%= message %>
            </div>
        <% 
            }
            if (errorMessage != null && !errorMessage.isEmpty()) {
        %>
            <div class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-6 rounded" role="alert">
                <%= errorMessage %>
            </div>
        <% 
            }
        %>

        <div class="table-container bg-white">
            <table class="w-full">
                <thead class="bg-gray-200 table-header">
                    <tr>
                        <th class="py-3 px-4 text-left text-gray-600 font-semibold">Product ID</th>
                        <th class="py-3 px-4 text-left text-gray-600 font-semibold">Name</th>
                        <th class="py-3 px-4 text-left text-gray-600 font-semibold">Price</th>
                        <th class="py-3 px-4 text-left text-gray-600 font-semibold">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        List<Product> products = (List<Product>) request.getAttribute("products");
                        if (products != null) {
                            for (Product product : products) {
                    %>
                        <tr class="table-row hover:bg-gray-50">
                            <td class="py-3 px-4 table-cell" data-label="Product ID"><%= product.getId() %></td>
                            <td class="py-3 px-4 table-cell" data-label="Name"><%= product.getName() %></td>
                            <td class="py-3 px-4 table-cell" data-label="Price">$<%= String.format("%.2f", product.getPrice()) %></td>
                            <td class="py-3 px-4 table-cell" data-label="Action">
                                <form action="<%= request.getContextPath() %>/onlineShop" method="post" class="flex items-center space-x-2">
                                    <input type="hidden" name="action" value="addToCart">
                                    <input type="hidden" name="productId" value="<%= product.getId() %>">
                                    <input type="number" name="quantity" value="1" min="1" 
                                           class="quantity-input w-16 border border-gray-300 rounded-md px-2 py-1 text-sm focus:ring-2 focus:ring-blue-500"
                                           aria-label="Quantity">
                                    <input type="submit" value="Add to Cart" 
                                           class="cart-button bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition-colors cursor-pointer"
                                           aria-label="Add <%= product.getName() %> to cart">
                                </form>
                            </td>
                        </tr>
                    <% 
                            }
                        } else {
                    %>
                        <tr>
                            <td colspan="4" class="py-3 px-4 text-center text-gray-500">No products available.</td>
                        </tr>
                    <% 
                        }
                    %>
                </tbody>
            </table>
        </div>

        <div class="mt-6">
            <a href="<%= request.getContextPath() %>/onlineShop?action=viewCart" 
               class="inline-block bg-gray-800 text-white px-6 py-2 rounded-md hover:bg-gray-900 transition-colors">
                View Cart
            </a>
        </div>
    </main>
</body>
</html>