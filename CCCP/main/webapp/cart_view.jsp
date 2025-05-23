<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map, cccp.model.Product" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart</title>
    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        /* Custom styles for modern UI */
        .cart-item {
            transition: all 0.3s ease;
        }
        .cart-item:hover {
            background-color: #f9fafb;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
        }
        .btn-primary {
            background: linear-gradient(to right, #3b82f6, #1e40af);
            transition: background 0.3s ease;
        }
        .btn-primary:hover {
            background: linear-gradient(to right, #1e40af, #3b82f6);
        }
        .btn-danger {
            background: linear-gradient(to right, #ef4444, #b91c1c);
            transition: background 0.3s ease;
        }
        .btn-danger:hover {
            background: linear-gradient(to right, #b91c1c, #ef4444);
        }
        .btn-secondary {
            background: linear-gradient(to right, #6b7280, #4b5563);
        }
        .btn-secondary:hover {
            background: linear-gradient(to right, #4b5563, #6b7280);
        }
        .quantity-input {
            transition: all 0.2s ease;
        }
        .quantity-input:focus {
            outline: none;
            ring: 2px solid #3b82f6;
            box-shadow: 0 0 5px rgba(59, 130, 246, 0.5);
        }
        /* Dark mode support */
        @media (prefers-color-scheme: dark) {
            body {
                background-color: #1f2937;
                color: #f9fafb;
            }
            .cart-item {
                background-color: #374151;
                border-color: #4b5563;
            }
            .message-success, .message-error {
                color: #f9fafb;
            }
            .cart-total {
                background-color: #374151;
            }
        }
        /* Responsive layout */
        .cart-item {
            display: grid;
            grid-template-columns: 2fr 1fr 1fr 1fr 1fr;
            gap: 1rem;
            align-items: center;
        }
        @media (max-width: 640px) {
            .cart-item {
                grid-template-columns: 1fr;
                gap: 0.5rem;
                padding: 1rem;
            }
            .cart-item > div::before {
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
        <h1 class="text-3xl font-extrabold tracking-tight">Syntex - Product Catalog</h1>
  	 <a href="<%= request.getContextPath() %>/customer.jsp" 
   class="btn-secondary text-white px-6 py-2 rounded-lg font-medium hover:shadow-lg transition-all">
    Go Back
	</a>
    </header>

    <main class="container mx-auto px-4 py-10">
        <% 
            String message = (String) request.getAttribute("message");
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (message != null && !message.isEmpty()) {
        %>
            <div class="message-success bg-green-50 border-l-4 border-green-500 text-green-700 p-4 mb-8 rounded-lg shadow-md" role="alert">
                <%= message %>
            </div>
        <% 
            }
            if (errorMessage != null && !errorMessage.isEmpty()) {
        %>
            <div class="message-error bg-red-50 border-l-4 border-red-500 text-red-700 p-4 mb-8 rounded-lg shadow-md" role="alert">
                <%= errorMessage %>
            </div>
        <% 
            }
            Map<Product, Integer> detailedCart = (Map<Product, Integer>) request.getAttribute("detailedCart");
            Double cartTotal = (Double) request.getAttribute("cartTotal");
            if (detailedCart == null || detailedCart.isEmpty()) {
        %>
            <div class="text-center py-10 text-gray-500 text-lg">
                Your cart is empty.
            </div>
        <% 
            } else {
        %>
            <div class="bg-white rounded-xl shadow-lg overflow-hidden">
                <div class="hidden sm:grid grid-cols-5 gap-4 bg-gray-100 p-4 font-semibold text-gray-600">
                    <div>Product Name</div>
                    <div>Price</div>
                    <div>Quantity</div>
                    <div>Total</div>
                    <div>Action</div>
                </div>
                <% 
                    for (Map.Entry<Product, Integer> entry : detailedCart.entrySet()) {
                        Product product = entry.getKey();
                        Integer quantity = entry.getValue();
                %>
                    <div class="cart-item bg-white border-b border-gray-200 p-4">
                        <div data-label="Product Name" class="text-gray-800 font-medium"><%= product.getName() %></div>
                        <div data-label="Price" class="text-gray-600">Rs.3<%= String.format("%.2f", product.getPrice()) %></div>
                        <div data-label="Quantity">
                            <form action="<%= request.getContextPath() %>/onlineShop" method="post" class="flex items-center space-x-2">
                                <input type="hidden" name="action" value="updateCart">
                                <input type="hidden" name="productId" value="<%= product.getId() %>">
<input type="number" name="quantity" value="<%= quantity %>" min="0" 
       class="quantity-input w-16 border border-gray-300 rounded-lg px-2 py-1 text-sm text-black focus:ring-2 focus:ring-blue-500"
       aria-label="Quantity for <%= product.getName() %>">

                                <input type="submit" value="Update" 
                                       class="btn-primary text-white px-3 py-1 rounded-lg font-medium hover:shadow-lg cursor-pointer"
                                       aria-label="Update quantity for <%= product.getName() %>">
                            </form>
                        </div>
                        <div data-label="Total" class="text-gray-800 font-semibold">$<%= String.format("%.2f", product.getPrice() * quantity) %></div>
                        <div data-label="Action">
                            <form action="<%= request.getContextPath() %>/onlineShop" method="post">
                                <input type="hidden" name="action" value="updateCart">
                                <input type="hidden" name="productId" value="<%= product.getId() %>">
                                <input type="hidden" name="quantity" value="0">
                                <input type="submit" value="Remove" 
                                       class="btn-danger text-white px-3 py-1 rounded-lg font-medium hover:shadow-lg cursor-pointer"
                                       aria-label="Remove <%= product.getName() %> from cart">
                            </form>
                        </div>
                    </div>
                <% 
                    }
                %>
                <div class="cart-total bg-gray-100 p-4 flex justify-between items-center text-lg font-bold">
                    <span>Total:</span>
                    <span>$<%= String.format("%.2f", cartTotal) %></span>
                </div>
            </div>
            <div class="mt-8 flex justify-center space-x-4">
                <form action="<%= request.getContextPath() %>/onlineShop" method="post">
                    <input type="hidden" name="action" value="checkout">
                    <input type="submit" value="Proceed to Checkout" 
                           class="btn-primary text-white px-8 py-3 rounded-lg font-medium hover:shadow-lg cursor-pointer">
                </form>
                <a href="<%= request.getContextPath() %>/onlineShop?action=viewProducts" 
                   class="btn-secondary text-white px-8 py-3 rounded-lg font-medium hover:shadow-lg">
                    Continue Shopping
                </a>
            </div>
        <% 
            }
        %>
    </main>
</body>
</html>